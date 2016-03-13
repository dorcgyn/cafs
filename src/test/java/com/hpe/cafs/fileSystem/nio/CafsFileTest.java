/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.fileSystem.nio;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Random;

import org.junit.Test;

/**
 * Created by dev on 3/13/16.
 */
public class CafsFileTest {
    @Test
    public void testCreateRootDirectory() throws Exception{
        final String dirName = "dir_" + new Random().nextInt();
        CafsFile.createDirectory(Paths.get("/" + dirName));
    }

    @Test
    public void testCreateDirectory() throws Exception{
        final String dirName = "dir_" + new Random().nextInt();
        CafsFile.createDirectory(Paths.get("/test2/" + dirName));
    }

    @Test
    public void testListRootDir() throws Exception{
        DirectoryStream<Path> dirs = CafsFile.newDirectoryStream(Paths.get("/"));
        Iterator<Path> iterator = dirs.iterator();

        System.out.println("List root dirs: ");
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
    }

    @Test
    public void testListDir() throws Exception{
        DirectoryStream<Path> dirs = CafsFile.newDirectoryStream(Paths.get("/test2/"));
        Iterator<Path> iterator = dirs.iterator();

        System.out.println("List dirs: ");
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
    }
}
