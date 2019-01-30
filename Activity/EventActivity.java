package com.example.nabab.tourmate.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabab.tourmate.Adapter.EventAdapter;
import com.example.nabab.tourmate.Class.FirebaseDatabaseReference;
import com.example.nabab.tourmate.PojoClass.EventPojo;
import com.example.nabab.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {
    private EditText travelDestinationEt, travelEstimatedBudgetEt, travelStartingDateEt, travelEndingDateEt;
    private TextView createEventTv;
    private Button createEventBtn;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private List<EventPojo> eventPojoList;
    private EventAdapter eventAdapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        createEventTv = findViewById(R.id.createEventTvId);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.showEventListRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventActivity.this));
        eventPojoList = new ArrayList<>();

        loadEventListFromDatabase();

        eventAdapter = new EventAdapter(EventActivity.this, eventPojoList);
        recyclerView.setAdapter(eventAdapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EventActivity.this);
                View anotherView = getLayoutInflater().inflate(R.layout.bottom_sheet_event, null);

                bottomSheetDialog.setContentView(anotherView);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) anotherView.getParent());
//                bottomSheetBehavior.getPeekHeight(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  100, getResources().getDisplayMetrics()));
                bottomSheetDialog.show();

                travelDestinationEt = anotherView.findViewById(R.id.travelDestinationEtId);
                travelEstimatedBudgetEt = anotherView.findViewById(R.id.travelEstimatedBudgetEtId);
                travelStartingDateEt = anotherView.findViewById(R.id.travelStartingDateEtId);
                travelEndingDateEt = anotherView.findViewById(R.id.travelEndingDateEtId);
                createEventBtn = anotherView.findViewById(R.id.createEventBtnId);

                createEventBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String travelDestination = travelDestinationEt.getText().toString();
                        String travelEstimatedBudget = travelEstimatedBudgetEt.getText().toString();
                        String travelStartingDate = travelStartingDateEt.getText().toString();
                        String travelEndingDate = travelEndingDateEt.getText().toString();

                        if (TextUtils.isEmpty(travelDestination)) {
                            travelDestinationEt.setError("Enter your travel destination");
                            travelDestinationEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(travelEstimatedBudget)) {
                            travelEstimatedBudgetEt.setError("Enter your estimated budget");
                            travelEstimatedBudgetEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(travelStartingDate)) {
                            travelStartingDateEt.setError("Enter your travel starting date");
                            travelStartingDateEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(travelEndingDate)) {
                            travelEndingDateEt.setError("Enter your travel ending date");
                            travelEndingDateEt.requestFocus();
                            return;

                        } else {
                            userId = firebaseAuth.getCurrentUser().getUid();

                            final ProgressDialog progressDialog = new ProgressDialog(EventActivity.this);
                            progressDialog.setMessage("Please waiting...");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(false);

                            FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Events");
                            String eventId = FirebaseDatabaseReference.userDatabaseReference.push().getKey();

                            EventPojo eventPojo = new EventPojo(travelDestination, travelEstimatedBudget, travelStartingDate, travelEndingDate, eventId);

                            FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Events").child(eventId).setValue(eventPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();

                                        travelDestinationEt.setText("");
                                        travelStartingDateEt.setText("");
                                        travelEndingDateEt.setText("");
                                        travelEstimatedBudgetEt.setText("");

                                        Toast.makeText(EventActivity.this, "Thank you", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(EventActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    private void loadEventListFromDatabase() {
        FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    EventPojo eventPojo = dataSnapshot1.getValue(EventPojo.class);
                    eventPojoList.add(eventPojo);
                    eventAdapter.notifyDataSetChanged();
                    createEventTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            startActivity(new Intent(EventActivity.this, MainActivity.class));
        }
        return true;
    }
}
