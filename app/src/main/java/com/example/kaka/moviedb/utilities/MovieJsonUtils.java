package com.example.kaka.moviedb.utilities;

import com.example.kaka.moviedb.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaka on 3/14/2017.
 */

public class MovieJsonUtils {

    public static final String ORIGNAL_TITLE = "original_title";
    public static final String POSTER_PATH = "poster_path";
    public static final String RELEASE_DATE = "release_date";
    public static final String OVERVIEW = "overview";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String BACKDRP_IMAGE = "backdrop_path";
    public static final String MOVIE_ID = "id";

    public static final String REVIEWS = "content";
    public static final String REVIEW_AUTHOR = "author";
    public static final String TRAILER_KEY = "key";
    public static final String TRAILER_NAME = "name";

    public static List<Movie> getMovieListFromJson(String movieJsonStr) throws JSONException {

        List<Movie> movieList = new ArrayList<>();
        final String MOVIE_LIST = "results";

        JSONObject rootMovieJson = new JSONObject(movieJsonStr);
        JSONArray allMovieJsonArray = rootMovieJson.getJSONArray(MOVIE_LIST);

        for (int i = 0; i < allMovieJsonArray.length(); i++) {
            JSONObject movieDataObj = allMovieJsonArray.getJSONObject(i);
            movieList.add(new Movie(movieDataObj.getString(MOVIE_ID),
                    movieDataObj.getString(ORIGNAL_TITLE),
                    movieDataObj.getString(POSTER_PATH),
                    movieDataObj.getString(RELEASE_DATE),
                    movieDataObj.getString(OVERVIEW),
                    movieDataObj.getString(VOTE_AVERAGE),
                    movieDataObj.getString(BACKDRP_IMAGE)));
        }
        return movieList;
    }
}
