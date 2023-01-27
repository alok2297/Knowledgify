package com.gap.mobigpk1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gap.mobigpk1.Model.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragAdapter extends RecyclerView.Adapter<NotificationFragAdapter.NotificationFragViewHolder> {

    private Context context;
    private ArrayList<Notification> list;
    private DatabaseReference notificationReference;
    private DatabaseReference postReference= FirebaseDatabase.getInstance().getReference().child("Posts");

    private String key;
    private String userId;

    public NotificationFragAdapter(Context context, ArrayList<Notification> list, DatabaseReference notificationReference) {
        this.context = context;
        this.list = list;
        this.notificationReference = notificationReference;
    }

    @NonNull
    @Override
    public NotificationFragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sv_notification_display , parent, false);
        return new NotificationFragViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationFragAdapter.NotificationFragViewHolder holder, int position) {
        Notification currentItem=list.get(holder.getAdapterPosition());

        holder.details.setText(currentItem.getCommenterName()+" commented on your post. Go And See!");

        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(currentItem.getPostKey()).child("delete").getValue().equals("false")) {
                    userId = snapshot.child(currentItem.getPostKey()).child("userId").getValue().toString();
                }
                else{
                    DatabaseReference item=notificationReference.child(currentItem.getNotifyKey());
                    item.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity) view.getContext();
                Intent intent=new Intent(activity,Comment.class);
                intent.putExtra("key",currentItem.getPostKey());
                intent.putExtra("userId",userId);
                activity.startActivity(intent);
            }
        });

//        Toast.makeText(context, ""+currentItem.getPostKey(), Toast.LENGTH_SHORT).show();
        holder.tvDelete.setVisibility(View.VISIBLE);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete!");
                builder.setMessage("Refresh to see the changes.");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference item=notificationReference.child(currentItem.getNotifyKey());
                        item.removeValue();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        try{
            Glide.with(context).load(currentItem.getCommentOnPost()).into(holder.postImg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationFragViewHolder extends RecyclerView.ViewHolder {


        private final TextView tvDelete;
        private ConstraintLayout notify;
        private TextView details;
        private ImageView postImg;

        public NotificationFragViewHolder(@NonNull View itemView) {
            super(itemView);

            details=itemView.findViewById(R.id.details);
            notify=itemView.findViewById(R.id.notify);
            postImg=itemView.findViewById(R.id.postImg);
            tvDelete=itemView.findViewById(R.id.tvDelete);



        }
    }
}
