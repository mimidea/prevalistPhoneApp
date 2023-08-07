package com.example.prevailist.fragments;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.prevailist.Friend;
import com.example.prevailist.R;

import java.util.ArrayList;
import java.util.Collections;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;

public class MyFriendListAdapter extends RecyclerView.Adapter<MyFriendListAdapter.FriendHolder> implements RVHAdapter {

    private Context mContext;
    private ArrayList<Friend> friends = null;
    private String token;
    private String header;

    public MyFriendListAdapter(ArrayList<Friend> friendsList, Context context, String headersOne, String headersTwo) {
        this.mContext = context;
        this.friends = friendsList;
        this.header = headersOne;
        this.token = headersTwo;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);
    }

    public class FriendHolder extends RecyclerView.ViewHolder implements RVHViewHolder {
        public TextView friendUsername;
        public TextView friendFullName;

        public ImageView addFriend;
        private Context mContext;
        public Friend mFriend;
        View view;

        public FriendHolder(View view){
            super(view);
            this.view = view;
            friendUsername = view.findViewById( R.id.friendUsername );
            friendFullName = view.findViewById( R.id.friendName );
            addFriend = view.findViewById(R.id.addFriend);
            mContext = view.getContext();
        }

        public void bindTask (Friend friend){
            mFriend = friend;
            friendUsername.setText( mFriend.getUsername() );
            String firstAndLast = mFriend.getFirstName() + " " + mFriend.getLastName();
            friendFullName.setText( firstAndLast );

        }

        @Override
        public void onItemSelected(int actionstate) {
            System.out.println("Item is selected");
        }

        @Override
        public void onItemClear() {
            System.out.println("Item is unselected");
        }


    }


    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.friends_view_friends_template, parent, false );
        FriendHolder friendsHolder = new FriendHolder( itemView );


        ImageView addFriendButton = itemView.findViewById(R.id.addFriend);


        View.OnClickListener listener = v -> {
            if (v == addFriendButton) {



            }
        };
        addFriendButton.setOnClickListener(listener);

        return friendsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    // Helper functions you might want to implement to make changes in the list as an event is fired
    private void remove(int position) {
        friends.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(0, friends.size());
    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(friends, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }

}
