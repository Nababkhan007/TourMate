package com.example.nabab.tourmate.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabab.tourmate.Activity.EventActivity;
import com.example.nabab.tourmate.Adapter.EventAdapter;
import com.example.nabab.tourmate.Adapter.EventExpensesAdapter;
import com.example.nabab.tourmate.Class.FirebaseDatabaseReference;
import com.example.nabab.tourmate.PojoClass.EventExpensesPojo;
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

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EventExpensesFragment extends Fragment {
    private String eventId;
    private String travelEstimatedBudget;
    private EditText travelExpensesDetailsEt, travelExpensesAmountEt;
    private TextView addEventExpensesTv;
    private RecyclerView recyclerView;
    private List<EventExpensesPojo> eventExpensesPojoList;
    private EventExpensesAdapter eventExpensesAdapter;
    private Button saveExpensestBtn;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String getEventId;



    @SuppressLint("ValidFragment")
    public EventExpensesFragment(String eventId, String travelEstimatedBudget) {
        this.eventId = eventId;
        this.travelEstimatedBudget = travelEstimatedBudget;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_expenses, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        Intent receiveData = getActivity().getIntent();
        getEventId = receiveData.getStringExtra("eventId");

        addEventExpensesTv = view.findViewById(R.id.addEventExpensesTvId);

        recyclerView = view.findViewById(R.id.showEventExpensesRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventExpensesPojoList = new ArrayList<>();

        loadEventListFromDatabase();

        eventExpensesAdapter = new EventExpensesAdapter(getActivity(), eventExpensesPojoList);
        recyclerView.setAdapter(eventExpensesAdapter);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                final View anotherView = getLayoutInflater().inflate(R.layout.bottom_sheet_event_expenses, null);
                bottomSheetDialog.setContentView(anotherView);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) anotherView.getParent());
//                bottomSheetBehavior.getPeekHeight(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  100, getResources().getDisplayMetrics()));
                bottomSheetDialog.show();

                travelExpensesDetailsEt = anotherView.findViewById(R.id.travelExpensesDetailsEtId);
                travelExpensesAmountEt = anotherView.findViewById(R.id.travelExpensesAmountEtId);
                saveExpensestBtn = anotherView.findViewById(R.id.saveExpensestBtnId);

                saveExpensestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String travelExpensesDetails = travelExpensesDetailsEt.getText().toString();
                        String travelExpensesAmount = travelExpensesAmountEt.getText().toString();

                        if (TextUtils.isEmpty(travelExpensesDetails)) {
                            travelExpensesDetailsEt.setError("Enter your travel destination");
                            travelExpensesDetailsEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(travelExpensesAmount)) {
                            travelExpensesAmountEt.setError("Enter your estimated budget");
                            travelExpensesAmountEt.requestFocus();
                            return;

                        } else {
                            userId = firebaseAuth.getCurrentUser().getUid();

                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage("Please waiting...");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(false);

                            EventExpensesPojo eventExpensesPojo = new EventExpensesPojo(travelExpensesDetails, travelExpensesAmount);

                            FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Events").child(getEventId).child("Expenses").push().setValue(eventExpensesPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();

                                        travelExpensesDetailsEt.setText("");
                                        travelExpensesAmountEt.setText("");

                                        Toast.makeText(getActivity(), "Thank you", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        return view;
    }

    private void loadEventListFromDatabase() {
        FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Events").child(getEventId).child("Expenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    EventExpensesPojo eventExpensesPojo = dataSnapshot1.getValue(EventExpensesPojo.class);
                    eventExpensesPojoList.add(eventExpensesPojo);
                    eventExpensesAdapter.notifyDataSetChanged();
                    addEventExpensesTv.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), "At First Added a Event", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
