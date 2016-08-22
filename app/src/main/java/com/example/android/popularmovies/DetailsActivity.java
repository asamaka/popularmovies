package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.FavoriteMoviesContract;
import com.example.android.popularmovies.data.FavoriteMoviesProvider;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.data.ReviewData;
import com.example.android.popularmovies.data.TrailerData;
import com.example.android.popularmovies.sync.FetchReviewDataAsync;
import com.example.android.popularmovies.sync.FetchTrailerDataAsync;
import com.example.android.popularmovies.sync.MovieListAdapter;
import com.example.android.popularmovies.sync.ReviewListAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = "DetailsActivity";
    private MovieData movie;
    private MovieListAdapter.TrailerListAdapter tlAdapter;
    private ReviewListAdapter rlAdapter;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailsFragment())
                    .commit();
        }
    }


}
