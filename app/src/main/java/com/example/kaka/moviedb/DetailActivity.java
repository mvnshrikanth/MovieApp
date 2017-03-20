package com.example.kaka.moviedb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.kaka.moviedb.MainActivity.MOVIE_DATA;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Movie movie;
        movie = this.getIntent().getParcelableExtra(MOVIE_DATA);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action" + movie.getOriginalTitle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageView imageViewBackdrop = (ImageView) findViewById(R.id.iv_movie_backdrop);
        ImageView imageViewPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        TextView textViewRlsDt = (TextView) findViewById(R.id.tv_release_date);
        TextView textViewRating = (TextView) findViewById(R.id.tv_rating);
        TextView textViewOverview = (TextView) findViewById(R.id.tv_overview);
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
            if (movie.getOverview().equals("null") || movie.getOverview().equals("")) {
                textViewOverview.setText("NA");
            } else {
                textViewOverview.setText(movie.getOverview());
            }
            if (movie.getVoteAverage().equals("null") || movie.getVoteAverage().equals("")) {
                textViewOverview.setText("NA");
            } else {
                textViewRating.setText(movie.getVoteAverage());
            }
            if (movie.getReleaseDate().equals("null") || movie.getReleaseDate().equals("")) {
                textViewOverview.setText("NA");
            } else {
                textViewRlsDt.setText(movie.getReleaseDate());
            }

        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Exception occurred", e);
        }


    }
}
