package com.reed.rankreader.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.reed.rankreader.R;
import com.reed.rankreader.utils.FileData;
import com.reed.rankreader.utils.WordClickableSpan;

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
    private TextView titleTextView;
    private TextView newWordsTextView;
    private TextView translationTextView;
    private TextView rankTextView;
    private ImageButton lightImgBtn;
    private Boolean isLight;
    private AppCompatSeekBar lightSKB;
    private String article;
    private String lesson;
    private String title;
    private String newWords;
    private String translation;
    private JSONObject rankJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        String str = getIntent().getStringExtra("article");
        article = str.substring(str.indexOf("First listen"),str.indexOf("New words and")).trim();
        title = getTitle(str);
        lesson = getIntent().getStringExtra("lesson");
        newWords = str.substring(str.indexOf("New words and"), str.indexOf("参考译文")).trim();
        translation = str.substring(str.indexOf("参考译文")).trim();
        try {
            rankJson = FileData.initRank(getAssets().open("nce4_words"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        isLight = false;
        initView();
        lessonTextView.setText(article);
        if (!isLight) {
            lightClick();
        }
        initListener();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lightImgBtn = (ImageButton) findViewById(R.id.light_imgbtn);
        lightSKB = (AppCompatSeekBar) findViewById(R.id.light_skb);
        rankTextView = (TextView) findViewById(R.id.rank_textView);
        toolbar.setTitle(lesson);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lessonTextView = (TextView) findViewById(R.id.lesson_textView);
        titleTextView = (TextView) findViewById(R.id.title_textView);
        titleTextView.setText(title);
        newWordsTextView = (TextView) findViewById(R.id.new_words_textView);
        newWordsTextView.setText(newWords);
        translationTextView = (TextView) findViewById(R.id.translation_textView);
        translationTextView.setText(translation);

    }

    private void initListener() {
        lightImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLight) {
                    isLight = false;
                    lightSKB.setVisibility(View.GONE);
                    rankTextView.setVisibility(View.GONE);
                    lightImgBtn.setBackgroundResource(R.mipmap.close);
                    lightClick();
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
            Matcher m = p.matcher(article.toLowerCase());
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

    private void lightClick() {
        SpannableString spannable = new SpannableString(article);
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(article);
        while (matcher.find()) {
            int wordStart = matcher.start();
            if (!isLetter(article.charAt(wordStart))) {
                continue;
            }
            int wordEnd = matcher.end();
            WordClickableSpan wcs = new WordClickableSpan(lessonTextView, spannable, wordStart, wordEnd);
            spannable.setSpan(wcs, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        lessonTextView.setText(spannable);
        lessonTextView.setMovementMethod(LinkMovementMethod.getInstance());

    }


    private Boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private String getTitle(String content) {
        int index = content.indexOf("First listen");
        Pattern p = Pattern.compile(" +");
        Matcher m = p.matcher(content.substring(0, index).trim());
        return m.replaceAll(" ");
    }
}
