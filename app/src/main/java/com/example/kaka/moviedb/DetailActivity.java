package com.example.kaka.moviedb;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;

import static com.example.kaka.moviedb.MainActivityFragment.MOVIE_DATA;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    Boolean mFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                mFlag = true;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                mFlag = false;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                mFlag = false;
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                mFlag = true;
                break;
            default:
                mFlag = false;
        }

        if (!mFlag) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MOVIE_DATA, this.getIntent().getParcelableExtra(MOVIE_DATA));

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(bundle);

            getFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, detailActivityFragment)
                    .commit();
        }

        final Movie movie;
        Boolean favInd = false;
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        final Boolean[] finalFavInd = new Boolean[1];
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

        movie = this.getIntent().getParcelableExtra(MOVIE_DATA);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, Long.parseLong(movie.getMovieId()));
        cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor.getCount() > 0) {
            favInd = false;
            floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            favInd = true;
            floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        finalFavInd[0] = favInd;

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (finalFavInd[0]) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
                    contentValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                    contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                    contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                    contentValues.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                    contentValues.put(MovieEntry.COLUMN_GENRE, movie.getGenre());
                    Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);

                    Snackbar.make(view, "Saved " + movie.getOriginalTitle() + " as favorite ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    finalFavInd[0] = false;
                } else {
                    Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, Long.parseLong(movie.getMovieId()));
                    int rowsDeleted = getContentResolver().delete(uri, null, null);

                    Snackbar.make(view, "Deleted " + movie.getOriginalTitle() + " from favorite ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    finalFavInd[0] = true;
                }
            }
        });

        ImageView imageViewBackdrop = (ImageView) findViewById(R.id.iv_movie_backdrop);

        try {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (movie.getBackdropPath().equals("null") || movie.getBackdropPath().equals("")) {
                imageViewBackdrop.setImageResource(R.drawable.not_found);
            } else {
                Picasso.with(this)
                        .load("http://image.tmdb.org/t/p/w1280/" + movie.getBackdropPath())
                        .into(imageViewBackdrop);
            }

        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Exception occurred", e);
        }

    }

}
