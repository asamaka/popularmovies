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
        setContentView(R.layout.activity_detail);
        ImageView bg = (ImageView) this.findViewById(R.id.movie_detail_background);
        ImageView poster = (ImageView) this.findViewById(R.id.movie_detail_poster);
        TextView title = (TextView) this.findViewById((R.id.movie_detail_title));
        TextView year = (TextView) this.findViewById((R.id.movie_detail_year));
        TextView overview = (TextView) this.findViewById((R.id.movie_detail_overview));
        TextView voteAvrg = (TextView) this.findViewById((R.id.movie_detail_vote_average));
        TextView voteCnt = (TextView) this.findViewById((R.id.movie_detail_vote_count));
        RecyclerView trailerListView = (RecyclerView) this.findViewById(R.id.trailer_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie = (MovieData) extras.get("movieDetails");
            title.setText(movie.title);
            year.setText(this.getString(R.string.movie_year, movie.release_date.get(Calendar.YEAR)));
            Picasso.with(this)
                    .load(movie.poster_url)
                    .into(poster);
            Picasso.with(this)
                    .load(movie.poster_url)
                    .into(bg);
            overview.setText(movie.overview);

            voteAvrg.setText(this.getString(R.string.rating_approx,movie.vote_average));
            voteCnt.setText(this.getString(R.string.vote_count,movie.vote_count));
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
