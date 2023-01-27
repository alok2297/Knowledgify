package com.gap.mobigpk1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gap.mobigpk1.Model.Update;

import java.util.ArrayList;

public class HomeUpdateFragAdapter extends RecyclerView.Adapter<HomeUpdateFragAdapter.HomeUpdateFragViewAdapter> {

    private Context context;
    private ArrayList<Update> list;

    public HomeUpdateFragAdapter(Context context, ArrayList<Update> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeUpdateFragViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sv_update_story,parent,false);
        return new HomeUpdateFragViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeUpdateFragViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        Update currentItem=list.get(holder.getAdapterPosition());
        holder.title.setText(currentItem.getTitle());

        
        holder.coverDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                Intent i=new Intent(activity,WebViewNV.class);
                i.putExtra("url",currentItem.getImage());
                i.putExtra("title","Today's Update");
                activity.startActivity(i);
            }
        });
        

        try {
            if (currentItem.getCoverImg() != null)
                Glide.with(context).load(currentItem.getCoverImg()).into(holder.coverDisplay);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeUpdateFragViewAdapter extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView coverDisplay;



        public HomeUpdateFragViewAdapter(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            coverDisplay =itemView.findViewById(R.id.profile_image);

        }
    }
}
