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
import com.gap.mobigpk1.Model.Update;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends Fragment {

    private RecyclerView PostRecycler,UpdateRecycler;
    private ProgressBar progressBar;
    private ArrayList<Posts> list;
    private ArrayList<Update> listUpdate;
    private HomeFragAdapter adapter;
    private HomeUpdateFragAdapter adapterUpdate;
    private DatabaseReference reference,referenceUpdate;
    private SwipeRefreshLayout swiperefresh;
    private String directed="fromHome";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        PostRecycler = v.findViewById(R.id.PostRecycler);
        UpdateRecycler=v.findViewById(R.id.UpdateRecycler);
        progressBar = v.findViewById(R.id.progressBar);
        swiperefresh = v.findViewById(R.id.swiperefresh);

        reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        referenceUpdate=FirebaseDatabase.getInstance().getReference().child("Updates");

        PostRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        PostRecycler.setHasFixedSize(true);

        UpdateRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        UpdateRecycler.setHasFixedSize(true);

        //adapter.notifyDataSetChanged();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotice();
                getUpdate();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        getNotice();
        getUpdate();
        return v;
    }



    private void getNotice() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    if(snapshot.child("type").getValue().equals("type"))
                    if (snapshot.child("delete").getValue().toString().equals("false") && !snapshot.child("spamReportsList").hasChild(user.getUid()))
                        list.add(0,new Posts(snapshot.getKey(), null, snapshot.child("name").getValue().toString(), snapshot.child("college").getValue().toString(), snapshot.child("likeCount").getValue().toString(), snapshot.child("commentCount").getValue().toString(), snapshot.child("type").getValue().toString(),snapshot.child("caption").getValue().toString(),"",snapshot.child("userId").getValue().toString(),snapshot.child("spamReports").getValue().toString()));
                    //String keyPost, String profile, String name, String college, String likeCount, String keyComment, String share, String postType
                }

                adapter =  new HomeFragAdapter(getContext(), list,directed);
//                adapter.notifyDataSetChanged();
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

    private void getUpdate() {
        referenceUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUpdate = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Update dataU = snapshot.getValue(Update.class);
                    listUpdate.add(0,dataU);
                }

                adapterUpdate =  new HomeUpdateFragAdapter(getContext(), listUpdate);
                adapterUpdate.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                UpdateRecycler.setAdapter(adapterUpdate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}