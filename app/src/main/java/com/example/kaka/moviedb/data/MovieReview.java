package com.example.kaka.moviedb.data;

/**
 * Created by Kaka on 4/9/2017.
 */

public class MovieReview {
    String review;
    String author;

    public MovieReview(String review, String author) {
        this.review = review;
        this.author = author;
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
}
