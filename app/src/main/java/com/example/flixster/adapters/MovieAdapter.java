package com.example.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Activity context;
    List<Movie> movies;

    public MovieAdapter(Activity context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflate layout from XML and return holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        ItemMovieBinding binding = ItemMovieBinding.inflate(context.getLayoutInflater());
        View movieView = binding.getRoot();
        return new ViewHolder(movieView, binding);
    }

    // Involves populating data into the view through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    // Return the total count of movies in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemMovieBinding binding;

        public ViewHolder(@NonNull View itemView, ItemMovieBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            binding.tvTitle.setText(movie.getTitle());
            binding.tvOverview.setText(movie.getOverview());
            String imageURL;
            RequestOptions options;
            // If phone is in landscape, imageURL is backdrop image
            // If phone is in portrait, imageURL is poster image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
                options = new RequestOptions().placeholder(R.drawable.flicks_backdrop_placeholder);
            } else {
                imageURL = movie.getPosterPath();
                options = new RequestOptions().placeholder(R.drawable.flicks_movie_placeholder);
            }

            Glide.with(context).load(imageURL).transform(new RoundedCorners(70)).apply(options).into(binding.ivPoster);
        }

        // When the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View view) {
            // Gets item position
            int position = getAdapterPosition();
            // Make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // Get the movie at the position
                Movie movie = movies.get(position);
                // Create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // Serialize the movie using parceler and use the short name as key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // Show the activity
                context.startActivity(intent);
            }
        }
    }
}
