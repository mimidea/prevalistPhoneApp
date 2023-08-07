package com.example.prevailist.fragments;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prevailist.Friend;
import com.example.prevailist.R;
import com.example.prevailist.internetActivity.UserURLStringGenerator;
import com.example.prevailist.logIn_SignIn.Registration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;


public class SearchFragment extends Fragment implements UserURLStringGenerator {
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private RecyclerView findNewFriendsContainer;
    SearchAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Friend> friendsList = new ArrayList<>();
    SharedPreferences sharedPref;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    SearchView searchView;

    private MyFriendListAdapter searchForFriendsListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search_, container, false);
        searchView = view.findViewById(R.id.search);
        sharedPref = getContext().getSharedPreferences(
                "PrevalistRegistered",Context.MODE_PRIVATE);
        findNewFriendsContainer = (RecyclerView) view.findViewById(R.id.friendsContainer);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        searchForNewFriends();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return view ;
    }

    private void searchForNewFriends() throws UnsupportedEncodingException {
        String currentSearch = searchView.getQuery().toString();
        String response = null;
        String searchQuery = currentSearch.equals("") ? "prevailist" : currentSearch;
        String urlString = getSearchForFriendUrlGenerator(searchQuery);
        System.out.println("URL STRING " + urlString);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                response1 -> {
                    if (response1.contains("Fail")) {
                        System.out.println("FAIL");
                        System.out.println(response1);
                    } else {
                        System.out.println("PASS");
                        System.out.print(response1);
                        System.out.println("@@@@" + convertResponseToFriends(response1));
                        ArrayList<Friend> friends = convertResponseToFriends(response1);
                        if (friends != null) {
                            String headersOne = getString(R.string.tokenAuth);
                            mAdapter = new SearchAdapter(friends);
                            findNewFriendsContainer.setAdapter(mAdapter);

                        }
                    }
                }, error -> System.out.println("That didn't work!" + error)) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.tokenAuth), sharedPref.getString("token", "empty"));
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private ArrayList<Friend> convertResponseToFriends(String response) {
        ArrayList<Friend> friends = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject friendObject = jsonArray.getJSONObject(i);
                String friendUsernameString = friendObject.getString("UserName");
                String friendFirstnameString = friendObject.getString("FirstName");
                String friendLastnameString = friendObject.getString("LastName");
                Friend friend = new Friend(friendUsernameString, friendFirstnameString, friendLastnameString);
                friends.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Friends " + friends.get(0).getUsername().toString());
        return friends;
    }
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (findNewFriendsContainer.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) findNewFriendsContainer.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        findNewFriendsContainer.setLayoutManager(mLayoutManager);
        findNewFriendsContainer.scrollToPosition(scrollPosition);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }
        }


