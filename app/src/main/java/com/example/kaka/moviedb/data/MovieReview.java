package com.example.kaka.moviedb.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kaka on 4/9/2017.
 */

public class MovieReview implements Parcelable {
    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
    private String review;
    private String author;

    public MovieReview(String review, String author) {
        this.review = review;
        this.author = author;
    }

    protected MovieReview(Parcel in) {
        review = in.readString();
        author = in.readString();
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(review);
        dest.writeString(author);
    }
}
