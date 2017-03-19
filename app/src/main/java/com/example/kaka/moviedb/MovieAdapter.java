package com.example.kaka.moviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kaka on 3/14/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final MovieAdapterOnClickHandler mClickHandler;
    private List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
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
        Log.v(LOG_TAG, String.valueOf(position));
        if (movie.getPosterPath().equals("null") || movie.getPosterPath().equals(null) || movie.getPosterPath().equals("")) {
            holder.imageViewPoster.setImageResource(R.drawable.not_found);
        } else {

            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w500/" + movie.getPosterPath())
                    .into(holder.imageViewPoster);
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

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageViewPoster;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageViewPoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Movie movie = movieList.get(getAdapterPosition());
            mClickHandler.onClick(movie);
        }
    }

}
