package com.example.prevailist.logIn_SignIn;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prevailist.R;
import com.example.prevailist.HomePage;

import com.example.prevailist.internetActivity.UserURLStringGenerator;
import com.google.android.material.textfield.TextInputEditText;


public class Registration extends AppCompatActivity implements UserURLStringGenerator {
    TextInputEditText editTextUsername, editTextFirstname, editTextLastname, editTextAge, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button registerButton;

    private final String username;
    private final String firstName;
    private final String lastName;
    private final int age;
    private final String email;
    private final String password;
    private final String confirmPassword;

    public Registration() {
        this.username = null;
        this.firstName = null;
        this.lastName = null;
        this.age = 0;
        this.email = null;
        this.password = null;
        this.confirmPassword = null;
    }

    public Registration(String username, String firstName, String lastName, int age, String email, String password, String confirmPassword) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public boolean validateRegistration() {
        if (!CreateAccounts.validatePassword(password, confirmPassword)) {
            System.out.println("Password and confirm password do not match");
            return false;
        }

        return true;
    }

    public CreateAccounts createUser() {
        if (validateRegistration()) {
            return new CreateAccounts(username, firstName, lastName, age, email, password);
        } else {
            return null;
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextUsername = findViewById(R.id.username);
        editTextFirstname = findViewById(R.id.firstname);
        editTextLastname = findViewById(R.id.lastName);
        editTextAge = findViewById(R.id.age);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        registerButton = findViewById(R.id.btn_register);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);

        registerButton.setOnClickListener(view -> {
            String username, firstName, lastName, email, password, confirmPassword, age;
            username = String.valueOf(editTextUsername.getText());
            firstName = String.valueOf(editTextFirstname.getText());
            lastName = String.valueOf(editTextLastname.getText());
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());
            confirmPassword = String.valueOf(editTextConfirmPassword.getText());
            age = String.valueOf(editTextAge.getText());

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(Registration.this, "Enter username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(firstName)) {
                Toast.makeText(Registration.this, "Enter Firstname", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(lastName)) {
                Toast.makeText(Registration.this, "Enter lastName", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Registration.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Registration.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(Registration.this, "Enter confirmPassword", Toast.LENGTH_SHORT).show();
                return;
            }

            String urlString = registerDetailsUrlGenerator(firstName, lastName, email, password, username, age);
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
                            Toast.makeText(Registration.this, "Account created.", Toast.LENGTH_SHORT).show();
                            // Redirect to HomePage activity
                            Intent intent = new Intent(Registration.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Registration.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> System.out.println("That didn't work!" + error));

            queue.add(stringRequest);
            System.out.println(response);
        });
    }

    public String getUsername() {
        return username;
    }
}
