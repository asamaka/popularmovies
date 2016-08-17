package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FetchMovieDataAsync extends AsyncTask<Object, Void, String> {
    private List<MovieData> movies;
    private Context context;

    @Override
    protected String doInBackground(Object[] objects) {
        try {
            context = ((Context) objects[0]);
            URL url;
            if (objects[1] == MainActivity.SortBy.POPULARITY) {
                url = new URL(context.getString(R.string.movie_api_popular_url, BuildConfig.MOVIE_DB_API_KEY));
            } else if (objects[1] == MainActivity.SortBy.RATING) {
                url = new URL(context.getString(R.string.movie_api_top_rated_url, BuildConfig.MOVIE_DB_API_KEY));
            } else {
                Log.e("Failed to request data", "Unknown sort type");
                return null;
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream is = connection.getInputStream();
            StringBuffer sb = new StringBuffer();
            if (is == null) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();

        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        movies = new ArrayList<MovieData>();
        try {
            JSONObject json = new JSONObject(result);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonObject = results.getJSONObject(i);
                MovieData m = new MovieData();
                if (!jsonObject.isNull("poster_path")) {
                    m.poster_url = context.getString(R.string.poster_image_url, jsonObject.getString("poster_path"));
                }
                if (!jsonObject.isNull("id")) {
                    m.id = jsonObject.getLong("id");
                }
                if (!jsonObject.isNull("title")) {
                    m.title = jsonObject.getString("title");
                }
                if (!jsonObject.isNull("overview")) {
                    m.overview = jsonObject.getString("overview");
                }
                if (!jsonObject.isNull("vote_average")) {
                    m.vote_average = jsonObject.getDouble("vote_average");
                }
                if (!jsonObject.isNull("vote_count")) {
                    m.vote_count = jsonObject.getLong("vote_count");
                }
                if (!jsonObject.isNull("popularity")) {
                    m.popularity = jsonObject.getDouble("popularity");
                }
                if (!jsonObject.isNull("release_date")) {
                    m.release_date = Calendar.getInstance();
                    m.release_date.setTime(Date.valueOf(jsonObject.getString("release_date")));
                }
                movies.add(m);
            }
            ((MainActivity) context).updateMovies(movies);

        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }


    }
}
