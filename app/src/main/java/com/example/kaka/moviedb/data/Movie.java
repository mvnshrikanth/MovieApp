package com.example.kaka.moviedb.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kaka on 3/14/2017.
 */

public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private String movieId;
    private String originalTitle;
    private String posterPath;
    private String releaseDate;
    private String overview;
    private String voteAverage;
    private String backdropPath;
    private String genre;


    protected Movie(Parcel in) {
        movieId = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        backdropPath = in.readString();
        genre = in.readString();
    }

    public Movie(String movieId,
                 String originalTitle,
                 String posterPath,
                 String releaseDate,
                 String overview,
                 String voteAverage,
                 String backdropPath, String genre) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.backdropPath = backdropPath;
        this.genre = genre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeString(backdropPath);
        dest.writeString(genre);
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


}
