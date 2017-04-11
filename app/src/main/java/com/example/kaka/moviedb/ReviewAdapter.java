package com.example.kaka.moviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kaka.moviedb.data.MovieReview;

import java.util.List;

/**
 * Created by Kaka on 4/9/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolderReview> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private List<MovieReview> movieReviewList;
    private Context context;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolderReview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_review_item, parent, false);
        return new MyViewHolderReview(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderReview holder, int position) {
        MovieReview movieReview = movieReviewList.get(position);

        if (movieReview.getAuthor().equals("null") || movieReview.getAuthor().equals("")) {
            holder.textViewAuthor.setText("N/A");
        } else {
            holder.textViewAuthor.setText(movieReview.getAuthor());
        }

        if (movieReview.getReview().equals("null") || movieReview.getReview().equals("")) {
            holder.textViewReview.setText("N/A");
        } else {
            holder.textViewReview.setText(movieReview.getReview());
        }
    }

    @Override
    public int getItemCount() {
        if (null == movieReviewList) return 0;
        return movieReviewList.size();
    }

    public void prepareMovieReviews(List<MovieReview> movieReviewList) {
        this.movieReviewList = movieReviewList;
        notifyDataSetChanged();
    }

    public class MyViewHolderReview extends RecyclerView.ViewHolder {

        public final TextView textViewAuthor;
        public final TextView textViewReview;

        public MyViewHolderReview(View itemView) {
            super(itemView);
            textViewAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
            textViewReview = (TextView) itemView.findViewById(R.id.tv_review);
        }
    }

}
