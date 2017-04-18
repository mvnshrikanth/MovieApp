package com.example.kaka.moviedb;


import android.app.Fragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieContract;
import com.example.kaka.moviedb.utilities.MovieLoader;
import com.example.kaka.moviedb.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.kaka.moviedb.MainActivity.MY_PREFERENCE;
import static com.example.kaka.moviedb.MainActivity.MY_SORT_PREFERENCE_KEY;
import static com.example.kaka.moviedb.MainActivity.SORT_TYPE_FAVORITE;

/**
 * Created by Kaka on 4/17/2017.
 */

public class MainActivityFragment extends Fragment
        implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {

    public static final String MOVIE_DATA = "MOVIE";
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 1;
    SharedPreferences sharedPreferences;
    private View view;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private View emptyView;
    private List<Movie> movieList;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);

        GridLayoutManager gridLayoutManager;

        movieList = new ArrayList<>();
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading Movies ...");
        progressDialog.show();

        emptyView = view.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_movie_list);
        movieAdapter = new MovieAdapter(view.getContext(), MainActivityFragment.this);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        } else {
            gridLayoutManager = new GridLayoutManager(view.getContext(), 4);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(movieAdapter);

        sharedPreferences = view.getContext().getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);

        loadMovieData(sharedPreferences.getString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_FAVORITE));

        return view;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        URL movieRequestUrl = null;
        if (args.getString("sort_type").equals(SORT_TYPE_FAVORITE)) {
            return new MovieLoader(view.getContext(), MovieContract.MovieEntry.CONTENT_URI);
        } else {
            movieRequestUrl = NetworkUtils.buildURL(view.getContext(), args.getString("sort_type"));
            return new MovieLoader(view.getContext(), movieRequestUrl);
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

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra(MOVIE_DATA, movie);
        startActivity(intent);
    }

    private void loadMovieData(String sort_type) {

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
