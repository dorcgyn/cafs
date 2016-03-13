/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.config;

import java.io.IOException;

import org.junit.Test;

/**
 * Created by dev on 3/13/16.
 */
public class ConfigurationFactoryTest {
    @Test
    public void testConfiguration() throws IOException {
        Configuration config = ConfigurationFactory.getConfig();

        System.out.println(config.getAssetContainerId());
    }
}
