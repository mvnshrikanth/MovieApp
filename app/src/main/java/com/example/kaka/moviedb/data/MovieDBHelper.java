package com.example.kaka.moviedb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kaka.moviedb.data.MovieContract.MovieEntry;

/**
 * Created by Kaka on 3/20/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = MovieDBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "movieDB.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "("
                + MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY,"
                + MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT,"
                + MovieEntry.COLUMN_POSTER_PATH + " TEXT,"
                + MovieEntry.COLUMN_RELEASE_DATE + " TEXT,"
                + MovieEntry.COLUMN_OVERVIEW + " TEXT,"
                + MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT,"
                + MovieEntry.COLUMN_BACKDROP_PATH + " TEXT,"
                + MovieEntry.COLUMN_GENRE + " TEXT" + ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
