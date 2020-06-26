package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

// Class that represents the activity when the application is launched
public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String CONFIGURATION_URL = "https://api.themoviedb.org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;
    List<String> widths_poster = new ArrayList<>();
    List<String> widths_backdrop = new ArrayList<>();
    String finalPosterWidth;
    String finalBackdropWidth;
    static String[] sizes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        movies = new ArrayList<>();
        // Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Retrieve the size of poster and backdrop to be used
        sizes = getImageWidth();

        // Set the adapter on recycler view
        binding.rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Access API to get information about each movie and notify adapter
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results" + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    // Access API to get the poster and backdrop sizes and choose one in the appropriate range
    private String[] getImageWidth() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(CONFIGURATION_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject images = jsonObject.getJSONObject("images");
                    JSONArray widthPosterFromJSON = images.getJSONArray("poster_sizes");
                    widths_poster = extractJsonArray(widthPosterFromJSON);
                    if (!widths_poster.isEmpty()) {
                        for (String s : widths_poster) {
                            finalPosterWidth = s.substring(1);
                            int intSize = Integer.parseInt(finalPosterWidth);
                            if (200 < intSize & intSize < 400) {
                                break;
                            }
                        }
                    }
                    JSONArray widthBackdropFromJSON = images.getJSONArray("backdrop_sizes");
                    widths_backdrop = extractJsonArray(widthBackdropFromJSON);
                    if (!widths_backdrop.isEmpty()) {
                        for (String s : widths_backdrop) {
                            finalBackdropWidth = s.substring(1);
                            int intSize = Integer.parseInt(finalBackdropWidth);
                            if (200 < intSize & intSize < 400) {
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.i("MovieDetailsActivity", "Failed to get video ID");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i("MovieDetailsActivity", "Failed to get data for video");
            }
        });

        return new String[]{finalPosterWidth, finalBackdropWidth};
    }

    // Returns a list of items in a JSONArray
    private List<String> extractJsonArray(JSONArray sizeJsonArray) throws JSONException {
        List<String> sizes = new ArrayList<>();
        for (int i = 0; i < sizeJsonArray.length(); i++) {
            sizes.add(sizeJsonArray.get(i).toString());
        }
        return sizes;
    }

    // Getter method for the sizes array
    public static String[] getSizes() {
        return sizes;
    }
}