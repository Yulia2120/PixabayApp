package com.obushko.pixabayapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class PixabayAdapter extends RecyclerView.Adapter<PixabayAdapter.PixabayViewHolder> {

    private final Context context;
    private final ArrayList<Hit> hits;

    public PixabayAdapter(Context context, ArrayList<Hit> hits){
        this.context = context;
        this.hits = hits;
    }

    @NonNull
    @Override
    public PixabayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_item,
                parent, false);
        return new PixabayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PixabayViewHolder holder, int position) {
        Hit currentHit = hits.get(position);

        String user = currentHit.getUser();
        String previewURL = currentHit.getPreviewURL();
        String tags = currentHit.getTags();
        int likes = currentHit.getLikes();

        holder.tagsTextView.setText(tags);
        Picasso.get().load(previewURL).fit().centerInside()
                .into(holder.photoImageView);
        holder.likesTextView.setText(likes + " likes");
        holder.userTextView.setText(user);

    }

    @Override
    public int getItemCount() {
        return hits.size();
    }

    public static class PixabayViewHolder extends RecyclerView.ViewHolder{

        ImageView photoImageView;
        TextView userTextView;
        TextView tagsTextView;
        TextView likesTextView;

        public PixabayViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            userTextView = itemView.findViewById(R.id.userTextView);
            tagsTextView = itemView.findViewById(R.id.tagsTextView);
            likesTextView = itemView.findViewById(R.id.likesTextView);
        }
    }
}
