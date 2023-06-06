package com.example.prevailist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prevailist.fragments.HomePageFragment;
import com.example.prevailist.fragments.MapFragment;
import com.example.prevailist.fragments.ProfileFragment;
import com.example.prevailist.fragments.SavedFragment;
import com.example.prevailist.fragments.SearchFragment;
import com.example.prevailist.internetActivity.UserURLStringGenerator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity implements UserURLStringGenerator  {

    BottomNavigationView bottomNavigationView;
    HomePageFragment homeFragment = new HomePageFragment();
    SearchFragment searchFragment = new SearchFragment();

    MapFragment mapFragment = new MapFragment();
    SavedFragment savedFragment = new SavedFragment();
    ProfileFragment profileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getTitle().toString()) {
                case "Search":
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                    return true;
                case "Map":
                    Intent mapIntent = new Intent(HomePage.this, MapFragment.class);
                    startActivity(mapIntent);
                return true;

                case "Saved":
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, savedFragment).commit();
                    return true;
                case "Profile":
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                    return true;
            }
            return false;
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();

        String urlString = getUser();
        String response = null;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                    System.out::println, error -> System.out.println("That didn't work!" + error)) {
                @Override
                public Map<String, String> getHeaders() {
                    final Map<String, String> headers = new HashMap<>();
                    SharedPreferences sharedPref;
                    sharedPref = getApplicationContext().getSharedPreferences(
                            "PrevalistRegistered", Context.MODE_PRIVATE);
                    headers.put(getString(R.string.tokenAuth), sharedPref.getString("token", "empty"));
                    return headers;
                }
            };
            queue.add(stringRequest);
            System.out.println(response);
    }


}