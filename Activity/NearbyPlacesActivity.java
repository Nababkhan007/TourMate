package com.example.nabab.tourmate.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nabab.tourmate.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class NearbyPlacesActivity extends AppCompatActivity {
    private TextView showLatitude, showLongitude;
    private CardView nearbyRestaurantShowCardView, nearbyBankShowCardView, nearbyHospitalShowCardView, nearbyClothingStoreShowCardView, nearbyMosqueShowCardView, nearbyCafeShowCardView, nearbyBusStationShowCardView, nearbyAtmShowCardView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double latitude;
    private double longitude;
    private Geocoder geocoder;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

        initialization();
        onClick();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    /*showLatitude.setText(String.valueOf(latitude));
                    showLongitude.setText(String.valueOf(longitude));*/
                    /*double lat =  Double.parseDouble(String.valueOf(latitude));
                    double lon =  Double.parseDouble(String.valueOf(longitude));*/

                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void onClick() {
        nearbyRestaurantShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeType = "restaurant";
                Intent intent = new Intent(NearbyPlacesActivity.this, ShowNearbyPlacesActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("place_type", placeType);
                startActivity(intent);
            }
        });

        nearbyBankShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeType = "bank";
                Intent intent = new Intent(NearbyPlacesActivity.this, ShowNearbyPlacesActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("place_type", placeType);
                startActivity(intent);
            }
        });

        nearbyHospitalShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeType = "hospital";
                Intent intent = new Intent(NearbyPlacesActivity.this, ShowNearbyPlacesActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("place_type", placeType);
                startActivity(intent);
            }
        });

        nearbyClothingStoreShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeType = "clothing_store";
                Intent intent = new Intent(NearbyPlacesActivity.this, ShowNearbyPlacesActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("place_type", placeType);
                startActivity(intent);
            }
        });

        nearbyMosqueShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeType = "mosque";
                Intent intent = new Intent(NearbyPlacesActivity.this, ShowNearbyPlacesActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("place_type", placeType);
                startActivity(intent);
            }
        });

        nearbyCafeShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeType = "cafe";
                Intent intent = new Intent(NearbyPlacesActivity.this, ShowNearbyPlacesActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("place_type", placeType);
                startActivity(intent);
            }
        });

        nearbyBusStationShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeType = "bus_station";
                Intent intent = new Intent(NearbyPlacesActivity.this, ShowNearbyPlacesActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("place_type", placeType);
                startActivity(intent);
            }
        });

        nearbyAtmShowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeType = "atm";
                Intent intent = new Intent(NearbyPlacesActivity.this, ShowNearbyPlacesActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                intent.putExtra("place_type", placeType);
                startActivity(intent);
            }
        });
    }

    private void initialization() {
        showLatitude = findViewById(R.id.showLatitudeId);
        showLongitude = findViewById(R.id.showLongitudeId);

        nearbyRestaurantShowCardView = findViewById(R.id.nearbyRestaurantShowCardViewId);
        nearbyBankShowCardView = findViewById(R.id.nearbyBankShowCardViewId);
        nearbyHospitalShowCardView = findViewById(R.id.nearbyHospitalShowCardViewId);
        nearbyClothingStoreShowCardView = findViewById(R.id.nearbyClothingStoreShowCardViewId);
        nearbyMosqueShowCardView = findViewById(R.id.nearbyMosqueShowCardViewId);
        nearbyCafeShowCardView = findViewById(R.id.nearbyCafeShowCardViewId);
        nearbyBusStationShowCardView = findViewById(R.id.nearbyBusStationShowCardViewId);
        nearbyAtmShowCardView = findViewById(R.id.nearbyAtmShowCardViewId);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(3000).setFastestInterval(1000);
        geocoder = new Geocoder(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int getItemId = item.getItemId();
        if(getItemId == R.id.homeMenuId){
            startActivity(new Intent(NearbyPlacesActivity.this, MainActivity.class));
        }
        return true;
    }
}
