package com.manymaidsinprovo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.manymaidsinprovo.Model.Area;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.util.ArrayList;

public class AreaAdapter extends RecyclerSwipeAdapter<AreaAdapter.AreaHolder> {

    private ArrayList<Area> dataSet;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemClickListener onDeleteClickListener;
    private AdapterView.OnItemClickListener onEditClickListener;

    public AreaAdapter(ArrayList<Area> dataSet, AdapterView.OnItemClickListener onItemClickListener, AdapterView.OnItemClickListener onDeleteClickListener, AdapterView.OnItemClickListener onEditClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onEditClickListener = onEditClickListener;
    }

    @NonNull
    @Override
    public AreaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_area, parent, false);
        return new AreaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AreaHolder holder, int position) {

        holder.tvAreaName.setText(dataSet.get(position).getAreaName());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null, holder.layoutItem, holder.getAdapterPosition(), 0);
            }
        });
        int indicatorValue = dataSet.get(position).getIndicatorValue();

        if (indicatorValue == 0) {
            holder.layoutMainIndicator.setVisibility(View.GONE);
        }
        holder.slArea.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.slArea.addDrag(SwipeLayout.DragEdge.Right, holder.itemView.findViewById(R.id.bottomWraper));
        holder.slArea.addDrag(SwipeLayout.DragEdge.Left, holder.itemView.findViewById(R.id.bottomWrapperJustDidIt));
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onItemClick(null, holder.tvAreaName, holder.getAdapterPosition(), 0);
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditClickListener.onItemClick(null, holder.tvEdit, holder.getAdapterPosition(), 0);
            }
        });


        holder.slArea.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.tvDelete));
            }
        });


        if (indicatorValue == 0) {
            holder.layoutMainIndicator.setVisibility(View.GONE);
        } else if (indicatorValue == 1) {
            holder.ivNormalIndicator.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 2) {
            holder.ivNormalIndicator.setVisibility(View.VISIBLE);
            holder.ivNormalIndicator2.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 3) {
            holder.ivNormalIndicator.setVisibility(View.VISIBLE);
            holder.ivNormalIndicator2.setVisibility(View.VISIBLE);
            holder.ivMediumIndicator.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 4) {
            holder.ivNormalIndicator.setVisibility(View.VISIBLE);
            holder.ivNormalIndicator2.setVisibility(View.VISIBLE);
            holder.ivMediumIndicator.setVisibility(View.VISIBLE);
            holder.ivMediumIndicator2.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 5) {
            holder.ivNormalIndicator.setVisibility(View.VISIBLE);
            holder.ivNormalIndicator2.setVisibility(View.VISIBLE);
            holder.ivMediumIndicator.setVisibility(View.VISIBLE);
            holder.ivMediumIndicator2.setVisibility(View.VISIBLE);
            holder.ivJustBeforeProactiveIndicator.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 6) {
            holder.layoutNormal.setVisibility(View.GONE);
            holder.layoutMedium.setVisibility(View.GONE);
            holder.ivJustBeforeProactiveIndicator.setVisibility(View.GONE);
            holder.ivProactiveIndicator.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.slArea;
    }

    public class AreaHolder extends RecyclerView.ViewHolder {

        TextView tvAreaName;
        LinearLayout layoutMainIndicator,layoutNormal,layoutMedium,layoutItem;
        ImageView ivNormalIndicator, ivNormalIndicator2, ivMediumIndicator, ivMediumIndicator2, ivJustBeforeProactiveIndicator, ivProactiveIndicator;
        SwipeLayout slArea;
        TextView tvDelete, tvEdit;


        public AreaHolder(@NonNull View itemView) {
            super(itemView);
            tvAreaName = itemView.findViewById(R.id.tvAreaName);
            layoutMainIndicator = itemView.findViewById(R.id.layoutMainIndicator);
            layoutNormal= itemView.findViewById(R.id.layoutNormal);
            layoutItem= itemView.findViewById(R.id.layoutItem);
            layoutMedium= itemView.findViewById(R.id.layoutMedium);
            ivNormalIndicator = itemView.findViewById(R.id.ivNormalIndicator);
            ivNormalIndicator2 = itemView.findViewById(R.id.ivNormalIndicator2);
            ivMediumIndicator = itemView.findViewById(R.id.ivMediumIndicator);
            ivMediumIndicator2 = itemView.findViewById(R.id.ivMediumIndicator2);
            ivJustBeforeProactiveIndicator = itemView.findViewById(R.id.ivJustBeforeProactiveIndicator);
            ivProactiveIndicator = itemView.findViewById(R.id.ivProactiveIndicator);
            slArea = itemView.findViewById(R.id.slArea);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            tvEdit = itemView.findViewById(R.id.tvEdit);

        }
    }
}
