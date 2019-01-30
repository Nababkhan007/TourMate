package com.example.nabab.tourmate.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabab.tourmate.Adapter.EventAdapter;
import com.example.nabab.tourmate.Fragment.EventExpensesCaptureMomentFragment;
import com.example.nabab.tourmate.PojoClass.EventPojo;
import com.example.nabab.tourmate.R;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
//    private TextView showTravelDestinationDetailsTv, showTravelStartingDateDetailsTv, showTravelEndingDateDetailsTv, showTravelEstimatedBudgetDetailsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadFragment(new EventExpensesCaptureMomentFragment());

        /*showTravelDestinationDetailsTv = findViewById(R.id.showTravelDestinationDetailsTvId);
        showTravelStartingDateDetailsTv = findViewById(R.id.showTravelStartingDateDetailsTvId);
        showTravelEndingDateDetailsTv = findViewById(R.id.showTravelEndingDateDetailsTvId);
        showTravelEstimatedBudgetDetailsTv = findViewById(R.id.showTravelEstimatedBudgetDetailsTvId);

        if (showTravelDestinationDetailsTv == null){
            Toast.makeText(this, "At First Added a Event", Toast.LENGTH_SHORT).show();

        } else if (showTravelStartingDateDetailsTv == null){
            Toast.makeText(this, "At First Added a Event", Toast.LENGTH_SHORT).show();

        } else if (showTravelEndingDateDetailsTv == null){
            Toast.makeText(this, "At First Added a Event", Toast.LENGTH_SHORT).show();

        } else if (showTravelEstimatedBudgetDetailsTv == null){
            Toast.makeText(this, "At First Added a Event", Toast.LENGTH_SHORT).show();

        } else {
            Intent receiveData = getIntent();
            List<EventPojo> eventPojoList = new ArrayList<>();
            EventAdapter eventAdapter = new EventAdapter(EventDetailsActivity.this, eventPojoList);
            String travelDestination =  receiveData.getStringExtra("TravelDestination");
            String travelStartingDate = receiveData.getStringExtra("TravelStartingDate");
            String travelEndingDate = receiveData.getStringExtra("TravelEndingDate");
            String travelEstimatedBudget = receiveData.getStringExtra("TravelEstimatedBudget");

            showTravelDestinationDetailsTv.setText(travelDestination);
            showTravelStartingDateDetailsTv.setText(travelStartingDate);
            showTravelEndingDateDetailsTv.setText(travelEndingDate);
            showTravelEstimatedBudgetDetailsTv.setText(travelEstimatedBudget);

        }*/

        /*Intent receiveData = getIntent();
        List<EventPojo> eventPojoList = new ArrayList<>();
        EventAdapter eventAdapter = new EventAdapter(EventDetailsActivity.this, eventPojoList);
        String travelDestination =  receiveData.getStringExtra("TravelDestination");
        String travelStartingDate = receiveData.getStringExtra("TravelStartingDate");
        String travelEndingDate = receiveData.getStringExtra("TravelEndingDate");
        String travelEstimatedBudget = receiveData.getStringExtra("TravelEstimatedBudget");

        showTravelDestinationDetailsTv.setText(travelDestination);
        showTravelStartingDateDetailsTv.setText(travelStartingDate);
        showTravelEndingDateDetailsTv.setText(travelEndingDate);
        showTravelEstimatedBudgetDetailsTv.setText(travelEstimatedBudget);*/
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutId, fragment);
        fragmentTransaction.commit();
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
            startActivity(new Intent(EventDetailsActivity.this, MainActivity.class));
        }
        return true;
    }
}
