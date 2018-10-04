package com.myheutahogy.simplecfd.DataUtils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Topic implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
    private final static String DELIMITER = "|||";
    private String mName;
    private String mDescription;
    private String mImageUrl;
    private ArrayList<Lecture> mLectures;

    public Topic(String name, String description, String imageUrl, ArrayList<Lecture> lectures) {
        mName = name;
        mDescription = description;
        mImageUrl = imageUrl;
        mLectures = lectures;
    }

    public Topic(Parcel in) {
        mName = in.readString();
        mDescription = in.readString();
        mImageUrl = in.readString();
        mLectures = in.createTypedArrayList(Lecture.CREATOR);
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public ArrayList<Lecture> getLectures() {
        return mLectures;
    }

    public String lecturesToString() {
        String lectures = "";
        for (Lecture lecture : mLectures) {
            lectures += lecture.toString() + DELIMITER;
        }
        // remove last delimiter
        return lectures.substring(0, lectures.length() - DELIMITER.length());
    }

    public void setLecturesFromString(String concatenatedLectures) {
        ArrayList<Lecture> lectures = new ArrayList<>();
        String[] tokens = concatenatedLectures.split(Pattern.quote(DELIMITER));
        for (String lec : tokens) {
            lectures.add(new Lecture(lec));
        }
        mLectures = lectures;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mDescription);
        parcel.writeString(mImageUrl);
        parcel.writeTypedList(mLectures);
    }
}
