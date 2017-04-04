package com.example.kaka.moviedb.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kaka on 3/20/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.kaka.moviedb";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public MovieContract() {
    }

    public static final class MovieEntry implements BaseColumns {

    }
}
