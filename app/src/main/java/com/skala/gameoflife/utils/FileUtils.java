package com.skala.gameoflife.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Skala
 */

public class FileUtils {
    private static final String DIR_BOARDS_ASSETS = "Boards";
    private static final String DIR_BOARDS_EXTERNAL = "GameOfLife";

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

    public static String[] getListBoardFromAssets(Context context) {
        String[] listBoardsName = null;
        try {
            listBoardsName = context.getAssets().list(DIR_BOARDS_ASSETS);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return listBoardsName;
    }

    public static JSONObject getBoardJSONFromFile(String pathNameFile) {
        File file = new File(getDirectoryBoards(), pathNameFile);
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

    public static String[] getListBoardFromExternal() {
        return getDirectoryBoards().list();
    }

    public static void saveBoardToExternal(Context context, String fileName, JSONObject jsonObject) {
        File file = new File(getDirectoryBoards(), fileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            buf.append(jsonObject.toString());
            buf.close();
            addTomMediaScanner(context, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String loadJSONFromAsset(Context context, String pathNameFile) {
        String output = null;
        try {
            InputStream is = context.getAssets().open(DIR_BOARDS_ASSETS + "/" + pathNameFile);
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

    private static File getDirectoryBoards() {
        File file = new File(Environment.getExternalStorageDirectory(), DIR_BOARDS_EXTERNAL);
        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    private static void addTomMediaScanner(Context context, File file) {
        addTomMediaScanner(context, Uri.fromFile(file));
    }

    private static void addTomMediaScanner(Context context, Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        context.sendBroadcast(mediaScanIntent);
    }
}
