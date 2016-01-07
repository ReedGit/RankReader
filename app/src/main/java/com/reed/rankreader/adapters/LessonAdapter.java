package com.reed.rankreader.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reed.rankreader.R;
import com.reed.rankreader.activities.LessonActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private LayoutInflater layoutInflater;
    private JSONArray lessonArray;
    private Context context;

    public LessonAdapter(Context context, JSONArray lessonArray) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.lessonArray = lessonArray;
    }

    @Override
    public LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LessonViewHolder(layoutInflater.inflate(R.layout.lesson_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LessonViewHolder holder, int position) {
        try {
            final JSONObject lessonJson = lessonArray.getJSONObject(position);
            Iterator<String> iterator = lessonJson.keys();
            final String lesson = iterator.next();
            holder.lessonVP.setText(lesson);
            holder.titleVP.setText(getTitle(lessonJson.getString(lesson)));
            holder.lessonCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LessonActivity.class);
                    try {
                        intent.putExtra("article", lessonJson.getString(lesson));
                        intent.putExtra("lesson",lesson);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return lessonArray.length();
    }

    public class LessonViewHolder extends RecyclerView.ViewHolder {

        TextView lessonVP;
        TextView titleVP;
        CardView lessonCV;

        public LessonViewHolder(View itemView) {
            super(itemView);
            lessonVP = (TextView) itemView.findViewById(R.id.rcv_lesson);
            titleVP = (TextView) itemView.findViewById(R.id.rcv_title);
            lessonCV = (CardView) itemView.findViewById(R.id.cv_lesson);
        }
    }

    private String getTitle(String article) {
        String title = article.replace("\n", "");
        int index = title.indexOf("First listen");
        Pattern p = Pattern.compile("\\s+");
        Matcher m = p.matcher(title.substring(0, index).trim());
        return m.replaceAll(" ");
    }
}
