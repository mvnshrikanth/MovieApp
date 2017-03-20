package com.example.kaka.moviedb.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.kaka.moviedb.Movie;

import java.net.URL;
import java.util.List;

/**
 * Created by Kaka on 3/19/2017.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = MovieLoader.class.getSimpleName();
    private URL movieRequestUrl;

    public MovieLoader(Context context, URL url) {
        super(context);
        movieRequestUrl = url;
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movieReturnList;
        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
            movieReturnList = MovieJsonUtils.getMovieListFromJson(jsonMovieResponse);
            return movieReturnList;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error", e);
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
