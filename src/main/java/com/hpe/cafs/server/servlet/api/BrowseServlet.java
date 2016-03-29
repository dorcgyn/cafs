/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.server.servlet.api;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hpe.cafs.fileSystem.nio.CafsFile;
import com.hpe.cafs.fileSystem.nio.CafsProvider;
import com.hpe.cafs.server.model.Inode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by dev on 3/17/16.
 */
public class BrowseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String[] splits = request.getRequestURI().split("api/v1/browse/");
        String assetId = null;
        if (splits.length == 2) {
            assetId = splits[1];
        }

        List<Inode> inodes = CafsProvider.getAssetChildrenById(assetId);

        JSONObject obj= new JSONObject();
        JSONArray folder = new JSONArray();
        for (Inode inode : inodes) {
            JSONObject asset = new JSONObject();
            asset.put("assetId", inode.getAssetId());
            asset.put("name", inode.getName());
            asset.put("type", inode.getType());
            folder.add(asset);
        }
        obj.put("folder", folder);
        response.getWriter().write(obj.toJSONString());
    }
}
