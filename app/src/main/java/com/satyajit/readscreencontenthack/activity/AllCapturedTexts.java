package com.satyajit.readscreencontenthack.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.satyajit.readscreencontenthack.R;
import com.satyajit.readscreencontenthack.models.SingleItemModel;
import com.satyajit.readscreencontenthack.adapters.TextsAdapter;
import com.satyajit.readscreencontenthack.utils.jsonLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllCapturedTexts extends AppCompatActivity {


    RecyclerView listRecycler;
    private List<SingleItemModel> itemList = new ArrayList<>();
    TextsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_captured_texts);

        initUI();

        setUpRecycler();

        getSupportActionBar().setTitle("Captured Texts");

    }

    void initUI(){

        listRecycler = findViewById(R.id.contentRecycler);

    }

    @Override
    public void finish() {

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); //Terminate the current Activity
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    void setUpRecycler(){



        adapter = new TextsAdapter(itemList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listRecycler.setLayoutManager(mLayoutManager);
        listRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        listRecycler.setAdapter(adapter);



        JSONObject jsonObject;


        SingleItemModel registeredModel;

        try {

            jsonObject = new jsonLoader(this, "data.json").loadJSONFromAsset();





                    JSONArray js = jsonObject.getJSONArray(Objects.requireNonNull(getIntent().getStringExtra("package_name")));

                    for (int i = 0; i < js.length(); i++){

                        registeredModel = new SingleItemModel(js.getString(i));

                        itemList.add(registeredModel);

                    }




            } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }






        adapter.notifyDataSetChanged();




    }

}
