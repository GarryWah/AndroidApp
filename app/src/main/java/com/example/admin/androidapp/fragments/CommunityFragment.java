package com.example.admin.androidapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.androidapp.activities.ProfileFragmentActivity;
import com.example.admin.androidapp.adapters.CommunityFragmentAdapter;
import com.example.admin.androidapp.models.ApiService;
import com.example.admin.androidapp.models.Data;
import com.example.admin.androidapp.models.Model;
import com.example.admin.androidapp.R;
import com.example.admin.androidapp.models.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 3/5/2017.
 */

public class CommunityFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommunityFragmentAdapter adapter;
    private String API_BASE_URL = "http://api.backendless.com/v1/";
    private List<Model> models = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new CommunityFragmentAdapter(models, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnClick(new CommunityFragmentAdapter.OnCommunityFragmentClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(getContext(), "I clicked on " + models.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ProfileFragmentActivity.class);
                intent.putExtra("id", models.get(position).getId());
                startActivity(intent);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiService service = retrofit.create(ApiService.class);
        Call<Data> call = service.getCommunity();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.e("Tag", String.valueOf(response.raw()));
                Data communityResponse = response.body();
                if (response.isSuccessful()) {
                   List<UserResponse> userResponses = communityResponse.getData();
                    Log.e("Tag", String.valueOf(userResponses.size()));
                    for (UserResponse userResponse : userResponses) {
                        Model model = new Model();
                        model.setId(userResponse.getId());
                        model.setName(userResponse.getName());
                        model.setPhoto(userResponse.getPhoto());
                        model.setDescription(userResponse.getDescription());
                        models.add(model);
                    }
                    Log.e("Tag", models.get(2).getName());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e("Tag", t.getLocalizedMessage());
            }
        });


        return view;
    }
}
