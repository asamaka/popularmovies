package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.FavoriteMoviesContract;
import com.example.android.popularmovies.data.MovieData;
import com.example.android.popularmovies.data.ReviewData;
import com.example.android.popularmovies.data.TrailerData;
import com.example.android.popularmovies.sync.FetchReviewDataAsync;
import com.example.android.popularmovies.sync.FetchTrailerDataAsync;
import com.example.android.popularmovies.sync.MovieListAdapter;
import com.example.android.popularmovies.sync.ReviewListAdapter;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

public class DetailsFragment extends Fragment {
    private static final String LOG_TAG = "DetailsActivity";
    private MovieData movie;
    private MovieListAdapter.TrailerListAdapter tlAdapter;
    private ReviewListAdapter rlAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = inflater.inflate(R.layout.activity_detail_fragment, container, false);

        ImageView bg = (ImageView) layout.findViewById(R.id.movie_detail_background);
        ImageView poster = (ImageView) layout.findViewById(R.id.movie_detail_poster);
        TextView title = (TextView) layout.findViewById((R.id.movie_detail_title));
        TextView year = (TextView) layout.findViewById((R.id.movie_detail_year));
        TextView overview = (TextView) layout.findViewById((R.id.movie_detail_overview));
        TextView voteAvrg = (TextView) layout.findViewById((R.id.movie_detail_vote_average));
        TextView voteCnt = (TextView) layout.findViewById((R.id.movie_detail_vote_count));
        RecyclerView trailerListView = (RecyclerView) layout.findViewById(R.id.trailer_list);
        RecyclerView reviewListView = (RecyclerView) layout.findViewById(R.id.review_list);
        ImageButton addToFavorites = (ImageButton) layout.findViewById(R.id.add_to_fav_button);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            try {
                movie = (MovieData) extras.get("movieDetails");
                title.setText(movie.title);
                try {
                    year.setText(this.getString(R.string.movie_year, movie.release_date.get(Calendar.YEAR)));
                }catch (Exception ex){
                    Log.e(LOG_TAG, "Failed to parse year! "+ex.getMessage());
                }
                try {
                    Picasso.with(getActivity())
                            .load(movie.poster_url)
                            .into(poster);

                    Picasso.with(getActivity())
                            .load(movie.poster_url)
                            .into(bg);
                }catch (Exception ex){
                    Log.e(LOG_TAG, "Failed to load poster image! "+ex.getMessage());
                }
                overview.setText(movie.overview);
                voteAvrg.setText(this.getString(R.string.rating_approx, movie.vote_average));
                voteCnt.setText(this.getString(R.string.vote_count, movie.vote_count));
                refreshFavoriteIcon(addToFavorites);
                new FetchTrailerDataAsync().execute(this, movie);
                new FetchReviewDataAsync().execute(this, movie);

                trailerListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                tlAdapter = new MovieListAdapter.TrailerListAdapter(getActivity(), movie.trailers);
                trailerListView.setAdapter(tlAdapter);


                reviewListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                rlAdapter = new ReviewListAdapter(getActivity(), movie.reviews);
                reviewListView.setAdapter(rlAdapter);
                addToFavorites.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContentResolver cr = getActivity().getContentResolver();
                        Uri uri = FavoriteMoviesContract.MovieEntry.buildFavoriteMovieUri(movie.id);

                        //if already favorite then delete
                        if (refreshFavoriteIcon(view)) {
                            cr.delete(uri, null, null);
                            refreshFavoriteIcon(view);
                            return;
                        }

                        ContentValues cv = new ContentValues();
                        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_DB_ID, movie.id);
                        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE, movie.title);
                        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_URL, movie.poster_url);
                        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY, movie.popularity);
                        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_YEAR, movie.release_date.get(Calendar.YEAR));
                        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.vote_average);
                        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT, movie.vote_count);
                        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW, movie.overview);
                        cr.insert(FavoriteMoviesContract.MovieEntry.CONTENT_URI, cv);

                        refreshFavoriteIcon(view);
                    }
                });
            }catch (Exception ex){
                Log.e(LOG_TAG, "Failed to load movie details! "+ex.getMessage());
            }
        }
        return layout;
    }

    public void updateMovieTrailers(List<TrailerData> trailers) {
        movie.trailers.clear();
        movie.trailers.addAll(trailers);
        tlAdapter.notifyDataSetChanged();

    }

    public void updateMovieReviews(List<ReviewData> reviews) {
        movie.reviews.clear();
        movie.reviews.addAll(reviews);
        rlAdapter.notifyDataSetChanged();

    }

    private boolean refreshFavoriteIcon(View view) {
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = FavoriteMoviesContract.MovieEntry.buildFavoriteMovieUri(movie.id);
        Cursor existingMovies = cr.query(uri, null, null, null, null);
        if (existingMovies.getCount() > 0) {
            ((ImageButton) view).setBackgroundResource(android.R.drawable.btn_star_big_on);
            return true;
        } else {
            ((ImageButton) view).setBackgroundResource(android.R.drawable.btn_star_big_off);
            return false;
        }

    }

}
