package com.myheutahogy.simplecfd.Loaders;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;

import com.myheutahogy.simplecfd.DataUtils.Topic;
import com.myheutahogy.simplecfd.DataUtils.TopicListContract;
import com.myheutahogy.simplecfd.DataUtils.TopicOnlineUtils;
import com.myheutahogy.simplecfd.R;

import org.json.JSONException;

import java.util.ArrayList;

public class TopicAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Topic>> {

    private ArrayList<Topic> mTopics;
    private ProgressBar mProgressBar;
    private Context mContext;

    public TopicAsyncTaskLoader(Context context) {
        super(context);
        mContext = context;
        mProgressBar = ((Activity) context).findViewById(R.id.topic_progress_view);
        if (mTopics == null) {
            forceLoad();
        } else {
            deliverResult(mTopics);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public ArrayList<Topic> loadInBackground() {
        try {
            mTopics = TopicOnlineUtils.getTopicsFromJSON();
            Uri uri = TopicListContract.TopicListEntry.CONTENT_URI;
            mContext.getContentResolver().delete(uri, null, null);
            for (Topic topic : mTopics) {
                ContentValues cv = new ContentValues();
                cv.put(TopicListContract.TopicListEntry.COLUMN_TOPIC_NAME,
                        topic.getName());
                cv.put(TopicListContract.TopicListEntry.COLUMN_TOPIC_DESCRIPTION,
                        topic.getDescription());
                cv.put(TopicListContract.TopicListEntry.COLUMN_TOPIC_IMAGE_URL,
                        topic.getImageUrl());
                cv.put(TopicListContract.TopicListEntry.COLUMN_TOPIC_LECTURES,
                        topic.lecturesToString());
                mContext.getContentResolver().insert(TopicListContract.TopicListEntry.CONTENT_URI, cv);
            }
            return mTopics;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
