package com.gap.mobigpk1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gap.mobigpk1.Model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends FirebaseRecyclerAdapter<Playlist,VideoAdapter.myViewHolder> {

    private OnItemClick mCallback;
    List<ConstraintLayout> constraintLayoutList = new ArrayList<>();

    public VideoAdapter(@NonNull FirebaseRecyclerOptions<Playlist> options, OnItemClick listener) {
        super(options);
        mCallback = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int i, @NonNull Playlist currItemPlaylist) {

        if (!constraintLayoutList.contains(holder.videoBtn)) {
            constraintLayoutList.add(holder.videoBtn);
        }
            
        holder.title.setText(currItemPlaylist.getTitle().substring(1+currItemPlaylist.getTitle().indexOf(' ')));
        holder.sno.setText(currItemPlaylist.getTitle().substring(0,currItemPlaylist.getTitle().indexOf(' ')));
        holder.length.setText(currItemPlaylist.getTime());
        
        holder.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick(currItemPlaylist.getLink(),currItemPlaylist.getTitle());
                for(ConstraintLayout btn : constraintLayoutList){
                    btn.setBackgroundColor(Color.parseColor("#ffffff"));
//                    holder.player.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_24,0, 0, 0);
                }
                //The selected card is set to colorSelected
                holder.videoBtn.setBackgroundColor(Color.parseColor("#FFE6E6E6"));
//                holder.player.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_24,0, 0, 0);

            }
        });



//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppCompatActivity activity=(AppCompatActivity) v.getContext();
//
//                Intent intent=new Intent(activity,video_lectures.class);
//                intent.putExtra("title",currItemPlaylist.getCategoryId());
//
//                Yodo1Mas.getInstance().showInterstitialAd(activity);
//                Yodo1Mas.getInstance().setInterstitialListener(new Yodo1Mas.InterstitialListener() {
//                    @Override
//                    public void onAdOpened(@NonNull Yodo1MasAdEvent event) {
//                    }
//
//                    @Override
//                    public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {
//
//                    }
//
//                    @Override
//                    public void onAdClosed(@NonNull Yodo1MasAdEvent event) {
//                        activity.startActivity(intent);
//                    }
//                });
//
//            }
//        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_playlist,parent,false);
        return new myViewHolder(v1);
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        private final TextView length,sno;
        private TextView player;
        private ConstraintLayout videoBtn;
        private TextView title;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            videoBtn=itemView.findViewById(R.id.videoBtn);
            sno=itemView.findViewById(R.id.sno);
//            player=itemView.findViewById(R.id.player);
            length=itemView.findViewById(R.id.length);

        }
    }
}


