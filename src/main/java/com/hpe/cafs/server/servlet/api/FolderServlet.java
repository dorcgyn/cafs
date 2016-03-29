/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.server.servlet.api;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hpe.caf.storage.sdk.model.AssetMetadata;
import com.hpe.cafs.fileSystem.nio.CafsProvider;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by dev on 3/17/16.
 */
public class FolderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        BufferedReader reader = request.getReader();

        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject)parser.parse(reader);
            String parentId = (String)obj.get("parentId");
            String name = (String)obj.get("name");
            AssetMetadata assetMetadata = CafsProvider.createFolder(name, parentId);

            // Generate response json
            JSONObject asset = new JSONObject();
            asset.put("assetId", assetMetadata.getAssetId());
            asset.put("name", assetMetadata.getName());
            asset.put("type", assetMetadata.getType());
            response.getWriter().write(asset.toJSONString());
        } catch(Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error in process folder request.");
        }
    }
}
