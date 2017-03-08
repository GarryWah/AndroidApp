package com.example.admin.androidapp;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Admin on 2/25/2017.
 */

public interface ApiService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherByCoord(@Query("appid") String appid,
                                            @Query("lat") String lat,
                                            @Query("lon") String lon,
                                            @Query("units") String units);

    @GET("data/Users")
    @Headers({"application-id:892B27F2-C4C2-42E0-FF52-5EAD8C9A4400",
            "secret-key:8E7D3E2E-384F-D83F-FF78-8C521B5FC800"})
    Call<Data> getCommunity();

    @GET("data/Users/{id}")
    @Headers({"application-id:892B27F2-C4C2-42E0-FF52-5EAD8C9A4400",
            "secret-key:8E7D3E2E-384F-D83F-FF78-8C521B5FC800"})
    Call<UserResponse> getCommunity(@Path("id") String id);

    @PUT("data/Users/{id}")
    @Headers({"application-id:892B27F2-C4C2-42E0-FF52-5EAD8C9A4400",
            "secret-key:8E7D3E2E-384F-D83F-FF78-8C521B5FC800"})
    Call<UserResponse> setUserInfo(@Path("id") String id,@Body UserRequest userRequest);
}