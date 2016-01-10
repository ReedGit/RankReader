package com.reed.rankreader.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.reed.rankreader.R;
import com.reed.rankreader.adapters.LessonFragmentAdapter;
import com.reed.rankreader.fragments.ArticleFragment;
import com.reed.rankreader.fragments.NewWordsFragment;
import com.reed.rankreader.fragments.QuestionFragment;
import com.reed.rankreader.fragments.TranslationFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LessonActivity extends AppCompatActivity {

    private TextView rankTextView;
    private ViewPager mViewPager;
    private ImageButton lightImgBtn;
    private String article;
    private String lesson;
    private String title;
    private String question;
    private String newWords;
    private String translation;

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
        initView();

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
        List<Fragment> fragmentList = new ArrayList<>();

        ArticleFragment articleFragment = new ArticleFragment();
        Bundle articleBundle = new Bundle();
        articleBundle.putString("title",title);
        articleBundle.putString("article", article);
        articleFragment.setArguments(articleBundle);
        fragmentList.add(articleFragment);

        QuestionFragment questionFragment = new QuestionFragment();
        Bundle questionBundle = new Bundle();
        questionBundle.putString("question", question);
        questionFragment.setArguments(questionBundle);
        fragmentList.add(questionFragment);

        NewWordsFragment newWordsFragment = new NewWordsFragment();
        Bundle newWordsBundle = new Bundle();
        newWordsBundle.putString("new_words", newWords);
        newWordsFragment.setArguments(newWordsBundle);
        fragmentList.add(newWordsFragment);

        TranslationFragment translationFragment = new TranslationFragment();
        Bundle translationBundle = new Bundle();
        translationBundle.putString("translation",translation);
        translationFragment.setArguments(translationBundle);
        fragmentList.add(translationFragment);


        LessonFragmentAdapter lessonFA = new LessonFragmentAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(lessonFA);
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
                    if (ArticleFragment.isLight) {
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


    private String getTitle(String str) {
        Pattern p = Pattern.compile(" +");
        Matcher m = p.matcher(str.trim());
        return m.replaceAll(" ");
    }
}
