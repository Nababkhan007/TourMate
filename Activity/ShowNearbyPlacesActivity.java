package com.example.nabab.tourmate.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nabab.tourmate.Adapter.NearbyPlacesAdapter;
import com.example.nabab.tourmate.Nearby.NearbyPlacesApiClient;
import com.example.nabab.tourmate.Nearby.NearbyPlacesApiService;
import com.example.nabab.tourmate.NearbyApiClass.NearbyResponse;
import com.example.nabab.tourmate.NearbyApiClass.Result;
import com.example.nabab.tourmate.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowNearbyPlacesActivity extends AppCompatActivity {
    private NearbyPlacesApiService nearbyPlacesApiService;
    private Double latitude;
    private Double longitude;
    private int radius = 1000;
    private String place_type;
    private RecyclerView recyclerView;
    private NearbyPlacesAdapter nearbyPlacesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nearby_places);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();

        String url = String.format("place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&key=%s", latitude, longitude, radius, place_type, getResources().getString(R.string.place_api_key));

        nearbyPlacesApiService = NearbyPlacesApiClient.getRetrofit().create(NearbyPlacesApiService.class);
        Call<NearbyResponse> nearbyResponseCall = nearbyPlacesApiService.getNearby(url);

        nearbyResponseCall.enqueue(new Callback<NearbyResponse>() {
            @Override
            public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                if (response.code()==200){
                    NearbyResponse nearbyResponse = response.body();
                    List<Result> resultList = nearbyResponse.getResults();
                    nearbyPlacesAdapter = new NearbyPlacesAdapter(resultList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ShowNearbyPlacesActivity.this));
                    recyclerView.setAdapter(nearbyPlacesAdapter);
                    Toast.makeText(ShowNearbyPlacesActivity.this, String.valueOf(resultList.size()), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<NearbyResponse> call, Throwable t) {

            }
        });

    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerViewId);

        Intent receiveData = getIntent();
        latitude =  receiveData.getDoubleExtra("Latitude", 0.0);
        longitude = receiveData.getDoubleExtra("Longitude", 0.0);
        place_type = receiveData.getStringExtra("place_type");

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
            startActivity(new Intent(ShowNearbyPlacesActivity.this, MainActivity.class));
        }
        return true;
    }
}
