package com.reed.rankreader.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileData {

    public static JSONObject initArticle(InputStream is) {
        InputStreamReader isr;
        JSONObject content = new JSONObject();
        JSONArray utArray = new JSONArray();
        JSONObject lsJO = new JSONObject();
        try {
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            boolean unitFlag = false;
            boolean lessonFlag = false;
            String unit = "";
            String lesson = "";
            String article = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("Unit ")) {
                    if (unitFlag) {
                        if (!unit.equals("")) {
                            if (content.has(unit)) {
                                for (int i = 0; i < utArray.length(); i++) {
                                    content.getJSONArray(unit).put(utArray.get(i));
                                }
                            } else {
                                content.put(unit, utArray);
                            }
                        }
                        utArray = new JSONArray();
                    }
                    unitFlag = true;
                    unit = line.substring(line.indexOf("Unit")).trim();
                } else if (line.contains("Lesson ")) {
                    if (lessonFlag) {
                        lsJO.put(lesson, article);
                        utArray.put(lsJO);
                        article = "";
                        lsJO = new JSONObject();
                    }
                    lessonFlag = true;
                    lesson = line.substring(line.indexOf("Lesson")).trim();
                } else {
                    article += line + "\n";
                }
            }
            lsJO.put(lesson, article);
            utArray.put(lsJO);
            for (int i = 0; i < utArray.length(); i++) {
                content.getJSONArray(unit).put(utArray.get(i));
            }
            is.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static JSONObject initRank(InputStream is){
        JSONObject rankJson = new JSONObject();
        try {
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String str = line.trim();
                    int length = str.length();
                    String rank = str.substring(length-1);
                    String word = str.substring(0, length-2).trim();
                    if (rankJson.has(rank)) {
                        rankJson.getJSONArray(rank).put(word);
                    } else {
                        rankJson.put(rank, new JSONArray().put(word));
                    }
                }
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rankJson;
    }
}
