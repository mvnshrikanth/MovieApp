package com.example.kaka.moviedb.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kaka.moviedb.data.MovieContract.MovieEntry;

/**
 * Created by Kaka on 3/20/2017.
 */

public class MovieProvider extends ContentProvider {

    private static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);
    }

    private MovieDBHelper movieDBHelper;

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                cursor = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_ID:
                selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return insertStock(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertStock(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getWritableDatabase();

        String movieID = values.getAsString(MovieEntry.COLUMN_MOVIE_ID);
        if (movieID.equals("")) throw new IllegalArgumentException("Requires a valid movie ID.");
        String movieTitle = values.getAsString(MovieEntry.COLUMN_ORIGINAL_TITLE);
        if (movieTitle.equals(""))
            throw new IllegalArgumentException("Requires a valid movie title.");
        String posterImage = values.getAsString(MovieEntry.COLUMN_POSTER_PATH);
        if (posterImage.equals(""))
            throw new IllegalArgumentException("Requires a valid movie posterImage.");
        String releaseDate = values.getAsString(MovieEntry.COLUMN_RELEASE_DATE);
        if (releaseDate.equals(""))
            throw new IllegalArgumentException("Requires a valid movie release date.");
        String movieOverview = values.getAsString(MovieEntry.COLUMN_OVERVIEW);
        if (movieOverview.equals(""))
            throw new IllegalArgumentException("Requires a valid movie overview text.");
        String voteAverage = values.getAsString(MovieEntry.COLUMN_VOTE_AVERAGE);
        if (voteAverage.equals(""))
            throw new IllegalArgumentException("Requires a valid movie vote average.");
        String BackDropImage = values.getAsString(MovieEntry.COLUMN_BACKDROP_PATH);
        if (BackDropImage.equals(""))
            throw new IllegalArgumentException("Requires a valid movie backdrop image.");
        String genre = values.getAsString(MovieEntry.COLUMN_GENRE);
        if (genre.equals(""))
            throw new IllegalArgumentException("Requires a valid movie genre text.");

        long id = sqLiteDatabase.insert(MovieEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                rowsDeleted = sqLiteDatabase.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                selection = MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqLiteDatabase.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return updateStocks(uri, values, selection, selectionArgs);
            case MOVIE_ID:
                selection = MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateStocks(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateStocks(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getWritableDatabase();

        if (values.containsKey(MovieEntry.COLUMN_MOVIE_ID)) {
            String movieID = values.getAsString(MovieEntry.COLUMN_MOVIE_ID);
            if (movieID.equals(""))
                throw new IllegalArgumentException("Requires a valid movie ID.");
        }

        if (values.containsKey(MovieEntry.COLUMN_ORIGINAL_TITLE)) {
            String movieTitle = values.getAsString(values.getAsString(MovieEntry.COLUMN_ORIGINAL_TITLE));
            if (movieTitle.equals(""))
                throw new IllegalArgumentException("Requires a valid movie title.");
        }

        if (values.containsKey(MovieEntry.COLUMN_POSTER_PATH)) {
            String movieTitle = values.getAsString(values.getAsString(MovieEntry.COLUMN_POSTER_PATH));
            if (movieTitle.equals(""))
                throw new IllegalArgumentException("Requires a valid movie posterImage.");
        }
        if (values.containsKey(MovieEntry.COLUMN_RELEASE_DATE)) {
            String releaseDate = values.getAsString(values.getAsString(MovieEntry.COLUMN_RELEASE_DATE));
            if (releaseDate.equals(""))
                throw new IllegalArgumentException("Requires a valid movie release date.");
        }
        if (values.containsKey(MovieEntry.COLUMN_OVERVIEW)) {
            String movieOverview = values.getAsString(values.getAsString(MovieEntry.COLUMN_OVERVIEW));
            if (movieOverview.equals(""))
                throw new IllegalArgumentException("Requires a valid movie overview text.");
        }
        if (values.containsKey(MovieEntry.COLUMN_VOTE_AVERAGE)) {
            String voteAverage = values.getAsString(values.getAsString(MovieEntry.COLUMN_VOTE_AVERAGE));
            if (voteAverage.equals(""))
                throw new IllegalArgumentException("Requires a valid movie vote average.");
        }
        if (values.containsKey(MovieEntry.COLUMN_BACKDROP_PATH)) {
            String backdropPath = values.getAsString(values.getAsString(MovieEntry.COLUMN_BACKDROP_PATH));
            if (backdropPath.equals(""))
                throw new IllegalArgumentException("Requires a valid movie backdrop image.");
        }
        if (values.containsKey(MovieEntry.COLUMN_GENRE)) {
            String genre = values.getAsString(values.getAsString(MovieEntry.COLUMN_GENRE));
            if (genre.equals(""))
                throw new IllegalArgumentException("Requires a valid movie genre text.");
        }


        if (values.size() == 0) {
            return 0;
        }

        int rowsUpdated = sqLiteDatabase.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
