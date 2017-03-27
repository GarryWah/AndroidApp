package com.example.admin.androidapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.androidapp.models.ApiService;
import com.example.admin.androidapp.models.Main;
import com.example.admin.androidapp.R;
import com.example.admin.androidapp.models.WeatherResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 3/5/2017.
 */

public class HomeFragment extends Fragment {
    private GoogleMap map;
    MapView mMapView;
    private LocationGooglePlayServicesProvider provider;
    private static Location currentlocation;
    private double latitude; // latitude
    private double longitude; // longitude
    private LocationManager locationManager;
    private TextView tvTemp;
    private TextView tv2;
    private final static String API_BASE_URL = "http://api.openweathermap.org/";
    private final static String API_TOKEN = "b6b1580e9d2a1d5b8668a36738565c3c";
    private final static String UNITS = "metric";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvTemp = (TextView) view.findViewById(R.id.tvTemp);
        tv2 = (TextView) view.findViewById(R.id.tv2);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String bestprovider = locationManager.getBestProvider(criteria, true);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                currentlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                latitude = currentlocation.getLatitude();
                longitude = currentlocation.getLongitude();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
                LatLng coord = new LatLng(latitude, longitude);
                map.addMarker(new MarkerOptions().position(coord));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 14));


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final ApiService service = retrofit.create(ApiService.class);
                Call<WeatherResponse> call = service.getWeatherByCoord(API_TOKEN, String.valueOf(latitude), String.valueOf(longitude), UNITS);
                call.enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        Log.e("Tag", String.valueOf(response.raw()));

                        WeatherResponse weatherResponse = response.body();
                        if (response.isSuccessful()) {
                            Main main = weatherResponse.getMain();
                            double temp = main.getTemp();
                            Log.e("Tag", String.valueOf(temp));
                            String txt = tv2.getText().toString();
                            tv2.setText(txt + " " + String.valueOf(temp));
                        }

                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Log.e("Tag", t.getLocalizedMessage());
                    }
                });

            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}