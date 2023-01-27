package com.gap.mobigpk1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.PollOptions;

import java.util.ArrayList;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.ViewHolder> {
    ArrayList<PollOptions> pollOptions;
    Context context;

    public PollAdapter(ArrayList<PollOptions> pollOptions, Context context) {
        this.pollOptions = pollOptions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvPollOptions.setText(pollOptions.get(position).getOptions());
        holder.result.setVisibility(View.INVISIBLE);
        holder.tvPollOptions.setTextColor(Color.parseColor("#535353"));
    }

    @Override
    public int getItemCount() {
        return pollOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView result;
        TextView tvPollOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPollOptions = itemView.findViewById(R.id.btOptions);
            result=itemView.findViewById(R.id.result);
        }
    }
}
