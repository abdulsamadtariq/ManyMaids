package com.manymaidsinprovo.Adapter;

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
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ToDoTaskAdapter extends RecyclerSwipeAdapter<ToDoTaskAdapter.AreaHolder> {

    private ArrayList<Task> dataSet;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemClickListener onDeleteClickListener;
    private AdapterView.OnItemClickListener onEditClickListener;
    private AdapterView.OnItemClickListener onJustDidItClickListener;
    private AdapterView.OnItemClickListener onBookMaidClickListener;
    private long diff;

    public ToDoTaskAdapter(ArrayList<Task> dataSet, AdapterView.OnItemClickListener onItemClickListener, AdapterView.OnItemClickListener onDeleteClickListener, AdapterView.OnItemClickListener onEditClickListener, AdapterView.OnItemClickListener onJustDidItClickListener, AdapterView.OnItemClickListener onBookMaidClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onEditClickListener = onEditClickListener;
        this.onJustDidItClickListener = onJustDidItClickListener;
        this.onBookMaidClickListener = onBookMaidClickListener;
    }


    @NonNull
    @Override
    public AreaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_to_do, parent, false);
        return new AreaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AreaHolder holder, int position) {

        holder.tvTaskName.setText(dataSet.get(position).getTaskName());
        holder.tvAssignTo.setText(dataSet.get(position).getUserName());
        holder.tvAreaName.setText(dataSet.get(position).getAreaName());

        long currentTimeStamp = System.currentTimeMillis();
        //long endTime = Long.parseLong(dataSet.get(position).getEndTime());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());

        try {
            Date endDate=sdf.parse(dataSet.get(position).getEndTime());
            diff=endDate.getTime()-currentTimeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long min = diff / 60000;
        long hours = min / 60;
        long days = hours / 24;
        long month = days / 30;
        long Year = month / 12;

        if (min < 1 && min > -1) {
            holder.tvDue.setText("Due Just now");
        } else if (min == 1) {
            holder.tvDue.setText("Due in " + min + " min");

        } else if (min > 1 && min <= 60) {
            holder.tvDue.setText("Due in " + min + " mins ");
        } else if (hours == 1) {
            holder.tvDue.setText("Due in " + hours + " hour");
        } else if (hours > 1 && hours <= 24) {
            holder.tvDue.setText("Due in " + hours + " hours ");
        } else if (days == 1) {
            holder.tvDue.setText("Due in " + days + " day ");
        } else if (days > 1 && days <= 30) {
            holder.tvDue.setText("Due in " + days + " days");
        } else if (month == 1) {
            holder.tvDue.setText("Due in " + month + " month");
        } else if (month > 1 && month <= 12) {
            holder.tvDue.setText("Due in " + month + " months");
        } else if (Year > 1) {
            holder.tvDue.setText("Due in " + Year + "Year");
        } else if (min < -1 && min > -3) {
            holder.tvDue.setText("overDue Just now");
        } else if (min < -1 && min > -60) {
            holder.tvDue.setText("overDue " + Math.abs(min) + " mins ");
        } else if (hours == -1) {
            holder.tvDue.setText("overDue " + Math.abs(hours) + " hour");
        } else if (hours < 1 && hours >= -24) {
            holder.tvDue.setText("overDue " + Math.abs(hours) + " hours ");
        } else if (days == -1) {
            holder.tvDue.setText("overDue  " + Math.abs(days) + " day ");
        } else if (days < -1 && days >= -30) {
            holder.tvDue.setText("overDue  " + Math.abs(days) + " days");
        } else if (month == -1) {
            holder.tvDue.setText("overDue " + Math.abs(month) + " month");
        } else if (month < -1 && month >= -12) {
            holder.tvDue.setText("overDue " + Math.abs(month) + " months");
        } else if (Year < -1) {
            holder.tvDue.setText("overDue " + Math.abs(Year) + "Year");
        }


        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null, holder.layoutItem, holder.getAdapterPosition(), 0);
            }
        });
        int indicatorValue = dataSet.get(position).getTaskIndicator();

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


        holder.slTask.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.slTask.addDrag(SwipeLayout.DragEdge.Right, holder.itemView.findViewById(R.id.bottomWraper));
        holder.slTask.addDrag(SwipeLayout.DragEdge.Left, holder.itemView.findViewById(R.id.bottomWrapperJustDidIt));
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onItemClick(null, holder.tvTaskName, holder.getAdapterPosition(), 0);
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditClickListener.onItemClick(null, holder.tvEdit, holder.getAdapterPosition(), 0);
            }
        });
        holder.tvJustDidIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJustDidItClickListener.onItemClick(null, holder.tvJustDidIt, holder.getAdapterPosition(), 0);
            }
        });

        holder.slTask.addSwipeListener(new SimpleSwipeListener() {
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.tvDelete));
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.tvBookMaid));
            }
        });
        holder.tvBookMaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBookMaidClickListener.onItemClick(null, holder.tvBookMaid, holder.getAdapterPosition(), 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.slTask;
    }

    public class AreaHolder extends RecyclerView.ViewHolder {

        TextView tvTaskName;
        LinearLayout layoutMainIndicator, layoutNormal, layoutMedium, layoutItem;
        ImageView ivNormalIndicator, ivNormalIndicator2, ivMediumIndicator, ivMediumIndicator2, ivJustBeforeProactiveIndicator, ivProactiveIndicator;
        SwipeLayout slTask;
        TextView tvDelete, tvEdit,tvBookMaid, tvJustDidIt, tvDue,tvAssignTo,tvAreaName;


        public AreaHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            layoutMainIndicator = itemView.findViewById(R.id.layoutMainIndicator);
            layoutNormal = itemView.findViewById(R.id.layoutNormal);
            layoutMedium = itemView.findViewById(R.id.layoutMedium);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            ivNormalIndicator = itemView.findViewById(R.id.ivNormalIndicator);
            ivNormalIndicator2 = itemView.findViewById(R.id.ivNormalIndicator2);
            ivMediumIndicator = itemView.findViewById(R.id.ivMediumIndicator);
            ivMediumIndicator2 = itemView.findViewById(R.id.ivMediumIndicator2);
            ivJustBeforeProactiveIndicator = itemView.findViewById(R.id.ivJustBeforeProactiveIndicator);
            ivProactiveIndicator = itemView.findViewById(R.id.ivProactiveIndicator);
            slTask = itemView.findViewById(R.id.slTask);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvJustDidIt = itemView.findViewById(R.id.tvJustDidIt);
            tvBookMaid = itemView.findViewById(R.id.tvBookMaid);
            tvDue = itemView.findViewById(R.id.tvDue);
            tvAssignTo = itemView.findViewById(R.id.tvAssignTo);
            tvAreaName = itemView.findViewById(R.id.tvAreaName);

        }
    }
}
