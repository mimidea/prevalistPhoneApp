package com.example.prevailist.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.prevailist.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

public class ProfileFragment extends Fragment {

    private EditText itineraryEditText;
    private RecyclerView itineraryRecyclerView;
    private ItineraryAdapter itineraryAdapter;
    private List<String> savedItineraries;
    private String itineraryContent;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profilePicture = view.findViewById(R.id.profile_picture);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Button savedTextButton = view.findViewById(R.id.saved_page_button);
        itineraryEditText = view.findViewById(R.id.itineraryEditText);
        Button saveItineraryButton = view.findViewById(R.id.saveItineraryButton);
        Button viewItineraryButton = view.findViewById(R.id.viewItineraryButton);
        itineraryRecyclerView = view.findViewById(R.id.itineraryRecyclerView);
        itineraryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        savedItineraries = new ArrayList<>();
        itineraryAdapter = new ItineraryAdapter(savedItineraries);
        itineraryRecyclerView.setAdapter(itineraryAdapter);

        saveItineraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itinerary = itineraryEditText.getText().toString();
                saveItineraryToFile(itinerary);

                // Clear the input field
                itineraryEditText.setText("");

                // Save the content of itineraryTextView
                itineraryContent = itinerary;

                // Refresh the list of saved itineraries
                savedItineraries.clear();
                savedItineraries.addAll(readItinerariesFromFile());
                itineraryAdapter.notifyDataSetChanged();
            }
        });


        viewItineraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the content of itineraryTextView to ItemItineraryActivity
                Intent intent = new Intent(getActivity(), ItemItineraryActivity.class);
                intent.putExtra("itineraryContent", itineraryContent);
                startActivity(intent);
            }
        });


        savedTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedItineraries.clear();
                savedItineraries.addAll(readItinerariesFromFile());
                itineraryAdapter.notifyDataSetChanged();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Handle calendar date selection
                String selectedDate = formatDate(year, month, dayOfMonth);
                Toast.makeText(getActivity(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(year - 1900, month, dayOfMonth);
        return sdf.format(date);
    }

    private void saveItineraryToFile(String itinerary) {
        String fileName = "itinerary.txt";

        try {
            File directory = new File(getActivity().getFilesDir(), "user_profiles");
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(directory, fileName);
            FileWriter writer = new FileWriter(file, true);
            writer.append(itinerary).append("\n");
            writer.close();

            Toast.makeText(getActivity(), "Itinerary saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to save itinerary", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> readItinerariesFromFile() {
        String fileName = "itinerary.txt";
        List<String> itineraries = new ArrayList<>();

        try {
            File file = new File(getActivity().getFilesDir() + File.separator + "user_profiles", fileName);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                itineraries.add(line);
            }

            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Itinerary file not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to read itinerary", Toast.LENGTH_SHORT).show();
        }

        // Logging statements to debug the file reading process
        Log.d("Itinerary", "Number of itineraries read: " + itineraries.size());
        for (String itinerary : itineraries) {
            Log.d("Itinerary", "Read itinerary: " + itinerary);
        }

        return itineraries;
    }

    private static class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

        private List<String> itineraries;

        public ItineraryAdapter(List<String> itineraries) {
            this.itineraries = itineraries;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itinerary, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String itinerary = itineraries.get(position);
            if (itinerary != null) {
                holder.itineraryTextView.setText(itinerary);
            } else {
                holder.itineraryTextView.setText("");
            }
        }

        @Override
        public int getItemCount() {
            return itineraries.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView itineraryTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itineraryTextView = itemView.findViewById(R.id.itineraryTextView);
            }
        }
    }
}
