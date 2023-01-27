package com.gap.mobigpk1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gap.mobigpk1.Model.ChildItem;

import java.util.Collections;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    private Context context;
    private List<ChildItem> childItemList;
    String postType = null;
    int normal = 0;
    int oneNight = 1;

    public void setChildItemList(Context context, List<ChildItem> childItemList){
        this.context = context;
        this.childItemList = childItemList;
        this.childItemList.removeAll(Collections.singleton(null));
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view;
        if(viewType==normal)
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.category_sub , parent, false);
        else
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.night_series , parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {

        ChildItem childItem = childItemList.get(position);
        holder.childName.setText(childItem.getChildName());

        Glide.with(context).load(childItem.getChildImage()).into(holder.childImageView);

        holder.childName.setVisibility(View.INVISIBLE);
        if(postType.equals("normal")) {
            holder.itemView.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent;
                if(childItem.getChildName().isEmpty()) {
                    intent = new Intent(activity, BooksInside.class);
                    intent.putExtra("bURL", childItem.getBookUrl());
                    intent.putExtra("path", childItem.getPath());
                }else{
                    intent = new Intent(activity, NotesList.class);
                    intent.putExtra("reference", childItem.getPath());
                    intent.putExtra("directedBy", childItem.getChildName());
                }
                activity.startActivity(intent);
            });
        }
        else if(postType.equals("night")){
            holder.childImageView.setImageURI(Uri.parse(childItem.getChildImage()));
            holder.itemView.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity, VideoLectures.class);
                intent.putExtra("mainLink", childItem.getBookUrl());
                activity.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(childItemList.get(position).getPath().contains("Night")){
            postType = "night";
            return oneNight;
        }
        else {
            postType = "normal";
            return normal;
        }
    }

    @Override
    public int getItemCount() {
        if (childItemList != null){
            return childItemList.size();
        }else{
            return  0;
        }
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder{
        private final TextView childName;
        private final ImageView childImageView;
        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);

            childName = itemView.findViewById(R.id.eachChildItemName);
            childImageView = itemView.findViewById(R.id.eachChildItemIV);
        }
    }
}
