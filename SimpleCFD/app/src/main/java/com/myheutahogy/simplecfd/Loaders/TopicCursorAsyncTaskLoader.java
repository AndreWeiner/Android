package com.myheutahogy.simplecfd.Loaders;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ProgressBar;

import com.myheutahogy.simplecfd.DataUtils.TopicListContract;
import com.myheutahogy.simplecfd.R;

public class TopicCursorAsyncTaskLoader extends AsyncTaskLoader<Cursor> {
    private Cursor mCursorData;
    private ProgressBar mProgressBar;
    private Context mContext;

    public TopicCursorAsyncTaskLoader(Context context) {
        super(context);
        mContext = context;
        mProgressBar = ((Activity) context).findViewById(R.id.topic_progress_view);
        if (mCursorData == null) {
            forceLoad();
        } else {
            deliverResult(mCursorData);
        }
    }

    @Override
    public Cursor loadInBackground() {
        mProgressBar.setVisibility(View.VISIBLE);
        try {
            return mContext.getContentResolver().query(
                    TopicListContract.TopicListEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    TopicListContract.TopicListEntry._ID
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deliverResult(Cursor data) {
        mCursorData = data;
        super.deliverResult(data);
    }
}
