package com.gap.mobigpk1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Photo extends AppCompatActivity {

    private ImageView ivPostImage;
    private FloatingActionButton ok;
    private Bitmap bmp;
    private StorageReference commentPhotoStorageReference;
    private DatabaseReference reference;
    private String key;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ivPostImage=(ImageView)findViewById(R.id.ivPostImage);
        ok=findViewById(R.id.ok);


        commentPhotoStorageReference= FirebaseStorage.getInstance().getReference().child("Comments");
        reference= FirebaseDatabase.getInstance().getReference().child("Comment").push();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        String uri = intent.getStringExtra("uriPhoto");
        key = intent.getStringExtra("key");
        getSupportActionBar().hide();

        if(getIntent().hasExtra("uriPhoto")) {
            ivPostImage.setImageURI(Uri.parse(uri));
        }
        key=reference.push().getKey();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] byteArray = stream.toByteArray();
                    commentPhotoStorageReference.child(key).putBytes(byteArray).addOnCompleteListener(task -> {
                        commentPhotoStorageReference.child(key).getDownloadUrl().addOnSuccessListener(uri -> {

                            setResult(Activity.RESULT_OK, new Intent().putExtra("commentPhotoUri", uri.toString()));
                            progressDialog.dismiss();
                            finish();
                        });
                    }).addOnFailureListener(e -> Toast.makeText(Photo.this, e.toString(), Toast.LENGTH_SHORT).show());

                } catch (IOException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        });



    }
}