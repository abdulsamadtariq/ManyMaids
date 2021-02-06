package com.manymaidsinprovo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manymaidsinprovo.Model.Address;
import com.manymaidsinprovo.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {


    private ArrayList<Address> dataSet;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public AddressAdapter(ArrayList<Address> dataSet, AdapterView.OnItemClickListener onItemClickListener, AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_address, parent, false);
        return new AddressHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddressHolder holder, int position) {


        String addressCaption = dataSet.get(position).getAddressCaption();
        holder.tvAddressCaption.setText(addressCaption);

        if (dataSet.get(position).isSelected()) {
            holder.tvAddressCaption.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.tvAddressCaption.setSelected(true);
        } else {
            holder.tvAddressCaption.setTextColor(holder.itemView.getResources().getColor(android.R.color.darker_gray));
            holder.tvAddressCaption.setSelected(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), 0);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemLongClickListener.onItemLongClick(null, holder.itemView, holder.getAdapterPosition(), 0);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class AddressHolder extends RecyclerView.ViewHolder {

        TextView tvAddressCaption;

        public AddressHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressCaption = itemView.findViewById(R.id.tvAddressCaption);
        }
    }
}
