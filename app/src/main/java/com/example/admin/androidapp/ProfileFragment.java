package com.example.admin.androidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 3/5/2017.
 */

public class ProfileFragment extends Fragment {
    private ImageView imageView;
    private EditText editText1;
    private EditText editText2;
    private String API_BASE_URL = "http://api.backendless.com/v1/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.profile_fragment, container, false);
        imageView = (ImageView) view.findViewById(R.id.image);
        editText1 = (EditText) view.findViewById(R.id.editText1);
        editText2 = (EditText) view.findViewById(R.id.editText2);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiService service = retrofit.create(ApiService.class);
        Call<UserResponse> call = service.getCommunity(sharedPreferences.getString("id",""));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.e("Tag", String.valueOf(response.raw()));
                UserResponse userResponse = response.body();
                if (response.isSuccessful()) {
                    String name = userResponse.getName();
                    String description = userResponse.getDescription();
                    Log.e("Tag", name + " " + description);
                    editText1.setText(name);
                    editText2.setText(description);
                    Glide.with(getContext()).load(userResponse.getPhoto()).into(imageView);
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Tag", t.getLocalizedMessage());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final ApiService service = retrofit.create(ApiService.class);
            SharedPreferences sharedPreferences=getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
            Call<UserResponse> call = service.setUserInfo(sharedPreferences.getString("id",null),
                    new UserRequest(editText1.getText().toString(),editText2.getText().toString()));
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    Log.e("Tag", String.valueOf(response.raw()));
                    UserResponse userResponse = response.body();
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Succesfully changed!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.e("Tag", t.getLocalizedMessage());
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

}
