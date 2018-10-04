package com.myheutahogy.simplecfd.DataUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TopicDbHelper extends SQLiteOpenHelper {
    final static int DB_VERSION = 1;
    private static final String DATABASE_NAME = "topic_list.db";

    public TopicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TOPIC_TABLE = "CREATE TABLE " +
                TopicListContract.TopicListEntry.TABLE_NAME + " (" +
                TopicListContract.TopicListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TopicListContract.TopicListEntry.COLUMN_TOPIC_NAME + " TEXT NOT NULL, " +
                TopicListContract.TopicListEntry.COLUMN_TOPIC_DESCRIPTION + " TEXT NOT NULL, " +
                TopicListContract.TopicListEntry.COLUMN_TOPIC_IMAGE_URL + " TEXT NOT NULL, " +
                TopicListContract.TopicListEntry.COLUMN_TOPIC_LECTURES + " TEXT NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TOPIC_TABLE);
    }

    /*
    Currently, the app can only receive updated topic content and does not
    save any user-specific information. Therefore, the only sensible way is
    to replace the old database entirely. This behavior has to be changed when
    user-specific data is stored.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TopicListContract.TopicListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
