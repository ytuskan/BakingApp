package com.app.tuskan.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yakup on 18.7.2018.
 */

public class Video implements Parcelable{
    private String shortDescription;
    private String description;
    private String videoUrl;

    public Video(String shortDescription, String description, String videoUrl) {
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
        dest.writeString(this.videoUrl);
    }

    public Video(Parcel in){
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoUrl = in.readString();

    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
