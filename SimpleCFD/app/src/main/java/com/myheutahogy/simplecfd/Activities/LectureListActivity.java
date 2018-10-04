package com.myheutahogy.simplecfd.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.myheutahogy.simplecfd.Adapter.LectureAdapter;
import com.myheutahogy.simplecfd.DataUtils.Topic;
import com.myheutahogy.simplecfd.DataUtils.TopicOnlineUtils;
import com.myheutahogy.simplecfd.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LectureListActivity extends AppCompatActivity {

    @BindView(R.id.lecture_list_image_view)
    ImageView mTopicImageView;
    @BindView(R.id.lecture_list_view)
    RecyclerView mLectureListView;
    private Topic mCurrentTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentTopic = getIntent().getExtras().getParcelable(Topic.class.getSimpleName());
        }
        if (TopicOnlineUtils.internetAvailable(this) && !mCurrentTopic.getImageUrl().equals("")) {
            Picasso.with(this).load(mCurrentTopic.getImageUrl()).into(mTopicImageView);
        } else {
            Toast.makeText(this, getResources().getText(R.string.offline_mode_message),
                    Toast.LENGTH_SHORT).show();
        }
        getSupportActionBar().setTitle(mCurrentTopic.getName());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mLectureListView.setLayoutManager(manager);
        mLectureListView.setAdapter(new LectureAdapter(mCurrentTopic.getLectures()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
