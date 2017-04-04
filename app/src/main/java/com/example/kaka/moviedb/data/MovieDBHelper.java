package com.example.kaka.moviedb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kaka on 3/20/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MovieDBHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
