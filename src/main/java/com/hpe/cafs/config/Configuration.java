/*******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
********************************************************************************/

package com.hpe.cafs.config;

import com.hpe.caf.identity.sdk.IdentityClient;

/**
 * Created by dev on 3/11/16.
 */
public class Configuration {

    public final static IdentityClient identityClient = new IdentityClient(
            "https://a1-dev-hap010.lab.lynx-connected.com:37500",   // IDM URL
            "https://a1-dev-hap3048.lab.lynx-connected.com:8443",   // KeyCloak URL
            "direct-grant-client",                                  // KeyCloak Client Id
            "caf");                                                 // KeyCloak Realm

    public static String getAccessToken() {
        return identityClient.directGrantAccessToken(
                "groovy@hp.com",                           // IDM Username
                "C@FSt0rage#Test",                         // IDM Password
                "343e9c6f-eb00-442e-a236-e0c3868ae81a");   // KeyCloak Client Secret
    }

    public final static String SERVER_NAME = "a1-dev-hap010.lab.lynx-connected.com";
    public final static String PORT = "444";
    /*
            .serverName("a1-dev-hap010.lab.lynx-connected.com")  // Hostname of CAF Storage gateway server
            .port("444")                             // The network port of the named server
            .downloadLocation("/tmp")                // Full path of the place to put downloaded files
            .downloadType("chunked")                 // Type of download to perform (only "chunked" implemented)
            .fileToUpload("/home/chris/pgadmin.log") // Full path of the file to upload
            .uploadType("chunked")                   // Type of upload to perform (either "oneshot" or "chunked")
            .accessToken(accessToken)                // The IDM access token for a CAF Storage user
            .build();*/
}
