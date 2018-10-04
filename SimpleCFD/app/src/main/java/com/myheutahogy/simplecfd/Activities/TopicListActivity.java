package com.myheutahogy.simplecfd.Activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.myheutahogy.simplecfd.BuildConfig;
import com.myheutahogy.simplecfd.DataUtils.Topic;
import com.myheutahogy.simplecfd.DataUtils.TopicOnlineUtils;
import com.myheutahogy.simplecfd.Loaders.TopicCursorLoader;
import com.myheutahogy.simplecfd.Loaders.TopicLoader;
import com.myheutahogy.simplecfd.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicListActivity extends AppCompatActivity {
    private final static int TOPIC_LOADER = 42;
    private final static int TOPIC_DB_LOADER = 43;
    private final static String LIFECYCLE_SCROLL_POSITION = "scroll_position";
    String addKey = BuildConfig.ADD_KEY;
    // My actual Block ID is not yet activated,
    // therefore I use the test block for now
    // String addKeyId = BuildConfig.ADD_KEY_ID;
    String addKeyId = "ca-app-pub-3940256099942544/1033173712";
    @BindView(R.id.topic_message_view)
    TextView mMessageView;
    @BindView(R.id.topic_progress_view)
    ProgressBar mProgressBar;
    @BindView(R.id.topic_grid_view)
    GridView mTopicGridView;
    private InterstitialAd mInterstitialAd;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            mCurrentPosition = 0;
        } else {
            mCurrentPosition = savedInstanceState.getInt(LIFECYCLE_SCROLL_POSITION);
        }
        MobileAds.initialize(this, addKey);
        mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId(addKey);
        mInterstitialAd.setAdUnitId(addKeyId);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }
        });
        loadAndDisplayTopicsFromDevice();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LIFECYCLE_SCROLL_POSITION, mTopicGridView.getFirstVisiblePosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:

                if (TopicOnlineUtils.internetAvailable(this)) {
                    Toast.makeText(this, getResources().getText(R.string.refresh_message),
                            Toast.LENGTH_SHORT).show();
                    LoaderManager loaderManager = getLoaderManager();
                    Loader<ArrayList<Topic>> topicLoader = loaderManager.getLoader(TOPIC_LOADER);
                    mProgressBar.bringToFront();
                    mProgressBar.setVisibility(View.VISIBLE);
                    if (topicLoader == null) {
                        loaderManager.initLoader(TOPIC_LOADER, new Bundle(), new TopicLoader(this));
                    } else {
                        loaderManager.restartLoader(TOPIC_LOADER, new Bundle(), new TopicLoader(this));
                    }
                } else {
                    Toast.makeText(this, getResources().getText(R.string.offline_mode_message),
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadAndDisplayTopicsFromDevice() {
        LoaderManager loaderManager = getLoaderManager();
        Loader<Cursor> cursorLoader = loaderManager.getLoader(TOPIC_DB_LOADER);
        Bundle bundle = new Bundle();
        bundle.putInt(LIFECYCLE_SCROLL_POSITION, mCurrentPosition);
        mMessageView.setText(getResources().getText(R.string.loading_offline_message));
        if (cursorLoader == null) {
            loaderManager.initLoader(TOPIC_DB_LOADER, bundle, new TopicCursorLoader(this, loaderManager));
        } else {
            loaderManager.restartLoader(TOPIC_DB_LOADER, bundle, new TopicCursorLoader(this, loaderManager));
        }
    }
}
