package com.gap.mobigpk1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.ChatModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    List<ChatModel> list;
    String username;

    boolean status;
    int send;
    int recieve;

    public ChatAdapter(List<ChatModel> list, String username) {
        this.list = list;
        this.username = username;

        status = false;
        send = 1;
        recieve = 2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if(viewType == send){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_send, parent, false);
        }
        else if (viewType == recieve){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recieve, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (status){
                textView = itemView.findViewById(R.id.tvSend);
            }
            else {
                textView = itemView.findViewById(R.id.tvRecieve);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getFrom().equals(username)){
            status = true;
            return send;
        }
        else {
            status = false;
            return recieve;
        }
    }

}
