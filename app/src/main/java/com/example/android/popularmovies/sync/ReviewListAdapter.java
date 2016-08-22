package com.example.android.popularmovies.sync;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.ReviewData;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {

    private Context context;
    private List<ReviewData> reviews;

    public ReviewListAdapter(Context context, List<ReviewData> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the recyclerview item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewData r = reviews.get(position);
        holder.itemView.setTag(position);
        holder.author.setText(r.author);
        holder.content.setText(r.content);
    }


    /**
     * Inner class to hold the views needed to display a single item in the recyvlerview
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {

        // Will display the user avatar
        ImageView thumbnail;
        TextView author;
        TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            //thumbnail = (ImageView) itemView.findViewById(R.id.review_thumbnail_image);
            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
        }

    }
}
