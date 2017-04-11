package com.example.kaka.moviedb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kaka on 4/6/2017.
 */

public class MovieOverviewsFragment extends Fragment {
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    private String overview;

    public MovieOverviewsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_overview_fragment, container, false);
        savedInstanceState = this.getArguments();
        this.overview = savedInstanceState.getString(MOVIE_OVERVIEW_KEY);
        TextView textView = (TextView) view.findViewById(R.id.tv_overview);
        textView.setText(overview);
        return view;
    }
}
