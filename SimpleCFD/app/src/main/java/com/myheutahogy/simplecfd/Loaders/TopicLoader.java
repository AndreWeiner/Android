package com.myheutahogy.simplecfd.Loaders;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myheutahogy.simplecfd.Activities.LectureListActivity;
import com.myheutahogy.simplecfd.Adapter.TopicAdapter;
import com.myheutahogy.simplecfd.DataUtils.Topic;
import com.myheutahogy.simplecfd.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicLoader implements LoaderManager.LoaderCallbacks<ArrayList<Topic>> {
    @BindView(R.id.topic_progress_view)
    ProgressBar mProgressBar;
    @BindView(R.id.topic_message_view)
    TextView mMessageView;
    @BindView(R.id.topic_grid_view)
    GridView mTopicGridView;
    @BindView(R.id.topic_list_wrapper)
    android.support.constraint.ConstraintLayout mTopicListWrapper;
    private Context mContext;

    public TopicLoader(Context context) {
        mContext = context;
        ButterKnife.bind(this, (Activity) mContext);
    }

    @Override
    public Loader<ArrayList<Topic>> onCreateLoader(int i, Bundle bundle) {
        return new TopicAsyncTaskLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Topic>> loader, final ArrayList<Topic> topics) {
        if (topics == null) {
            mMessageView.setText(mContext.getResources().getText(R.string.no_topics_message));
        } else {
            mMessageView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mTopicListWrapper.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            mTopicGridView.setAdapter(new TopicAdapter(mContext, topics));
            mTopicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    //updateIngredientsWidget(mContext, recipes.get(position));
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
    public void onLoaderReset(Loader<ArrayList<Topic>> loader) {

    }
}
