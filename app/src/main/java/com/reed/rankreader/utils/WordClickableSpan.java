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
        SpannableString sn = new SpannableString(spannable);
        CharacterStyle span;
        span = new ForegroundColorSpan(Color.parseColor("#FF9800"));
        sn.setSpan(span, wordStart, wordEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sn);
    }
}
