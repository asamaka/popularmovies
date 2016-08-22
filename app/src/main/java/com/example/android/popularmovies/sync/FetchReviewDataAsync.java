package com.example.android.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmovies.DetailsActivity;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.DetailsFragment;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.ReviewData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FetchReviewDataAsync extends AsyncTask<Object, Void, String> {
    private List<ReviewData> reviews;
    private DetailsFragment context;
    private MovieData movie;

    @Override
    protected String doInBackground(Object[] objects) {
        try {
            context = ((DetailsFragment) objects[0]);
            movie = ((MovieData) objects[1]);
            URL url = new URL(context.getString(R.string.movie_api_reviews_url, movie.id, BuildConfig.MOVIE_DB_API_KEY));
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
            Toast.makeText(context.getActivity(),R.string.offline_message,Toast.LENGTH_LONG).show();;
            return;
        }
        reviews = new ArrayList<ReviewData>();
        try {
            JSONObject json = new JSONObject(result);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonObject = results.getJSONObject(i);
                ReviewData r = new ReviewData();
                if (!jsonObject.isNull("author")) {
                    r.author = jsonObject.getString("author");
                }
                if (!jsonObject.isNull("content")) {
                    r.content = jsonObject.getString("content");
                }
                reviews.add(r);
            }
            ((DetailsFragment) context).updateMovieReviews(reviews);
        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }


    }
}
