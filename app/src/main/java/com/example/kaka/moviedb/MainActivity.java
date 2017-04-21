package com.example.kaka.moviedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFERENCE = "MyPrefs";
    public static final String MY_SORT_PREFERENCE_KEY = "sort_pref_key";
    public static final String SORT_TYPE_POPULARITY = "popularity";
    public static final String SORT_TYPE_RATING = "rating";
    public static final String SORT_TYPE_FAVORITE = "favorite";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    Boolean mFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (!mFlag) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        sharedPreferences = getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!networkAvailable()) {
            editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_FAVORITE);
            editor.apply();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No network connection available to fetch movie data, only favorites can be seen if you have any.")
                    .setTitle("Network Connection Unavailable")
                    .create()
                    .show();
        }

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_main_container, new MainActivityFragment(), "MainFragment")
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        sharedPreferences = getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (item.getItemId()) {
            case R.id.action_sort_by_popularity:
                editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_POPULARITY);
                editor.apply();
                break;
            case R.id.action_sort_by_rating:
                editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_RATING);
                editor.apply();
                break;
            case R.id.action_sort_by_favorite:
                editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_FAVORITE);
                editor.apply();
                break;
        }

        if (!networkAvailable()) {
            editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_FAVORITE);
            editor.apply();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No network connection available to fetch movie data, only favorites can be seen if you have any.")
                    .setTitle("Network Connection Unavailable")
                    .create()
                    .show();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_container, new MainActivityFragment(), "MainFragment")
                .commit();

        return super.onOptionsItemSelected(item);
    }

    public boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}


