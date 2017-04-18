package com.example.kaka.moviedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String MY_PREFERENCE = "MyPrefs";
    public static final String MY_SORT_PREFERENCE_KEY = "sort_pref_key";
    public static final String SORT_TYPE_POPULARITY = "popularity";
    public static final String SORT_TYPE_RATING = "rating";
    public static final String SORT_TYPE_FAVORITE = "favorite";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_POPULARITY);
        editor.apply();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        loadMainFragment();
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
                return true;
            case R.id.action_sort_by_rating:
                editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_RATING);
                editor.apply();
                return true;
            case R.id.action_sort_by_favorite:
                editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_FAVORITE);
                editor.apply();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void loadMainFragment() {

        if (!networkAvailable()) {
            sharedPreferences = getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_FAVORITE);
            editor.apply();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No network connection available to fetch movie data, only favorites can be seen if you have any.")
                    .setTitle("Network Connection Unavailable")
                    .create()
                    .show();
        }

        MainActivityFragment mainActivityFragment = new MainActivityFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_main_container, mainActivityFragment)
                .commit();

    }

    public boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(MY_SORT_PREFERENCE_KEY)) {
            if (!networkAvailable()) {
                sharedPreferences = getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MY_SORT_PREFERENCE_KEY, SORT_TYPE_FAVORITE);
                editor.apply();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("No network connection available to fetch movie data, only favorites can be seen if you have any.")
                        .setTitle("Network Connection Unavailable")
                        .create()
                        .show();
            }
            MainActivityFragment mainActivityFragment = new MainActivityFragment();

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main_container, mainActivityFragment)
                    .commit();

        }
    }
}


