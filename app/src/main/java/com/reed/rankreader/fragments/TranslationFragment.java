package com.reed.rankreader.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reed.rankreader.R;

public class TranslationFragment extends Fragment {

    private String mTranslation;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mTranslation = bundle.getString("translation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.viewpager_translation, container, false);
            TextView translationTV = (TextView) mView.findViewById(R.id.translation_textView);
            translationTV.setText(mTranslation);
        }
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
