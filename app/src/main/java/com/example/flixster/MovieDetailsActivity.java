package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    TextView tvDate;
    ImageView ivBackdrop;
    RatingBar rbVoteAverage;
    Integer movieId;

    String movieKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Resolve the view objects
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        tvDate = findViewById(R.id.tvDate);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        ivBackdrop = findViewById(R.id.ivBackdrop);

        // Unwrap the movie passed in via itnent, using its simple name as key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s", movie.getTitle()));

        // Set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvDate.setText(movie.getReleaseDate());

        // Vote average is 0..10, covert to 0..5
        float voteAverage = movie.getVoteAverage().floatValue();
        if (voteAverage > 0) {
           voteAverage /= 2.0f;
        }
        rbVoteAverage.setRating(voteAverage);

        movieId = movie.getId();

        String VIDEO_URL = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(VIDEO_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    movieKey = results.getJSONObject(0).getString("key");
                } catch (JSONException e) {
                    Log.i("MovieDetailsActivity", "Failed to get video ID");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i("MovieDetailsActivity", "Failed to get data for video");
            }
        });

        RequestOptions options = new RequestOptions().placeholder(R.drawable.flicks_backdrop_placeholder);
        String imageURL = movie.getBackdropPath();
        Glide.with(this).load(imageURL).transform(new RoundedCorners(70)).apply(options).into(ivBackdrop);

        ivBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent i = new Intent(context, MovieTrailerActivity.class);
                i.putExtra("videoID", movieKey);
                context.startActivity(i);
            }
        });

    }
}