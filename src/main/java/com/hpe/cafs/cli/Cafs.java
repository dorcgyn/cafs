/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *******************************************************************************/

package com.hpe.cafs.cli;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.hpe.cafs.fileSystem.nio.CafsFile;

/**
 * Created by dev on 3/16/16.
 */
public class Cafs {
    public static void main(String[] args) throws Exception {

        final CommandLineHelper cmd = new CommandLineHelper(args);
        if (!cmd.validateCmdOptions()) {
            System.out.println("Command line option is invalid.");
            return;
        }

        if (cmd.isMkdir()) {
            CafsFile.createDirectory(Paths.get(cmd.getPath()));
            System.out.println("Directory " + cmd.getPath() + " is created successful");
            return;
        }

        if (cmd.isRemove()) {
            CafsFile.delete(Paths.get(cmd.getPath()));
            System.out.println(cmd.getPath() + " is removed successful");
            return;
        }

        if (cmd.isLs()) {
            DirectoryStream<Path> dirs = CafsFile.newDirectoryStream(Paths.get(cmd.getPath()));
            Iterator iterator = dirs.iterator();
            while (iterator.hasNext()) {
                Path fullPath = Paths.get(cmd.getPath(), iterator.next().toString());
                System.out.println(fullPath.toString());
            }
            return;
        }
    }

}
