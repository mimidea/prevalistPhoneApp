package com.example.prevailist.fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.prevailist.R;

public class ItemItineraryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_itinerary);

        TextView itineraryTextView = findViewById(R.id.itineraryTextView);

        String itineraryContent = getIntent().getStringExtra("itineraryContent");

        if (itineraryContent != null) {
            itineraryTextView.setText(itineraryContent);
        }
    }
}

