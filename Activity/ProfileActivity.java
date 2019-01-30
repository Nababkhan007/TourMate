package com.example.nabab.tourmate.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabab.tourmate.Class.FirebaseDatabaseReference;
import com.example.nabab.tourmate.PojoClass.UsersPojo;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private EditText profileUserFirstNameEt, profileUserLastNameEt, profileUserPhoneNumberEt, profileUserOccupationEt, profileUserDateOfBirthEt, profileUserAddressEt;
    private ImageView profileProfileIv;
    private Button profileUserInfoSaveBtn;
    private TextView userProfileFullNameTv, userProfilePhoneNumberTv, userProfileOccupationTv, userProfileDateOfBirthTv, userProfileAddressTv;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private static final int REQUEST_CODE_FOR_GALLERY = 1;
    private String downLoadImageUrlLink;
    private UsersPojo usersPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userProfileFullNameTv = findViewById(R.id.userProfileFullNameTvId);
        userProfilePhoneNumberTv = findViewById(R.id.userProfilePhoneNumberTvId);
        userProfileOccupationTv = findViewById(R.id.userProfileOccupationTvId);
        userProfileDateOfBirthTv = findViewById(R.id.userProfileDateOfBirthTvId);
        userProfileAddressTv = findViewById(R.id.userProfileAddressTvId);
        imageView = findViewById(R.id.userProfileIvId);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        loadProfileDataFromDatabase();


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProfileActivity.this);
                final View anotherView = getLayoutInflater().inflate(R.layout.bottom_sheet_profile, null);
                bottomSheetDialog.setContentView(anotherView);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) anotherView.getParent());
//                bottomSheetBehavior.getPeekHeight(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  100, getResources().getDisplayMetrics()));
                bottomSheetDialog.show();

                profileUserFirstNameEt = anotherView.findViewById(R.id.profileUserFirstNameEtId);
                profileUserLastNameEt = anotherView.findViewById(R.id.profileUserLastNameEtId);
                profileUserPhoneNumberEt = anotherView.findViewById(R.id.profileUserPhoneNumberEtId);
                profileUserOccupationEt = anotherView.findViewById(R.id.profileUserOccupationEtId);
                profileUserDateOfBirthEt = anotherView.findViewById(R.id.profileUserDateOfBirthEtId);
                profileUserAddressEt = anotherView.findViewById(R.id.profileUserAddressEtId);
                profileUserInfoSaveBtn = anotherView.findViewById(R.id.profileUserInfoSaveBtnId);
                profileProfileIv = anotherView.findViewById(R.id.profileProfileIvId);


                /*profileUserFirstNameEt.setText(usersPojo.getFirstName());*/
                /*profileUserLastNameEt.setText(usersPojo.getLastName());
                profileUserPhoneNumberEt.setText(usersPojo.getPhoneNumber());
                profileUserOccupationEt.setText(usersPojo.getOccupation());
                profileUserDateOfBirthEt.setText(usersPojo.getDateOfBirth());
                profileUserAddressEt.setText(usersPojo.getAddress());
                Picasso.get().load(usersPojo.getDownLoadImageUrlLink()).placeholder(R.drawable.add_profile_image_icon).into(imageView);

*/
                //take a photo from gallery
                profileProfileIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        if (intent.resolveActivity(getPackageManager()) !=null){
                            startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY);
                        }
                    }
                });

                profileUserInfoSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String firstName = profileUserFirstNameEt.getText().toString();
                        String lastName = profileUserLastNameEt.getText().toString();
                        String phoneNumber = profileUserPhoneNumberEt.getText().toString();
                        String occupation = profileUserOccupationEt.getText().toString();
                        String dateOfBirth = profileUserDateOfBirthEt.getText().toString();
                        String address = profileUserAddressEt.getText().toString();

                        if (TextUtils.isEmpty(firstName)) {
                            profileUserFirstNameEt.setError("Enter your first name");
                            profileUserFirstNameEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(lastName)) {
                            profileUserLastNameEt.setError("Enter your last name");
                            profileUserLastNameEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(phoneNumber)) {
                            profileUserPhoneNumberEt.setError("Enter your phone number");
                            profileUserPhoneNumberEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(occupation)) {
                            profileUserOccupationEt.setError("Enter your phone number");
                            profileUserOccupationEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(dateOfBirth)) {
                            profileUserDateOfBirthEt.setError("Enter your phone number");
                            profileUserDateOfBirthEt.requestFocus();
                            return;

                        } else if (TextUtils.isEmpty(address)) {
                            profileUserAddressEt.setError("Enter your phone number");
                            profileUserAddressEt.requestFocus();
                            return;

                        } else if (imageView == null) {
                            Toast.makeText(ProfileActivity.this, "Please upload you profile picture", Toast.LENGTH_SHORT).show();

                        } else {
                            String userId = firebaseAuth.getCurrentUser().getUid();

                            final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                            progressDialog.setMessage("Please waiting...");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(false);

                            usersPojo = new UsersPojo(firstName, lastName, phoneNumber, occupation, dateOfBirth, address, downLoadImageUrlLink);
                            FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Profile").setValue(usersPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();

                                        profileUserFirstNameEt.setText("");
                                        profileUserLastNameEt.setText("");
                                        profileUserPhoneNumberEt.setText("");
                                        profileUserOccupationEt.setText("");
                                        profileUserDateOfBirthEt.setText("");
                                        profileUserAddressEt.setText("");


                                        Toast.makeText(ProfileActivity.this, "Thank you", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void loadProfileDataFromDatabase() {
        FirebaseDatabaseReference.userDatabaseReference.child(userId).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    UsersPojo usersPojo = dataSnapshot.getValue(UsersPojo.class);

                    userProfileFullNameTv.setText(usersPojo.getFirstName()+" "+usersPojo.getLastName());
                    userProfilePhoneNumberTv.setText(usersPojo.getPhoneNumber());
                    userProfileOccupationTv.setText(usersPojo.getOccupation());
                    userProfileDateOfBirthTv.setText(usersPojo.getDateOfBirth());
                    userProfileAddressTv.setText(usersPojo.getAddress());
                    Picasso.get().load(usersPojo.getDownLoadImageUrlLink()).placeholder(R.drawable.add_profile_image_icon).into(imageView);


                } else {
                    Toast.makeText(ProfileActivity.this, "At First Complete Your Profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_GALLERY && resultCode == RESULT_OK){

            final Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                profileProfileIv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            final StorageReference imageFilePath = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(imageUri.getLastPathSegment());
            imageFilePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
        }
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
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        }
        return true;
    }
}

