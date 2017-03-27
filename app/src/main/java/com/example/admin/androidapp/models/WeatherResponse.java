package com.example.admin.androidapp.models;

/**
 * Created by Admin on 2/25/2017.
 */

public class WeatherResponse {
    //   @SerializedName("coord")
    private Coordinates coord;
    private Main main;

    public Coordinates getCoord() {
        return coord;
    }

    public Main getMain() {
        return main;
    }
}
