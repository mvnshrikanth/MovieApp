package com.example.kaka.moviedb.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.kaka.moviedb.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kaka on 3/14/2017.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIE_TOP_RATED_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?";
    private static final String MOVIE_POPULAR_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
    private static final String SORT_PARAM = "sort_by";
    private static final String API_KEY_PARAM = "api_key";

    public static URL buildURL(Context context, String sort_by) {
        URL url = null;
        Uri builtUri;
        if (sort_by.equals("popularity")) {
            builtUri = Uri.parse(MOVIE_POPULAR_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sort_by)
                    .appendQueryParameter(API_KEY_PARAM, context.getResources().getString(R.string.api_key))
                    .build();
        } else {
            builtUri = Uri.parse(MOVIE_TOP_RATED_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sort_by)
                    .appendQueryParameter(API_KEY_PARAM, context.getResources().getString(R.string.api_key))
                    .build();
        }
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Exception occurred", e);
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();

        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0) {
                return null;
            }

            return stringBuffer.toString();

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

}
