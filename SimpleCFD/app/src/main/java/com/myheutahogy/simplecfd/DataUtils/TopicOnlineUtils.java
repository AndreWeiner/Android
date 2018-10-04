package com.myheutahogy.simplecfd.DataUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public final class TopicOnlineUtils {
    final static String JSON_TOPIC_URL =
            "https://raw.githubusercontent.com/AndreWeiner/Android/master/topics_list.json";
    final static String DELIMITER = "\\A";
    // JSON keys
    final static String TOPIC_NAME = "topic_name";
    final static String TOPIC_DESCRIPTION = "topic_description";
    final static String TOPIC_IMAGE_URL = "topic_image_url";
    final static String TOPIC_LECTURES = "lectures";
    final static String LECTURE_TITLE = "lecture_title";
    final static String LECTURE_TEXT_URL = "lecture_text_url";
    final static String LECTURE_VIDEO_ID = "lecture_video_id";

    /**
     * Make http request and return result String.
     *
     * @return The result of the http request.
     */
    private static String getHttpString(String urlString) {
        String httpResponse = null;
        URL url = null;
        // build URL
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // make HTTP request
        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter(DELIMITER);
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                httpResponse = scanner.next();
                in.close();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }

    public static ArrayList<Topic> getTopicsFromJSON() throws JSONException {
        ArrayList<Topic> topics = new ArrayList<>();
        String JSONString = getHttpString(JSON_TOPIC_URL);
        if (!JSONString.equals("")) {
            JSONArray jsonArray = new JSONArray(JSONString);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject topic = jsonArray.getJSONObject(i);
                    String topicName = topic.getString(TOPIC_NAME);
                    String topicDes = topic.getString(TOPIC_DESCRIPTION);
                    String topicUrl = topic.getString(TOPIC_IMAGE_URL);

                    JSONArray lecturesJSON = topic.getJSONArray(TOPIC_LECTURES);
                    ArrayList<Lecture> lectures = new ArrayList<>();
                    for (int j = 0; j < lecturesJSON.length(); j++) {
                        JSONObject lecture = lecturesJSON.getJSONObject(j);
                        String title = lecture.getString(LECTURE_TITLE);
                        String textUrl = lecture.getString(LECTURE_TEXT_URL);
                        String lectureText = getHttpString(textUrl);
                        String videoId = lecture.getString(LECTURE_VIDEO_ID);
                        lectures.add(new Lecture(
                                        title, lectureText, videoId
                                )
                        );
                    }
                    topics.add(new Topic(
                                    topicName, topicDes, topicUrl, lectures
                            )
                    );
                }

            }
        }
        return topics;
    }

    public static boolean internetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
