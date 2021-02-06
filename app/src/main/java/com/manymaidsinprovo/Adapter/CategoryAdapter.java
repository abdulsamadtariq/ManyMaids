package com.manymaidsinprovo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manymaidsinprovo.Model.CleaningType;
import com.manymaidsinprovo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{


    private ArrayList<CleaningType> dataSet;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemClickListener onReadMoreClickListener;


    public CategoryAdapter(ArrayList<CleaningType> dataSet, AdapterView.OnItemClickListener onItemClickListener, AdapterView.OnItemClickListener onReadMoreClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
        this.onReadMoreClickListener = onReadMoreClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_category,parent,false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryHolder holder, int position) {

        int numberOfCleaner=dataSet.get(position).getNumberOfCleaner();
        if(numberOfCleaner==1){
            Picasso.get().load(R.drawable.maid_1).into(holder.ivNumOfCleaners);
        }else if(numberOfCleaner==2){
            Picasso.get().load(R.drawable.maids_2).into(holder.ivNumOfCleaners);
        }else {
            Picasso.get().load(R.drawable.maid_1).into(holder.ivNumOfCleaners);
        }

        holder.rbRating.setRating(dataSet.get(position).getRating());
        holder.tvCatName.setText(dataSet.get(position).getCategoryName());
        holder.tvCatPrice.setText("$" + dataSet.get(position).getCategoryPricePerCleaning());
        holder.tvCatHours.setText(dataSet.get(position).getHours());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null,holder.itemView,holder.getAdapterPosition(),0);
            }
        });
        holder.tvReadReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReadMoreClickListener.onItemClick(null,holder.itemView,holder.getAdapterPosition(),0);
            }
        });

        if (dataSet.get(position).isSelected()) {
            holder.layoutCleaningType.setSelected(true);
        } else {
            holder.layoutCleaningType.setSelected(false);
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder{

        ImageView ivNumOfCleaners;
        TextView tvReadReviews;
        RatingBar rbRating;
        TextView tvCatName,tvCatPrice,tvCatHours;
        RelativeLayout layoutCleaningType;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            ivNumOfCleaners=itemView.findViewById(R.id.ivNumOfCleaners);
            layoutCleaningType=itemView.findViewById(R.id.layoutCleaningType);
            tvReadReviews=itemView.findViewById(R.id.tvReadReviews);
            rbRating=itemView.findViewById(R.id.rbRating);
            tvCatName=itemView.findViewById(R.id.tvCatName);
            tvCatPrice=itemView.findViewById(R.id.tvCatPrice);
            tvCatHours=itemView.findViewById(R.id.tvCatHours);
        }
    }
}
