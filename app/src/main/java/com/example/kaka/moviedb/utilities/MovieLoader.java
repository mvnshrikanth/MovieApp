package com.example.kaka.moviedb.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieContract.MovieEntry;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaka on 3/19/2017.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = MovieLoader.class.getSimpleName();
    private URL movieRequestUrl;
    private Uri movieRequestUri;

    public MovieLoader(Context context, URL url) {
        super(context);
        movieRequestUrl = url;
    }

    public MovieLoader(Context context, Uri uri) {
        super(context);
        movieRequestUri = uri;
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movieReturnList = new ArrayList<>();
        Cursor cursor;
        String[] projection = {
                MovieEntry.COLUMN_MOVIE_ID,
                MovieEntry.COLUMN_ORIGINAL_TITLE,
                MovieEntry.COLUMN_POSTER_PATH,
                MovieEntry.COLUMN_RELEASE_DATE,
                MovieEntry.COLUMN_OVERVIEW,
                MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieEntry.COLUMN_BACKDROP_PATH,
                MovieEntry.COLUMN_GENRE
        };

        try {
            if (movieRequestUrl != null) {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                movieReturnList = MovieJsonUtils.getMovieListFromJson(jsonMovieResponse);
                return movieReturnList;
            } else {
                cursor = getContext().getContentResolver().query(movieRequestUri,
                        projection,
                        null,
                        null,
                        null);
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    movieReturnList.add(new Movie(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)),
                            cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE)),
                            cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)),
                            cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)),
                            cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE)),
                            cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH)),
                            cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_GENRE))
                    ));
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error", e);
            return null;
        }
        return movieReturnList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
