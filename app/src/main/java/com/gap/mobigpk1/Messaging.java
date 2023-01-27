package com.gap.mobigpk1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Messaging extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    String userName, profession;
    List<String> users;
    ChatUsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        String name = getIntent().getStringExtra("name");
        recyclerView = findViewById(R.id.userRecyclerView);

        if(name != null) {
            reference.child("Chats").child(user.getUid()).child("username").setValue(name);
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(Messaging.this));

        reference.child("users").child(user.getUid()).child("profession").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profession = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.child("Chats").child(user.getUid()).child("username")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users = new ArrayList<>();
                        userName = snapshot.getValue().toString();
                        getTeachers();
                        adapter = new ChatUsersAdapter(users, userName, user.getUid(), Messaging.this);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }

    public void getTeachers(){
        reference.child("Chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(!snapshot.getKey().equals(user.getUid()) && profession.equals("Student")){
                    if(snapshot.child("profession").getValue().toString().equals("Teacher")) {
                        users.add(snapshot.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }
                else if(!snapshot.getKey().equals(user.getUid()) && profession.equals("Teacher")){
                    if(snapshot.child("profession").getValue().toString().equals("Student")) {
                        users.add(snapshot.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}