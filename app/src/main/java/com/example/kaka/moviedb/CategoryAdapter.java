package com.example.kaka.moviedb;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kaka.moviedb.data.MovieReview;
import com.example.kaka.moviedb.data.MovieTrailer;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kaka on 4/6/2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<MovieTrailer> movieTrailerList;
    private List<MovieReview> movieReviewList;
    private String overview;

    public CategoryAdapter(Context context, android.support.v4.app.FragmentManager fm, String overview,
                           List<MovieReview> movieReviewList, List<MovieTrailer> movieTrailerList) {
        super(fm);
        mContext = context;
        this.movieReviewList = movieReviewList;
        this.movieTrailerList = movieTrailerList;
        this.overview = overview;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        if (position == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(MovieOverviewsFragment.MOVIE_OVERVIEW_KEY, overview);
            MovieOverviewsFragment movieOverviewsFragment = new MovieOverviewsFragment();
            movieOverviewsFragment.setArguments(bundle);
            return movieOverviewsFragment;
        } else if (position == 1) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MovieTrailersFragment.MOVIE_TRAILERS_KEY, (Serializable) movieTrailerList);
            MovieTrailersFragment movieTrailersFragment = new MovieTrailersFragment();
            movieTrailersFragment.setArguments(bundle);
            return movieTrailersFragment;
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MovieReviewsFragment.MOVIE_REVIEW_KEY, (Serializable) movieReviewList);
            MovieReviewsFragment movieReviewsFragment = new MovieReviewsFragment();
            movieReviewsFragment.setArguments(bundle);
            return movieReviewsFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.str_category_overview);
        } else if (position == 1) {
            return mContext.getString(R.string.str_category_trailers);
        } else {
            return mContext.getString(R.string.str_category_reviews);
        }
    }

}
