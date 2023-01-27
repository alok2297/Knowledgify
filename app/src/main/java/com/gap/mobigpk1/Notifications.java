package com.gap.mobigpk1;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gap.mobigpk1.Model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notifications extends Fragment {


    private RecyclerView notifyRecycler;
    private FirebaseUser user;
    private DatabaseReference notificationReference;
    private ArrayList<Notification> list;
    private NotificationFragAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swiperefresh;
    private TextView txtNot;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_notifications, container, false);
        progressBar = v.findViewById(R.id.progressBar);
        txtNot = v.findViewById(R.id.tv);
        notifyRecycler=v.findViewById(R.id.notifyRecyclerView);
        swiperefresh=v.findViewById(R.id.swiperefresh);

        user = FirebaseAuth.getInstance().getCurrentUser();
        notificationReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Notifications");

        notifyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        notifyRecycler.setHasFixedSize(true);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setNotification();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        setNotification();

        return v;
    }


    private void setNotification() {
        notificationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Notification data=snapshot.getValue(Notification.class);
                    list.add(0,data);
                }
                if(list.size()!=0)
                    txtNot.setVisibility(View.INVISIBLE);

                adapter =  new NotificationFragAdapter(getContext(), list,notificationReference);
//                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                notifyRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}