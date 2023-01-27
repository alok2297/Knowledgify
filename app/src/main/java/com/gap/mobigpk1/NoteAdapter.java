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
import com.gap.mobigpk1.Model.Note;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;

public class NoteAdapter extends FirebaseRecyclerAdapter<Note, NoteAdapter.nViewHolder> {

    public NoteAdapter(@NonNull FirebaseRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull nViewHolder noteViewHolder, int i, @NonNull Note note) {
        noteViewHolder.categoryName.setText(note.getTitle());
        noteViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity) v.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(note.getLink()));
                Yodo1Mas.getInstance().showInterstitialAd(activity);
                Yodo1Mas.getInstance().setInterstitialListener(new Yodo1Mas.InterstitialListener() {
                    @Override
                    public void onAdOpened(@NonNull Yodo1MasAdEvent event) {
                    }

                    @Override
                    public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {

                    }

                    @Override
                    public void onAdClosed(@NonNull Yodo1MasAdEvent event) {
                        activity.startActivity(intent);
                    }
                });



            }
        });
    }

    @NonNull
    @Override
    public nViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_btn_firebase, parent, false);
        return new nViewHolder(v1);
    }

    public static class nViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;
        public RecyclerView category_recyclerView;
        public RecyclerView.LayoutManager manager;
        RelativeLayout relativeLayout;

        public nViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relative);
            manager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            categoryName = itemView.findViewById(R.id.category_name);
            category_recyclerView = itemView.findViewById(R.id.recyclerView);
            category_recyclerView.setLayoutManager(manager);
        }
    }
}