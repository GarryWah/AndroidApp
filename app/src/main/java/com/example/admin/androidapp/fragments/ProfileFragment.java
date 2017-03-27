package com.example.admin.androidapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.bumptech.glide.Glide;
import com.example.admin.androidapp.models.ApiService;
import com.example.admin.androidapp.R;
import com.example.admin.androidapp.models.UserRequest;
import com.example.admin.androidapp.models.UserResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 3/5/2017.
 */

public class ProfileFragment extends Fragment {
    private static final int CHANGE_NAME = 0;
    private static final int CHANGE_DESCRIPTION = 1;
    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;
    private String API_BASE_URL = "http://api.backendless.com/v1/";
    private SharedPreferences sharedPreferences;
    private UserResponse user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        textView1 = (TextView) view.findViewById(R.id.textView1);
        textView2 = (TextView) view.findViewById(R.id.textView2);
        final String idFromIntent = getActivity().getIntent().getStringExtra("id");
        sharedPreferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        final String idFromSharedPreferences = sharedPreferences.getString("id", null);
        final String id = idFromIntent != null
                ? idFromIntent
                : idFromSharedPreferences;
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadProfile(id);
            }
        }).start();
        imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = {"Upload photo from camera", "Upload photo from gallery"};
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Change photo")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, 0);
                                        break;
                                    case 1:
                                        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intentGallery, 1);
                                        break;
                                }

                            }


                        })
                        .create();
                dialog.show();
            }
        });

        if (id.equals(idFromSharedPreferences)) {
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(id, CHANGE_NAME);
                }
            });
            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(id, CHANGE_DESCRIPTION);
                }
            });
        }
        return view;
    }

    private void showDialog(final String id, final int type) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog1, null);
        final EditText editText3 = (EditText) view.findViewById(R.id.editText3);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(type == CHANGE_NAME ? "Edit your name" : "Edit your description")
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadName(editText3.getText().toString(), type);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    private void uploadName(final String text, final int type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRequest request;
        if (type == CHANGE_NAME)
            request = new UserRequest(text, user.getDescription());
        else
            request = new UserRequest(user.getName(), text);

        final ApiService service = retrofit.create(ApiService.class);
        Call<UserResponse> call = service.setUserInfo(sharedPreferences.getString("id", null),
                request);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.e("Tag", String.valueOf(response.raw()));
                UserResponse userResponse = response.body();
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Succesfully changed!", Toast.LENGTH_LONG).show();
                    if (type == CHANGE_NAME) {
                        user.setName(text);
                    } else {
                        user.setDescription(text);
                    }
                    setUI();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Tag", t.getLocalizedMessage());
            }
        });
    }

    private void loadProfile(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiService service = retrofit.create(ApiService.class);
        Call<UserResponse> call = service.getCommunity(id);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.e("Tag", String.valueOf(response.raw()));
                UserResponse userResponse = response.body();
                if (response.isSuccessful()) {
                    user = userResponse;
                    setUI();
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Tag", t.getLocalizedMessage());
            }
        });
    }

    private void setUI() {
        textView1.setText(user.getName());
        textView2.setText(user.getDescription());
        Glide.with(getContext()).load(user.getPhoto()).into(imageView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap photo = BitmapFactory.decodeStream(inputStream);
                    setPhoto(photo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case 1:
                if (resultCode == getActivity().RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    setPhoto(photo);

                }
                break;
        }

    }

    private void setPhoto(Bitmap photo) {
        imageView.setImageBitmap(photo);
        Backendless.Files.Android.upload(photo,
                Bitmap.CompressFormat.JPEG,
                100,
                "avatar_" + sharedPreferences.getString("id", null) + ".jpeg",
                "avatars",
                new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(final BackendlessFile backendlessFile) {
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(getActivity(), backendlessFault.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
