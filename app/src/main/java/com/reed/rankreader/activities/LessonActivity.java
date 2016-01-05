package com.reed.rankreader.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.reed.rankreader.R;
import com.reed.rankreader.utils.FileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LessonActivity extends AppCompatActivity {

    private TextView lessonTextView;
    private TextView rankTextView;
    private FloatingActionButton lightFAB;
    private Boolean isLight;
    private AppCompatSeekBar lightSKB;
    private String article;
    private JSONObject rankJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        article = getIntent().getStringExtra("article");
        try {
            rankJson = FileData.initRank(getAssets().open("nce4_words"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        isLight = false;
        initView();
        lessonTextView.setText(article);
        lightFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLight) {
                    isLight = false;
                    lightSKB.setVisibility(View.GONE);
                    rankTextView.setVisibility(View.GONE);
                    lessonTextView.setText(article);
                } else {
                    isLight = true;
                    lightSKB.setVisibility(View.VISIBLE);
                    int progress = lightSKB.getProgress();
                    rankTextView.setText("高亮等级：" + progress);
                    rankTextView.setVisibility(View.VISIBLE);
                    List<String> keyList = getKey(progress);
                    lessonTextView.setText(highlight(keyList));
                }
            }
        });
        lightSKB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                List<String> keyList = getKey(progress);
                lessonTextView.setText(highlight(keyList));
                rankTextView.setText("高亮等级：" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lightFAB = (FloatingActionButton) findViewById(R.id.light_fab);
        lightSKB = (AppCompatSeekBar) findViewById(R.id.light_skb);
        rankTextView = (TextView) findViewById(R.id.rank_textView);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lessonTextView = (TextView) findViewById(R.id.lesson_textView);
    }

    private List<String> getKey(int progress) {
        List<String> keyList = new ArrayList<>();
        for (int i = 0; i <= progress; i++) {
            try {
                JSONArray keyArray = rankJson.getJSONArray(Integer.toString(i));
                for (int j = 0; j < keyArray.length(); j++) {
                    keyList.add(keyArray.getString(j));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return keyList;
    }

    private SpannableString highlight(List<String> keyList) {
        SpannableString spannable = new SpannableString(article);
        CharacterStyle span;
        for (String key : keyList) {
            Pattern p = Pattern.compile(key);
            Matcher m = p.matcher(article);
            while (m.find()) {
                if (m.start() == 0) {
                    if (m.end() < article.length()) {
                        if (isLetter(article.charAt(m.end()))) {
                            continue;
                        }
                    }
                } else {
                    if (isLetter(article.charAt(m.start() - 1))) {
                        continue;
                    } else {
                        if (m.end() < article.length()) {
                            if (isLetter(article.charAt(m.end()))) {
                                continue;
                            }
                        }
                    }
                }
                span = new ForegroundColorSpan(Color.BLUE);
                spannable.setSpan(span, m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannable;
    }

    private Boolean isLetter(char c) {
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }
}
