package com.satyajit.readscreencontenthack.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class jsonLoader {

    String file;
    Context context;

    public jsonLoader(Context context, String file ){

        this.file = file;
        this.context = context;

    }

    public JSONObject loadJSONFromAsset() throws FileNotFoundException, JSONException {

        JSONObject contents;

        FileInputStream fis = context.openFileInput(file);
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {

            contents = new JSONObject(stringBuilder.toString());
        }

        return contents;

    }

    public JSONArray returnJSONArray(String arrayName) throws JSONException, FileNotFoundException {


        return loadJSONFromAsset().getJSONArray(arrayName);



    }

    public  String returnStringValue(String event_name, String value) throws  Exception{

        return loadJSONFromAsset().getJSONObject(event_name).get(value).toString();

    }

}
