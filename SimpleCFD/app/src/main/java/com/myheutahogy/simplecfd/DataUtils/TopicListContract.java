package com.myheutahogy.simplecfd.DataUtils;

import android.net.Uri;
import android.provider.BaseColumns;

public class TopicListContract {
    public static final String AUTHORITY = "com.myheutahogy.simplecfd";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TOPICS = "topics";

    public static final class TopicListEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TOPICS).build();

        public static final String TABLE_NAME = "topic_list";
        public static final String COLUMN_TOPIC_NAME = "topic_name";
        public static final String COLUMN_TOPIC_IMAGE_URL = "topic_image_url";
        public static final String COLUMN_TOPIC_DESCRIPTION = "topic_description";
        public static final String COLUMN_TOPIC_LECTURES = "topic_lectures";
    }
}
