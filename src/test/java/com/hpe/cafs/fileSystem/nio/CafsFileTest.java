/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *******************************************************************************/

package com.hpe.cafs.fileSystem.nio;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by dev on 3/13/16.
 */
public class CafsFileTest {
    @Ignore
    @Test
    public void testCreateRootDirectory() throws Exception{
        final String dirName = "dir_" + new Random().nextInt();
        CafsFile.createDirectory(Paths.get("/" + dirName));
    }

    @Ignore
    @Test
    public void testCreateDirectory() throws Exception{
        final String dirName = "dir_" + new Random().nextInt();
        CafsFile.createDirectory(Paths.get("/test2/" + dirName));
    }

    @Ignore
    @Test
    public void testListRootDir() throws Exception{
        DirectoryStream<Path> dirs = CafsFile.newDirectoryStream(Paths.get("/"));
        Iterator<Path> iterator = dirs.iterator();

        System.out.println("List root dirs: ");
        while (iterator.hasNext()) {
            String fileName = iterator.next().toString();
            Map<String,Object> attrs = CafsFile.readAttributes(Paths.get("/", fileName), null);
            System.out.println(fileName + ".\tType: " + attrs.get("type"));
        }
    }

    @Ignore
    @Test
    public void testSubDir() throws Exception{
        DirectoryStream<Path> dirs = CafsFile.newDirectoryStream(Paths.get("/test/"));
        Iterator<Path> iterator = dirs.iterator();

        System.out.println("List dirs: ");
        while (iterator.hasNext()) {
            String fileName = iterator.next().toString();
            Map<String,Object> attrs = CafsFile.readAttributes(Paths.get("/test", fileName), null);
            System.out.println(fileName + "\ttype: " + attrs.get("type"));
        }
    }

    @Ignore
    @Test
    public void removeDir() throws Exception {
        CafsFile.delete(Paths.get("/dir_281621541"));
    }
}
