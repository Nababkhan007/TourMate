package com.example.nabab.tourmate.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nabab.tourmate.Adapter.ViewPagerAdapter;
import com.example.nabab.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventExpensesCaptureMomentFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private String getEventId;
    private String getTravelEstimatedBudget;
    private int getTravelEstimatedBudgetId;

    public EventExpensesCaptureMomentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_expenses_capture_moment, container, false);
        tabLayout = view.findViewById(R.id.tabLayoutId);
        viewPager = view.findViewById(R.id.viewPagerId);

        Intent receiveData = getActivity().getIntent();
        getEventId = receiveData.getStringExtra("eventId");
        getTravelEstimatedBudget = receiveData.getStringExtra("travelEstimatedBudget");
        getTravelEstimatedBudgetId = Integer.parseInt(getTravelEstimatedBudget);

        EventExpensesFragment eventExpensesFragment = new EventExpensesFragment(getEventId, getTravelEstimatedBudget);
        EventCaptureMomentFragment eventCaptureMomentFragment = new EventCaptureMomentFragment(getEventId);

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(new EventExpensesFragment(getEventId, getTravelEstimatedBudget), "Travel Expenses");
        viewPagerAdapter.addFragment(new EventCaptureMomentFragment(getTravelEstimatedBudget), "Travel Moment");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
