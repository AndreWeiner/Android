package com.myheutahogy.simplecfd.DataUtils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.regex.Pattern;

public class Lecture implements Parcelable {
    private String mTitle;
    private String mLectureText;
    private String mVideoId;
    private final static String DELIMITER = "||";

    public Lecture(String title, String lectureText, String videoId){
        mTitle = title;
        mLectureText = lectureText;
        mVideoId = videoId;
    }

    public Lecture(String concatenatedLecture) {
        String[] tokens = concatenatedLecture.split(Pattern.quote(DELIMITER));
        mTitle = tokens[0];
        mLectureText = tokens[1];
        mVideoId = tokens[2];
    }

    public Lecture(Parcel in) {
        mTitle = in.readString();
        mLectureText = in.readString();
        mVideoId = in.readString();
    }

    public String getTitle(){
        return mTitle;
    }

    public String getLectureText(){
        return mLectureText;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String toString() {
        return mTitle + DELIMITER + mLectureText + DELIMITER + mVideoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mLectureText);
        parcel.writeString(mVideoId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };
}
