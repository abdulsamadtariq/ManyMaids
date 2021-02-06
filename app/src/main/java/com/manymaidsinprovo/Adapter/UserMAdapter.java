package com.manymaidsinprovo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.manymaidsinprovo.Model.UserM;
import com.manymaidsinprovo.R;

import java.util.ArrayList;

public class UserMAdapter extends RecyclerSwipeAdapter<UserMAdapter.UserHolder> {

    private ArrayList<UserM> dataSet;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemClickListener onDeleteClickListener;

    public UserMAdapter(ArrayList<UserM> dataSet, AdapterView.OnItemClickListener onItemClickListener, AdapterView.OnItemClickListener onDeleteClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_user_m, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, int position) {

        holder.tvUserName.setText(dataSet.get(position).getUserName());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null,holder.layoutItem,holder.getAdapterPosition(),0);
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onItemClick(null,holder.tvDelete,holder.getAdapterPosition(),0);
            }
        });

        holder.slUser.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.slUser.addDrag(SwipeLayout.DragEdge.Right, holder.itemView.findViewById(R.id.bottomWraper));
        holder.slUser.addSwipeListener(new SimpleSwipeListener() {
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.tvDelete));
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.slUser;
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        TextView tvDelete, tvUserName;
        SwipeLayout slUser;
        LinearLayout layoutItem;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            slUser = itemView.findViewById(R.id.slUser);
        }
    }
}
