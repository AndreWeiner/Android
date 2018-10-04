package com.myheutahogy.simplecfd.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.myheutahogy.simplecfd.Adapter.LectureAdapter;
import com.myheutahogy.simplecfd.DataUtils.Lecture;
import com.myheutahogy.simplecfd.Fragments.LectureReadFragment;
import com.myheutahogy.simplecfd.Fragments.LectureWatchFragment;
import com.myheutahogy.simplecfd.Interfaces.ButtonCallbackListener;
import com.myheutahogy.simplecfd.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LectureActivity extends AppCompatActivity implements ButtonCallbackListener {

    private final static String LECTURE_INSTANCE = "lecture";
    private final static String LECTURE_ID = "lecture_id";
    private final static String LECTURE_READ = "lecture_read_fragment";
    private final static String LECTURE_WATCH = "lecture_watch_fragment";
    @BindView(R.id.lecture_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.lecture_view_pager)
    ViewPager mViewPager;
    private ArrayList<Lecture> mLectures;
    private int mCurrentLectureId;
    private LectureReadFragment mLectureReadFragment;
    private LectureWatchFragment mLectureWatchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        ButterKnife.bind(this);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mLectures = extra.getParcelableArrayList(Lecture.class.getSimpleName());
            mCurrentLectureId = extra.getInt(LECTURE_ID);
        }
        if (savedInstanceState != null) {
            mLectures = savedInstanceState.getParcelableArrayList(LECTURE_INSTANCE);
            mCurrentLectureId = savedInstanceState.getInt(LECTURE_ID);
            mLectureReadFragment = (LectureReadFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, LECTURE_READ);
            mLectureWatchFragment = (LectureWatchFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, LECTURE_WATCH);
        } else {
            createLectureFragments();
        }
        getSupportActionBar().setTitle(mLectures.get(mCurrentLectureId).getTitle());
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LECTURE_INSTANCE, mLectures);
        outState.putInt(LECTURE_ID, mCurrentLectureId);
        getSupportFragmentManager().putFragment(outState, LECTURE_READ, mLectureReadFragment);
        getSupportFragmentManager().putFragment(outState, LECTURE_WATCH, mLectureWatchFragment);
    }

    private void createLectureFragments() {
        Lecture currentLecture = mLectures.get(mCurrentLectureId);
        mLectureReadFragment = new LectureReadFragment();
        mLectureReadFragment.setLectureText(currentLecture.getLectureText());
        mLectureReadFragment.setLectureId(mCurrentLectureId);
        mLectureReadFragment.setLastLecture(mLectures.size() - 1);
        mLectureWatchFragment = new LectureWatchFragment();
        mLectureWatchFragment.setVideoId(currentLecture.getVideoId());
        mLectureWatchFragment.setLectureId(mCurrentLectureId);
        mLectureWatchFragment.setLastLecture(mLectures.size() - 1);
    }

    private void setupViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(mLectureReadFragment, getResources().getString(R.string.lecture_read));
        pagerAdapter.addFragment(mLectureWatchFragment, getResources().getString(R.string.lecture_watch));
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onButtonCallback(int nextLecture, boolean toRead) {
        mCurrentLectureId = nextLecture;
        LectureAdapter.updateLastLectureWidget(this, mLectures.get(mCurrentLectureId));
        getSupportActionBar().setTitle(mLectures.get(mCurrentLectureId).getTitle());
        createLectureFragments();
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        if (!toRead) {
            mTabLayout.getTabAt(1).select();
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
