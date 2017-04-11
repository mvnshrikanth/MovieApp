package com.example.kaka.moviedb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaka.moviedb.data.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kaka on 4/9/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolderTrailer> {

    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();
    private Context context;
    private List<MovieTrailer> movieTrailerList;

    public TrailerAdapter(Context context) {
        this.context = context;
    }

    public void prepareMovieTrailers(List<MovieTrailer> movieTrailerList) {
        this.movieTrailerList = movieTrailerList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolderTrailer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_trailer_item, parent, false);
        return new MyViewHolderTrailer(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderTrailer holder, int position) {
        final MovieTrailer movieTrailer = movieTrailerList.get(position);
        if (movieTrailer.getTrailer_name().equals("null") || movieTrailer.getTrailer_name().equals("")) {
            holder.textViewTrailerName.setText("N/A");
        } else {
            holder.textViewTrailerName.setText(movieTrailer.getTrailer_name());
        }

        if (movieTrailer.getTrailer_key().equals("null") || movieTrailer.getTrailer_key().equals("")) {
            holder.imageViewTrailerPoster.setImageResource(R.drawable.not_found);
        } else {
            Picasso.with(context)
                    .load("http://img.youtube.com/vi/" + movieTrailer.getTrailer_key() + "/hqdefault.jpg")
                    .into(holder.imageViewTrailerPoster);
        }
        holder.imageViewTrailerPoster.setBackgroundColor(Color.TRANSPARENT);

        holder.imageViewTrailerPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTube(movieTrailer.getTrailer_key());
            }
        });

        holder.textViewTrailerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTube(movieTrailer.getTrailer_key());
            }
        });

    }

    public void openYouTube(String VideoKey) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + VideoKey));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (null == movieTrailerList) return 0;
        return movieTrailerList.size();
    }

    public class MyViewHolderTrailer extends RecyclerView.ViewHolder {
        public final ImageView imageViewTrailerPoster;
        public final TextView textViewTrailerName;

        public MyViewHolderTrailer(View itemView) {
            super(itemView);

            textViewTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            imageViewTrailerPoster = (ImageView) itemView.findViewById(R.id.iv_trailer_poster);
        }
    }
}
