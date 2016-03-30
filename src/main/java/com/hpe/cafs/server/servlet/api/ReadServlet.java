/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.server.servlet.api;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hpe.cafs.fileSystem.nio.CafsProvider;
import com.hpe.cafs.server.model.Inode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by dev on 3/17/16.
 */
public class ReadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String[] splits = request.getRequestURI().split("api/v1/read/");
        String assetId = null;
        if (splits.length == 2) {
            assetId = splits[1];
        } else {
            return;
        }

        try {
            String content = CafsProvider.readFile(assetId);
            JSONObject obj= new JSONObject();
            obj.put("content", content);
            response.getWriter().write(obj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
