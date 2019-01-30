package com.example.nabab.tourmate.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabab.tourmate.Class.FirebaseDatabaseReference;
import com.example.nabab.tourmate.Fragment.DashboardFragment;
import com.example.nabab.tourmate.PojoClass.UsersPojo;
import com.example.nabab.tourmate.R;
import com.example.nabab.tourmate.Weather.WeatherApiClient;
import com.example.nabab.tourmate.Weather.WeatherApiService;
import com.example.nabab.tourmate.WeatherApiClass.WeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView drawerLayoutProfileIv;
    private TextView drawerLayoutProfileNameTv, drawerLayoutProfileMailTv, weatherShowDashboardTv;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String downLoadImageUrlLink;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double latitude;
    private double longitude;
    private Geocoder geocoder;
    private static final int REQUEST_CODE = 1;
    private WeatherApiService weatherApiService;
    private String units = "metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        getWeather();

        getDeviceLocaton();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        loadProfileDataFromDatabase();
        getUserMailId();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        drawerLayoutProfileIv = header.findViewById(R.id.drawerLayoutProfileIvId);
        drawerLayoutProfileNameTv = header.findViewById(R.id.drawerLayoutProfileNameTvId);
        drawerLayoutProfileMailTv = header.findViewById(R.id.drawerLayoutProfileMailTvId);

        loadFragment(new DashboardFragment());
    }

    private void initialization() {
        weatherShowDashboardTv = findViewById(R.id.weatherShowDashboardTvId);
    }

    private void getUserMailId() {
        FirebaseDatabaseReference.userDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    UsersPojo usersPojo = dataSnapshot.getValue(UsersPojo.class);

                    drawerLayoutProfileMailTv.setText(usersPojo.getEmail());

                } else {
                    Toast.makeText(MainActivity.this, "At First Added Some Information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadProfileDataFromDatabase() {
        FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    UsersPojo usersPojo = dataSnapshot.getValue(UsersPojo.class);

                    drawerLayoutProfileNameTv.setText(usersPojo.getFirstName()+" "+usersPojo.getLastName());
                    Picasso.get().load(usersPojo.getDownLoadImageUrlLink()).placeholder(R.drawable.add_profile_image_icon).into(drawerLayoutProfileIv);

                } else {
                    Toast.makeText(MainActivity.this, "At First Added Some Information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDeviceLocaton() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(3000).setFastestInterval(1000);
        geocoder = new Geocoder(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

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

    private void getWeather() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        String url = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s", latitude, longitude, units, getResources().getString(R.string.weather_api_key));
        weatherApiService = WeatherApiClient.getRetrofit().create(WeatherApiService.class);
        Call<WeatherResponse> weatherResponseCall = weatherApiService.getCurrentWeather(url);

        weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.code() == 200){
                    WeatherResponse weatherResponse = response.body();

                    /*weatherShowDashboardTv.setText(weatherResponse.getMain().getTempMin()+" °C");*/

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.dashboardProfileMenuId) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.travelEventMenuId) {
            startActivity(new Intent(MainActivity.this, EventActivity.class));

        } else if (id == R.id.travelEventExpenseMenuId) {
            startActivity(new Intent(MainActivity.this, EventActivity.class));

        } else if (id == R.id.nearbyPlaceMenuId) {
            startActivity(new Intent(MainActivity.this, NearbyPlacesActivity.class));

        } else if (id == R.id.travelMomentMenuId) {
            startActivity(new Intent(MainActivity.this, EventActivity.class));

        } else if (id == R.id.weatherUpdateMenuId) {
            startActivity(new Intent(MainActivity.this, ShowWeatherReportActivity.class));

        } else if (id == R.id.profileMenuId) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

        } else if (id == R.id.logOutMenuId) {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutId, fragment);
        fragmentTransaction.commit();
    }
}
