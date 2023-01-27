package com.gap.mobigpk1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.ParentItem;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    private Context context;
    private List<ParentItem> parentItemList;

    public void setParentItemList(Context context, List<ParentItem> parentItemList){
        this.context = context;
        this.parentItemList = parentItemList;
    }
    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category , parent , false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {

        ParentItem parentItem = parentItemList.get(position);
        holder.parentName.setText(parentItem.getParentName());

        holder.childRecyclerView.setHasFixedSize(true);
        holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext() , LinearLayoutManager.HORIZONTAL,false));
        ChildAdapter childAdapter = new ChildAdapter();
        childAdapter.setChildItemList(context,parentItem.getChildItemList());
        holder.childRecyclerView.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (parentItemList != null){
            return parentItemList.size();
        }else{
            return 0;
        }

    }

    public class ParentViewHolder extends RecyclerView.ViewHolder{
        private TextView parentName;
        private RecyclerView childRecyclerView;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            parentName = itemView.findViewById(R.id.eachParentName);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
        }
    }
}