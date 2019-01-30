package com.example.nabab.tourmate.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nabab.tourmate.Activity.EventActivity;
import com.example.nabab.tourmate.Activity.NearbyPlacesActivity;
import com.example.nabab.tourmate.Activity.ShowWeatherReportActivity;
import com.example.nabab.tourmate.Class.FirebaseDatabaseReference;
import com.example.nabab.tourmate.PojoClass.EventPojo;
import com.example.nabab.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private CardView eventShowDashboardCardView, nearbyPlacesShowDashboardCardView, weatherShowDashboardCardView;
    private TextView eventShowDashboardTv;
    private String userId;
    private List<EventPojo> eventPojoList;
    private FirebaseAuth firebaseAuth;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        eventShowDashboardTv = view.findViewById(R.id.eventShowDashboardTvId);

        eventShowDashboardCardView = view.findViewById(R.id.eventShowDashboardCardViewId);
        nearbyPlacesShowDashboardCardView = view.findViewById(R.id.nearbyPlacesShowDashboardCardViewId);
        weatherShowDashboardCardView = view.findViewById(R.id.weatherShowDashboardCardViewId);

        weatherShowDashboardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShowWeatherReportActivity.class));
            }
        });

        nearbyPlacesShowDashboardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NearbyPlacesActivity.class));
            }
        });

        eventShowDashboardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EventActivity.class));
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        eventPojoList = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    EventPojo eventPojo = dataSnapshot1.getValue(EventPojo.class);
                    eventPojoList.add(eventPojo);
                    eventPojoList.size();
                    }
                    progressDialog.dismiss();
                    eventShowDashboardTv.setText(String.valueOf(eventPojoList.size()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        return view;
    }

}
