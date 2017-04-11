package com.example.kaka.moviedb.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kaka on 4/9/2017.
 */


public class MovieTrailer implements Parcelable {
    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
    String trailer_name;
    String trailer_key;

    public MovieTrailer(String trailer_name, String trailer_key) {
        this.trailer_name = trailer_name;
        this.trailer_key = trailer_key;
    }

    protected MovieTrailer(Parcel in) {
        trailer_name = in.readString();
        trailer_key = in.readString();
    }

    public String getTrailer_name() {
        return trailer_name;
    }

    public void setTrailer_name(String trailer_name) {
        this.trailer_name = trailer_name;
    }

    public String getTrailer_key() {
        return trailer_key;
    }

    public void setTrailer_key(String trailer_key) {
        this.trailer_key = trailer_key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailer_name);
        dest.writeString(trailer_key);
    }
}



