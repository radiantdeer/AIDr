package com.aidr.aidr;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class DiseaseDB {

    private static String filename = "diseases.json";
    private static JSONArray entries = new JSONArray();

    /*
    private DiseaseDB() {

    } */

    public static void initialize(Context context) {
        AssetManager am = context.getAssets();
        InputStream fin = null;
        try {
            fin = am.open(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fin != null) {
            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(fin);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bytes != null) {
                String in = new String(bytes);
                try {
                    entries = new JSONArray(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /* Search disease by ID.
     * Returns -1 if disease not found */
    public static int getDiseaseIdByName(String name) {
        int out = -1;
        boolean found = false;
        int i = 0;
        int count = entries.length();
        while ((i < count) && !found) {
            JSONObject curr = null;
            try {
                curr = (JSONObject) entries.get(i);
                if ((curr != null) && (((String) curr.get("name")).equals(name))) {
                    found = true;
                    i = ((Number) curr.get("id")).intValue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!found) {
                i++;
            }
        }
        if (found) {
            out = i;
        }
        return out;
    }

    /* Get disease details by ID
    *  Returns null if ID not found */
    public static JSONObject getDiseaseById(int id) {
        JSONObject out = null;
        JSONObject curr = null;
        boolean found = false;
        int i = 0;
        int count = entries.length();

        while ((i < count) && !found) {
            try {
                curr = (JSONObject) entries.get(i);
                if ((curr != null) && (((Number)curr.get("id")).intValue() == id)) {
                    found = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!found) {
                i++;
            }
        }
        if (found) {
            out = curr;
        }
        return out;
    }

}
