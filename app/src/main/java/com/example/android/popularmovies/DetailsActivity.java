package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * Created by asser on 8/14/16.
 */

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movide_detail_layout);
        ImageView bg = (ImageView) this.findViewById(R.id.movie_detail_background);
        ImageView poster = (ImageView) this.findViewById(R.id.movie_detail_poster);
        TextView title = (TextView) this.findViewById((R.id.movie_detail_title));
        TextView overview = (TextView) this.findViewById((R.id.movie_detail_overview));
        TextView popularity = (TextView) this.findViewById((R.id.movie_detail_popularity));
        TextView voteAvrg = (TextView) this.findViewById((R.id.movie_detail_vote_average));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            MovieData movie = (MovieData)extras.get("movieDetails");
            title.setText(this.getString(R.string.movie_title_year,movie.title,movie.release_date.get(Calendar.YEAR)));
            Picasso.with(this)
                    .load(movie.poster_url)
                    .into(poster);
            Picasso.with(this)
                    .load(movie.poster_url)
                    .into(bg);
            overview.setText(movie.overview);
            popularity.setText(String.valueOf(this.getString(R.string.movie_popularity_approx,movie.popularity)));
            voteAvrg.setText(String.valueOf(movie.vote_average));
        }

    }
}
