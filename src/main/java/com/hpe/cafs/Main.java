/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
********************************************************************************/

package com.hpe.cafs;

import java.util.Random;

import com.hpe.caf.storage.sdk.StorageClient;
import com.hpe.caf.storage.sdk.model.AssetContainer;
import com.hpe.caf.storage.sdk.model.requests.CreateAssetContainerRequest;
import com.hpe.cafs.config.Configuration;

/**
 * Created by dev on 3/7/16.
 */
public class Main {
    
    public static void main(String [] args) throws Exception{
        final StorageClient storageClient = new StorageClient(Configuration.SERVER_NAME, Configuration.PORT);
        final String containerName = "Container_" + new Random().nextInt();
        final AssetContainer container = storageClient.createAssetContainer(new CreateAssetContainerRequest(
                 Configuration.getAccessToken(), containerName));
        final String containerId = container.getContainerId();
        //final WrappedKey containerKey = container.getContainerKey();
        System.out.println("Created asset container \"" + containerName + "\" with Id " + containerId);
    }
}
