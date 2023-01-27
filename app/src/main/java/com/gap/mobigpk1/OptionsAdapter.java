package com.gap.mobigpk1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gap.mobigpk1.Model.Options;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    public ArrayList<Options> options;
    Context context;

    public OptionsAdapter(ArrayList<Options> options, Context context) {
        this.options = options;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvOptions;
        TextView btClear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOptions = itemView.findViewById(R.id.tvOption);
            btClear = itemView.findViewById(R.id.btClear);

            btClear.setOnClickListener(view -> {
                options.remove(options.get(getPosition()));
                notifyDataSetChanged();
            });

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_res, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvOptions.setText(options.get(position).getOptions());
    }

    @Override
    public int getItemCount() {
        return options.size();
    }


}
