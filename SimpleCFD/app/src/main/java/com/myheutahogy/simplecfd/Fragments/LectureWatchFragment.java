package com.myheutahogy.simplecfd.Fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.myheutahogy.simplecfd.BuildConfig;
import com.myheutahogy.simplecfd.Interfaces.ButtonCallbackListener;
import com.myheutahogy.simplecfd.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LectureWatchFragment extends Fragment {

    private final static String VIDEO_ID = "video_id";
    private final static String VIDEO_TIME = "video_time";
    private final static String VIDEO_PLAYING = "video_playing";
    String apiKey = BuildConfig.API_KEY;
    @BindView(R.id.youtube_fragment_container)
    FrameLayout mYoutubeContainer;
    @BindView(R.id.next_button_watch)
    Button mNextButton;
    @BindView(R.id.prev_button_watch)
    Button mPrevButton;
    TabLayout mTabLayout;
    private ButtonCallbackListener mButtonCallBackListener;
    private YouTubePlayer mYoutubePlayer;
    private String mVideoId;
    private int mLectureId;
    private int mLastLecture;
    private int mStartAt;
    private boolean mIsPlaying;

    public LectureWatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lecture_watch, container, false);
        ButterKnife.bind(this, rootView);
        mTabLayout = getActivity().findViewById(R.id.lecture_tabs);
        mStartAt = 0;
        mIsPlaying = true;
        if (savedInstanceState != null) {
            mVideoId = savedInstanceState.getString(VIDEO_ID);
            mStartAt = savedInstanceState.getInt(VIDEO_TIME);
            mIsPlaying = savedInstanceState.getBoolean(VIDEO_PLAYING);
        }
        final int orientation = getResources().getConfiguration().orientation;
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment_container, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(apiKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    mYoutubePlayer = youTubePlayer;
                    if (mIsPlaying) {
                        mYoutubePlayer.setFullscreenControlFlags(0);
                        mYoutubePlayer.loadVideo(mVideoId, mStartAt);
                    } else {
                        mYoutubePlayer.cueVideo(mVideoId, mStartAt);
                    }
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        hideSystemUI();
                    }
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        mButtonCallBackListener = (ButtonCallbackListener) getActivity();

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonCallBackListener.onButtonCallback(mLectureId + 1, false);
            }
        });
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonCallBackListener.onButtonCallback(mLectureId - 1, false);
            }
        });

        hideOrShowButtons();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(VIDEO_ID, mVideoId);
        outState.putInt(VIDEO_TIME, mYoutubePlayer.getCurrentTimeMillis());
        outState.putBoolean(VIDEO_PLAYING, mYoutubePlayer.isPlaying());
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    public void setLectureId(int lectureId) {
        mLectureId = lectureId;
    }

    public void setLastLecture(int lastLecture) {
        mLastLecture = lastLecture;
    }

    private void hideOrShowButtons() {
        if (mLectureId == 0) {
            mPrevButton.setVisibility(View.INVISIBLE);
        } else {
            mPrevButton.setVisibility(View.VISIBLE);
        }
        if (mLectureId == mLastLecture) {
            mNextButton.setVisibility(View.INVISIBLE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        mTabLayout.setVisibility(View.GONE);
    }

}
