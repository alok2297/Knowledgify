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

import com.gap.mobigpk1.Model.Note;

import java.util.ArrayList;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesListAdapterViewHolder> {
    private NotesList notesList;
    private ArrayList<Note> list;

    public NotesListAdapter(NotesList notesList, ArrayList<Note> list) {
        this.notesList = notesList;
        this.list = list;
    }

    @NonNull
    @Override
    public NotesListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_playlist , parent, false);
        return new NotesListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.NotesListAdapterViewHolder holder, int position) {
        Note current=list.get(holder.getAdapterPosition());
        holder.title.setText(current.getTitle().substring(1+current.getTitle().indexOf(' ')));
        holder.sno.setText(current.getTitle().substring(0,current.getTitle().indexOf(' ')));

//        holder.title.setText(current.getTitle());
        holder.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity) v.getContext();
                Intent i=new Intent(activity,WebViewNV.class);
                i.putExtra("url",current.getLink());
                activity.startActivity(i);
            }
        });
        holder.length.setText(current.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotesListAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView length,sno;
        private TextView player;
        private ConstraintLayout videoBtn;
        private TextView title;

        public NotesListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            videoBtn=itemView.findViewById(R.id.videoBtn);
            sno=itemView.findViewById(R.id.sno);
//            player=itemView.findViewById(R.id.player);
            length=itemView.findViewById(R.id.length);

        }
    }
}
