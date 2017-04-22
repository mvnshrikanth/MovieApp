package com.example.kaka.moviedb;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaka.moviedb.data.Movie;
import com.example.kaka.moviedb.data.MovieContract.MovieEntry;
import com.example.kaka.moviedb.data.MovieReview;
import com.example.kaka.moviedb.data.MovieTrailer;
import com.example.kaka.moviedb.utilities.MovieJsonUtils;
import com.example.kaka.moviedb.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.kaka.moviedb.MainActivity.MY_PREFERENCE;
import static com.example.kaka.moviedb.MainActivity.MY_SORT_PREFERENCE_KEY;
import static com.example.kaka.moviedb.MainActivity.SORT_TYPE_FAVORITE;
import static com.example.kaka.moviedb.MainActivityFragment.MOVIE_DATA;

/**
 * Created by Kaka on 4/18/2017.
 */

public class DetailActivityFragment extends Fragment {

    public static final String MOVIE_TRAILERS = "videos";
    public static final String MOVIE_REVIEWS = "reviews";
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public View view;
    Boolean mFlag = false;
    SharedPreferences sharedPreferences;
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

        sharedPreferences = view.getContext().getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                mFlag = true;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                mFlag = false;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                mFlag = false;
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                mFlag = true;
                break;
            default:
                mFlag = false;
        }

        final Movie movie = this.getArguments().getParcelable(MOVIE_DATA);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_tab_layout);

        if (mFlag) {
            Boolean favInd = false;
            final Boolean[] finalFavInd = new Boolean[1];
            Cursor cursor;

            String[] projection = {
                    MovieEntry.COLUMN_MOVIE_ID,
                    MovieEntry.COLUMN_ORIGINAL_TITLE,
                    MovieEntry.COLUMN_POSTER_PATH,
                    MovieEntry.COLUMN_RELEASE_DATE,
                    MovieEntry.COLUMN_OVERVIEW,
                    MovieEntry.COLUMN_VOTE_AVERAGE,
                    MovieEntry.COLUMN_BACKDROP_PATH,
                    MovieEntry.COLUMN_GENRE
            };

            Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, Long.parseLong(movie.getMovieId()));
            cursor = view.getContext().getContentResolver().query(uri, projection, null, null, null);

            if (cursor.getCount() > 0) {
                favInd = true;
                floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                favInd = false;
                floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
            finalFavInd[0] = favInd;

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!finalFavInd[0]) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
                        contentValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                        contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                        contentValues.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                        contentValues.put(MovieEntry.COLUMN_GENRE, movie.getGenre());
                        Uri uri = view.getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);

                        Snackbar.make(view, "Saved " + movie.getOriginalTitle() + " as favorite ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        finalFavInd[0] = false;
                    } else {
                        Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, Long.parseLong(movie.getMovieId()));
                        int rowsDeleted = view.getContext().getContentResolver().delete(uri, null, null);


                        Snackbar.make(view, "Deleted " + movie.getOriginalTitle() + " from favorite ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        finalFavInd[0] = true;
                    }

                    if (sharedPreferences.getString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_FAVORITE).equals(SORT_TYPE_FAVORITE)) {

                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_main_container, new MainActivityFragment())
                                .commit();

                        getFragmentManager().beginTransaction()
                                .remove(getFragmentManager().findFragmentById(R.id.fragment_detail_container))
                                .commit();
                    }
                }
            });
        } else

        {
            floatingActionButton.setVisibility(View.GONE);
        }

        movieReviewsList = new ArrayList<>();
        movieTrailersList = new ArrayList<>();

        progressDialog = new

                ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading Movies Details...");
        progressDialog.show();


        ImageView imageViewPoster = (ImageView) view.findViewById(R.id.iv_movie_poster);
        TextView textViewRlsDt = (TextView) view.findViewById(R.id.tv_release_date);
        TextView textViewRating = (TextView) view.findViewById(R.id.tv_rating);
        TextView textViewGenre = (TextView) view.findViewById(R.id.tv_genre);
        TextView textViewTitle = (TextView) view.findViewById(R.id.tv_movie_title);

        try

        {

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

                String date = movie.getReleaseDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                Date testDate = null;
                try {
                    testDate = sdf.parse(date);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yy");
                String newFormat = formatter.format(testDate);

                textViewRlsDt.setText(newFormat);
            }

            if (movie.getGenre().equals("null") || movie.getGenre().equals("")) {
                textViewGenre.setText("NA");
            } else {
                textViewGenre.setText(movie.getGenre());
            }

        } catch (
                NullPointerException e)

        {
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
