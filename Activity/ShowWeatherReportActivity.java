package com.example.nabab.tourmate.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabab.tourmate.ForecastWeatherApiClass.ForecastWeatherResponse;
import com.example.nabab.tourmate.R;
import com.example.nabab.tourmate.Weather.WeatherApiClient;
import com.example.nabab.tourmate.Weather.WeatherApiService;
import com.example.nabab.tourmate.WeatherApiClass.WeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowWeatherReportActivity extends AppCompatActivity {
    private TextView showWeatherTemperatureAreaTv, showWeatherTemperatureTv, showWeatherTemperatureTypeTv, showForecastWeatherTemperatureTv;
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
        setContentView(R.layout.activity_show_weather_report);

        initialization();

        getWeather();

        getForecastWeather();

        getDeviceLocaton();

    }

    private void getForecastWeather() {
        String url = String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s", latitude, longitude, units, getResources().getString(R.string.weather_api_key));
        Call<ForecastWeatherResponse> forecastWeatherResponseCall = weatherApiService.getForecastWeather(url);

        forecastWeatherResponseCall.enqueue(new Callback<ForecastWeatherResponse>() {
            @Override
            public void onResponse(Call<ForecastWeatherResponse> call, Response<ForecastWeatherResponse> response) {
                ForecastWeatherResponse forecastWeatherResponse = response.body();

                /*showForecastWeatherTemperatureTv.setText(forecastWeatherResponse.getCity().getCountry());*/
            }

            @Override
            public void onFailure(Call<ForecastWeatherResponse> call, Throwable t) {
                Toast.makeText(ShowWeatherReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

                    /*showWeatherTemperatureAreaTv.setText(weatherResponse.getSys().getCountry());*/
                    showWeatherTemperatureTv.setText(weatherResponse.getMain().getTempMin()+" °C");
//                    showWeatherTemperatureTypeTv.setText(weatherResponse.getSys().getType());

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(ShowWeatherReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialization() {
        /*showWeatherTemperatureAreaTv = findViewById(R.id.showWeatherTemperatureAreaTvId);*/
        showWeatherTemperatureTv = findViewById(R.id.showWeatherTemperatureTvId);
        /*showWeatherTemperatureTypeTv = findViewById(R.id.showWeatherTemperatureTypeTvId);*/
        /*showForecastWeatherTemperatureTv = findViewById(R.id.showForecastWeatherTemperatureTvId);*/
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
            startActivity(new Intent(ShowWeatherReportActivity.this, MainActivity.class));
        }
        return true;
    }
}
