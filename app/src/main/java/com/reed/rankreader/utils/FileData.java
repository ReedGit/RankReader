package com.reed.rankreader.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class FileData {

    public static JSONObject initData(InputStream is) {
        JSONObject dataJson = new JSONObject();
        try {
            int length = is.available();
            byte[] bytes = new byte[length];
            int result = is.read(bytes);
            if (result != -1) {
                String dataString = new String(bytes);
                dataJson = new JSONObject(dataString);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return dataJson;
    }

}
