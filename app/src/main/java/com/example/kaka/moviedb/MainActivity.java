package com.example.kaka.moviedb;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieContract;
import com.example.kaka.moviedb.utilities.MovieLoader;
import com.example.kaka.moviedb.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {

    public static final String MOVIE_DATA = "MOVIE";
    public static final String SORT_TYPE_POPULARITY = "popularity";
    public static final String SORT_TYPE_RATING = "rating";
    public static final String SORT_TYPE_FAVORITE = "favorite";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 1;

    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private View emptyView;
    private List<Movie> movieList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayoutManager gridLayoutManager;

        movieList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Movies ...");

        emptyView = findViewById(R.id.empty_view);
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
            case R.id.action_sort_by_favorite:
                loadMovieData(SORT_TYPE_FAVORITE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(String sort_type) {

        if (!networkAvailable()) {
            sort_type = SORT_TYPE_FAVORITE;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No network connection available to fetch movie data, only favorites can be seen if you have any.")
                    .setTitle("Network Connection Unavailable")
                    .create()
                    .show();
            progressDialog.show();
            LoaderManager loaderManager = getLoaderManager();
            Bundle bundle = new Bundle();
            bundle.putString("sort_type", sort_type);

            if (movieList.size() <= 0) {
                loaderManager.initLoader(MOVIE_LOADER_ID, bundle, this);
            } else {
                loaderManager.restartLoader(MOVIE_LOADER_ID, bundle, this);
            }
        } else {
            progressDialog.show();
            LoaderManager loaderManager = getLoaderManager();
            Bundle bundle = new Bundle();
            bundle.putString("sort_type", sort_type);

            if (movieList.size() <= 0) {
                loaderManager.initLoader(MOVIE_LOADER_ID, bundle, this);
            } else {
                loaderManager.restartLoader(MOVIE_LOADER_ID, bundle, this);
            }
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_DATA, movie);
        startActivity(intent);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        URL movieRequestUrl = null;
        if (args.getString("sort_type").equals(SORT_TYPE_FAVORITE)) {
            return new MovieLoader(this, MovieContract.MovieEntry.CONTENT_URI);
        } else {
            movieRequestUrl = NetworkUtils.buildURL(MainActivity.this, args.getString("sort_type"));
            return new MovieLoader(this, movieRequestUrl);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movieReturnList) {

        progressDialog.setProgress(100);
        progressDialog.dismiss();

        if (movieReturnList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
            movieList = movieReturnList;
            movieAdapter.prepareMovieList(movieReturnList);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieList.clear();
    }

    public boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

