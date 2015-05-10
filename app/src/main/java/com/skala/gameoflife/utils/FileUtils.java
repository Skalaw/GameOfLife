package com.skala.gameoflife.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Skala
 */

public class FileUtils {
    private static final String DIR_BOARDS = "Boards";

    public static JSONObject getBoardJSONFromAssets(Context context, String pathNameFile) {
        String output = loadJSONFromAsset(context, pathNameFile);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static String loadJSONFromAsset(Context context, String pathNameFile) {
        String output = null;
        try {
            InputStream is = context.getAssets().open(DIR_BOARDS + "/" + pathNameFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            output = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return output;
    }

    public static JSONObject getBoardJSONFromFile(String pathNameFile) {
        File file = new File(DIR_BOARDS, pathNameFile);
        if (!file.exists()) {
            return null;
        }

        String output = loadBoard(file);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static String loadBoard(File file) {
        String output = null;
        try {
            FileInputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            output = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }
}
