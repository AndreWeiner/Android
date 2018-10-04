package com.myheutahogy.simplecfd.DataUtils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TopicContentProvider extends ContentProvider {

    private static final int TOPICS = 100;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TopicDbHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(
                TopicListContract.AUTHORITY,
                TopicListContract.PATH_TOPICS,
                TOPICS
        );
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new TopicDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case TOPICS:
                cursor = db.query(
                        TopicListContract.TopicListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case TOPICS:
                long id = db.insert(
                        TopicListContract.TopicListEntry.TABLE_NAME,
                        null,
                        values
                );
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TopicListContract.TopicListEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int topicsDeleted;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case TOPICS:
                topicsDeleted = db.delete(
                        TopicListContract.TopicListEntry.TABLE_NAME,
                        "1",
                        null
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }
        if (topicsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return topicsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
