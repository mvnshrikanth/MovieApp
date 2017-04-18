package com.example.kaka.moviedb;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieContract.MovieEntry;
import com.example.kaka.moviedb.data.MovieReview;
import com.example.kaka.moviedb.data.MovieTrailer;
import com.example.kaka.moviedb.utilities.MovieJsonUtils;
import com.example.kaka.moviedb.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.kaka.moviedb.MainActivityFragment.MOVIE_DATA;

public class DetailActivity extends AppCompatActivity {
    public static final String MOVIE_TRAILERS = "videos";
    public static final String MOVIE_REVIEWS = "reviews";
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private List<MovieTrailer> movieTrailersList;
    private List<MovieReview> movieReviewsList;
    private String overview;
    private ProgressDialog progressDialog;
    private Boolean loadTrailerFlag = false;
    private Boolean loadReviewFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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


        movieReviewsList = new ArrayList<>();
        movieTrailersList = new ArrayList<>();
        movie = this.getIntent().getParcelableExtra(MOVIE_DATA);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Movies Details...");
        progressDialog.show();

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
        ImageView imageViewPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        TextView textViewRlsDt = (TextView) findViewById(R.id.tv_release_date);
        TextView textViewRating = (TextView) findViewById(R.id.tv_rating);
        TextView textViewGenre = (TextView) findViewById(R.id.tv_genre);
        TextView textViewTitle = (TextView) findViewById(R.id.tv_movie_title);

        try {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (movie.getOriginalTitle().equals("null") || movie.getOriginalTitle().equals("")) {
                textViewTitle.setText("NA");
            } else {
                textViewTitle.setText(movie.getOriginalTitle());
            }

            if (movie.getBackdropPath().equals("null") || movie.getBackdropPath().equals("")) {
                imageViewBackdrop.setImageResource(R.drawable.not_found);
            } else {
                Picasso.with(this)
                        .load("http://image.tmdb.org/t/p/w1280/" + movie.getBackdropPath())
                        .into(imageViewBackdrop);
            }
            if (movie.getPosterPath().equals("null") || movie.getPosterPath().equals("")) {
                imageViewPoster.setImageResource(R.drawable.not_found);
            } else {
                Picasso.with(this)
                        .load("http://image.tmdb.org/t/p/w500/" + movie.getPosterPath())
                        .into(imageViewPoster);
            }
            if (movie.getVoteAverage().equals("null") || movie.getVoteAverage().equals("")) {
                textViewRating.setText("NA");
            } else {
                textViewRating.setText(movie.getVoteAverage());
            }
            if (movie.getReleaseDate().equals("null") || movie.getReleaseDate().equals("")) {
                textViewRlsDt.setText("NA");
            } else {
                textViewRlsDt.setText(movie.getReleaseDate());
            }

            if (movie.getGenre().equals("null") || movie.getGenre().equals("")) {
                textViewGenre.setText("NA");
            } else {
                textViewGenre.setText(movie.getGenre());
            }

        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Exception occurred", e);
        }

        loadMovieTrailersAndReviews(movie.getMovieId());

        overview = movie.getOverview();
    }

    public void loadViewPager() {
        if (loadTrailerFlag && loadReviewFlag) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
            CategoryAdapter categoryAdapter = new CategoryAdapter(getApplicationContext(), getSupportFragmentManager(), overview, movieReviewsList, movieTrailersList);
            viewPager.setAdapter(categoryAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void loadMovieTrailersAndReviews(String movieId) {
        MovieReviewAsyncTask movieReviewAsyncTask = new MovieReviewAsyncTask();
        movieReviewAsyncTask.execute(movieId);
        MovieTrailersAsyncTask movieTrailerAsyncTask = new MovieTrailersAsyncTask();
        movieTrailerAsyncTask.execute(movieId);
    }

    private boolean isMovieFavorite(String movieID) {
        return true;
    }

    public class MovieReviewAsyncTask extends AsyncTask<String, Void, List<MovieReview>> {
        private final String LOG_TAG = MovieReviewAsyncTask.class.getSimpleName();
        List<MovieReview> movieReviews;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MovieReview> doInBackground(String... params) {
            try {
                URL movieReviewUrl = NetworkUtils.buildURL(DetailActivity.this, DetailActivity.MOVIE_REVIEWS, params[0]);
                String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewUrl);
                movieReviews = MovieJsonUtils.getMovieReviewListFromJson(jsonReviewResponse);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error,e");
            }

            return movieReviews;
        }

        @Override
        protected void onPostExecute(List<MovieReview> movieReviews) {
            progressDialog.setProgress(100);
            progressDialog.dismiss();
            movieReviewsList = movieReviews;
            loadReviewFlag = true;
            loadViewPager();
        }
    }

    public class MovieTrailersAsyncTask extends AsyncTask<String, Void, List<MovieTrailer>> {
        private final String LOG_TAG = MovieTrailersAsyncTask.class.getSimpleName();
        List<MovieTrailer> movieTrailers;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MovieTrailer> doInBackground(String... params) {
            try {
                URL movieTrailersUrl = NetworkUtils.buildURL(DetailActivity.this, DetailActivity.MOVIE_TRAILERS, params[0]);
                String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(movieTrailersUrl);
                movieTrailers = MovieJsonUtils.getMovieTrailersListFromJson(jsonTrailersResponse);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error,e");
            }

            return movieTrailers;
        }

        @Override
        protected void onPostExecute(List<MovieTrailer> movieTrailers) {
            progressDialog.setProgress(100);
            progressDialog.dismiss();
            movieTrailersList = movieTrailers;
            loadTrailerFlag = true;
            loadViewPager();
        }
    }
}
