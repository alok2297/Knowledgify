package com.gap.mobigpk1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gap.mobigpk1.Model.Comments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CommentFragAdapter extends RecyclerView.Adapter<CommentFragAdapter.CommentFragViewAdapter> {

    private Context context;
    private ArrayList<Comments> list;
    private String postkey;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
    DatabaseReference commentReference = FirebaseDatabase.getInstance().getReference().child("Comments");
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String postType = null;
    int photo = 0;
    int caption = 2;

    public CommentFragAdapter(Context context, ArrayList<Comments> list, String postkey) {
        this.context = context;
        this.list = list;
        this.postkey = postkey;
    }

    @NonNull
    @Override
    public CommentFragViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == photo){
            view = LayoutInflater.from(context).inflate(R.layout.sv_comment_layout,parent,false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.sv_comment_caption_layout,parent,false);
        }

        return new CommentFragViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentFragViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        Comments currentItem=list.get(holder.getAdapterPosition());




        userReference.child(currentItem.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.Name.setText(snapshot.child("name").getValue().toString());
                holder.College.setText(snapshot.child("college").getValue().toString());
                Glide.with(context).load(snapshot.child("profilePic").getValue().toString()).into(holder.Profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Picasso.get().load(currentItem.getProfile).into(holder.Profile);
        holder.Caption.setText(currentItem.getTextComment());

        if(user.getUid().equals(currentItem.getName())) {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete!");
                builder.setMessage("Refresh post after deleting comment.");
                builder.setPositiveButton("YES", (dialog, which) -> {
                    DatabaseReference item = commentReference.child(postkey).child(currentItem.getCommentKey());
                    item.removeValue();
                    dialog.dismiss();
                });

                builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

                AlertDialog alert = builder.create();
                alert.show();

            });
        }
        else{
            holder.ivDelete.setVisibility(View.INVISIBLE);
        }
//
        if(postType.equals("photo")){

            Glide.with(context).load(currentItem.getPicComment()).into(holder.Display);

            holder.Display.setOnClickListener(view -> {
                AppCompatActivity activity=(AppCompatActivity) view.getContext();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(firebase_model_all.getLink()));

                Intent intent=new Intent(activity,ImageViewFull.class);
                intent.putExtra("fullPic",currentItem.getPicComment());
                activity.startActivity(intent);
            });

            //88888888888888888888888

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getType().equals("photo")){
            postType = "photo";
            return photo;
        }
        else {
            postType = "caption";
            return caption;
        }
    }

    public class CommentFragViewAdapter extends RecyclerView.ViewHolder {
        private final ImageView ivDelete;
        private TextView Caption;

        private TextView Name,College, tvPollDes, tvLike, tvComment, tvShare;
        private ImageView Profile,Display;
        private RecyclerView recyclerView, resultRecyclerView;
        private LinearLayout optionsLayout, resultsLayout;

        public CommentFragViewAdapter(@NonNull View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.userName);
            College=itemView.findViewById(R.id.College);
            Profile=itemView.findViewById(R.id.profile_image);
            Caption=itemView.findViewById(R.id.caption);
            ivDelete=itemView.findViewById(R.id.ivDelete);

            if(postType.equals("photo")){
                Display=itemView.findViewById(R.id.addStoryImg);
            }
        }
    }
}

