package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {

    private Context context;
    private List<TrailerData> trailers;

    TrailerListAdapter(Context context, List<TrailerData> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the recyclerview item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_list_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int trailerId = (Integer) view.getTag();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.youtube_video_url,
                                trailers.get(trailerId).key)));
                context.startActivity(intent);
            }
        });
        return new TrailerViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TrailerData m = trailers.get(position);
        holder.itemView.setTag(position);

        Picasso.with(context)
                .load(context.getString(R.string.youtube_thumbnail_url, m.key))
                .into(holder.thumbnail);

        holder.name.setText(m.name);
    }


    /**
     * Inner class to hold the views needed to display a single item in the recyvlerview
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder {

        // Will display the poster
        ImageView thumbnail;
        TextView name;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.trailer_thumbnail_image);
            name = (TextView) itemView.findViewById(R.id.trailer_name);
        }

    }
}
