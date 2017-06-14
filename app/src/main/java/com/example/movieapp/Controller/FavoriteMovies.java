package com.example.movieapp.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.movieapp.View.Movie;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Toni on 4/22/2016.
 */
public class FavoriteMovies {


    public static void storeFavorites(Context context, ArrayList<Movie> movies) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("MovieList",Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(movies);
        editor.putString("Movies", jsonFavorites);
        editor.commit();
    }
    public static ArrayList loadFavorites(Context context)
    {
        SharedPreferences settings;
        List favorites;
        settings = context.getSharedPreferences("MovieList",Context.MODE_PRIVATE);
        if (settings.contains("Movies")) {
            String jsonFavorites = settings.getString("Movies", null);
            Gson gson = new Gson();
            Movie[] favoriteItems = gson.fromJson(jsonFavorites,Movie[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return null;
        return (ArrayList) favorites;
    }

    public static void DeleteFavoriteMovies(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("MovieList", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public static void DeleteFavoriteMovieById(Context context,int id)
    {
       ArrayList<Movie> arrayList=loadFavorites(context);
        if(arrayList!=null)
        {
            for (int i=0;i<arrayList.size();i++)
            {
                if ((arrayList.get(i).getId()==id))
                    arrayList.remove(i);
            }
            DeleteFavoriteMovies(context);
            storeFavorites(context,arrayList);
        }


    }
}
