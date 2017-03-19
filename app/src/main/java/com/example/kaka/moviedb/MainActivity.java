package com.example.kaka.moviedb;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kaka.moviedb.utilities.MovieJsonUtils;
import com.example.kaka.moviedb.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    public static final String MOVIE_DATA = "MOVIE";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public final String SORT_TYPE_POPULARITY = "popularity";
    public final String SORT_TYPE_RATING = "rating";
    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private TextView textView;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayoutManager gridLayoutManager;

        movieList = new ArrayList<>();

        textView = (TextView) findViewById(R.id.tv_error_message_display);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        movieAdapter = new MovieAdapter(MainActivity.this, MainActivity.this);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        } else {
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 4);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(movieAdapter);

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        loadMovieData(SORT_TYPE_POPULARITY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_popularity:
                loadMovieData(SORT_TYPE_POPULARITY);
                return true;
            case R.id.action_sort_by_rating:
                loadMovieData(SORT_TYPE_RATING);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(String sort_type) {
        new FetchMovieData().execute(sort_type);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_DATA, movie);
        startActivity(intent);
    }

    public class FetchMovieData extends AsyncTask<String, Void, List<Movie>> {
        private final String LOG_TAG = FetchMovieData.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            List<Movie> movieReturnList;
            if (params.length == 0) {
                return null;
            }

            URL movieRequestUrl = NetworkUtils.buildURL(MainActivity.this, params[0]);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                movieReturnList = MovieJsonUtils.getMovieListFromJson(jsonMovieResponse);
                return movieReturnList;
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movieReturnList) {
            progressBar.setVisibility(View.INVISIBLE);
            if (movieReturnList != null) {
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                movieList = movieReturnList;
                movieAdapter.prepareMovieList(movieReturnList);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
            }

        }
    }

}
