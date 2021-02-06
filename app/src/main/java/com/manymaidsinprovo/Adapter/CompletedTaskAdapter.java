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

public class CompletedTaskAdapter extends RecyclerSwipeAdapter<CompletedTaskAdapter.AreaHolder> {

    private ArrayList<Task> dataSet;
    public CompletedTaskAdapter(ArrayList<Task> dataSet) {
        this.dataSet = dataSet;
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
        holder.tvDue.setText(dataSet.get(position).getCompletedDate());
        holder.layoutIndicator.setVisibility(View.GONE);
        //holder.slTask.setVisibility(View.GONE);
        holder.bottomWrapperJustDidIt.setVisibility(View.GONE);

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
        LinearLayout bottomWrapperJustDidIt,layoutIndicator,layoutMainIndicator, layoutNormal, layoutMedium, layoutItem;
        //ImageView ivNormalIndicator, ivNormalIndicator2, ivMediumIndicator, ivMediumIndicator2, ivJustBeforeProactiveIndicator, ivProactiveIndicator;
        //SwipeLayout slTask;
        //TextView tvDelete, tvEdit,tvBookMaid, tvJustDidIt, tvDue,tvAssignTo,tvAreaName;
        TextView tvDue,tvAssignTo,tvAreaName;


        public AreaHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            layoutMainIndicator = itemView.findViewById(R.id.layoutMainIndicator);
            layoutIndicator= itemView.findViewById(R.id.layoutIndicator);
            layoutNormal = itemView.findViewById(R.id.layoutNormal);
            layoutMedium = itemView.findViewById(R.id.layoutMedium);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            /*ivNormalIndicator = itemView.findViewById(R.id.ivNormalIndicator);
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
            */
            tvDue = itemView.findViewById(R.id.tvDue);
            bottomWrapperJustDidIt=itemView.findViewById(R.id.bottomWrapperJustDidIt);
            tvAssignTo = itemView.findViewById(R.id.tvAssignTo);
            tvAreaName = itemView.findViewById(R.id.tvAreaName);

        }
    }
}
