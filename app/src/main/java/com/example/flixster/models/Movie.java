package com.example.flixster.models;

import com.example.flixster.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    String releaseDate;
    Integer id;
    String subString;

    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        releaseDate = jsonObject.getString("release_date");
        id = jsonObject.getInt("id");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        String width = MainActivity.getPoster_size()[0];
        if (width != null) {
            return String.format("https://image.tmdb.org/t/p/%s/%s", width, posterPath);
        }
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);

    }

    public String getBackdropPath() {
        String width = MainActivity.getPoster_size()[1];
        if (width != null) {
            return String.format("https://image.tmdb.org/t/p/%s/%s", width, backdropPath);
        }
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return "Release Date: " + releaseDate;
    }

    public int getId() {
        return id;
    }
}
