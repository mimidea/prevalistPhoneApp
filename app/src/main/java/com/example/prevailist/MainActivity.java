package com.example.prevailist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.prevailist.logIn_SignIn.Login;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prevailist.logIn_SignIn.Registration;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView userDetails;

    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.signUp);
        loginButton = findViewById(R.id.btn_login);
        userDetails = findViewById(R.id.user_details);

        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Registration.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });
    }
}
