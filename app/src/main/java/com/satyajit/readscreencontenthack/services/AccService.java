package com.satyajit.readscreencontenthack.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractList;
import java.util.ArrayList;

public class AccService extends AccessibilityService {
    private AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    private AbstractList<AccessibilityNodeInfo> textViewNodes;

    File origin_file;
    FileWriter fileWriter = null;
    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;

    //AccessibilityEvent info;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {



        AccessibilityNodeInfo source = event.getSource();

        if (source == null) {


            return;
        }

        if (!source.getPackageName().toString().equals("com.satyajit.readscreencontenthack")&&!source.getPackageName().toString().equals("com.android.systemui")) {

            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            textViewNodes = new ArrayList<>();

            findChildViews(rootNode);

            for (AccessibilityNodeInfo mNode : textViewNodes) {
                if (mNode.getText() == null) {
                    return;
                }
                String tv1Text = mNode.getText().toString();

                //getSharedPreferences(source.getPackageName().toString(),MODE_PRIVATE).edit().putString()

                Log.d("Event", tv1Text + "");

                JSONObject data;

                try {

                    data = loadJSON("data.json");
                    if (!data.has(source.getPackageName().toString())) {

                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(tv1Text);
                        data.put(source.getPackageName().toString(), jsonArray);


                    } else {

                        JSONArray jsonArray = data.getJSONArray(source.getPackageName().toString());
                        jsonArray.put(tv1Text);
                        data.put(source.getPackageName().toString(), jsonArray);

                    }

                    fileWriter = new FileWriter(origin_file.getAbsoluteFile());
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(data.toString());
                    bufferedWriter.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public JSONObject loadJSON(String file) throws FileNotFoundException, JSONException {

        JSONObject contents;

        FileInputStream fis = getApplicationContext().openFileInput(file);
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


    
    private void findChildViews(AccessibilityNodeInfo parentView) {



        if (parentView == null || parentView.getClassName() == null ) {
            return;
        }

        int childCount = parentView.getChildCount();

        if (childCount == 0 && (parentView.getClassName().toString().contentEquals("android.widget.TextView"))) {
            textViewNodes.add(parentView);
        } else {
            for (int i = 0; i < childCount; i++) {
                findChildViews(parentView.getChild(i));
            }
        }



}

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onServiceConnected() {
        // Set the type of events that this service wants to listen to. Others won't be passed to this service.
        // We are only considering windows state changed event.
        Log.d("IOP","Service Connected");

        origin_file = new File(getApplicationContext().getFilesDir(),"data.json");

        if (!origin_file.exists()){

            try {
                origin_file.createNewFile();
                fileWriter = new FileWriter(origin_file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("{}");
                bufferedWriter.close();



            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        // If you only want this service to work with specific applications, set their package names here. Otherwise, when the service is activated, it will listen to events from all applications.
      //  info.packageNames = new String[] {"com.example.android.myFirstApp", "com.example.android.mySecondApp"};
        // Set the type of feedback your service will provide. We are setting it to GENERIC.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        // Default services are invoked only if no package-specific ones are present for the type of AccessibilityEvent generated.
        // This is a general-purpose service, so we will set some flags
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS; info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY; info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        // We are keeping the timeout to 0 as we don’t need any delay or to pause our accessibility events
        info.notificationTimeout = 0;
        this.setServiceInfo(info);
    }

}