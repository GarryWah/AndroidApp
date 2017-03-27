package com.example.admin.androidapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.admin.androidapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText editText1;
    private EditText editText2;
    private TextView textView;
    private Button button;
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;
    protected static final String appId = "892B27F2-C4C2-42E0-FF52-5EAD8C9A4400";
    protected static final String secretKey = "8E7D3E2E-384F-D83F-FF78-8C521B5FC800";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText1 = (EditText) findViewById(R.id.eText1);
        editText2 = (EditText) findViewById(R.id.eText2);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        textView = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.login);
        Backendless.initApp(getApplicationContext(), appId, secretKey, "v1");
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        if (email != null) {
            editText1.setText(email);
            editText2.setText(password);
            enterMainScreen();
        }
        if (sharedPreferences.getBoolean("remember", false))
            checkBox.setChecked(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Backendless.UserService.login(editText1.getText().toString(), editText2.getText().toString(), new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        Log.i("Success", response.getEmail());
                        if (checkBox.isChecked()) {
                            sharedPreferences.edit().putString("email", response.getEmail()).apply();
                            sharedPreferences.edit().putString("password", response.getPassword()).apply();
                            sharedPreferences.edit().putBoolean("remember", true).apply();
                        }
                        sharedPreferences.edit().putString("id", response.getObjectId()).apply();
                        // sharedPreferences.edit().putString(response.getEmail()).apply();
                        enterMainScreen();
                    }
                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("Error", fault.toString());
                        Toast.makeText(getApplicationContext(), "Incorrect mail or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void enterMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
