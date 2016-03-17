/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.server.servlet;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hpe.cafs.fileSystem.nio.CafsFile;

/**
 * Created by dev on 3/17/16.
 */
public class LsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        /*
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>LS</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());*/
        DirectoryStream<Path> dirs = CafsFile.newDirectoryStream(Paths.get("/"));
        Iterator<Path> iterator = dirs.iterator();

        ArrayList<String> paths = new ArrayList();
        while (iterator.hasNext()) {
            paths.add(iterator.next().toString());
        }
        request.setAttribute("paths", paths);

        RequestDispatcher rd = request.getRequestDispatcher("test.jsp");
        rd.forward(request, response);
    }
}
