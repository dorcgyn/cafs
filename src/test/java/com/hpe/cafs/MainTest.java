/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs;

import java.util.Random;

import com.hpe.caf.storage.sdk.StorageClient;
import com.hpe.caf.storage.sdk.model.AssetContainer;
import com.hpe.caf.storage.sdk.model.requests.CreateAssetContainerRequest;
import com.hpe.cafs.config.Configuration;
import com.hpe.cafs.config.ConfigurationFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by dev on 3/13/16.
 */
public class MainTest {
    @Ignore
    @Test
    public void testCreateContainer() throws Exception{
        Configuration config = ConfigurationFactory.getConfig();

        final StorageClient storageClient = new StorageClient(config.getServerName(), config.getPort());
        final String containerName = "Container_" + new Random().nextInt();

        final CreateAssetContainerRequest createAssetContainerRequest =
                new CreateAssetContainerRequest(config.getAccessToken(), containerName);
        createAssetContainerRequest.setAllowHardDelete(true);
        final AssetContainer container = storageClient.createAssetContainer(createAssetContainerRequest);
        final String containerId = container.getContainerId();
        //final WrappedKey containerKey = container.getContainerKey();
        System.out.println("Created asset container \"" + containerName + "\" with Id " + containerId);
    }
}
