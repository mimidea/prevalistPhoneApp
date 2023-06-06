package com.example.prevailist.logIn_SignIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prevailist.R;
import com.example.prevailist.HomePage;
import com.example.prevailist.SearchPage;
import com.example.prevailist.internetActivity.UserURLStringGenerator;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity implements UserURLStringGenerator {

    TextInputEditText editTextEmail, editTextPassword;
    Button loginButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = findViewById(R.id.signUp);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(view -> {
            String email, password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Implement your login logic here

            String urlString = loginUrlGenerator(email, password);
            String response = null;

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                    response1 -> {
                        if (response1.contains("Token:")) {
                            SharedPreferences sharedPref;
                            sharedPref = getApplicationContext().getSharedPreferences(
                                    "PrevalistRegistered", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token", response1);
                            editor.commit();
                            String tokens = sharedPref.getString("token", "empty");
                            System.out.println("TOKEN!!!!!! " +tokens);
                            Toast.makeText(Login.this, "Login successful.", Toast.LENGTH_SHORT).show();
                            // Redirect to HomePage activity
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Login attempt failed.", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> System.out.println("That didn't work!" + error));

            queue.add(stringRequest);
            System.out.println(response);
        });

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Registration.class);
            startActivity(intent);
            finish();
        });
    }
}