package com.example.kaka.moviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kaka on 3/14/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.textViewTitle.setText(movie.getOriginalTitle());
        Log.v(LOG_TAG, String.valueOf(position));
        if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w342/" + movie.getPosterPath())
                    .into(holder.imageViewPoster);
        } else {
            holder.imageViewPoster.setImageResource(R.drawable.not_found);
        }
    }

    @Override
    public int getItemCount() {
        if (null == movieList) return 0;
        return movieList.size();
    }

    public void prepareMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView textViewTitle;
        public final ImageView imageViewPoster;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            imageViewPoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
