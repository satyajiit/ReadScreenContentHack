package com.satyajit.readscreencontenthack.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.satyajit.readscreencontenthack.services.AccService;
import com.satyajit.readscreencontenthack.adapters.ItemsAdapter;
import com.satyajit.readscreencontenthack.R;
import com.satyajit.readscreencontenthack.models.SingleItemModel;
import com.satyajit.readscreencontenthack.utils.jsonLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {


    RecyclerView listRecycler;
    private List<SingleItemModel> itemList = new ArrayList<>();
    ItemsAdapter adapter;
    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      check(); //Acc activation check

        setUpObserver();

        initUI();

        setUpRecycler();

    }

    void setUpRecycler(){



        adapter = new ItemsAdapter(itemList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listRecycler.setLayoutManager(mLayoutManager);
        listRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        listRecycler.setAdapter(adapter);



            JSONObject jsonObject;

            SingleItemModel registeredModel;

            try {

                jsonObject = new jsonLoader(this, "data.json").loadJSONFromAsset();

                Iterator<String> iter = jsonObject.keys();

                while (iter.hasNext()) {


                    String key = iter.next();

                    registeredModel = new SingleItemModel(key);

                    itemList.add(registeredModel);





                }





            } catch (FileNotFoundException | JSONException e) {
                e.printStackTrace();
            }


        if (itemList.size() != 0)
            empty.setVisibility(View.GONE);


        adapter.notifyDataSetChanged();




    }


    void setUpObserver(){

        ContentObserver observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                boolean accessibilityServiceEnabled = isAccessibilityServiceEnabled(MainActivity.this, AccService.class);
                //Do something here

                if (accessibilityServiceEnabled)
                    Toasty.success(MainActivity.this, "Enabled", Toast.LENGTH_LONG).show();
                else {

                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toasty.error(MainActivity.this, "Please enable Acc Service", Toast.LENGTH_LONG).show();
                }

            }
        };

        Uri uri = Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        this.getContentResolver().registerContentObserver(uri, false, observer);

    }

    @Override
    protected void onResume() {
        super.onResume();
       // check();
    }

    void initUI(){

        listRecycler = findViewById(R.id.contentRecycler);
        empty = findViewById(R.id.empty);

    }

    void check(){

        if (!isAccessibilityServiceEnabled(this, AccService.class)){

            Toasty.error(MainActivity.this, "Please enable Acc Service", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }


    }


    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> accessibilityService) {
        ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(),  Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null)
            return false;

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName))
                return true;
        }

        return false;
    }


    public void reload(View view) {

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(400);
        rotate.setInterpolator(new LinearInterpolator());

        view.startAnimation(rotate);

        itemList.clear();
        setUpRecycler();

    }


}
