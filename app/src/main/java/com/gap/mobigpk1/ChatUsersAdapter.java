package com.gap.mobigpk1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.ViewHolder> {

    List<String> userList;
    String username, uid;
    Context context;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public ChatUsersAdapter(List<String> userList, String username, String uid, Context context) {
        this.userList = userList;
        this.username = username;
        this.context = context;
        this.uid = uid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sv_chat_btn, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        reference.child("Chats").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String othername = snapshot.child("username").getValue().toString();

                holder.tvUsername.setText(othername);

                holder.ivUserImage.setImageResource(R.drawable.ic_person);

                holder.cardView.setOnClickListener(view -> {
                    int pos = holder.getAdapterPosition();
                    Intent intent = new Intent(context, DM.class);
                    intent.putExtra("username", username);
                    intent.putExtra("othername", othername);
                    intent.putExtra("uid", uid);
                    intent.putExtra("ouid", userList.get(pos));
                    context.startActivity(intent);
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvUsername;
        CircleImageView ivUserImage;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}