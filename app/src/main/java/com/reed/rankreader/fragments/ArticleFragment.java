package com.reed.rankreader.fragments;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ArticleFragment extends Fragment {

    private AppCompatSeekBar lightSKB;
    private TextView articleTV;
    private TextView titleTV;
    private String mTitle;
    private String mArticle;
    private JSONObject mRankJson;
    public static Boolean isLight;
    private TextView rankTextView;
    private ImageButton lightImgBtn;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLight = false;
        try {
            mRankJson = FileData.initData(getActivity().getAssets().open("words.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.viewpager_article, container, false);
            lightSKB = (AppCompatSeekBar) mView.findViewById(R.id.light_skb);
            articleTV = (TextView) mView.findViewById(R.id.lesson_textView);
            titleTV = (TextView) mView.findViewById(R.id.title_textView);
            rankTextView = (TextView) getActivity().findViewById(R.id.rank_textView);
            lightImgBtn = (ImageButton) getActivity().findViewById(R.id.light_imgbtn);
            if (isLight) {
                lightSKB.setVisibility(View.VISIBLE);
            }
            titleTV.setText(mTitle);
            articleTV.setText(lightClick(mArticle));
            articleTV.setMovementMethod(LinkMovementMethod.getInstance());
            initListener();
        }
        return mView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
    }

    @Override
    public void setArguments(Bundle bundle) {
        mTitle = bundle.getString("title");
        mArticle = bundle.getString("article");
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
                    articleTV.setText(lightClick(mArticle));
                    articleTV.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    isLight = true;
                    lightSKB.setVisibility(View.VISIBLE);
                    lightImgBtn.setBackgroundResource(R.mipmap.open);
                    int progress = lightSKB.getProgress();
                    rankTextView.setText("高亮等级：" + progress);
                    rankTextView.setVisibility(View.VISIBLE);
                    List<String> keyList = getKey(progress);
                    articleTV.setText(highlight(keyList));
                }
            }
        });
        lightSKB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                List<String> keyList = getKey(progress);
                articleTV.setText(highlight(keyList));
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
                JSONArray keyArray = mRankJson.getJSONArray(Integer.toString(i));
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
        SpannableString spannable = new SpannableString(mArticle);
        CharacterStyle span;
        for (String key : keyList) {
            Pattern p = Pattern.compile(key.toLowerCase());
            Matcher m = p.matcher(mArticle.toLowerCase());
            while (m.find()) {
                if (m.start() == 0) {
                    if (m.end() < mArticle.length()) {
                        if (isLetter(mArticle.charAt(m.end()))) {
                            continue;
                        }
                    }
                } else {
                    if (isLetter(mArticle.charAt(m.start() - 1))) {
                        continue;
                    } else {
                        if (m.end() < mArticle.length()) {
                            if (isLetter(mArticle.charAt(m.end()))) {
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
            if (!isLetter(mArticle.charAt(wordStart))) {
                continue;
            }
            int wordEnd = matcher.end();
            WordClickableSpan wcs = new WordClickableSpan(articleTV, spannable, wordStart, wordEnd);
            spannable.setSpan(wcs, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }


    private Boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

}
