package com.reed.rankreader.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reed.rankreader.R;


public class QuestionFragment extends Fragment {

    private String mQuestion;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.viewpager_question, container, false);
            TextView questionTV = (TextView) mView.findViewById(R.id.question_tv);
            questionTV.setText(mQuestion);
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
        mQuestion = bundle.getString("question");
    }
}
