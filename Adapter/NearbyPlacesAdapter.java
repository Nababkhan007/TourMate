package com.example.nabab.tourmate.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nabab.tourmate.NearbyApiClass.Result;
import com.example.nabab.tourmate.R;

import java.util.List;

public class NearbyPlacesAdapter extends RecyclerView.Adapter<NearbyPlacesAdapter.NearbyPlacesViewHolder> {
    List<Result> resultList;

    public NearbyPlacesAdapter(List<Result> resultList) {
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public NearbyPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_nearby_searches_design, viewGroup, false);
        return new NearbyPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyPlacesViewHolder nearbyPlacesViewHolder, int i) {
        nearbyPlacesViewHolder.showNearbySearchesAreaNameTv.setText(resultList.get(i).getName());
        nearbyPlacesViewHolder.showNearbySearchesAreaRateTv.setText(String.valueOf(resultList.get(i).getRating()));
        try{
             boolean value = resultList.get(i).getOpeningHours().getOpenNow();
             if(!value){
                 nearbyPlacesViewHolder.showNearbySearchesAreaOpenHoursTv.setText("Close");
                 }else{
                 nearbyPlacesViewHolder.showNearbySearchesAreaOpenHoursTv.setText("Open");
             }

        }catch (Exception e){

        }
        nearbyPlacesViewHolder.showNearbySearchesAreaAddressTv.setText(resultList.get(i).getVicinity());

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class NearbyPlacesViewHolder extends RecyclerView.ViewHolder{
        private TextView showNearbySearchesAreaNameTv, showNearbySearchesAreaAddressTv, showNearbySearchesAreaRateTv,showNearbySearchesAreaOpenHoursTv;

        public NearbyPlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            showNearbySearchesAreaNameTv = itemView.findViewById(R.id.showNearbySearchesAreaNameTvId);
            showNearbySearchesAreaAddressTv = itemView.findViewById(R.id.showNearbySearchesAreaAddressTVId);
            showNearbySearchesAreaRateTv = itemView.findViewById(R.id.showNearbySearchesAreaRateTvId);
            showNearbySearchesAreaOpenHoursTv = itemView.findViewById(R.id.showNearbySearchesAreaOpenHoursTvId);

        }
    }
}
