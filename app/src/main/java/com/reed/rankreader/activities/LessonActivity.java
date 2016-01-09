package com.reed.rankreader.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.reed.rankreader.R;
import com.reed.rankreader.adapters.ArticleAdapter;
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
    private TextView questionTextView;
    private TextView rankTextView;
    private ViewPager mViewPager;
    private ImageButton lightImgBtn;
    private Boolean isLight;
    private AppCompatSeekBar lightSKB;
    private String article;
    private String lesson;
    private String title;
    private String question;
    private String newWords;
    private String translation;
    private JSONObject rankJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        lesson = getIntent().getStringExtra("lesson");
        String contentStr = getIntent().getStringExtra("content");
        try {
            JSONObject content = new JSONObject(contentStr);
            article = content.getString("article");
            title = getTitle(content.getString("title"));
            newWords = content.getString("newWords");
            question = content.getString("question");
            translation = content.getString("translation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isLight = false;
        initView();
        showArticle();
        showQuestion();
        showNewWords();
        showTranslation();
        initListener();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(lesson);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lightImgBtn = (ImageButton) findViewById(R.id.light_imgbtn);
        rankTextView = (TextView) findViewById(R.id.rank_textView);

        TabLayout tabLesson = (TabLayout) findViewById(R.id.tab_lesson);
        TabLayout.Tab questionTab = tabLesson.newTab();
        questionTab.setText("问题");
        TabLayout.Tab articleTab = tabLesson.newTab();
        articleTab.setText("文章");
        TabLayout.Tab newWordsTab = tabLesson.newTab();
        newWordsTab.setText("生词和短语");
        TabLayout.Tab translationTab = tabLesson.newTab();
        translationTab.setText("参考译文");
        tabLesson.addTab(articleTab, 0);
        tabLesson.addTab(questionTab, 1);
        tabLesson.addTab(newWordsTab, 2);
        tabLesson.addTab(translationTab, 3);

        mViewPager = (ViewPager) findViewById(R.id.vp_lesson);
        View articleView = LayoutInflater.from(this).inflate(R.layout.viewpager_article, mViewPager, false);
        View questionView = LayoutInflater.from(this).inflate(R.layout.viewpager_question, mViewPager, false);
        View newWordsView = LayoutInflater.from(this).inflate(R.layout.viewpager_new_words, mViewPager, false);
        View translationView = LayoutInflater.from(this).inflate(R.layout.viewpager_translation, mViewPager, false);
        List<View> viewList = new ArrayList<>();
        viewList.add(articleView);
        viewList.add(questionView);
        viewList.add(newWordsView);
        viewList.add(translationView);

        lightSKB = (AppCompatSeekBar) articleView.findViewById(R.id.light_skb);
        lessonTextView = (TextView) articleView.findViewById(R.id.lesson_textView);
        titleTextView = (TextView) articleView.findViewById(R.id.title_textView);
        questionTextView = (TextView) questionView.findViewById(R.id.question_tv);
        newWordsTextView = (TextView) newWordsView.findViewById(R.id.new_words_textView);
        translationTextView = (TextView) translationView.findViewById(R.id.translation_textView);

        ArticleAdapter articleAdapter = new ArticleAdapter(viewList);
        mViewPager.setAdapter(articleAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLesson));
        tabLesson.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() != 0) {
                    lightImgBtn.setVisibility(View.GONE);
                    rankTextView.setVisibility(View.GONE);
                } else {
                    lightImgBtn.setVisibility(View.VISIBLE);
                    if (isLight) {
                        rankTextView.setVisibility(View.VISIBLE);
                    } else {
                        rankTextView.setVisibility(View.GONE);
                    }
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

    private void showArticle() {
        titleTextView.setText(title);
        lessonTextView.setText(lightClick(article));
        lessonTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showQuestion() {
        questionTextView.setText(question);
    }

    private void showNewWords() {
        newWordsTextView.setText(newWords);
    }

    private void showTranslation() {
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
                    lessonTextView.setText(lightClick(article));
                    lessonTextView.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    isLight = true;
                    lightSKB.setVisibility(View.VISIBLE);
                    lightImgBtn.setBackgroundResource(R.mipmap.open);
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
        try {
            rankJson = FileData.initData(getAssets().open("words.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Pattern p = Pattern.compile(key.toLowerCase());
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
        return lightClick(spannable);
    }

    private SpannableString lightClick(CharSequence source) {
        SpannableString spannable = new SpannableString(source);
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            int wordStart = matcher.start();
            if (!isLetter(article.charAt(wordStart))) {
                continue;
            }
            int wordEnd = matcher.end();
            WordClickableSpan wcs = new WordClickableSpan(lessonTextView, spannable, wordStart, wordEnd);
            spannable.setSpan(wcs, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }


    private Boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private String getTitle(String str) {
        Pattern p = Pattern.compile(" +");
        Matcher m = p.matcher(str.trim());
        return m.replaceAll(" ");
    }
}
