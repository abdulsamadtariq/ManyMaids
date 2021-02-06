package com.manymaidsinprovo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.BookingDetail;
import com.manymaidsinprovo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingDetailAdapter extends RecyclerView.Adapter<BookingDetailAdapter.BookingDetailHolder> {

    private ArrayList<BookingDetail> dataSet;

    private long diff;

    public BookingDetailAdapter(ArrayList<BookingDetail> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public BookingDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_booking_detail, parent, false);
        return new BookingDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookingDetailHolder holder, final int position) {


        int status = dataSet.get(position).getStatus();
        final int userRole = SessionHelper.getCurrentUser(holder.itemView.getContext()).getUserRole();

        if (status == 0) {
            holder.tvBookingStatus.setTextColor(holder.tvBookingStatus.getResources().getColor(R.color.colorAreaGreen));
            if (userRole == 1) {
                holder.tvBookingStatus.setText("Submitted by User.");
            } else {
                holder.tvBookingStatus.setText("Submitted by me.");
            }
        } else if (status == 1) {
            holder.spUserOperation.setVisibility(View.GONE);
            holder.tvBookingStatus.setTextColor(holder.tvBookingStatus.getResources().getColor(R.color.colorAreaGreen));
            holder.tvBookingStatus.setText("Job Done.");
        } else if (status == 2) {

            holder.spUserOperation.setVisibility(View.GONE);
            holder.tvBookingStatus.setTextColor(holder.tvBookingStatus.getResources().getColor(R.color.colorAreaRed));
            if (userRole == 1) {
                holder.tvBookingStatus.setText("Cancelled by User.payment added to wallet.");
            } else {
                holder.tvBookingStatus.setText("Cancelled by me.payment added to wallet.");
            }
        } else if (status == 3) {
            holder.spUserOperation.setVisibility(View.GONE);
            holder.tvBookingStatus.setTextColor(holder.tvBookingStatus.getResources().getColor(R.color.colorAreaRed));
            holder.tvBookingStatus.setText("Cancelled by admin.payment added to wallet.");
        } else if (status == 4) {
            holder.spUserOperation.setVisibility(View.GONE);
            holder.tvBookingStatus.setTextColor(holder.tvBookingStatus.getResources().getColor(R.color.colorAreaGreen));
            holder.tvBookingStatus.setText("Cleaning in progress.");
        }
        holder.tvCleaningType.setText(dataSet.get(position).getCleaningType().getCategoryName());
        holder.tvTime.setText(dataSet.get(position).getStartTime());
        holder.tvArrivalDate.setText(dataSet.get(position).getStartDate());
        holder.tvHowOften.setText(dataSet.get(position).getHowOften());
        holder.tvHowOftenDays.setText(dataSet.get(position).getDays());
        holder.tvAddressCaption.setText(dataSet.get(position).getAddress().get(0).getAddressCaption());
        holder.tvStreet.setText(dataSet.get(position).getAddress().get(0).getBuildingNo() + " ," + dataSet.get(position).getAddress().get(0).getStreetAddress());
        holder.tvCity.setText(dataSet.get(position).getAddress().get(0).getCity() + " ," + dataSet.get(position).getAddress().get(0).getZipcode() + "," + dataSet.get(position).getAddress().get(0).getCountry());
        holder.tvPhone.setText(dataSet.get(position).getAddress().get(0).getContactList().get(0).getPhone());
        double updated = dataSet.get(position).getUpdatedPrice();
        holder.tvAmount.setText("$" + updated);
        holder.tvQuantity.setText(dataSet.get(position).getQuantity() + "x");
        if (dataSet.get(position).getDays() != null) {
            holder.tvHowOftenDays.setVisibility(View.VISIBLE);
        }

        holder.spUserOperation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedOperation = holder.spUserOperation.getSelectedItem().toString();
                if (selectedOperation.equals("None")) {
                    return;
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Confirmation")
                            .setMessage("Are you sure to cancel this task?\nNote:Payment will be added to your wallet for Next bookings")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    updateBookingToCancel(holder.itemView.getContext(), dataSet.get(position).getOrder_detail_id(), dataSet.get(position).getUpdatedPrice());
                                    //   Toast.makeText(holder.itemView.getContext(), "update", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setCancelable(false)
                            .setNeutralButton("cancel", null)
                            .create().show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       /* holder.ivRemove.setOnClickListener(new View.OnClickListener() {
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
*/
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

    private void updateBookingToCancel(final Context context, final int order_detail_id, final double updatedPrice) {

        final int userRole = SessionHelper.getCurrentUser(context).getUserRole();

        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.updateBooking, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    if (status == 0) {
                        Toast.makeText(context, "Error:" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(SessionHelper.getCurrentUser(context).getUserId()));
                map.put("order_detail_id", String.valueOf(order_detail_id));
                map.put("walletAmount", String.valueOf(updatedPrice));
                if (userRole == 1) {
                    map.put("status", "3");
                } else {
                    map.put("status", "2");
                }
                return map;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class BookingDetailHolder extends RecyclerView.ViewHolder {

        TextView tvBookingStatus, tvCleaningType, tvTime, tvArrivalDate, tvHowOften, tvHowOftenDays, tvAddressCaption, tvStreet, tvCity, tvPhone, tvAmount, tvQuantity;

        TextView tvTaskName;
        LinearLayout layoutMainIndicator, layoutNormal, layoutMedium;
        ImageView ivNormalIndicator, ivNormalIndicator2, ivMediumIndicator, ivMediumIndicator2, ivJustBeforeProactiveIndicator, ivProactiveIndicator;
        TextView tvDue, tvAssignTo, tvAreaName;
        Spinner spUserOperation;


        public BookingDetailHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
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
            spUserOperation = itemView.findViewById(R.id.spUserOperation);

        }
    }
}
