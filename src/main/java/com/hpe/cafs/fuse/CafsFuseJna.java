/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 ******************************************************************************
 */

package com.hpe.cafs.fuse;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.hpe.cafs.fileSystem.nio.CafsFile;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import net.fusejna.FlockCommand;
import net.fusejna.FuseFilesystem;
import net.fusejna.StructFlock;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.StructStatvfs;
import net.fusejna.StructTimeBuffer;
import net.fusejna.XattrFiller;
import net.fusejna.XattrListFiller;
import net.fusejna.types.TypeMode;

/**
 * Created by dev on 3/20/16.
 */
public class CafsFuseJna extends FuseFilesystem{
    @Override
    public int access(String s, int i) {
        return 0;
    }

    @Override
    public void afterUnmount(File file) {

    }

    @Override
    public void beforeMount(File file) {

    }

    @Override
    public int bmap(String s, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int chmod(String s, TypeMode.ModeWrapper modeWrapper) {
        return 0;
    }

    @Override
    public int chown(String s, long l, long l2) {
        return 0;
    }

    @Override
    public int create(String s, TypeMode.ModeWrapper modeWrapper, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public void destroy() {

    }

    @Override
    public int fgetattr(String s, StructStat.StatWrapper statWrapper, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int flush(String s, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int fsync(String s, int i, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int fsyncdir(String s, int i, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int ftruncate(String s, long l, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int getattr(String s, StructStat.StatWrapper statWrapper) {
        // stop searching for unused thing....
        if (isUseLessFile(s)) {
            return -ErrorCodes.ENOENT();
        }

        try {
            if (s.equals(File.separator)) {
                statWrapper.setMode(TypeMode.NodeType.DIRECTORY);
                return 0;
            } else {
                boolean isDir = CafsFile.isDirectory(Paths.get(s));
                if (isDir) {
                    statWrapper.setMode(TypeMode.NodeType.DIRECTORY);
                    return 0;
                } else {
                    statWrapper.setMode(TypeMode.NodeType.FILE);
                    return 0;
                }
            }
        } catch (Exception e) {
            return -ErrorCodes.ENOENT();
        }
    }

    private boolean isUseLessFile(String s) {
        if (s.contains("/tls") ||
                s.contains("/x86_64") ||
                s.contains("/libselinux.so.1") ||
                s.contains("/libcap.so.2") ||
                s.contains("/libacl.so.1") ||
                s.contains("/libc.so.6") ||
                s.contains("/libpcre.so.1") ||
                s.contains("/liblzma.so.5") ||
                s.contains("/libdl.so.2") ||
                s.contains("/libattr.so.1") ||
                s.contains("/libpthread.so.0") ||
                s.contains("/.Trash-1000") ||
                s.contains("/.Trash") ||
                s.contains("/libnss_files.so.2")) {
            return true;
        }
        return false;
    }

    @Override
    protected String getName() {
        return null;
    }

    @Override
    protected String[] getOptions() {
        return new String[0];
    }

    @Override
    public int getxattr(String s, String s2, XattrFiller xattrFiller, long l, long l2) {
        xattrFiller.set("value1".getBytes());
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public int link(String s, String s2) {
        return 0;
    }

    @Override
    public int listxattr(String s, XattrListFiller xattrListFiller) {
        if (!s.equals("/")) {
            return - ErrorCodes.ENOTSUP();
        }
        xattrListFiller.add("attr1");
        return 0;
    }

    @Override
    public int lock(String s, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper, FlockCommand flockCommand, StructFlock.FlockWrapper flockWrapper) {
        return 0;
    }

    @Override
    public int mkdir(String s, TypeMode.ModeWrapper modeWrapper) {
        try {
            CafsFile.createDirectory(Paths.get(s));
        } catch(Exception e) {
            System.out.println("mkdir fails as: " + e);
            return -ErrorCodes.EIO();
        }
        return 0;
    }

    @Override
    public int mknod(String s, TypeMode.ModeWrapper modeWrapper, long l) {
        return 0;
    }

    @Override
    public int open(String s, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int opendir(String s, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int read(String s, ByteBuffer byteBuffer, long l, long l2, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    @Override
    public int readdir(String s, DirectoryFiller directoryFiller) {
        DirectoryStream<Path> dirs = null;

        directoryFiller.add(".");
        directoryFiller.add("..");

        try {
            dirs = CafsFile.newDirectoryStream(Paths.get(s));
        } catch(Exception e) {
            System.out.println("read dir fails as: " + e);
        }

        if (dirs == null || dirs.iterator() == null) return 0;

        // loop custom directories
        Iterator iterator = dirs.iterator();
        while (iterator.hasNext()) {
            directoryFiller.add(iterator.next().toString());
        }
        return 0;
    }

    @Override
    public int readlink(String s, ByteBuffer byteBuffer, long l) {
        return -ErrorCodes.ENOENT();
    }

    @Override
    public int release(String s, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        try {
            CafsFile.delete(Paths.get(s));
        } catch (Exception e) {
            return -ErrorCodes.ENOENT();
        }
        return 0;
    }

    @Override
    public int releasedir(String s, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        try {
            CafsFile.delete(Paths.get(s));
        } catch (Exception e) {
            return -ErrorCodes.ENOENT();
        }
        return 0;
    }

    @Override
    public int removexattr(String s, String s2) {
        return 0;
    }

    @Override
    public int rename(String s, String s2) {
        return 0;
    }

    @Override
    public int rmdir(String s) {
        return 0;
    }

    @Override
    public int setxattr(String s, String s2, ByteBuffer byteBuffer, long l, int i, int i2) {
        return 0;
    }

    @Override
    public int statfs(String s, StructStatvfs.StatvfsWrapper statvfsWrapper) {
        return 0;
    }

    @Override
    public int symlink(String s, String s2) {
        return 0;
    }

    @Override
    public int truncate(String s, long l) {
        return 0;
    }

    @Override
    public int unlink(String s) {
        return 0;
    }

    @Override
    public int utimens(String s, StructTimeBuffer.TimeBufferWrapper timeBufferWrapper) {
        return 0;
    }

    @Override
    public int write(String s, ByteBuffer byteBuffer, long l, long l2, StructFuseFileInfo.FileInfoWrapper fileInfoWrapper) {
        return 0;
    }

    public static void main(String[] args) throws Exception{
        CafsFuseJna fuseJna = new CafsFuseJna();
        try {
            fuseJna.log(true).mount("/tmp/cafs/caf1");
        } finally {
            fuseJna.unmount();
        }
    }
}
