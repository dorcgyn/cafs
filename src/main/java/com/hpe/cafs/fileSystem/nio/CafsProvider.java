/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.fileSystem.nio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hpe.caf.storage.common.crypto.WrappedKey;
import com.hpe.caf.storage.sdk.StorageClient;
import com.hpe.caf.storage.sdk.exceptions.StorageServiceConnectException;
import com.hpe.caf.storage.sdk.exceptions.StorageServiceException;
import com.hpe.caf.storage.sdk.model.Asset;
import com.hpe.caf.storage.sdk.model.AssetMetadata;
import com.hpe.caf.storage.sdk.model.AssetMetadataList;
import com.hpe.caf.storage.sdk.model.requests.DeleteAssetRequest;
import com.hpe.caf.storage.sdk.model.requests.DownloadAssetRequest;
import com.hpe.caf.storage.sdk.model.requests.GetAssetContainerEncryptionKeyRequest;
import com.hpe.caf.storage.sdk.model.requests.GetAssetMetadataRequest;
import com.hpe.caf.storage.sdk.model.requests.GetCustomAssetMetadataRequest;
import com.hpe.caf.storage.sdk.model.requests.GetTopLevelAssetsRequest;
import com.hpe.caf.storage.sdk.model.requests.SetCustomAssetMetadataRequest;
import com.hpe.caf.storage.sdk.model.requests.UploadAssetRequest;
import com.hpe.cafs.config.Configuration;
import com.hpe.cafs.config.ConfigurationFactory;
import com.hpe.cafs.fileSystem.nio.channel.FileByteChannel;
import com.hpe.cafs.util.CustomJsonUtil;

/**
 * Created by dev on 3/13/16.
 */
public class CafsProvider extends FileSystemProvider {

    private static StorageClient storageClient = null;
    private static String containerId = null;
    private static String accessToken = null;
    private static WrappedKey containerkey = null;
    static {
        try {
            Configuration config = ConfigurationFactory.getConfig();
            storageClient = new StorageClient(config.getServerName(), config.getPort());
            containerId = config.getAssetContainerId();
            accessToken = config.getAccessToken();

            containerkey = storageClient.getAssetContainerEncryptionKey(
                    new GetAssetContainerEncryptionKeyRequest(accessToken, containerId));
        } catch (Exception e) {
            System.out.println("Can't connect to storage server!");
        }
    }

    private Path curPath = null;
    public CafsProvider (Path currentPath) {
        this.curPath = currentPath;
    }

    @Override
    public String getScheme() {
        return curPath.getFileName().toString();
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        return Cafs.getFileSystem();
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        return Cafs.getFileSystem();
    }

    @Override
    public Path getPath(URI uri) {
        return curPath;
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        if (options.contains(StandardOpenOption.READ)) {
            String assetId = getAssetId(path);
            if (assetId == null) {
                return new FileByteChannel(getAssetId(path.getParent()), path.getFileName().toString(), new ArrayList<Byte>());
            }
            try {
                Asset asset = storageClient.downloadAsset(new DownloadAssetRequest(accessToken, containerId, assetId, containerkey));
                int size = asset.getAssetMetadata().getSize().intValue();
                List<Byte> content = new ArrayList<Byte>(size);
                InputStream is = asset.getDecryptedStream();
                for (int i = 0; i < size; i++) {
                    content.add((byte)is.read());
                }
                return new FileByteChannel(getAssetId(path.getParent()), path.getFileName().toString(), content);
            } catch (Exception sse) {
                throw new IOException("Unable to open file");
            }
        } else {
            // if (options.contains(StandardOpenOption.WRITE))
            if (getAssetId(path) != null) {
                throw new RuntimeException("File already exists. Unable to create new file");
            }
            return new FileByteChannel(getAssetId(path.getParent()), path.getFileName().toString());
        }

    }

    // List Directory
    @Override
    public DirectoryStream<Path> newDirectoryStream(final Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {

        if (dir == null) return null;

        return new DirectoryStream<Path>() {
            @Override
            public Iterator<Path> iterator() {
                try {

                    ArrayList<Path> paths = new ArrayList<Path>();
                    if (dir.toString().equals(File.separator)) {
                        // for root directory
                        final AssetMetadataList assetMetadatas = storageClient.getTopLevelAssets(
                                new GetTopLevelAssetsRequest(accessToken, containerId));
                        for (AssetMetadata assetMetadata : assetMetadatas.getEntries()) {
                            paths.add(Paths.get(assetMetadata.getName()));
                        }
                    } else {
                        // for sub directory, get parent directory
                        String parentId = getAssetId(dir);

                        final String retrievedJson = storageClient.getCustomAssetMetadata(
                                new GetCustomAssetMetadataRequest(accessToken, containerId, parentId));
                        List<CustomJsonUtil.InodeObj> inodes = CustomJsonUtil.getSubInodes(retrievedJson);

                        for (CustomJsonUtil.InodeObj inode : inodes) {
                            paths.add(Paths.get(inode.name));
                        }
                    }
                    return paths.iterator();

                } catch (Exception e) {
                    System.out.println("Exception in newDirectoryStream: " + e);
                    return null;
                }
            }

            @Override
            public void close() throws IOException {
            }
        };
    }

    // create directory
    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        String parentId = getAssetId(dir.getParent());
        try {
            // Use a 1 byte fake data for content of dir asset
            InputStream is = new ByteArrayInputStream(new byte[1]);

            createFile(dir.getFileName().toString(), is, 1L, parentId, CafsFile.TYPE_DIR);

            // System.out.println("Create Directory " + assetMetadata.getName() + ", " + assetMetadata.getType());
        } catch (Exception e) {
            System.out.println("Exception in createDirectory: " + e);
            e.printStackTrace();
        }
    }

    public static void createFile(String fileName, InputStream is, long size, String parentId, String type) throws Exception {
        AssetMetadata assetMetadata = storageClient.uploadAssetOneShot(new UploadAssetRequest(accessToken, containerId,
                fileName, containerkey, is)
                .withAssetType(type).withAssetSize(size).withParentId(parentId)
                .withCustomMetadata(CustomJsonUtil.INIT_DIR_CUSTOM_METADATA));

        // If it is not root level asset, add child info into custom metadata
        if (parentId != null) {
            final String retrievedJson = storageClient.getCustomAssetMetadata(
                    new GetCustomAssetMetadataRequest(accessToken, containerId, parentId));
            String newJson = CustomJsonUtil.addSubInode(retrievedJson, assetMetadata.getName(), assetMetadata.getAssetId());

            storageClient.setCustomAssetMetadata(new SetCustomAssetMetadataRequest(
                    accessToken, containerId, parentId, newJson));
        }
    }

    @Override
    public void delete(Path path) throws IOException {
        String assetId = getAssetId(path);
        if (assetId == null) {
            throw new RuntimeException("Could not remove root directory.");
        }
        try {
            String parentId = getAssetId(path.getParent());
            if (parentId != null) {
                // delete subnode info in custom asset metadata
                final String retrievedJson = storageClient.getCustomAssetMetadata(
                        new GetCustomAssetMetadataRequest(accessToken, containerId, parentId));
                String newJson = CustomJsonUtil.removeSubInode(retrievedJson, assetId);
                storageClient.setCustomAssetMetadata(new SetCustomAssetMetadataRequest(
                        accessToken, containerId, parentId, newJson));
            }
            storageClient.deleteAsset(new DeleteAssetRequest(accessToken, containerId, assetId));
        } catch (StorageServiceException e) {
            if (e.getHTTPStatus() == 403) {
                throw new RuntimeException("You don't have permission to delete it.");
            } else if (e.getHTTPStatus() == 409) {
                throw new RuntimeException("This directory has file/subdir inside. Use recursive delete.");
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        return false;
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        return false;
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        return null;
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        // TODO:
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        return null;
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        return null;
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        final Map<String, Object> attributeMap;
        try {
            String assetId = getAssetId(path);
            AssetMetadata assetMetadata = storageClient.getAssetMetadata(
                    new GetAssetMetadataRequest(accessToken, containerId, assetId));
            attributeMap = new HashMap<String, Object>();
            attributeMap.put("type", assetMetadata.getType());
            attributeMap.put("custom", assetMetadata.getCustomMetadata());
        } catch (Exception e) {
            System.out.println("Exception in readAttributes. " + e);
            return null;
        }

        return attributeMap;
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        throw new RuntimeException("Not Implemented");
    }

    private String getAssetId(Path path) {
        return getAssetIdRecur(path, 0, null);
    }

    // return asset id
    private String getAssetIdRecur(Path path, int index, String parentId) {
        // Recursive end condition
        if (path.getNameCount() <= index) {
            return parentId;
        }

        try {
            if (path.isAbsolute() && index == 0) {
                // For root asset case
                final AssetMetadataList assetMetadatas = storageClient.getTopLevelAssets(
                        new GetTopLevelAssetsRequest(accessToken, containerId));
                for (AssetMetadata assetMetadata : assetMetadatas.getEntries()) {
                    if (assetMetadata.getName().equals(path.getName(0).toString())) {
                        return getAssetIdRecur(path, index + 1, assetMetadata.getAssetId());
                    }
                }
                // Return null for not found
                return null;
            } else {
                // For sub dir case
                final String retrievedJson = storageClient.getCustomAssetMetadata(
                        new GetCustomAssetMetadataRequest(accessToken, containerId, parentId));
                List<CustomJsonUtil.InodeObj> subInodes = CustomJsonUtil.getSubInodes(retrievedJson);

                for (CustomJsonUtil.InodeObj inode : subInodes) {
                    if (inode.name.equals(path.getName(index).toString())) {
                        return getAssetIdRecur(path, index + 1, inode.assetId);
                    }
                }
                // Return null for not found
                return null;
            }
        } catch (Exception e) {
            new RuntimeException("Exception in getParent: " + e);
        }
        return null;
    }

}
