/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by dev on 3/13/16.
 */
public class CustomJsonUtil {
    private final static String SUB_DIRS = "subdir";

    private final static String SUB_NAME = "name";
    private final static String SUB_ID = "id";

    public final static String INIT_DIR_CUSTOM_METADATA;
    static {
        JSONObject obj= new JSONObject();
        JSONArray inodes = new JSONArray();
        obj.put(SUB_DIRS, inodes);
        INIT_DIR_CUSTOM_METADATA = obj.toJSONString();
    }

    public static JSONObject getCustomJson(final String jsonStr){
        try {
            JSONParser parser = new JSONParser();
            return (JSONObject)parser.parse(jsonStr);
        } catch (ParseException e) {
            System.out.println("Parsel Json fail: " + e);
            throw new RuntimeException("Parsel Json fail: " + e);
        }
    }


    public static String addSubInode(String origJson, String subDirName, String subDirId) {
        // get parent dir
        JSONObject obj = getCustomJson(origJson);
        JSONArray subDirs = (JSONArray)obj.get(SUB_DIRS);

        // create new dir json obj
        JSONObject dir = new JSONObject();
        dir.put(SUB_NAME, subDirName);
        dir.put(SUB_ID, subDirId);

        // add in parent and return json string
        subDirs.add(dir);
        return obj.toJSONString();
    }

    public static String removeSubInode(String origJson, String assetId) {
        // get parent dir
        JSONObject obj = getCustomJson(origJson);
        JSONArray subDirs = (JSONArray)obj.get(SUB_DIRS);

        // remove sub inode from json obj
        for (int i = 0; i < subDirs.size(); i++) {
            JSONObject subInode = (JSONObject)subDirs.get(i);
            String inodeId = (String)subInode.get(SUB_ID);
            if (inodeId.equals(assetId)) {
                subDirs.remove(i);
                break;
            }
        }

        // return json string
        return obj.toJSONString();
    }

    public static List<InodeObj> getSubInodes(String origJson) {
        JSONObject obj = getCustomJson(origJson);
        JSONArray subDirs = (JSONArray)obj.get(SUB_DIRS);
        Iterator iterator = subDirs.iterator();

        ArrayList<InodeObj> dirArray = new ArrayList<InodeObj>();
        while (iterator.hasNext()) {
            JSONObject dirObject = (JSONObject)iterator.next();

            InodeObj inode = new InodeObj();
            inode.assetId = dirObject.get(SUB_ID).toString();
            inode.name = dirObject.get(SUB_NAME).toString();

            dirArray.add(inode);
        }
        return dirArray;
    }

    public static class InodeObj {
        public String assetId;
        public String name;
    }
}
