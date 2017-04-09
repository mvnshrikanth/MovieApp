package com.example.kaka.moviedb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kaka on 4/6/2017.
 */

public class MovieOverviewsFragment extends Fragment {
    public MovieOverviewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.movie_overview, container, false);
//        return rootView;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
