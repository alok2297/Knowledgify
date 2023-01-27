package com.gap.mobigpk1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gap.mobigpk1.Model.Options;
import com.gap.mobigpk1.Model.PollOptions;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class AddPost extends AppCompatActivity {
    private LinearLayout attached;
    private ImageView ivPostImage;
    private TextView limit;
    private TextView imgtxt;
    private EditText caption;

    private DatabaseReference reference, abuseReference;
    private StorageReference storageReference;

    SharedPreferences sharedPreferences;
    public static final String FILENAME = "com.gap.mobigpk1.User_Details";

    private Bitmap bmp;

    private boolean photo, document, pollCreated;
    String key;

    private Uri pdfData;
    private String pdfname;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<PollOptions> pollOptions;
    private ProgressDialog progressDialog;
    StorageReference imgRef;
    private String token="";
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post2);

        TextView post = findViewById(R.id.post);
        ImageView profile = findViewById(R.id.profile);
        TextView name = findViewById(R.id.name);
        TextView college = findViewById(R.id.clg);
        limit=findViewById(R.id.limit);
        caption=findViewById(R.id.caption);
        TextView addPhoto = findViewById(R.id.addPhoto);
        TextView addFile = findViewById(R.id.addDoc);
        TextView createPoll = findViewById(R.id.createPoll);
        ivPostImage = findViewById(R.id.ivPostImage);
        imgtxt = findViewById(R.id.imgtxt);
        attached=findViewById(R.id.attached);
        recyclerView = findViewById(R.id.recyclerView2);
        ImageView ivBack = findViewById(R.id.back);

        abuseReference = FirebaseDatabase.getInstance().getReference();


        progressDialog=new ProgressDialog(AddPost.this);
        progressDialog.setMessage("Uploading Post...");
        progressDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences(FILENAME, MODE_PRIVATE);
        String displayName = sharedPreferences.getString("name", null);
        String collegeName = sharedPreferences.getString("college", null);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                        } else {
                            Toast.makeText(AddPost.this, "Something went wrong!", Toast.LENGTH_SHORT).show();;
                        }
                    }
                });

        if(displayName != null){
            name.setText(displayName);
        }

        if(collegeName != null){
            college.setText(collegeName);
        }

        bmp = null;
        photo = false;
        document = false;

        reference= FirebaseDatabase.getInstance().getReference().child("Posts").push();
        storageReference= FirebaseStorage.getInstance().getReference();
        key = reference.getKey();
        imgRef = FirebaseStorage.getInstance().getReference().child("Posts").child(key);

        ivBack.setOnClickListener(view -> finish());

        String question = getIntent().getStringExtra("question");
        pollCreated = getIntent().getBooleanExtra("pollCreated", false);

        caption.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                limit.setText(charSequence.length()+"");
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addFile.setOnClickListener(view -> {
            attached.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            openFileManager();
        });

        if(pollCreated){
            caption.setText(question);

            String arrayAsString = getIntent().getExtras().getString("options");
            Options[] options = new Gson().fromJson(arrayAsString, Options[].class);

            pollOptions = new ArrayList<>();

            for(Options o : options){
                pollOptions.add(new PollOptions(o.getOptions()));
            }

            myAdapter = new PollAdapter(pollOptions, AddPost.this);
            layoutManager = new LinearLayoutManager(this);

            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        else {
            recyclerView.setVisibility(View.INVISIBLE);
        }

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(AddPost.this).load(snapshot.child("profilePic").getValue().toString()).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addPhoto.setOnClickListener(view -> AddPhoto());

        createPoll.setOnClickListener(view -> {
            photo = false;
            document = false;
            Intent intent = new Intent(AddPost.this, CreatePoll.class);
            startActivity(intent);
            finish();
        });



        post.setOnClickListener(view -> {
            if(!caption.getText().toString().isEmpty()) {
                reference.child("caption").setValue(caption.getText().toString().trim());
                abuseReference.child("Abuse").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds: snapshot.getChildren()) {
                            if(caption.getText().toString().contains(ds.getKey())){
                                reference.child("caption").setValue("****");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                reference.child("delete").setValue("true");
                reference.child("spamReports").setValue("0");
                reference.child("name").setValue(displayName);
                reference.child("likeCount").setValue("0");
                reference.child("commentCount").setValue("0");
                reference.child("college").setValue(collegeName);
                userReference.child("token").setValue(token);

                reference.child("postKey").setValue(key);
                reference.child("userId").setValue(user.getUid());

                userReference.child("Posts").child(key).setValue(key);
                progressDialog.show();

                if (photo) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    long length = bmp.getByteCount();
                    reference.child("type").setValue("photo");

                    if (length/(1024*1024*10) > 10) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "You can select images only less than 10 MB", Toast.LENGTH_SHORT).show();
                    } else {
                        final UploadTask uploadTask = imgRef.putBytes(byteArray);
                        uploadTask.addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                uploadTask.addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    reference.child("imgUrl").setValue(uri.toString())
                                            .addOnSuccessListener(unused -> {
                                                progressDialog.dismiss();
                                                reference.child("delete").setValue("false");
                                                Intent i = new Intent(this, MainActivity.class);
                                                startActivity(i);
                                            }).addOnFailureListener(e -> Toast.makeText(AddPost.this, "Something went wrong", Toast.LENGTH_LONG).show());
                                }));
                            } else {
                                Toast.makeText(AddPost.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else if(document){

                    reference.child("type").setValue("document");
                    final UploadTask uploadTask=storageReference.child("Posts").child(key).putFile(pdfData);

                    uploadTask.addOnCompleteListener(task -> storageReference.child("Posts").child(key).getDownloadUrl().addOnSuccessListener(uri ->
                            reference.child("docUrl").setValue(uri.toString())
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(AddPost.this, "Successfully Uploaded", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    reference.child("delete").setValue("false");
                                    Intent i=new Intent(this,MainActivity.class);
                                    startActivity(i);

                                })
                                .addOnFailureListener(e -> Toast.makeText(AddPost.this, "Something went wrong", Toast.LENGTH_LONG).show())));

                }
                else if(pollCreated){
                    String arrayAsString = getIntent().getExtras().getString("options");
                    Options[] options = new Gson().fromJson(arrayAsString, Options[].class);

                    int count = 1;
                    for(Options o : options){
                        reference.child("Options").child("option"+count).child("description").setValue(o.getOptions());
                        reference.child("Options").child("option"+count).child("totalVotes").setValue(0);
                        count++;
                    }

                    reference.child("Options").child("optionsCount").setValue(String.valueOf(count-1));
                    reference.child("type").setValue("poll");
                    reference.child("delete").setValue("false");
                    Intent intent = new Intent(AddPost.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    reference.child("type").setValue("text");
                    reference.child("delete").setValue("false");
                    finish();
                }
            }
            else {
                caption.setError("Please provide a caption");
                caption.requestFocus();
            }

        });

    }
    @Override
    public void onBackPressed() {
        finish();
    }


    private void AddPhoto() {
        attached.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        photo = true;
        pollCreated = false;
        document = false;

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,2);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 3 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 1);
        }
    }

    private void openFileManager() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Document"),4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==4 && resultCode==RESULT_OK) {
            if(data != null) {
                pdfData = data.getData();
                photo = false;
                document = true;
                pollCreated = false;
            }

            if(pdfData.toString().startsWith("content://")){
                Cursor cursor;
                try {
                    cursor=AddPost.this.getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        pdfname=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            if(pdfData.toString().startsWith("file://")){
                pdfname=new File(pdfData.toString()).getName();
            }
            attached.setVisibility(View.VISIBLE);
            imgtxt.setText(pdfname);

        }

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Objects.requireNonNull(data).getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Glide.with(AddPost.this).load(bmp).into(ivPostImage);

                attached.setVisibility(View.VISIBLE);
                photo = true;
                document = false;
                pollCreated = false;
            }
        }
    }
}