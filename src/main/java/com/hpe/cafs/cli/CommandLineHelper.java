/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *******************************************************************************/

package com.hpe.cafs.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Created by dev on 3/16/16.
 */
public class CommandLineHelper {
    private boolean isMkdir = false;
    private boolean isLs = false;
    private boolean isRemove = false;
    private boolean isPut = false;
    private boolean isCat = false;
    private boolean isGet = false;
    private boolean isCopyToLocal = false;
    private boolean isCopyFromLocal = false;
    private boolean isHelp = false;

    private String path = null; 
    private String sourceFile = null;
    private String destinationFile = null;

    public CommandLineHelper(String[] args) throws Exception {
        CommandLine cmdLine;
        final CommandLineParser parser = new DefaultParser();

        try {
            final Options options = getCommandOptions();
            cmdLine = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            System.err.println("unable to parse command line options: "
                    + e.toString());
            throw e;
        }

        isMkdir = cmdLine.hasOption(MKDIR);
        isLs = cmdLine.hasOption(LS);
        isRemove = cmdLine.hasOption(RM);
        isCat = cmdLine.hasOption(CAT);
        isCopyToLocal = cmdLine.hasOption(COPYTOLOCAL);
        isCopyFromLocal = cmdLine.hasOption(COPYFROMLOCAL);
        isHelp = cmdLine.hasOption(HELP);
        
        if (isCopyToLocal || isCopyFromLocal) {
        	if (args.length == 3) {
        		sourceFile = args[1];
        		destinationFile = args[2];
        	}
        } else {
        	// Get Path String from last arg
        	if (args.length > 1) {
        		path = args[args.length-1].trim();
        	}
        }
    }

    public boolean validateCmdOptions() {
    	int numOfCmdOptions = 0;
    	numOfCmdOptions = (isMkdir?1:0) + (isLs?1:0) + (isRemove?1:0) + (isCat?1:0) +
    					  (isCopyToLocal?1:0) + (isCopyFromLocal?1:0) + (isHelp?1:0);
    	if (numOfCmdOptions != 1) {
    		// Only one command option is allowed per invocation of CLI.
    		return false;
    	}
    	
    	// Validate the parameters of the specified option
    	if (isCopyToLocal || isCopyFromLocal) {
    		if ( (sourceFile == null) || (destinationFile == null) ) {
    			// CopyToLocal and copyFromLocal options require two parameters
    			return false;
    		}
    	}
    	
    	return true;
    	
        // return ((isMkdir?1:0) + (isLs?1:0) + (isRemove?1:0) + (isCat?1:0)== 1);
    }

    private final static String MKDIR = "mkdir";
    private final static String RM = "rm";
    private final static String LS = "ls";
    private final static String CAT = "cat";
    private final static String COPYTOLOCAL = "copyToLocal";
    private final static String COPYFROMLOCAL = "copyFromLocal";
    private final static String HELP = "help";

    private Options getCommandOptions() {
        final Options options = new Options();
        // mkdir cmd
        options.addOption(Option.builder(MKDIR).longOpt(MKDIR).desc("Make a new directory")
                .build());
        // delete cmd
        options.addOption(Option.builder(RM).longOpt(RM).desc("Delete a file/dir")
                .build());

        // ls cmd
        options.addOption(Option.builder(LS).longOpt(LS).desc("List directory")
                .build());

        // cat cmd
        options.addOption(Option.builder(CAT).longOpt(CAT).desc("Cat file")
                .build());
        
        // copyToLocal cmd
        options.addOption(Option.builder(COPYTOLOCAL).longOpt(COPYTOLOCAL).desc("Copy CAFS file to local file")
        		.build());

        // copyFromLocal cmd
        options.addOption(Option.builder(COPYFROMLOCAL).longOpt(COPYFROMLOCAL).desc("Copy local file to CAFS file")
        		.build());

        // help command
        options.addOption(Option.builder(HELP).longOpt(HELP).desc("help")
        		.build());
        
        return options;
    }

    public boolean isLs() {
        return isLs;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public boolean isMkdir() {
        return isMkdir;
    }

    public boolean isCat() {
        return isCat;
    }
    
    public boolean isCopyToLocal() {
    	return isCopyToLocal;
    }
    
    public boolean isCopyFromLocal() {
    	return isCopyFromLocal;
    }
    
    public boolean isHelp() {
    	return isHelp;
    }

    public String getPath() {
        return path;
    }

    public String getSourceFile() {
    	return sourceFile;
    }
    
    public String getDestinationFile() {
    	return destinationFile;
    }
}
