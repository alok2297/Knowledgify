package com.gap.mobigpk1;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    ProfilePosts profileFragment=new ProfilePosts();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference;
    private TextView myActivity,myPosts;
    private TextView name;
    private View show1,show;
    private SharedPreferences sharedPreferences;
    private TextView college,profession;
    private ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_me);

        name=findViewById(R.id.name);
        ivProfileImage=findViewById(R.id.ivProfileImage);
        college=findViewById(R.id.college);
        profession=findViewById(R.id.profession);
        myPosts=findViewById(R.id.myPosts);
        myActivity=findViewById(R.id.myActivity);
        show1=findViewById(R.id.show1);
        show=findViewById(R.id.show);
        String userId=getIntent().getStringExtra("userId");

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);




        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue().toString());
                college.setText(snapshot.child("college").getValue().toString());
                profession.setText(snapshot.child("profession").getValue().toString());
                Glide.with(Profile.this).load(snapshot.child("profilePic").getValue().toString()).into(ivProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Bundle bundle = new Bundle();
        bundle.putString("target", "myPosts");
        bundle.putString("userId", userId);
        profileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.postPersonal,profileFragment).commit();
        show1.setBackgroundColor(Color.WHITE);


        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFragment = new ProfilePosts();
                bundle.putString("target", "myPosts");
                profileFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.postPersonal, profileFragment).commit();
                show1.setBackgroundColor(Color.WHITE);
                show.setBackgroundColor(Color.BLACK);
            }

        });

        myActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!user.getUid().equals(userId)){
                    Toast.makeText(Profile.this, "Sorry!, You can't see others' activity.", Toast.LENGTH_SHORT).show();
                }
                else {
                    bundle.putString("target", "myActivity");
                    profileFragment = new ProfilePosts();
                    profileFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.postPersonal, profileFragment).commit();
                    show.setBackgroundColor(Color.WHITE);
                    show1.setBackgroundColor(Color.BLACK);
                }

            }
        });

    }
}