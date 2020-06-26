package com.example.flixster.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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

// Adapter class for the recycle view of movie items
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

    // Binds data into respective views through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    // Returns the total count of movies in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // Class that contains all views in each item of the recycler view
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemMovieBinding binding;

        public ViewHolder(@NonNull View itemView, ItemMovieBinding firstBind) {
            super(itemView);
            firstBind.getRoot();
            binding = firstBind;
            itemView.setOnClickListener(this);
        }

        // Binds data to respective view to appear on the activity
        public void bind(Movie movie) {
            binding.tvTitle.setText(movie.getTitle());
            binding.tvOverview.setText(movie.getOverview());
            String imageURL;
            RequestOptions options;
            // Assigns URL of image based on the orientation of the phone
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
                options = new RequestOptions().placeholder(R.drawable.flicks_backdrop_placeholder);
            } else {
                imageURL = movie.getPosterPath();
                options = new RequestOptions().placeholder(R.drawable.flicks_movie_placeholder);
            }

            Glide.with(context).load(imageURL).transform(new RoundedCorners(70)).apply(options).into(binding.ivPoster);
        }

        // Calls the MovieDetailsActivity when a row is clicked
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);
            }
        }
    }
}
