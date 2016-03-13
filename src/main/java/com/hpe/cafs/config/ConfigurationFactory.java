/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.config;

import java.io.IOException;

/**
 * Created by dev on 3/13/16.
 */
public class ConfigurationFactory {
    // Empty private Constructor for singleton
    private ConfigurationFactory() {
    }

    private static Configuration configuration;

    private final static String CONFIG_FILE = "config.properties";

    public static Configuration getConfig() throws IOException{
        if (configuration == null) {
            configuration = new Configuration(CONFIG_FILE);
        }
        return configuration;
    }
}
