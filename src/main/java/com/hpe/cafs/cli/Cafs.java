/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *******************************************************************************/

package com.hpe.cafs.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

import com.hpe.cafs.fileSystem.nio.CafsFile;
import org.apache.commons.io.IOUtils;

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

        if (cmd.isHelp()) {
        	printHelp();
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
                printDirectoryEntry(fullPath);
            }
            return;
        }

        if (cmd.isCat()) {
            if (CafsFile.isDirectory(Paths.get(cmd.getPath()))) {
                System.out.println(cmd.getPath() + " is a directory.");
                return;
            }
            BufferedReader bufferedReader = CafsFile.newBufferedReader(Paths.get(cmd.getPath()));
            String str = IOUtils.toString(bufferedReader);
            System.out.println(str);
            bufferedReader.close();
            return;
        }
        
        if (cmd.isCopyToLocal()) {
            if (CafsFile.isDirectory(Paths.get(cmd.getSourceFile()))) {
                System.out.println(cmd.getPath() + " is a directory.");
                return;
            }

            System.out.println("CopyToLocal: " 
        			+ " Source File: "
        			+ cmd.getSourceFile()
        			+ " Destination File: "
        			+ cmd.getDestinationFile());
            
            copyFile(cmd.getSourceFile(), cmd.getDestinationFile(), true);
        }
        
        if (cmd.isCopyFromLocal()) {
        	// CafsFile.delete(Paths.get(cmd.getDestinationFile()));
            /*if (CafsFile.isDirectory(Paths.get(cmd.getDestinationFile()))) {
                System.out.println(cmd.getPath() + " is a directory.");
                return;
            }*/
            
            copyFile(cmd.getSourceFile(), cmd.getDestinationFile(), false);
        }
    }
    
    private static void printHelp() {
    	String helpText = "Command Line Interface for CAFS \n" +
    					  "Commands: \n" +
    					  "\tmkdir <fullPathName>: Create directory corresponding to given fullPathName on CAFS.\n" +
    					  "\trm <fullPathName>: Remove directory corresponding to given fullPathName from CAFS.\n" +
    					  "\tls <fullPathName>: List name, size, and type of files in the directory corresponding to given fullPathName in CAFS.\n" +
    					  "\tcat <fullPathName>: Display contents of the file corresponding to given fullPathName on CAFS.\n" +
    					  "\tcopyFromLocal <sourcePathName> <destinationPathName>: Copy the source file (corresponding to sourcePathName on source operating system) to destination file (corresponding to destinationPathName on CAFS).\n" +
    					  "\tcopyToLocal <sourcePathName> <destinationPathName>: Copy the source file (corresponding to sourcePathName on CAFS) to destination file (corresponding to destinationPathName on destination operating system).\n";
		System.out.println(helpText);
	}

	private static void copyFile(String sourceFile, String destinationFile, boolean fromCafs) {
    	BufferedReader br = null;
    	BufferedWriter bw = null;
    	
    	try {
    		if (fromCafs) {
    			br = CafsFile.newBufferedReader(Paths.get(sourceFile));
    			bw = new BufferedWriter(new FileWriter(destinationFile));
    		} else {
    			br = new BufferedReader(new FileReader(sourceFile));
    			bw = CafsFile.newBufferedWriter(Paths.get(destinationFile));
    		}
    		// Read data from br and write it to bw
    		for (int i = br.read(); i != -1; i = br.read()) {
    			bw.append((char)i);
    		}
    	} catch (FileNotFoundException e) {
    		System.err.println("Error opening source file: " + sourceFile + "Details: " + e);
    	} catch (IOException e) {
    		System.err.println("Error while opening/writing  " + destinationFile + "Details: " + e);
    	} catch (Exception e) {
    		System.err.println("Unknown exception : Details: " + e);
    	} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
					System.err.println("Error while closing destination file");
				}
			}
			
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					System.err.println("Error while closing source file");
				}
			}
       	}
    }
    
    private static void printDirectoryEntry(Path fullPath) {
    	String fileType = "-";
    	String filePermissions = "rwxrwxrwx"; // TODO: Get actual permissions of this file.
    	
    	try {
			Map<String, Object> attributeMap = CafsFile.readAttributes(fullPath, null);

			if (attributeMap != null) {
				if (attributeMap.containsKey("type")) {
					Object typeObj = attributeMap.get("type");
					if (typeObj != null) {
						String typeString = typeObj.toString();
						if (typeString.compareToIgnoreCase("DIR") == 0) {
							fileType = "d";
						}
					}
					
					// System.out.print("\ttype: " + attributeMap.get("type"));
				}
			}
			
			System.out.print(fileType + filePermissions);
			System.out.print("\t");;

			if (attributeMap != null) {
				if (attributeMap.containsKey("size")) {
					System.out.print(attributeMap.get("size"));
				}
			} else {
				System.out.print("0");
			}
			
			System.out.print("\t");
			System.out.println(fullPath.toString());
			
			// System.out.println("Number of attributes: " + attributeMap.size());
			// System.out.println("type: " + attributeMap.get("type")
		} catch (IOException e) {
			System.out.println("Error getting attribute information");
			// e.printStackTrace();
		}
    }
}
