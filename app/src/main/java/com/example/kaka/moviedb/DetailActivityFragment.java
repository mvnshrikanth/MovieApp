package com.example.kaka.moviedb;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieReview;
import com.example.kaka.moviedb.data.MovieTrailer;
import com.example.kaka.moviedb.utilities.MovieJsonUtils;
import com.example.kaka.moviedb.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.kaka.moviedb.MainActivityFragment.MOVIE_DATA;

/**
 * Created by Kaka on 4/18/2017.
 */

public class DetailActivityFragment extends Fragment {

    public static final String MOVIE_TRAILERS = "videos";
    public static final String MOVIE_REVIEWS = "reviews";
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public View view;
    private List<MovieTrailer> movieTrailersList;
    private List<MovieReview> movieReviewsList;
    private Boolean loadTrailerFlag = false;
    private Boolean loadReviewFlag = false;
    private ProgressDialog progressDialog;
    private String overview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_fragment, container, false);

        final Movie movie = this.getArguments().getParcelable(MOVIE_DATA);
        movieReviewsList = new ArrayList<>();
        movieTrailersList = new ArrayList<>();
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading Movies Details...");
        progressDialog.show();


        ImageView imageViewPoster = (ImageView) view.findViewById(R.id.iv_movie_poster);
        TextView textViewRlsDt = (TextView) view.findViewById(R.id.tv_release_date);
        TextView textViewRating = (TextView) view.findViewById(R.id.tv_rating);
        TextView textViewGenre = (TextView) view.findViewById(R.id.tv_genre);
        TextView textViewTitle = (TextView) view.findViewById(R.id.tv_movie_title);

        try {

            if (movie.getOriginalTitle().equals("null") || movie.getOriginalTitle().equals("")) {
                textViewTitle.setText("NA");
            } else {
                textViewTitle.setText(movie.getOriginalTitle());
            }


            if (movie.getPosterPath().equals("null") || movie.getPosterPath().equals("")) {
                imageViewPoster.setImageResource(R.drawable.not_found);
            } else {
                Picasso.with(view.getContext())
                        .load("http://image.tmdb.org/t/p/w500/" + movie.getPosterPath())
                        .into(imageViewPoster);
            }
            if (movie.getVoteAverage().equals("null") || movie.getVoteAverage().equals("")) {
                textViewRating.setText("NA");
            } else {
                textViewRating.setText(movie.getVoteAverage());
            }
            if (movie.getReleaseDate().equals("null") || movie.getReleaseDate().equals("")) {
                textViewRlsDt.setText("NA");
            } else {
                textViewRlsDt.setText(movie.getReleaseDate());
            }

            if (movie.getGenre().equals("null") || movie.getGenre().equals("")) {
                textViewGenre.setText("NA");
            } else {
                textViewGenre.setText(movie.getGenre());
            }

        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Exception occurred", e);
        }

        loadMovieTrailersAndReviews(movie.getMovieId());
        overview = movie.getOverview();
        return view;
    }

    public void loadViewPager() {
        if (loadTrailerFlag && loadReviewFlag) {
            ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(),
                    getChildFragmentManager(),
                    overview,
                    movieReviewsList,
                    movieTrailersList);
            viewPager.setAdapter(categoryAdapter);

            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
    }


    private void loadMovieTrailersAndReviews(String movieId) {
        MovieReviewAsyncTask movieReviewAsyncTask = new MovieReviewAsyncTask();
        movieReviewAsyncTask.execute(movieId);
        MovieTrailersAsyncTask movieTrailerAsyncTask = new MovieTrailersAsyncTask();
        movieTrailerAsyncTask.execute(movieId);
    }

    public class MovieReviewAsyncTask extends AsyncTask<String, Void, List<MovieReview>> {
        private final String LOG_TAG = MovieReviewAsyncTask.class.getSimpleName();
        List<MovieReview> movieReviews;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MovieReview> doInBackground(String... params) {
            try {
                URL movieReviewUrl = NetworkUtils.buildURL(getActivity(), MOVIE_REVIEWS, params[0]);
                String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewUrl);
                movieReviews = MovieJsonUtils.getMovieReviewListFromJson(jsonReviewResponse);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error,e");
            }

            return movieReviews;
        }

        @Override
        protected void onPostExecute(List<MovieReview> movieReviews) {
            progressDialog.setProgress(100);
            progressDialog.dismiss();
            movieReviewsList = movieReviews;
            loadReviewFlag = true;
            loadViewPager();
        }
    }

    public class MovieTrailersAsyncTask extends AsyncTask<String, Void, List<MovieTrailer>> {
        private final String LOG_TAG = MovieTrailersAsyncTask.class.getSimpleName();
        List<MovieTrailer> movieTrailers;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MovieTrailer> doInBackground(String... params) {
            try {
                URL movieTrailersUrl = NetworkUtils.buildURL(getActivity(), MOVIE_TRAILERS, params[0]);
                String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(movieTrailersUrl);
                movieTrailers = MovieJsonUtils.getMovieTrailersListFromJson(jsonTrailersResponse);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error,e");
            }

            return movieTrailers;
        }

        @Override
        protected void onPostExecute(List<MovieTrailer> movieTrailers) {
            progressDialog.setProgress(100);
            progressDialog.dismiss();
            movieTrailersList = movieTrailers;
            loadTrailerFlag = true;
            loadViewPager();
        }
    }

}
