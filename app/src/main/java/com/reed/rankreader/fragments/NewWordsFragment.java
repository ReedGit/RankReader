package com.reed.rankreader.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reed.rankreader.R;


public class NewWordsFragment extends Fragment {

    private String mNewWords;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.viewpager_new_words, container, false);
            TextView newWordsTV = (TextView) mView.findViewById(R.id.new_words_textView);
            newWordsTV.setText(mNewWords);
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
        mNewWords = bundle.getString("new_words");
    }
}
