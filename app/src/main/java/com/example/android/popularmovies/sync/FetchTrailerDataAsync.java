package com.example.android.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmovies.DetailsActivity;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.TrailerData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FetchTrailerDataAsync extends AsyncTask<Object, Void, String> {
    private List<TrailerData> trailers;
    private Context context;
    private MovieData movie;

    @Override
    protected String doInBackground(Object[] objects) {
        try {
            context = ((Context) objects[0]);
            movie = ((MovieData) objects[1]);
            URL url = new URL(context.getString(R.string.movie_api_videos_url, movie.id, BuildConfig.MOVIE_DB_API_KEY));
            ;

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
        if(result== null || result == ""){
            Toast.makeText(context,R.string.offline_message,Toast.LENGTH_LONG).show();
            return;
        }
        try {
            trailers = new ArrayList<TrailerData>();
            JSONObject json = new JSONObject(result);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonObject = results.getJSONObject(i);
                TrailerData t = new TrailerData();
                if (!jsonObject.isNull("key")) {
                    t.key = jsonObject.getString("key");
                }
                if (!jsonObject.isNull("name")) {
                    t.name = jsonObject.getString("name");
                }
                trailers.add(t);
            }
            ((DetailsActivity) context).updateMovieTrailers(trailers);
        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
    }
}
