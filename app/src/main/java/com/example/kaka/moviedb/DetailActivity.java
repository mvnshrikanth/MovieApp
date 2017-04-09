package com.example.kaka.moviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaka.moviedb.data.Movie;
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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action" + movie.getOriginalTitle(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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

//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
//        CategoryAdapter categoryAdapter = new CategoryAdapter(this, getSupportFragmentManager());
//        viewPager.setAdapter(categoryAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);


    }
}
