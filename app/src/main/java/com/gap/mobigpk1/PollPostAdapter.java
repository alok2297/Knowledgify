package com.gap.mobigpk1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.Poll;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PollPostAdapter extends RecyclerView.Adapter<PollPostAdapter.ViewHolder>{
    private String option;
    private int totalVotes;
    ArrayList<Poll> poll;
    Context context;


    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public PollPostAdapter(ArrayList<Poll> poll, Context context, String option, int totalVotes) {
        this.poll = poll;
        this.context = context;
        this.option=option;
        this.totalVotes=totalVotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(option.equals("option")){

            holder.seek_bar1.setClickable(false);
            holder.result.setVisibility(View.INVISIBLE);
            holder.btOptions.setOnClickListener(view -> {
                int presentVotes = poll.get(position).getVotes();
                poll.get(position).setVotes(presentVotes+1);
                reference.child("Posts").child(poll.get(position).getPostKey()).child("Options").child("option"+(position+1)).child("totalVotes").setValue(presentVotes+1);
                reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("poll").child(poll.get(position).getPostKey()).setValue(poll.get(position).getPostKey());

                holder.seek_bar1.setProgress(0);
            });
        }
        else{
            holder.result.setVisibility(View.VISIBLE);
            holder.result.setText(poll.get(position).getVotes()+" votes");
            holder.seek_bar1.setProgress(25);
        }

        holder.btOptions.setText(poll.get(position).getOptionDes());


    }

    @Override
    public int getItemCount() {
        return poll.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private SeekBar seek_bar1;
        private TextView result,btOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btOptions = itemView.findViewById(R.id.btOptions);
            result = itemView.findViewById(R.id.result);
            seek_bar1 = itemView.findViewById(R.id.seek_bar1);
        }
    }

}
