package com.myheutahogy.simplecfd.Adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myheutahogy.simplecfd.Activities.LastLectureWidget;
import com.myheutahogy.simplecfd.Activities.LectureActivity;
import com.myheutahogy.simplecfd.DataUtils.Lecture;
import com.myheutahogy.simplecfd.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder> {

    private Context mContext;
    private ArrayList<Lecture> mLectures;

    public LectureAdapter(ArrayList<Lecture> lectures) {
        mLectures = lectures;
    }

    public static void updateLastLectureWidget(Context context, Lecture lastLecture) {
        Intent intent = new Intent(context, LastLectureWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(LastLectureWidget.LECTURE_SERVICE, lastLecture);
        int[] ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, LastLectureWidget.class));
        if (ids != null && ids.length > 0) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }

    }

    @Override
    public int getItemCount() {
        return mLectures.size();
    }

    @Override
    public LectureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutId = R.layout.lecture_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutId, parent, false);
        return new LectureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LectureViewHolder holder, int position) {
        holder.bind(position, mLectures.get(position).getTitle());
    }

    public class LectureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final static String LECTURE_ID = "lecture_id";
        @BindView(R.id.lecture_item_id)
        TextView mIdTextView;
        @BindView(R.id.lecture_item_title)
        TextView mTitleTextView;
        private int mPosition;

        public LectureViewHolder(View lectureView) {
            super(lectureView);
            lectureView.setOnClickListener(this);
            ButterKnife.bind(this, lectureView);
        }

        public void bind(int id, String title) {
            mTitleTextView.setText(title);
            mIdTextView.setText(Integer.toString(id));
            mPosition = id;
        }

        @Override
        public void onClick(View view) {
            updateLastLectureWidget(mContext, mLectures.get(mPosition));
            Intent intent = new Intent(mContext, LectureActivity.class);
            intent.putExtra(Lecture.class.getSimpleName(), mLectures);
            intent.putExtra(LECTURE_ID, mPosition);
            mContext.startActivity(intent);
        }

    }
}
