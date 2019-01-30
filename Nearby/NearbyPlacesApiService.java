package com.example.nabab.tourmate.Nearby;

import com.example.nabab.tourmate.NearbyApiClass.NearbyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyPlacesApiService {
    @GET
    Call<NearbyResponse> getNearby(@Url String url);
}
