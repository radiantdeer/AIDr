package com.aidr.aidr;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class DiseaseDB {

    private static String diseaseFilename = "diseases.json";
    private static String drugFilename = "drugs.json";
    private static String tipsDir = "tips/";
    private static JSONArray diseaseEntries = new JSONArray();
    private static JSONArray drugEntries = new JSONArray();
    private static Context context;

    /*
    private DiseaseDB() {

    } */

    public static void initialize(Context context) {
        DiseaseDB.context = context;
        System.out.println("Initializing DiseaseDB...");
        AssetManager am = DiseaseDB.context.getAssets();

        /* Loading disease database */
        InputStream fin = null;
        try {
            fin = am.open(diseaseFilename);
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
                    diseaseEntries = new JSONArray(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* Loading drug database */
        fin = null;
        try {
            fin = am.open(drugFilename);
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
                    drugEntries = new JSONArray(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /* Search disease by ID.
     * Returns -1 if disease not found */
    public static int getDiseaseIdByNameIgnoreCase(String name) {
        String lowercase = name.toLowerCase();
        int out = -1;
        boolean found = false;
        int i = 0;
        int count = diseaseEntries.length();
        while ((i < count) && !found) {
            JSONObject curr = null;
            try {
                curr = (JSONObject) diseaseEntries.get(i);
                if ((curr != null) && (((String) curr.get("name")).toLowerCase().equals(lowercase))) {
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

    /* Search disease by ID.
     * Returns -1 if disease not found */
    public static int getDiseaseIdByName(String name) {
        int out = -1;
        boolean found = false;
        int i = 0;
        int count = diseaseEntries.length();
        while ((i < count) && !found) {
            JSONObject curr = null;
            try {
                curr = (JSONObject) diseaseEntries.get(i);
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
        int count = diseaseEntries.length();

        while ((i < count) && !found) {
            try {
                curr = (JSONObject) diseaseEntries.get(i);
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

    /* Get disease name by ID
     * Returns an empty string if ID not found */
    public static String getDiseaseNameById(int id) {
        String out = "";
        boolean found = false;
        int i = 0;
        int count = diseaseEntries.length();

        while ((i < count) && !found) {
            try {
                JSONObject curr = (JSONObject) diseaseEntries.get(i);
                if ((curr != null) && (((Number)curr.get("id")).intValue() == id)) {
                    found = true;
                    out = curr.getString("name");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!found) {
                i++;
            }
        }
        return out;
    }

    /* Get drug details by ID
     * Returns null if ID not found */
    public static JSONObject getDrugById(int id) {
        JSONObject out = null;
        JSONObject curr = null;
        boolean found = false;
        int i = 0;
        int count = drugEntries.length();

        while ((i < count) && !found) {
            try {
                curr = (JSONObject) drugEntries.get(i);
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

    /* Get drugs viable for a certain disease
     * If disease is not valid, it will also returns an empty array */
    public static JSONArray getDrugsByDiseaseId(int id) {
        JSONArray out = new JSONArray();
        JSONObject disease = getDiseaseById(id);

        if (disease != null) {
            try {
                JSONArray drugList = disease.getJSONArray("drugs");
                for (int i = 0;i < drugList.length(); i++) {
                    JSONObject temp = getDrugById(drugList.getInt(i));
                    if (temp != null) {
                        out.put(temp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return out;
    }

    /* Loads tips from file for a certain disease
       If disease is not valid, it will return null */
    public static String getTipsByDiseaseId(int id) {
        JSONObject disease = getDiseaseById(id);
        String out = null;

        if (disease != null) {
            try {
                String tipsFilename = disease.getString("tips");
                if (!tipsFilename.equals("")) {
                    AssetManager am = context.getAssets();
                    InputStream fin = am.open(tipsDir + tipsFilename);
                    byte[] bytes = IOUtils.toByteArray(fin);
                    out = new String(bytes);
                }
            } catch (Exception e) {
                // ignore, move along... Because it shouldn't happen
                e.printStackTrace();
            }
        }

        return out;
    }

}
