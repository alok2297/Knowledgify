package com.gap.mobigpk1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    public static final String FILENAME = "com.gap.mobigpk1.User_Details";

    ImageView ivProfileImage;
    EditText etName, etCollege, etMobile;
    Spinner spProfession;
    Button btSubmit;
    TextView tvError;
    SharedPreferences sharedPreferences;

    Bitmap bmp;

    Uri uri;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference = storage.getReference("user_profiles").child(user.getUid());

    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
    DatabaseReference referenceChat = FirebaseDatabase.getInstance().getReference("Chats");
    private ProgressDialog progressDialog;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ivProfileImage = findViewById(R.id.ivProfileImage);
        etCollege = findViewById(R.id.etCollege);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        spProfession = findViewById(R.id.spProfession);
        btSubmit = findViewById(R.id.btSubmit);
        tvError = findViewById(R.id.tvError);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Setting Up Profile...");
        progressDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences(FILENAME, MODE_PRIVATE);


        referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(user.getUid())){
                    String name1 = snapshot.child(user.getUid()).child("name").getValue().toString();
                    String college = snapshot.child(user.getUid()).child("college").getValue().toString();
                    String mobile1 = snapshot.child(user.getUid()).child("mobile").getValue().toString();
                    etName.setText(name1);
                    etCollege.setText(college);
                    etMobile.setText(mobile1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Token for FCM
        FirebaseMessaging.getInstance().subscribeToTopic("Alert");
        FirebaseMessaging.getInstance().subscribeToTopic("Notification");


        ivProfileImage.setOnClickListener(view -> {
            if(ContextCompat
                    .checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        btSubmit.setOnClickListener(view -> {
            etName.clearFocus();
            etCollege.clearFocus();
            etMobile.clearFocus();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            progressDialog.show();
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful()) {
                                String token = task.getResult().getToken();
                                referenceProfile.child(user.getUid()).child("token").setValue(token);
                            } else {

                            }
                        }
                    });
            if(!etName.getText().toString().isEmpty()){
                editor.putString("name", etName.getText().toString().trim());
                referenceProfile.child(user.getUid()).child("name").setValue(etName.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                            referenceChat.child(user.getUid()).child("username").setValue(etName.getText().toString().trim());
                            if (!etCollege.getText().toString().isEmpty()) {
                                editor.putString("college", etCollege.getText().toString().trim());
                                referenceProfile.child(user.getUid()).child("college").setValue(etCollege.getText().toString().trim())
                                        .addOnCompleteListener(task1 -> {
                                            referenceChat.child(user.getUid()).child("college").setValue(etCollege.getText().toString().trim());
                                            if(!etMobile.getText().toString().isEmpty() && etMobile.getText().toString().length() == 10){
                                                editor.putString("mobile", etMobile.getText().toString().trim());
                                                referenceChat.child(user.getUid()).child("mobile").setValue(etMobile.getText().toString().trim());
                                                referenceProfile.child(user.getUid()).child("mobile").setValue(etMobile.getText().toString().trim())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (!spProfession.getSelectedItem().equals("Profession")) {
                                                                    referenceChat.child(user.getUid()).child("profession").setValue(spProfession.getSelectedItem().toString());
                                                                    editor.putString("profession", spProfession.getSelectedItem().toString());
                                                                    referenceProfile.child(user.getUid()).child("profession").setValue(spProfession.getSelectedItem().toString())
                                                                            .addOnCompleteListener(task2 -> {
                                                                                progressDialog.show();
                                                                                editor.putBoolean("profileCompleted", true);
                                                                                editor.apply();
                                                                                if(bmp==null)
                                                                                    defaultPhoto();
                                                                                uploadPhoto();

                                                                            });
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    tvError.setVisibility(View.VISIBLE);
                                                                }
                                                            }
                                                        });
                                            }
                                            else {
                                                progressDialog.dismiss();
                                                etMobile.requestFocus();
                                                etMobile.setError("Enter valid mobile!");
                                            }
                                        });
                            }
                            else {
                                progressDialog.dismiss();
                                etCollege.requestFocus();
                                etCollege.setError("Enter your college!");
                            }
                        });
            }
            else {
                progressDialog.dismiss();
                etName.requestFocus();
                etName.setError("Enter your name!");
            }


        });



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.profession, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProfession.setAdapter(adapter);

    }

    private void defaultPhoto() {
        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile);

        ContentResolver contentResolver = getContentResolver();
        try {
            if(Build.VERSION.SDK_INT < 28) {
                bmp = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                bmp = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length >0){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            if(Build.VERSION.SDK_INT >= 28){
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), data.getData());
                try {
                    bmp = ImageDecoder.decodeBitmap(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ivProfileImage.setImageBitmap(bmp);
            uri = data.getData();
        }
    }

    void uploadPhoto(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        final UploadTask uploadTask=reference.child("profile_image").putBytes(byteArray);
        uploadTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> reference.child("profile_image").getDownloadUrl().addOnSuccessListener(uri1 -> {
                    referenceProfile.child(user.getUid()).child("profilePic").setValue(uri1.toString())
                            .addOnSuccessListener(unused -> {
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                            }).addOnFailureListener(e ->Toast.makeText(ProfileActivity.this, "Sorry! There is a problem.", Toast.LENGTH_SHORT).show());
                }
                ));
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Sorry! There is a problem.", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean profileCompleted = sharedPreferences.getBoolean("profileCompleted", false);

        boolean fromLogin = getIntent().getBooleanExtra("fromLogin", false);

        if(profileCompleted && fromLogin){
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}