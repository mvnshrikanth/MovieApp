package com.example.kaka.moviedb;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaka.moviedb.data.MovieTrailer;

import java.util.List;

/**
 * Created by Kaka on 4/6/2017.
 */

public class MovieTrailersFragment extends Fragment {
    public static final String MOVIE_TRAILERS_KEY = "trailer";

    public MovieTrailersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_trailers_fragment, container, false);
        savedInstanceState = this.getArguments();
        List<MovieTrailer> movieTrailerList = (List<MovieTrailer>) savedInstanceState.getSerializable(MOVIE_TRAILERS_KEY);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_movie_trailer_list);
        TrailerAdapter trailerAdapter = new TrailerAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(trailerAdapter);
        trailerAdapter.prepareMovieTrailers(movieTrailerList);
        return view;
    }

}
