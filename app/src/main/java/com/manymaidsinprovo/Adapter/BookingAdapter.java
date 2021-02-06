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

import com.manymaidsinprovo.Model.Booking;
import com.manymaidsinprovo.Model.BookingDetail;
import com.manymaidsinprovo.Model.CartItem;
import com.manymaidsinprovo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {

    private ArrayList<Booking> dataSet;
    private ArrayList<BookingDetail> bookingDetailArrayList;
    private BookingDetailAdapter bookingDetailAdapter;

    public BookingAdapter(ArrayList<Booking> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_booking, parent, false);
        return new BookingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder holder, int position) {

        holder.tvOrderId.setText(""+dataSet.get(position).getOrderId());

        SimpleDateFormat sdf=new SimpleDateFormat("EEEE, MMMM d , yyyy", Locale.US);
        long timeStamp=Long.parseLong(dataSet.get(position).getOrderTime());
        String date=sdf.format(timeStamp);

        bookingDetailArrayList=dataSet.get(position).getBookingDetailArrayList();
        holder.tvBookingCount.setText(""+bookingDetailArrayList.size());

        holder.tvOrderDate.setText(date);
        holder.tvOrderBill.setText("$"+dataSet.get(position).getTotalBill());


        bookingDetailAdapter=new BookingDetailAdapter(bookingDetailArrayList);
        holder.rvBookingDetail.setAdapter(bookingDetailAdapter);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class BookingHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId, tvOrderDate, tvOrderBill,tvBookingCount;
        RecyclerView rvBookingDetail;

        public BookingHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderBill = itemView.findViewById(R.id.tvOrderBill);
            tvBookingCount = itemView.findViewById(R.id.tvBookingCount);
            rvBookingDetail = itemView.findViewById(R.id.rvBookingDetails);
        }


    }
}
