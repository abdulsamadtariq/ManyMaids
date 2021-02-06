package com.manymaidsinprovo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manymaidsinprovo.Model.CustomTimePicker;
import com.manymaidsinprovo.R;

import java.util.ArrayList;

public class CustomTimePickerAdapter extends RecyclerView.Adapter<CustomTimePickerAdapter.CustomTimePickerHolder> {

    private ArrayList<CustomTimePicker> dataSet;
    private AdapterView.OnItemClickListener onItemClickListener;


    public CustomTimePickerAdapter(ArrayList<CustomTimePicker> dataSet, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CustomTimePickerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_time, parent, false);
        return new CustomTimePickerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomTimePickerHolder holder, int position) {


        holder.tvTime.setText(dataSet.get(position).getTime());
        if (dataSet.get(position).isSelected()) {
            holder.tvTime.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.tvTime.setSelected(true);
        } else {
            holder.tvTime.setTextColor(holder.itemView.getResources().getColor(android.R.color.darker_gray));
            holder.tvTime.setSelected(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class CustomTimePickerHolder extends RecyclerView.ViewHolder {

        TextView tvTime;

        public CustomTimePickerHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
