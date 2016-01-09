package com.reed.rankreader.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import com.reed.rankreader.R;
import com.reed.rankreader.adapters.LessonAdapter;
import com.reed.rankreader.utils.FileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private JSONObject content;
    private RecyclerView mRecyclerView;
    private LessonAdapter lessonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            content = FileData.initData(getAssets().open("four.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_unit);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_main_lesson);

        setSupportActionBar(toolbar);

        Iterator<String> iterator = content.keys();
        List<Integer> list = new ArrayList<>();
        while (iterator.hasNext()) {
            String unit = iterator.next();
            list.add(Integer.parseInt(unit.substring(5)));
        }
        Collections.sort(list);
        for (int i = 0; i<list.size();i++){
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText("Unit "+list.get(i));
            mTabLayout.addTab(tab);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            lessonAdapter = new LessonAdapter(this, content.getJSONArray(mTabLayout.getTabAt(0).getText().toString()));
            mRecyclerView.setAdapter(lessonAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    String unit = tab.getText().toString();
                    lessonAdapter = new LessonAdapter(MainActivity.this, content.getJSONArray(unit));
                    mRecyclerView.setAdapter(lessonAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
