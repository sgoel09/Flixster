package com.example.flixster.models;

import com.example.flixster.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

// Class that defines the information for a movie
@Parcel
public class Movie {

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    String releaseDate;
    Integer id;
    String width;

    public Movie() {}

    // Retrieves all the information for the movie from the JSONObject
    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        releaseDate = jsonObject.getString("release_date");
        id = jsonObject.getInt("id");
    }

    // Returns a list of movies from the JSONArray
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    // Returns the URL path for the poster image
    public String getPosterPath() {
        String width = MainActivity.getSizes()[0];
        if (width != null) {
            return String.format("https://image.tmdb.org/t/p/%s/%s", width, posterPath);
        }
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);

    }

    // Returns the URL path for the backdrop image
    public String getBackdropPath() {
        String width = MainActivity.getSizes()[1];
        if (width != null) {
            return String.format("https://image.tmdb.org/t/p/%s/%s", width, backdropPath);
        }
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    // Returns the title of the movie
    public String getTitle() {
        return title;
    }

    // Returns the overview of the movie
    public String getOverview() {
        return overview;
    }

    // Returns the average of the movie's votes
    public Double getVoteAverage() {
        return voteAverage;
    }

    // Returns the release date of the movie
    public String getReleaseDate() {
        return "Release Date: " + releaseDate;
    }

    // Returns the ID of the movie
    public int getId() {
        return id;
    }
}
