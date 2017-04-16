package com.example.kaka.moviedb.utilities;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieReview;
import com.example.kaka.moviedb.data.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaka on 3/14/2017.
 */

public class MovieJsonUtils {

    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String RELEASE_DATE = "release_date";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String BACKDROP_IMAGE = "backdrop_path";
    private static final String MOVIE_ID = "id";
    private static final String GENRE = "genre_ids";
    private static final String REVIEWS = "content";
    private static final String REVIEW_AUTHOR = "author";
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_NAME = "name";

    public static List<Movie> getMovieListFromJson(String movieJsonStr) throws JSONException {

        List<Movie> movieList = new ArrayList<>();
        final String MOVIE_LIST = "results";

        JSONObject rootMovieJson = new JSONObject(movieJsonStr);
        JSONArray allMovieJsonArray = rootMovieJson.getJSONArray(MOVIE_LIST);

        for (int i = 0; i < allMovieJsonArray.length(); i++) {
            JSONObject movieDataObj = allMovieJsonArray.getJSONObject(i);
            movieList.add(new Movie(movieDataObj.getString(MOVIE_ID),
                    movieDataObj.getString(ORIGINAL_TITLE),
                    movieDataObj.getString(POSTER_PATH),
                    movieDataObj.getString(RELEASE_DATE),
                    movieDataObj.getString(OVERVIEW),
                    movieDataObj.getString(VOTE_AVERAGE),
                    movieDataObj.getString(BACKDROP_IMAGE),
                    getGenre(movieDataObj.getJSONArray(GENRE))));
        }
        return movieList;
    }

    public static List<MovieReview> getMovieReviewListFromJson(String jsonReviewResponse) throws JSONException {
        List<MovieReview> movieReviewsList = new ArrayList<>();
        final String MOVIE_REVIEW_LIST = "results";

        JSONObject movieReviewsJson = new JSONObject(jsonReviewResponse);
        JSONArray movieReviewsArray = movieReviewsJson.getJSONArray(MOVIE_REVIEW_LIST);

        for (int i = 0; i < movieReviewsArray.length(); i++) {
            JSONObject movieReviewObj = movieReviewsArray.getJSONObject(i);
            movieReviewsList.add(new MovieReview(movieReviewObj.getString(REVIEWS),
                    movieReviewObj.getString(REVIEW_AUTHOR)));
        }

        return movieReviewsList;
    }

    public static List<MovieTrailer> getMovieTrailersListFromJson(String jsonTrailersResponse) throws JSONException {
        List<MovieTrailer> movieTrailersList = new ArrayList<>();
        final String MOVIE_TRAILER_LIST = "results";

        JSONObject movieTrailersJson = new JSONObject(jsonTrailersResponse);
        JSONArray movieTrailersArray = movieTrailersJson.getJSONArray(MOVIE_TRAILER_LIST);

        for (int i = 0; i < movieTrailersArray.length(); i++) {
            JSONObject movieTrailerObj = movieTrailersArray.getJSONObject(i);
            movieTrailersList.add(new MovieTrailer(movieTrailerObj.getString(TRAILER_NAME),
                    movieTrailerObj.getString(TRAILER_KEY)));
        }

        return movieTrailersList;
    }

    private static String getGenre(JSONArray jsonArray) throws JSONException {
        String genre = "";
        int len = 0;
        //Show at max three genre
        if (jsonArray.length() >= 3) {
            len = 3;
        } else {
            len = jsonArray.length();
        }
        for (int i = 0; i < len; i++) {

            if (i >= 1) {
                genre = genre + " | ";
            }

            switch (jsonArray.getInt(i)) {
                case 28:
                    genre = genre + "Action";
                    break;
                case 12:
                    genre = genre + "Adventure";
                    break;
                case 16:
                    genre = genre + "Animation";
                    break;
                case 35:
                    genre = genre + "Comedy";
                    break;
                case 80:
                    genre = genre + "Crime";
                    break;
                case 99:
                    genre = genre + "Documentary";
                    break;
                case 18:
                    genre = genre + "Drama";
                    break;
                case 10751:
                    genre = genre + "Family";
                    break;
                case 14:
                    genre = genre + "Fantasy";
                    break;
                case 36:
                    genre = genre + "History";
                    break;
                case 27:
                    genre = genre + "Horror";
                    break;
                case 10402:
                    genre = genre + "Music";
                    break;
                case 9648:
                    genre = genre + "Mystery";
                    break;
                case 10749:
                    genre = genre + "Romance";
                    break;
                case 878:
                    genre = genre + "Science Fiction";
                    break;
                case 10770:
                    genre = genre + "TV Movie";
                    break;
                case 53:
                    genre = genre + "Thriller";
                    break;
                case 10752:
                    genre = genre + "War";
                    break;
                case 37:
                    genre = genre + "Western";
                    break;
                default:
                    genre = genre + "misc";
            }
        }
        return genre;
    }


}
