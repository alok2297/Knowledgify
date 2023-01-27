package com.gap.mobigpk1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.gap.mobigpk1.Model.Comments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Comment extends AppCompatActivity {

    private ImageView attachment;
    private EditText comment;
    private DatabaseReference reference;
    private String postkey,commentPhotoUri="";
    public static final String FILENAME = "com.gap.mobigpk1.User_Details";
    private ArrayList<Comments> list;
    private CommentFragAdapter adapter;
    private RecyclerView commentRecycler;
    private ProgressBar progressBar;
    private String key;
    private String pic;
    private ImageView Profile;
    private SwipeRefreshLayout swiperefresh;
    private DatabaseReference postReference;

    private DatabaseReference userReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String token;
    private String userId;
    private DatabaseReference postuserReference;
    private String keyNotify;
    private TextView commentVis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        postkey = intent.getStringExtra("key");
        userId=intent.getStringExtra("userId");

        postuserReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        keyNotify=postuserReference.child("Notifications").push().getKey();
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        postReference=FirebaseDatabase.getInstance().getReference().child("Posts").child(postkey);
        reference= FirebaseDatabase.getInstance().getReference().child("Comments").child(postkey);
        key = reference.push().getKey();

//////////////////////////////////////////////////////////
        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild("imgUrl"))
                    pic=snapshot.child("imgUrl").getValue().toString();
                else
                    pic="https://firebasestorage.googleapis.com/v0/b/gurukul-9d4c9.appspot.com/o/ic_launcher.png?alt=media&token=3e713402-b25f-4f29-ac75-53de733fbf41";

                userId=snapshot.child("userId").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(Comment.this).load(snapshot.child("profilePic").getValue().toString()).into(Profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postuserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                token=snapshot.child("token").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        commentRecycler=findViewById(R.id.commentRecyclerView);
        comment=findViewById(R.id.etMessage);
        attachment=findViewById(R.id.attach);
        Profile=findViewById(R.id.profile);
        ImageView postComment = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressBar);
        swiperefresh=findViewById(R.id.swiperefresh);
        commentVis=findViewById(R.id.textView4);








        commentRecycler.setLayoutManager(new LinearLayoutManager(Comment.this));
        commentRecycler.setHasFixedSize(true);
        getComment();

        SharedPreferences sharedPreferences = getSharedPreferences(FILENAME, MODE_PRIVATE);
        String displayName = sharedPreferences.getString("name", null);

        attachment.setOnClickListener(view -> AddPhoto());


        postComment.setOnClickListener(view -> {
            if(!(comment.getText().toString().isEmpty()) ||  !(commentPhotoUri.isEmpty())){

                postReference.child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.hasChild(user.getUid()))
                            snapshot.child(user.getUid()).getRef().setValue("0");
                        else
                            snapshot.child(user.getUid()).getRef().setValue(Integer.parseInt(snapshot.child(user.getUid()).getValue().toString())+1 +"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                reference.child(key).child("name").setValue(user.getUid());
                reference.child(key).child("college").setValue("collegeName");
                reference.child(key).child("textComment").setValue(comment.getText().toString());
                userReference.child("Comments").child(postkey).setValue("true");

                if(!user.getUid().equals(userId)) {
                    postuserReference.child("Notifications").child(keyNotify).child("commenterName").setValue(displayName);

                    postuserReference.child("Notifications").child(keyNotify).child("commentOnPost").setValue(pic);
                    postuserReference.child("Notifications").child(keyNotify).child("postKey").setValue(postkey);
                    postuserReference.child("Notifications").child(keyNotify).child("notifyKey").setValue(keyNotify);

                }

                if(!(comment.getText().toString().isEmpty())){
                    reference.child(key).child("type").setValue("caption");
                }
                reference.child(key).child("commentKey").setValue(key);

                if(!(commentPhotoUri.isEmpty())) {
                    reference.child(key).child("picComment").setValue(commentPhotoUri);
                    reference.child(key).child("type").setValue("photo");
                }
                getComment();

                Intent i=new Intent(Comment.this,nNotification.class);
                i.putExtra("token",token);

                i.putExtra("body", displayName + " commented on your post");

                i.putExtra("image", pic);

                if(!user.getUid().equals(userId))
                    startActivity(i);

                keyNotify=postuserReference.child("Notifications").push().getKey();
                key = reference.push().getKey();
                commentPhotoUri="";
                comment.setText("");
                comment.clearFocus();
                attachment.setColorFilter(getResources().getColor(R.color.colorBlack));

                //Toast.makeText(Comment.this, commentPhotoUri, Toast.LENGTH_SHORT).show();
            }
            else{
                comment.setError("Empty Field");
            }
        });

        swiperefresh.setOnRefreshListener(() -> {
            getComment();
            Handler handler = new Handler();
            handler.postDelayed(() -> swiperefresh.setRefreshing(false), 1000);
        });
        getComment();


    }

    private void getComment() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comments dataU = snapshot.getValue(Comments.class);
                    list.add(0,dataU);
                }
                if(list.size()==0)
                    commentVis.setVisibility(View.VISIBLE);

                postReference.child("commentCount").setValue(list.size()+"");


                adapter=  new CommentFragAdapter(Comment.this, list,postkey);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                commentRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AddPhoto() {
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Uri uri = data.getData();
                Intent i=new Intent(this,Photo.class);
                i.putExtra("uriPhoto", uri.toString());
                i.putExtra("key", postkey);
                attachment.setColorFilter(getResources().getColor(R.color.colorGreen));
                startActivityForResult(i, 8);
                //startActivity(i);
            }
            if (requestCode == 8) {
                commentPhotoUri = data.getStringExtra("commentPhotoUri");
//                Toast.makeText(this, commentPhotoUri+"", Toast.LENGTH_SHORT).show();
            }
        }





    }

}