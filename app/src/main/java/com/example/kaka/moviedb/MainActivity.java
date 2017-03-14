package com.example.kaka.moviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieAdapter movieAdapter;
        List<Movie> movieList;
        movieList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(MainActivity.this);
        recyclerView.setAdapter(movieAdapter);
        movieList.add(new Movie("1", "Diwali"));
        movieList.add(new Movie("2", "Siwali"));
        movieList.add(new Movie("3", "Piwali"));
        movieList.add(new Movie("4", "Miwali"));
        movieAdapter.prepareMovieList(movieList);
    }

}
