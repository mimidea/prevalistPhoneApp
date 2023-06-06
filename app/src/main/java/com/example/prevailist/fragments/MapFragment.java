package com.example.prevailist.fragments;

import static android.content.ContentValues.TAG;

import static com.example.prevailist.service.VerifiyConstants.ERROR_DIALOG_REQUEST;

import android.app.Dialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;

import android.Manifest;

import com.directions.route.RoutingListener;
import com.example.prevailist.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Location myLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;
    private List<Polyline> polylines = null;

    private ArrayList<Route> routes;
    private int shortestRouteIndex;
    private EditText TF_location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_);
        requestPermission();
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String apiKey = appInfo.metaData.getString("AIzaSyAaNX1fn-1iMGRrf0bTgTjg25_EApDCG_U");
            // Use the apiKey variable in your code
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map_);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapFragment.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapFragment.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        shortestRouteIndex = 0;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                locationPermission = true;
                onRoutingStart();
                getMyLocation();
                isServicesOK();
                onRoutingSuccess( routes, shortestRouteIndex );
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener(){
            @Override
            public void onMyLocationClick(@NonNull Location location) {

            }

            public void onMyLocationChange(Location location) {
                myLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        latLng, 16f);
                mMap.animateCamera(cameraUpdate);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                end = latLng;
                mMap.clear();
                if (myLocation != null) {
                    start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    findRoutes(start, end);
                } else {
                    // Handle the case where myLocation is null
                    // You can show an error message or take appropriate action
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getMyLocation();
        direction();
    }
    private void direction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("destination", "47.0303692, 24.9112415" )
                .appendQueryParameter("origin","47.0799357, 25.2344534" )
                .appendQueryParameter("mode","walking")
                .appendQueryParameter("key", "AIzaSyAaNX1fn-1iMGRrf0bTgTjg25_EApDCG_U")
                .toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    System.out.println("!!!!!!!!!RESPONSE!!!!!" + response);
                    String status = response.getString("status");
                    if(status.equals("OK")){
                        JSONArray routes = response.getJSONArray("routes");

                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = null;

                        for (int i=0; i<routes.length(); i++){
                            points = new ArrayList<>();
                            polylineOptions = new PolylineOptions();
                            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

                            for(int j=0; j<legs.length(); j++){
                                JSONArray steps = legs.optJSONObject(j).getJSONArray("steps");

                                for (int k=0; k<steps.length(); k++){
                                    try {
                                        String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");

                                        String newEncodedString = polyline.replace("\\\\", "\\");
                                        List<LatLng> list = decodePoly(newEncodedString);

                                        for (int l = 0; l < list.size(); l++) {
                                            LatLng position = new LatLng((list.get(l)).latitude, (list.get(l)).longitude);
                                            points.add(position);
                                        }
                                    } catch (Exception e){
                                        System.out.println(e);
                                    }
                                }
                            }
                            polylineOptions.addAll(points);
                            polylineOptions.width(10);
                            polylineOptions.color(ContextCompat.getColor(MapFragment.this, R.color.purple_500));
                            polylineOptions.geodesic(true);
                        }
                        mMap.addPolyline(polylineOptions);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(47.0303692, 24.9112415)).title("Marker 1"));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(47.0799357, 25.2344534)).title("Marker 1"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(jsonObjectRequest);
    }
    private List<LatLng> decodePoly(String encode) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encode.length();
        int lat = 0, lng = 0;

        while (index< len){
            int b, shift = 0, result = 0;
            do {
                b = encode.charAt(index++) - 63;
                result |= (b& 0x1f)<<shift;
                shift +=5;
            } while (b>= 0x20);
            int dlat = ((result & 1) !=0? ~(result>>1): (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encode.charAt(index++)-63;
                result |=(b & 0x1f) <<shift;
                shift +=5;
            }while (b > 0x20);
            int dlng = ((result & 1) !=0? ~(result>>1): (result >> 1));
            lng = dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5 )));
            poly.add(p);
        }
        return poly;
    }
    public void onMapClick(LatLng latLng) {
        if (start == null) {
            start = latLng;
            mMap.addMarker(new MarkerOptions().position(start).title("Start Location"));
        } else if (end == null) {
            end = latLng;
            mMap.addMarker(new MarkerOptions().position(end).title("End Location"));
            findRoutes(start, end);
        }
    }

    public void findRoutes(LatLng start, LatLng end) {
        routes = new ArrayList<>();
        if (start == null || end == null) {
            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
        } else {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener((RoutingListener)this)
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .key("AIzaSyAaNX1fn-1iMGRrf0bTgTjg25_EApDCG_U")
                    .build();
            routing.execute();
        }
    }
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
        findRoutes(start, end);
    }

    public void onRoutingStart() {
        Toast.makeText(this, "Finding Route...", Toast.LENGTH_LONG).show();
    }


    public void onRoutingSuccess(ArrayList<Route> routes, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            for (Polyline polyline : polylines) {
                polyline.remove();
            }
            polylines.clear();
        }

        polylines = new ArrayList<>();

        for (int i = 0; i < routes.size(); i++) {
            PolylineOptions polyOptions = new PolylineOptions();
            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.design_default_color_primary));
                polyOptions.width(7);
                polyOptions.addAll(routes.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylines.add(polyline);
            } else {
                // Add other routes with different colors or styles if needed
            }
        }

        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(start);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(end);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);

        mMap.animateCamera(center);
        mMap.moveCamera(zoom);
    }

    public void onRoutingCancelled() {
        findRoutes(start, end);
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        findRoutes(start, end);
    }

}