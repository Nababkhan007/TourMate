package com.example.nabab.tourmate.Weather;

import com.example.nabab.tourmate.ForecastWeatherApiClass.ForecastWeatherResponse;
import com.example.nabab.tourmate.WeatherApiClass.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherApiService {
    @GET
    Call<WeatherResponse> getCurrentWeather(@Url String url);

    @GET
    Call<ForecastWeatherResponse> getForecastWeather(@Url String url);

}
