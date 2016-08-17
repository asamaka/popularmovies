package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {
    private MovieData movie;
    private TrailerListAdapter rvAdapter;

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
        RecyclerView trailerListView = (RecyclerView) this.findViewById(R.id.trailer_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie = (MovieData) extras.get("movieDetails");
            title.setText(this.getString(R.string.movie_title_year, movie.title, movie.release_date.get(Calendar.YEAR)));
            Picasso.with(this)
                    .load(movie.poster_url)
                    .into(poster);
            Picasso.with(this)
                    .load(movie.poster_url)
                    .into(bg);
            overview.setText(movie.overview);
            popularity.setText(String.valueOf(this.getString(R.string.movie_popularity_approx, movie.popularity)));
            voteAvrg.setText(String.valueOf(movie.vote_average));
            new FetchTrailerDataAsync().execute(this, movie);

            trailerListView.setLayoutManager(new LinearLayoutManager(this));
            rvAdapter = new TrailerListAdapter(this, movie.trailers);
            trailerListView.setAdapter(rvAdapter);
        }


    }

    public void updateMovieTrailers(List<TrailerData> t) {
        movie.trailers.clear();
        movie.trailers.addAll(t);
        rvAdapter.notifyDataSetChanged();
    }
}
