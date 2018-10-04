package com.myheutahogy.simplecfd.Loaders;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.myheutahogy.simplecfd.Activities.LectureListActivity;
import com.myheutahogy.simplecfd.Adapter.TopicAdapter;
import com.myheutahogy.simplecfd.DataUtils.Topic;
import com.myheutahogy.simplecfd.DataUtils.TopicListContract;
import com.myheutahogy.simplecfd.DataUtils.TopicOnlineUtils;
import com.myheutahogy.simplecfd.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String TOPIC_SCROLL_POSITION = "scroll_position";
    private final static int TOPIC_LOADER = 42;
    LoaderManager mLoaderManager;
    @BindView(R.id.topic_progress_view)
    ProgressBar mProgressBar;
    @BindView(R.id.topic_message_view)
    TextView mMessageView;
    @BindView(R.id.topic_grid_view)
    GridView mTopicGridView;
    @BindView(R.id.topic_list_wrapper)
    android.support.constraint.ConstraintLayout mTopicListWrapper;
    private Context mContext;
    private int mCurrentPosition;
    private FirebaseAnalytics mFirebaseAnalytics;

    public TopicCursorLoader(Context context, LoaderManager loaderManager) {
        mContext = context;
        mLoaderManager = loaderManager;
        ButterKnife.bind(this, (Activity) context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        mCurrentPosition = bundle.getInt(TOPIC_SCROLL_POSITION);
        return new TopicCursorAsyncTaskLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        final ArrayList<Topic> topics = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndex(TopicListContract.TopicListEntry.COLUMN_TOPIC_NAME));
            String description = cursor.getString(
                    cursor.getColumnIndex(TopicListContract.TopicListEntry.COLUMN_TOPIC_DESCRIPTION));
            String image_url = cursor.getString(
                    cursor.getColumnIndex(TopicListContract.TopicListEntry.COLUMN_TOPIC_IMAGE_URL));
            String all_lectures = cursor.getString(
                    cursor.getColumnIndex(TopicListContract.TopicListEntry.COLUMN_TOPIC_LECTURES));
            Topic topic = new Topic(name, description, image_url, null);
            topic.setLecturesFromString(all_lectures);
            topics.add(topic);
        }
        cursor.close();

        if (topics.size() < 1) { // no topics saved on device, get online resources
            Loader<ArrayList<Topic>> topicLoader = mLoaderManager.getLoader(TOPIC_LOADER);
            if (TopicOnlineUtils.internetAvailable(mContext)) {
                mMessageView.setText(mContext.getResources().getText(R.string.loading_online_message));
                if (topicLoader == null) {
                    mLoaderManager.initLoader(TOPIC_LOADER, new Bundle(), new TopicLoader(mContext));
                } else {
                    mLoaderManager.restartLoader(TOPIC_LOADER, new Bundle(), new TopicLoader(mContext));
                }
            } else {
                mProgressBar.setVisibility(View.GONE);
                mMessageView.setText(mContext.getResources().getString(R.string.no_internet_message));
            }
        } else {
            mMessageView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mTopicListWrapper.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            if (!TopicOnlineUtils.internetAvailable(mContext)) {
                Toast.makeText(mContext, mContext.getResources().getText(R.string.offline_mode_message),
                        Toast.LENGTH_LONG).show();
            }
            mTopicGridView.setAdapter(new TopicAdapter(mContext, topics));
            mTopicGridView.smoothScrollToPosition(mCurrentPosition);
            mTopicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    //updateIngredientsWidget(mContext, recipes.get(position));
                    // Send selected topic to Firebase
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topics.get(position).getName());
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    // Open lecture list view
                    Intent intent = new Intent(mContext, LectureListActivity.class);
                    intent.putExtra(Topic.class.getSimpleName(), topics.get(position));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, view, "topic_image_transition");
                    mContext.startActivity(intent, options.toBundle());
                }
            });
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
