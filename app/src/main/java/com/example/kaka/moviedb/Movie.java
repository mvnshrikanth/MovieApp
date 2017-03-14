package com.example.kaka.moviedb;

/**
 * Created by Kaka on 3/14/2017.
 */

public class Movie {

    private String movieId;
    private String originalTitle;
    private String posterPath;
    private String releaseDate;
    private String overview;
    private String voteAverage;
    private String backdropPath;

    public Movie(String movieId, String originalTitle) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
    }

    public Movie(String movieId,
                 String originalTitle,
                 String posterPath,
                 String releaseDate,
                 String overview,
                 String voteAverage,
                 String backdropPath) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.backdropPath = backdropPath;
    }

    public Movie() {

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
}
