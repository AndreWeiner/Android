package com.myheutahogy.simplecfd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myheutahogy.simplecfd.DataUtils.Topic;
import com.myheutahogy.simplecfd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopicAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Topic> mTopics;

    public TopicAdapter(Context context, ArrayList<Topic> topics) {
        mContext = context;
        mTopics = topics;
    }

    @Override
    public int getCount() {
        return mTopics.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Topic topic = mTopics.get(position);

        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.topic_card_item, null);
        }

        final ImageView thumbnail = view.findViewById(R.id.topic_image_view);
        final TextView name = view.findViewById(R.id.topic_name_view);
        final TextView description = view.findViewById(R.id.topic_description_view);

        name.setText(topic.getName());
        description.setText(topic.getDescription());
        String thumbnailUrl = topic.getImageUrl();
        // if image Url is empty only the grey background will be visible
        if (!thumbnailUrl.equals("")) {
            Picasso.with(mContext).load(thumbnailUrl).into(thumbnail);
        }

        return view;
    }
}
