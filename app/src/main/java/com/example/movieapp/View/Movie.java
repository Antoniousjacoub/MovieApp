package com.example.movieapp.View;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tony on 4/1/2016.
 */
public class Movie implements Parcelable {


    private int id;
    private String title; // original_title
    private String language; //original_language
    private String image; // backdrop_path
    private String rating; // vote_average
    static String baseURLForImage = "http://image.tmdb.org/t/p/w185/";

    private String release_date;
    private String vote_count;
    private String overview;


    public Movie(JSONObject movie) throws JSONException {

        this.id = movie.getInt("id");
        this.title = movie.getString("original_title");
        this.language = movie.getString("original_language");
        this.image = movie.getString("poster_path");
        this.rating = movie.getString("vote_average");

        this.release_date = movie.getString("release_date");
        this.vote_count = movie.getString("vote_count");
        this.overview = movie.getString("overview");


    }

    public Movie(int id,String title,String releaseDate,String rating,String overview,String language,String imageurl,String vote) {
        this.id = id;
        this.title=title;
        this.release_date=releaseDate;
        this.rating=rating;
        this.overview=overview;
        this.language=language;
        this.image=imageurl;
        this.vote_count=vote;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public String getImage() {
        return baseURLForImage + image;
    }

    public String getRating() {
        return rating;
    }


    public String getOverview() {
        return this.overview;
    }

    public String getRelease_date() {
        return this.release_date;
    }

    public String getVote_count() {
        return this.vote_count;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(release_date);
    }
}

