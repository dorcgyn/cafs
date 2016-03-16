/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.fileSystem.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;

/**
 * Created by dev on 3/13/16.
 */
public class CafsFile {

     public final static String TYPE_DIR = "DIR";
     public final static String TYPE_FILE = "FILE";

     public static Path createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
         provider(dir).createDirectory(dir, attrs);
         return dir;
     }

    /**
     * Creates a directory by creating all nonexistent parent directories first.
     * Unlike the {@link #createDirectory createDirectory} method, an exception
     * is not thrown if the directory could not be created because it already
     * exists.
     */
    public static Path createDirectories(Path dir, FileAttribute<?>... attrs)
            throws IOException
    {
        // attempt to create the directory
        try {
            createAndCheckIsDirectory(dir, attrs);
            return dir;
        } catch (FileAlreadyExistsException x) {
            // file exists and is not a directory
            throw x;
        } catch (IOException x) {
            // parent may not exist or other reason
        }
        SecurityException se = null;
        try {
            dir = dir.toAbsolutePath();
        } catch (SecurityException x) {
            // don't have permission to get absolute path
            se = x;
        }
        // find a decendent that exists
        Path parent = dir.getParent();
        while (parent != null) {
            try {
                provider(parent).checkAccess(parent);
                break;
            } catch (NoSuchFileException x) {
                // does not exist
            }
            parent = parent.getParent();
        }
        if (parent == null) {
            // unable to find existing parent
            if (se != null)
                throw se;
            throw new IOException("Root directory does not exist");
        }

        // create directories
        Path child = parent;
        for (Path name: parent.relativize(dir)) {
            child = child.resolve(name);
            createAndCheckIsDirectory(child, attrs);
        }
        return dir;
    }

    /**
     * Opens a directory, returning a {@link DirectoryStream} to iterate over
     * the entries in the directory. The elements returned by the directory
     * stream's {@link DirectoryStream#iterator iterator} are of type {@code
     * Path}, each one representing an entry in the directory. The {@code Path}
     * objects are obtained as if by {@link Path#resolve(Path) resolving} the
     * name of the directory entry against {@code dir}. The entries returned by
     * the iterator are filtered by matching the {@code String} representation
     * of their file names against the given <em>globbing</em> pattern.
     */
    public static DirectoryStream<Path> newDirectoryStream(Path dir)
            throws IOException
    {
        return new CafsProvider(dir).newDirectoryStream(dir, null);
    }

    /**
     * Used by createDirectories to attempt to create a directory. A no-op
     * if the directory already exists.
     */
    private static void createAndCheckIsDirectory(Path dir,
                                                  FileAttribute<?>... attrs)
            throws IOException
    {
        try {
            createDirectory(dir, attrs);
        } catch (FileAlreadyExistsException x) {
            if (!isDirectory(dir, LinkOption.NOFOLLOW_LINKS))
                throw x;
        }
    }

    /**
     * Tests whether a file is a directory.
     */
    public static boolean isDirectory(Path path, LinkOption... options) {
        try {
            return readAttributes(path, "type", options).get("type").equals(TYPE_DIR);
        } catch (IOException ioe) {
            return false;
        }
    }

    /**
     * Reads a set of file attributes as a bulk operation.
     */
    public static Map<String,Object> readAttributes(Path path, String attributes,
                                                    LinkOption... options)
            throws IOException
    {
        return provider(path).readAttributes(path, attributes, options);
    }

    /**
     * Deletes a file.
     */
    public static void delete(Path path) throws IOException {
        provider(path).delete(path);
    }

    private static FileSystemProvider provider(Path path) {
        return new CafsProvider(path);
    }
}
