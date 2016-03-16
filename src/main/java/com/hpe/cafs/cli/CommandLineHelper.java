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

    private String path = null;

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

        // Get Path String from last arg
        path = args[args.length-1].trim();
    }

    public boolean validateCmdOptions() {
        return ((isMkdir?1:0) + (isLs?1:0) + (isRemove?1:0) == 1);
    }

    private final static String MKDIR = "mkdir";
    private final static String RM = "rm";
    private final static String LS = "ls";
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

    public String getPath() {
        return path;
    }
}
