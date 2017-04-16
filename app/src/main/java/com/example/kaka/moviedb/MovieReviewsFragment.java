package com.example.kaka.moviedb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaka.moviedb.data.MovieReview;

import java.util.List;

/**
 * Created by Kaka on 4/6/2017.
 */

public class MovieReviewsFragment extends Fragment {
    public static final String MOVIE_REVIEW_KEY = "review";

    public MovieReviewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_review_fragment, container, false);
        savedInstanceState = this.getArguments();
        List<MovieReview> movieReviewList = (List<MovieReview>) savedInstanceState.getSerializable(MOVIE_REVIEW_KEY);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_movie_review_list);
        ReviewAdapter reviewAdapter = new ReviewAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reviewAdapter);
        reviewAdapter.prepareMovieReviews(movieReviewList);
        return view;
    }
}
