package com.example.kaka.moviedb.data;

/**
 * Created by Kaka on 4/9/2017.
 */


public class MovieTrailer {
    String trailer_name;
    String trailer_key;

    public MovieTrailer(String trailer_name, String trailer_key) {
        this.trailer_name = trailer_name;
        this.trailer_key = trailer_key;
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
}



