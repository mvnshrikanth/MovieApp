package com.example.kaka.moviedb;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieReview;
import com.example.kaka.moviedb.data.MovieTrailer;
import com.example.kaka.moviedb.utilities.MovieJsonUtils;
import com.example.kaka.moviedb.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.kaka.moviedb.MainActivity.MOVIE_DATA;

public class DetailActivity extends AppCompatActivity {
    public static final String MOVIE_TRAILERS = "videos";
    public static final String MOVIE_REVIEWS = "reviews";
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private List<MovieTrailer> movieTrailersList;
    private List<MovieReview> movieReviewsList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Movie movie;
        movieReviewsList = new ArrayList<>();
        movieTrailersList = new ArrayList<>();
        movie = this.getIntent().getParcelableExtra(MOVIE_DATA);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Movies Details...");
        progressDialog.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action " + movie.getOriginalTitle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(categoryAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        loadMovieTrailersAndReviews(movie.getMovieId());

    }

    private void loadMovieTrailersAndReviews(String movieId) {
        MovieReviewAsyncTask movieReviewAsyncTask = new MovieReviewAsyncTask();
        movieReviewAsyncTask.execute(movieId);
        MovieTrailersAsyncTask movieTrailerAsyncTask = new MovieTrailersAsyncTask();
        movieTrailerAsyncTask.execute(movieId);
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
                URL movieReviewUrl = NetworkUtils.buildURL(DetailActivity.this, params[0]);
                String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewUrl);
                movieReviews = MovieJsonUtils.getMovieReviewListFromJson(jsonReviewResponse);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error,e");
            }

            return movieReviews;
        }

        @Override
        protected void onPostExecute(List<MovieReview> movieReviews) {
            movieReviewsList = movieReviews;
            progressDialog.setProgress(100);
            progressDialog.dismiss();
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
                URL movieTrailersUrl = NetworkUtils.buildURL(DetailActivity.this, params[0]);
                String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(movieTrailersUrl);
                movieTrailers = MovieJsonUtils.getMovieTrailersListFromJson(jsonTrailersResponse);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error,e");
            }

            return movieTrailers;
        }

        @Override
        protected void onPostExecute(List<MovieTrailer> movieTrailrs) {
            movieTrailersList = movieTrailrs;
            progressDialog.setProgress(100);
            progressDialog.dismiss();
            Toast.makeText(DetailActivity.this, "Trailer Name" + movieReviewsList, Toast.LENGTH_SHORT).show();
        }
    }


}
