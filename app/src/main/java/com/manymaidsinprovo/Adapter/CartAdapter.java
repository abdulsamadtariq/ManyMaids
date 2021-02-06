package com.manymaidsinprovo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manymaidsinprovo.Model.CartItem;
import com.manymaidsinprovo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    private ArrayList<CartItem> dataSet;
    private AdapterView.OnItemClickListener onRemoveClickListener;
    private AdapterView.OnItemClickListener onDecrementClickListener;
    private AdapterView.OnItemClickListener onIncrementClickListener;
    private long diff;

    public CartAdapter(ArrayList<CartItem> dataSet, AdapterView.OnItemClickListener onRemoveClickListener, AdapterView.OnItemClickListener onDecrementClickListener, AdapterView.OnItemClickListener onIncrementClickListener) {
        this.dataSet = dataSet;
        this.onRemoveClickListener = onRemoveClickListener;
        this.onDecrementClickListener = onDecrementClickListener;
        this.onIncrementClickListener = onIncrementClickListener;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_cart, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder, int position) {

        holder.tvCleaningType.setText(dataSet.get(position).getCleaningType().getCategoryName());
        holder.tvTime.setText(dataSet.get(position).getTime());
        holder.tvArrivalDate.setText(dataSet.get(position).getDate());
        holder.tvHowOften.setText(dataSet.get(position).getHowOften());
        holder.tvHowOftenDays.setText(dataSet.get(position).getDays());
        holder.tvAddressCaption.setText(dataSet.get(position).getAddress().getAddressCaption());
        holder.tvStreet.setText(dataSet.get(position).getAddress().getBuildingNo() + " ," + dataSet.get(position).getAddress().getStreetAddress());
        holder.tvCity.setText(dataSet.get(position).getAddress().getCity() + " ," + dataSet.get(position).getAddress().getZipcode() + "," + dataSet.get(position).getAddress().getCountry());
        holder.tvPhone.setText(dataSet.get(position).getAddress().getContactList().get(0).getPhone());
        double updated = Double.parseDouble(dataSet.get(position).getTotalBill()) * dataSet.get(position).getQuantity();
        holder.tvAmount.setText("$" + updated);
        holder.tvQuantity.setText(String.valueOf(dataSet.get(position).getQuantity()));
        if (dataSet.get(position).getDays() != null) {
            holder.tvHowOftenDays.setVisibility(View.VISIBLE);
        }


        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRemoveClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), 0);
            }
        });

        holder.ibDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDecrementClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), 0);
            }
        });
        holder.ibIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIncrementClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), 0);
            }
        });

        //


        holder.tvTaskName.setText(dataSet.get(position).getTask().getTaskName());
        holder.tvAssignTo.setText(dataSet.get(position).getTask().getUserName());
        holder.tvAreaName.setText(dataSet.get(position).getTask().getAreaName());

        long currentTimeStamp = System.currentTimeMillis();
        //long endTime = Long.parseLong(dataSet.get(position).getEndTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());

        try {
            Date endDate = sdf.parse(dataSet.get(position).getTask().getEndTime());
            diff = endDate.getTime() - currentTimeStamp;
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


        int indicatorValue = dataSet.get(position).getTask().getTaskIndicator();

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

    public class CartHolder extends RecyclerView.ViewHolder {

        TextView tvCleaningType, tvTime, tvArrivalDate, tvHowOften, tvHowOftenDays, tvAddressCaption, tvStreet, tvCity, tvPhone, tvAmount, tvQuantity;
        ImageView ivRemove;
        ImageButton ibIncrease, ibDecrease;

        TextView tvTaskName;
        LinearLayout layoutMainIndicator, layoutNormal, layoutMedium;
        ImageView ivNormalIndicator, ivNormalIndicator2, ivMediumIndicator, ivMediumIndicator2, ivJustBeforeProactiveIndicator, ivProactiveIndicator;
        TextView tvDue, tvAssignTo, tvAreaName;


        public CartHolder(@NonNull View itemView) {
            super(itemView);
            tvCleaningType = itemView.findViewById(R.id.tvCleaningType);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvArrivalDate = itemView.findViewById(R.id.tvArrivalDate);
            tvHowOften = itemView.findViewById(R.id.tvHowOften);
            tvHowOftenDays = itemView.findViewById(R.id.tvHowOftenDays);
            tvAddressCaption = itemView.findViewById(R.id.tvAddressCaption);
            tvStreet = itemView.findViewById(R.id.tvStreet);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivRemove = itemView.findViewById(R.id.ivRemove);
            ibIncrease = itemView.findViewById(R.id.ibIncrease);
            ibDecrease = itemView.findViewById(R.id.ibDecrease);


            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            layoutMainIndicator = itemView.findViewById(R.id.layoutMainIndicator);
            layoutNormal = itemView.findViewById(R.id.layoutNormal);
            layoutMedium = itemView.findViewById(R.id.layoutMedium);
            ivNormalIndicator = itemView.findViewById(R.id.ivNormalIndicator);
            ivNormalIndicator2 = itemView.findViewById(R.id.ivNormalIndicator2);
            ivMediumIndicator = itemView.findViewById(R.id.ivMediumIndicator);
            ivMediumIndicator2 = itemView.findViewById(R.id.ivMediumIndicator2);
            ivJustBeforeProactiveIndicator = itemView.findViewById(R.id.ivJustBeforeProactiveIndicator);
            ivProactiveIndicator = itemView.findViewById(R.id.ivProactiveIndicator);
            tvDue = itemView.findViewById(R.id.tvDue);
            tvAssignTo = itemView.findViewById(R.id.tvAssignTo);
            tvAreaName = itemView.findViewById(R.id.tvAreaName);

        }
    }
}
