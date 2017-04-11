package com.example.kaka.moviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kaka.moviedb.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kaka on 3/14/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolderMovie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final MovieAdapterOnClickHandler mClickHandler;
    private List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public MyViewHolderMovie onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MyViewHolderMovie(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderMovie holder, int position) {
        Movie movie = movieList.get(position);

        if (movie.getPosterPath().equals("null") || movie.getPosterPath().equals("")) {
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

    public class MyViewHolderMovie extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageViewPoster;

        public MyViewHolderMovie(View itemView) {
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
