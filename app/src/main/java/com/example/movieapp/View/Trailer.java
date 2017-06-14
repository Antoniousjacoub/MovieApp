package com.example.movieapp.View;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Toni on 4/19/2016.
 */
public class Trailer {

    private String id;
    private String key;
    private String name;


    public Trailer() {

    }

    public Trailer(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.key = trailer.getString("key");
        this.name = trailer.getString("name");

    }

    public String getId() {
        return id;
    }

    public String getKey() { return key; }

    public String getName() { return name; }

}
