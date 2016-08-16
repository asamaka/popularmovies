package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private Context context;
    private List<MovieData> movies;

    // Constructor
    public MovieListAdapter(Context context, List<MovieData> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the recyclerview item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView clickedImage = (ImageView) view;
                int id = (Integer) clickedImage.getTag();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("movieDetails", movies.get(id));
                context.startActivity(intent);
            }
        });
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieData m = movies.get(position);
        holder.itemView.setTag(position);
        Picasso.with(context)
                .load(m.poster_url)
                .into(holder.poster);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }


    /**
     * Inner class to hold the views needed to display a single item in the recyvlerview
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {

        // Will display the poster
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster_image);
        }

    }
}
