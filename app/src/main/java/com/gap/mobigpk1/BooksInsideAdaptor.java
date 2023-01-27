package com.gap.mobigpk1;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.Video;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class BooksInsideAdaptor extends RecyclerView.Adapter<BooksInsideAdaptor.BooksInsideAdapterViewHolder> {

    private final ArrayList<Video> list;
    private final DatabaseReference videosReference;

    public BooksInsideAdaptor(ArrayList<Video> list, DatabaseReference videosReference) {
        this.list = list;
        this.videosReference = videosReference;
    }


    @NonNull
    @Override
    public BooksInsideAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sv_video_lec_list , parent, false);
        return new BooksInsideAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksInsideAdaptor.BooksInsideAdapterViewHolder holder, int position) {
        Video currentItem=list.get(holder.getAdapterPosition());

        holder.title.setText(currentItem.getTitle().substring(1+currentItem.getTitle().indexOf(' ')));
        holder.content.setText(currentItem.getContent());
        holder.sno.setText(currentItem.getTitle().substring(0,currentItem.getTitle().indexOf(' ')));

//        Picasso.get().load(currentItem.getIcon()).into(holder.icon);
        holder.length.setText(currentItem.getLength());

        holder.videoBtn.setOnClickListener(view -> {
            AppCompatActivity activity=(AppCompatActivity) view.getContext();
            Intent intent=new Intent(activity,VideoLectures.class);
            intent.putExtra("vTopic",currentItem.getTitle());
            intent.putExtra("reference", (videosReference +"/"+(holder.getAdapterPosition()+1)).substring(10));
            intent.putExtra("mainLink",currentItem.getLink());
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BooksInsideAdapterViewHolder extends RecyclerView.ViewHolder{

        private final ConstraintLayout videoBtn;
        private final TextView length;
        private final TextView content;
        private final TextView title;
        private final TextView sno;


        public BooksInsideAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            sno=itemView.findViewById(R.id.sno);
            title=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
            length=itemView.findViewById(R.id.length);
            videoBtn=itemView.findViewById(R.id.videoBtn);

        }
    }
}
