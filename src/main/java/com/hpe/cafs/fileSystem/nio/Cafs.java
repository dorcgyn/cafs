/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *******************************************************************************/

package com.hpe.cafs.fileSystem.nio;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

/**
 * Created by dev on 3/13/16.
 */
public class Cafs extends FileSystem{


    public static Cafs getFileSystem() {
        return new Cafs();
    }

    // Not Use
    @Override
    public FileSystemProvider provider() {
        return null;
    }

    // Not Use
    @Override
    public void close() throws IOException {

    }

    // implement health check
    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public String getSeparator() {
        return "/";
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        System.out.println("Not Implement getRootDirectories");
        return null;
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        System.out.println("Not Implement getFileStores");
        return null;
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        System.out.println("Not Implement supportedFileAttributeViews");
        return null;
    }

    @Override
    public Path getPath(String first, String... more) {
        System.out.println("Not Implement getPath");
        return null;
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        System.out.println("Not Implement getPathMatcher");
        return null;
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        System.out.println("Not Implement getUserPrincipalLookupService");
        return null;
    }

    @Override
    public WatchService newWatchService() throws IOException {
        System.out.println("Not Implement newWatchService");
        return null;
    }
}
