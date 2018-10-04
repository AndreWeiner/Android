package com.myheutahogy.simplecfd.Activities;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.RemoteViews;

import com.myheutahogy.simplecfd.DataUtils.Lecture;
import com.myheutahogy.simplecfd.R;

/**
 * Implementation of App Widget functionality.
 */
public class LastLectureWidget extends AppWidgetProvider {

    public static final String LECTURE_SERVICE = "lastLecture";
    private Lecture mLastLecture;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Lecture lastLecture) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_lecture_widget);
        if (lastLecture != null) {
            views.setTextViewText(R.id.appwidget_title, lastLecture.getTitle());
            views.setTextViewText(R.id.appwidget_lecture, Html.fromHtml(lastLecture.getLectureText()));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, mLastLecture);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.hasExtra(LECTURE_SERVICE)) {
            mLastLecture = intent.getParcelableExtra(LECTURE_SERVICE);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(
                    context.getPackageName(), LastLectureWidget.class.getName());
            int[] widgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            onUpdate(context, appWidgetManager, widgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

