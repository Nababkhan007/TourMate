package com.example.nabab.tourmate.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabab.tourmate.Adapter.EventCaptureMomentAdapter;
import com.example.nabab.tourmate.Adapter.EventExpensesAdapter;
import com.example.nabab.tourmate.Class.FirebaseDatabaseReference;
import com.example.nabab.tourmate.PojoClass.EventExpensesPojo;
import com.example.nabab.tourmate.PojoClass.EventPojo;
import com.example.nabab.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EventCaptureMomentFragment extends Fragment {
    private TextView addEventCaptureMomentDetailsTv;
    private EditText travelCaptureMomentDetailsEt;
    private ImageView travelCaptureMomentIv;
    private Button saveMomentBtn;
    private static final int REQUEST_CODE_FOR_CAMERA = 2;
    private String downLoadImageUrlLink;
    private EventPojo eventPojo;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String eventId;
    private String getEventId;
    private RecyclerView recyclerView;
    private List<EventPojo> eventPojoList;
    private EventCaptureMomentAdapter eventCaptureMomentAdapter;

    @SuppressLint("ValidFragment")
    public EventCaptureMomentFragment(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_capture_moment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        Intent receiveData = getActivity().getIntent();
        getEventId = receiveData.getStringExtra("eventId");

        addEventCaptureMomentDetailsTv = view.findViewById(R.id.addEventCaptureMomentDetailsTvId);

        recyclerView = view.findViewById(R.id.showEventCaptureMomentDetailsRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventPojoList = new ArrayList<>();

        loadEventCapturMomentDetailsFromDatabase();

        eventCaptureMomentAdapter = new EventCaptureMomentAdapter(getActivity(), eventPojoList);
        recyclerView.setAdapter(eventCaptureMomentAdapter);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                final View anotherView = getLayoutInflater().inflate(R.layout.bottom_sheet_event_capture_moment, null);
                bottomSheetDialog.setContentView(anotherView);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) anotherView.getParent());
//                bottomSheetBehavior.getPeekHeight(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  100, getResources().getDisplayMetrics()));
                bottomSheetDialog.show();

                travelCaptureMomentDetailsEt = anotherView.findViewById(R.id.travelCaptureMomentDetailsEtId);
                travelCaptureMomentIv = anotherView.findViewById(R.id.travelCaptureMomentIvId);
                saveMomentBtn = anotherView.findViewById(R.id.saveMomentBtnId);

                travelCaptureMomentIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                        startActivityForResult(intent, REQUEST_CODE_FOR_CAMERA);
                    }
                });

                saveMomentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String travelCaptureMomentDetails = travelCaptureMomentDetailsEt.getText().toString();

                        if (TextUtils.isEmpty(travelCaptureMomentDetails)) {
                            travelCaptureMomentDetailsEt.setError("Enter your travel capture moment details");
                            travelCaptureMomentDetailsEt.requestFocus();
                            return;

                        } else {
                            userId = firebaseAuth.getCurrentUser().getUid();

                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage("Please waiting...");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(false);

                            eventPojo = new EventPojo(travelCaptureMomentDetails, downLoadImageUrlLink);
                            FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Events").child(getEventId).child("CaptureMoment").push().setValue(eventPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();

                                        travelCaptureMomentDetailsEt.setText("");

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

    private void loadEventCapturMomentDetailsFromDatabase() {
        FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Events").child(getEventId).child("CaptureMoment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        EventPojo eventPojo = dataSnapshot1.getValue(EventPojo.class);
                        eventPojoList.add(eventPojo);
                        eventCaptureMomentAdapter.notifyDataSetChanged();
                        addEventCaptureMomentDetailsTv.setVisibility(View.GONE);
                    }
                }
                else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_CAMERA && resultCode == RESULT_OK){
            /*Uri imageUri = data.getData();*/

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            byte[] uri = byteArrayOutputStream.toByteArray();
            travelCaptureMomentIv.setImageBitmap(bitmap);

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            final StorageReference imageFilePath = FirebaseStorage.getInstance().getReference().child("CaptureMomentImages").child("photos_"+System.currentTimeMillis());
            imageFilePath.putBytes(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downLoadImageUrlLink = uri.toString();
                        }
                    });
                }
            });
                /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                travelCaptureMomentIv.setImageBitmap(bitmap);*/
        }
    }
}
