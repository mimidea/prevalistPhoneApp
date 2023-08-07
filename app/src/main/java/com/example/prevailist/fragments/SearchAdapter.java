package com.example.prevailist.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.prevailist.Friend;
import com.example.prevailist.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Friend> myFriends;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView fullName;

        public ViewHolder(View v) {
            super(v);
            userName = v.findViewById(R.id.friendUsername);
            fullName = v.findViewById(R.id.friendName);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Item is selected");
                }
            });
        }
    }

    public SearchAdapter(List<Friend> friends) {
        myFriends = friends;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.friends_view_friends_template, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        String fullName = myFriends.get(position).getFirstName() + " " + myFriends.get(position).getLastName();
        viewHolder.fullName.setText(fullName);
        viewHolder.userName.setText(myFriends.get(position).getUsername());

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myFriends.size();
    }
}