package com.gap.mobigpk1;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gap.mobigpk1.Model.firebase_model_all;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;

public class firebaseAdapter extends FirebaseRecyclerAdapter<firebase_model_all, firebaseAdapter.firebaseViewHolder> {

    public firebaseAdapter(@NonNull FirebaseRecyclerOptions<firebase_model_all> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull firebaseViewHolder firebase_model_allViewHolder, int i, @NonNull  firebase_model_all firebase_model_all) {
        firebase_model_allViewHolder.categoryName.setText(firebase_model_all.getTitle());

        firebase_model_allViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity) v.getContext();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(firebase_model_all.getLink()));

                Intent intent=new Intent(activity,firebaseview.class);
                intent.putExtra("title",firebase_model_all.getLink());
                activity.startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public firebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.firebase_all_btn, parent, false);
        return new firebaseViewHolder(v1);
    }

    public static class firebaseViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;
        public RecyclerView category_recyclerView;
        public RecyclerView.LayoutManager manager;
        RelativeLayout relativeLayout;

        public firebaseViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relative);
            manager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            categoryName = itemView.findViewById(R.id.category_name);
            category_recyclerView = itemView.findViewById(R.id.recyclerView);
            category_recyclerView.setLayoutManager(manager);
        }
    }
}