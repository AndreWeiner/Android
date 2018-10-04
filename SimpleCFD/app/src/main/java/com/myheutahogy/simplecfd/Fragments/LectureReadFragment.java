package com.myheutahogy.simplecfd.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.myheutahogy.simplecfd.Interfaces.ButtonCallbackListener;
import com.myheutahogy.simplecfd.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LectureReadFragment extends Fragment {

    private final static String MIME_TYPE = "text/html; charset=utf-8";
    private final static String ENCODING = "utf-8";
    private final static String LECTURE_TEXT = "lecture_text";
    private final static String LECTURE_ID = "lecture_id";
    private final static String LECTURE_LAST_ID = "lecture_last_id";
    private final static String LIFE_CYCLE_SCROLL = "scroll_position";
    @BindView(R.id.lecture_read_view)
    WebView mWebView;
    @BindView(R.id.next_button_read)
    Button mNextButton;
    @BindView(R.id.prev_button_read)
    Button mPrevButton;
    private String mLectureText;
    private int mLectureId;
    private int mLastLecture;
    private float mRelScrollPosition;
    private ButtonCallbackListener mButtonCallBackListener;


    public LectureReadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lecture_read, container, false);
        ButterKnife.bind(this, rootView);
        mRelScrollPosition = 0.0f;
        if (savedInstanceState != null) {
            mLectureText = savedInstanceState.getString(LECTURE_TEXT);
            mLectureId = savedInstanceState.getInt(LECTURE_ID);
            mLastLecture = savedInstanceState.getInt(LECTURE_LAST_ID);
            mRelScrollPosition = savedInstanceState.getFloat(LIFE_CYCLE_SCROLL);
        }
        mWebView.loadData(mLectureText, MIME_TYPE, ENCODING);
        WebSettings settings = mWebView.getSettings();
        settings.setTextZoom(getResources().getInteger(R.integer.web_view_zoom));
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                float height = mWebView.getHeight();//mWebView.getContentHeight();
                mWebView.scrollTo(0, (int) (mRelScrollPosition * height));
            }
        });

        mButtonCallBackListener = (ButtonCallbackListener) getActivity();

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonCallBackListener.onButtonCallback(mLectureId + 1, true);
            }
        });
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonCallBackListener.onButtonCallback(mLectureId - 1, true);
            }
        });

        hideOrShowButtons();

        return rootView;

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LECTURE_TEXT, mLectureText);
        outState.putInt(LECTURE_ID, mLectureId);
        outState.putInt(LECTURE_LAST_ID, mLastLecture);
        outState.putFloat(LIFE_CYCLE_SCROLL, computeRelativeScrollPosition());
    }

    public void setLectureText(String text) {
        mLectureText = text;
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

    private float computeRelativeScrollPosition() {
        float posY = mWebView.getScrollY();
        float height = mWebView.getHeight();
        return posY / height;
    }

}
