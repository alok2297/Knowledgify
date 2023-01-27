package com.gap.mobigpk1;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gap.mobigpk1.Model.Posts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfilePosts extends Fragment {
    private RecyclerView PostRecycler;
    private ProgressBar progressBar;
    private ArrayList<Posts> list;
    private HomeFragAdapter adapter;
    private DatabaseReference reference;
    private SwipeRefreshLayout swiperefresh;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile_posts, container, false);

        String userId=requireArguments().getString("userId");

        PostRecycler = v.findViewById(R.id.PostRecycler);
        progressBar = v.findViewById(R.id.progressBar);
        swiperefresh = v.findViewById(R.id.swiperefresh);

        reference = FirebaseDatabase.getInstance().getReference().child("Posts");

        PostRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        PostRecycler.setHasFixedSize(true);

//        PostRecycler.setClickable(false);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyPosts(userId);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        if(requireArguments().getString("target").equals("myActivity")){
            getMyActivity(userId);
        }
        else{
            getMyPosts(userId);
        }

        return v;
    }

    private void getMyActivity(String userId) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("delete").getValue().toString().equals("false"))
                        if(snapshot.child("Comments").hasChild(userId))
                            if(!snapshot.child("spamReportsList").hasChild(userId))
                            list.add(new Posts(snapshot.getKey(), null, snapshot.child("name").getValue().toString(), snapshot.child("college").getValue().toString(), snapshot.child("likeCount").getValue().toString(), snapshot.child("commentCount").getValue().toString(), snapshot.child("type").getValue().toString(),snapshot.child("caption").getValue().toString(),"",snapshot.child("userId").getValue().toString(),snapshot.child("spamReports").getValue().toString()));
                }


                adapter =  new HomeFragAdapter(getContext(), list, "");

                progressBar.setVisibility(View.GONE);
                PostRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getMyPosts(String userId) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("delete").getValue().toString().equals("false"))
                        if(snapshot.child("userId").getValue().equals(userId))
                            list.add(new Posts(snapshot.getKey(), null, snapshot.child("name").getValue().toString(), snapshot.child("college").getValue().toString(), snapshot.child("likeCount").getValue().toString(), snapshot.child("commentCount").getValue().toString(), snapshot.child("type").getValue().toString(),snapshot.child("caption").getValue().toString(),"",snapshot.child("userId").getValue().toString(),snapshot.child("spamReports").getValue().toString()));
                }

                adapter =  new HomeFragAdapter(getContext(), list, "myPost");
                progressBar.setVisibility(View.GONE);
                PostRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}