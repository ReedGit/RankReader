package com.reed.rankreader.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.reed.rankreader.fragments.ArticleFragment;


public class WordClickableSpan extends ClickableSpan {

    private TextView textView;
    private SpannableString spannable;
    private int wordStart;
    private int wordEnd;

    public WordClickableSpan(TextView textView, SpannableString spannable, int wordStart, int wordEnd) {
        this.textView = textView;
        this.spannable = spannable;
        this.wordStart = wordStart;
        this.wordEnd = wordEnd;
    }

    @Override
    public void updateDrawState(TextPaint ds) {

    }

    @Override
    public void onClick(View widget) {
        CharacterStyle span = new ForegroundColorSpan(Color.parseColor("#FF9800"));
        ArticleFragment.mSpannable = new SpannableString(spannable);
        ArticleFragment.mSpannable.setSpan(span, wordStart, wordEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString sn;
        if (!ArticleFragment.isLight) {
            sn = new SpannableString(spannable);
        } else {
            sn = new SpannableString(ArticleFragment.mRankSpan);
        }
        sn.setSpan(span, wordStart, wordEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(sn);
    }
}
