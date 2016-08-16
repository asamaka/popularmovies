package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MovieData> movies;
    private MovieListAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.all_movies_list);
        rv.setHasFixedSize(false);

        rv.setLayoutManager(new GridLayoutManager(this, 4));


        movies = new ArrayList<>();

        rvAdapter = new MovieListAdapter(this, movies);

        rv.setAdapter(rvAdapter);

        AsyncTask t = new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object[] objects) {
                try {

                    URL url = new URL(((Context)objects[0]).getString(R.string.movie_api_url, BuildConfig.MOVIE_DB_API_KEY));
                    Log.d("Requesting", url.toString());
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

                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray results = json.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject jsonObject = results.getJSONObject(i);
                        MovieData m = new MovieData();
                        if (!jsonObject.isNull("poster_path")) {
                            m.poster_url = "http://image.tmdb.org/t/p/w500/" +
                                    jsonObject.getString("poster_path");
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
                        if (!jsonObject.isNull("popularity")) {
                            m.popularity = jsonObject.getDouble("popularity");
                        }
                        if (!jsonObject.isNull("release_date")) {
                            m.release_date = Calendar.getInstance();
                            m.release_date.setTime(Date.valueOf(jsonObject.getString("release_date")));
                        }

                        movies.add(m);
                    }
                    rvAdapter.notifyDataSetChanged();

                } catch (Exception ex) {

                }


            }
        };

        t.execute(this);


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort_by_popular_menu_item:
                Collections.sort(movies, new Comparator<MovieData>() {
                    @Override
                    public int compare(MovieData movieData, MovieData t1) {
                        return (int) (t1.popularity - movieData.popularity);
                    }
                });

                rvAdapter.notifyDataSetChanged();
                return true;
            case R.id.sort_by_votes_menu_item:
                Collections.sort(movies, new Comparator<MovieData>() {
                    @Override
                    public int compare(MovieData movieData, MovieData t1) {
                        return (int) (t1.vote_average - movieData.vote_average);
                    }
                });

                rvAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
