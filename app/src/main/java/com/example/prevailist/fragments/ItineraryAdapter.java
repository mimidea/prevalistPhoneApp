package com.example.prevailist.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prevailist.R;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private List<String> itineraries;
    private String itineraryContent;

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
            itineraryContent = itinerary;
        } else {
            holder.itineraryTextView.setText("");
            itineraryContent = "";
        }
    }

    public String getItineraryContent() {
        return itineraryContent;
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
