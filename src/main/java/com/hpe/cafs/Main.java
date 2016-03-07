/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
********************************************************************************/

package com.hpe.cafs;

import java.util.Random;

import com.hpe.caf.storage.sdk.StorageClient;
import com.hpe.caf.storage.sdk.model.AssetContainer;
import com.hpe.caf.storage.sdk.model.requests.CreateAssetContainerRequest;

/**
 * Created by dev on 3/7/16.
 */
public class Main {

    public final static String SERVER_NAME = "localhost";
    public final static String PORT = "8443";
    public final static String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIzYzEwMzkyMi01NTA2LTRmMmEtOTgzMC02YmU5MmFjNjRiYzYiLCJleHAiOjE0NTczODgwNTAsIm5iZiI6MCwiaWF0IjoxNDU3Mzg0NDUwLCJpc3MiOiJodHRwczovL2ExLWRldi1rc2EwNDUubGFiLmx5bngtY29ubmVjdGVkLmNvbTo4NDQzL2F1dGgvcmVhbG1zL2NhZiIsImF1ZCI6ImRpcmVjdC1ncmFudC1jbGllbnQiLCJzdWIiOiJlN2I1NDM4My1hNmMxLTQ1OTQtOGJmNy0wMGE1N2ZkMTk4YzYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJkaXJlY3QtZ3JhbnQtY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6ImMyMDRkMmMzLWRkZTktNDkxMi04YTI1LTg5OTVlOThjOTgxNiIsImNsaWVudF9zZXNzaW9uIjoiMjJmOWQ0MzYtMTBlMC00MzFiLWIxMWEtMDA0ZGRlOTQ2ZmM3IiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsidmlldy1wcm9maWxlIiwibWFuYWdlLWFjY291bnQiXX19LCJlbWFpbCI6ImZhcm91dEBocGUuY29tIiwibmFtZSI6Ik91dCIsImZhbWlseV9uYW1lIjoiT3V0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoidWlkPTQ0ODU2NzEwNDk3NTY3NzQ0LG91PXVzZXJzLG89Mjc0MTU4NjMwNDkwNjEzNzYsb3U9Y3VzdG9tZXJzLGRjPWhld2xldHRwYWNrYXJkZW50ZXJwcmlzZSxkYz1ocCxkYz1jb20ifQ.lYJwodqF5QP5XwDrp5T-ssJK1axUjCoBQZupPlbCo_Tp2tGT2sJ_szr7EN8q0LCU76irSaFxNJ7iiA83le1CoG_954_yOVTxRMthzjBIAXJrtcu3kdp7khlidvE2RWmKeTMGU7dUJIrHWr2hYloLe76EExQV40h9ydFcguFHanD6949mPnyit5ktygit7npYSmX6LkYh7TSWgjvua2mT5hcZcUeb8SflDE95lTpJ35k9x7d1ErV9dJV6U86R_BSPJ0VbgXwpAUQgW0K8xrZbq3Gxjc-teE4psGC3Nltd87O7TH_dzdXPvrKF5xAfFuvbsK56ovs5WR-bThaErhNdlQ";

    public static void main(String [] args) throws Exception{
        final StorageClient storageClient = new StorageClient(SERVER_NAME, PORT);
        final String containerName = "Container_" + new Random().nextInt();
        final AssetContainer container = storageClient.createAssetContainer(new CreateAssetContainerRequest(
                 ACCESS_TOKEN, containerName));
        final String containerId = container.getContainerId();
        //final WrappedKey containerKey = container.getContainerKey();
        System.out.println("Created asset container \"" + containerName + "\" with Id " + containerId);
    }
}
