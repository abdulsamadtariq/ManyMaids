package com.manymaidsinprovo.Dialouge;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Address;
import com.manymaidsinprovo.Model.CleaningType;
import com.manymaidsinprovo.Model.Contact;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.CartHelper;
import com.manymaidsinprovo.SQLite.UserAddressHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BottomSheetOrderSummary extends BottomSheetDialogFragment {

    private ImageView ivHide;
    private TextView tvCleaningType, tvPrice, tvDiscount, tvPerItemPrice, tvSubsDiscount, tvTotalPrice, tvHowOftenTop;
    private TextView tvHowOften, tvHowOftenDays, tvArrivalTime, tvArrivalDate, tvAddressCaption, tvStreet, tvCity, tvPhone1, tvPhone2, tvMaidsCount;
    private TextInputLayout tilInstruction;
    private TextInputEditText titInstruction;
    private Button btnAddToCart;
    private Context mContext;
    private double totalBill;
    private double subscriptionDiscount;
    private CleaningType cleaningType;
    private String date;
    private String time;
    private String howOften;
    private ArrayList<String> selectedDays;
    private Address address;
    private String phone;
    private Task selectedTask;

    private TextView tvTaskName;
    private LinearLayout layoutMainIndicator, layoutNormal, layoutMedium;
    private ImageView ivNormalIndicator, ivNormalIndicator2, ivMediumIndicator, ivMediumIndicator2, ivJustBeforeProactiveIndicator, ivProactiveIndicator;
    private TextView tvDue, tvAssignTo, tvAreaName;
    private long diff;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getContext();
        View view = inflater.inflate(R.layout.layout_bottom_sheet_order_summary, container, false);

        tvCleaningType = view.findViewById(R.id.tvCleaningType);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        tvPerItemPrice = view.findViewById(R.id.tvPerItemPrice);
        tvSubsDiscount = view.findViewById(R.id.tvSubsDiscount);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvHowOftenTop = view.findViewById(R.id.tvHowOftenTop);
        tvHowOften = view.findViewById(R.id.tvHowOften);
        tvHowOftenDays = view.findViewById(R.id.tvHowOftenDays);
        tvArrivalTime = view.findViewById(R.id.tvArrivalTime);
        tvArrivalDate = view.findViewById(R.id.tvArrivalDate);
        tvAddressCaption = view.findViewById(R.id.tvAddressCaption);
        tvStreet = view.findViewById(R.id.tvStreet);
        tvCity = view.findViewById(R.id.tvCity);
        tvPhone1 = view.findViewById(R.id.tvPhone1);
        tvPhone2 = view.findViewById(R.id.tvPhone2);
        tilInstruction = view.findViewById(R.id.tilInstruction);
        titInstruction = view.findViewById(R.id.titInstruction);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        tvMaidsCount = view.findViewById(R.id.tvMaidsCount);
        ivHide = view.findViewById(R.id.ivHide);


        tvTaskName = view.findViewById(R.id.tvTaskName);
        layoutMainIndicator = view.findViewById(R.id.layoutMainIndicator);
        layoutNormal = view.findViewById(R.id.layoutNormal);
        layoutMedium = view.findViewById(R.id.layoutMedium);
        ivNormalIndicator = view.findViewById(R.id.ivNormalIndicator);
        ivNormalIndicator2 = view.findViewById(R.id.ivNormalIndicator2);
        ivMediumIndicator = view.findViewById(R.id.ivMediumIndicator);
        ivMediumIndicator2 = view.findViewById(R.id.ivMediumIndicator2);
        ivJustBeforeProactiveIndicator = view.findViewById(R.id.ivJustBeforeProactiveIndicator);
        ivProactiveIndicator = view.findViewById(R.id.ivProactiveIndicator);
        tvDue = view.findViewById(R.id.tvDue);
        tvAssignTo = view.findViewById(R.id.tvAssignTo);
        tvAreaName = view.findViewById(R.id.tvAreaName);

        selectedDays=new ArrayList<>();


        return view;
    }

    private void showTask() {

        tvTaskName.setText(selectedTask.getTaskName());
        tvAssignTo.setText(selectedTask.getUserName());
        tvAreaName.setText(selectedTask.getAreaName());

        long currentTimeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());

        try {
            Date endDate = sdf.parse(selectedTask.getEndTime());
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
            tvDue.setText("Due Just now");
        } else if (min == 1) {
            tvDue.setText("Due in " + min + " min");

        } else if (min > 1 && min <= 60) {
            tvDue.setText("Due in " + min + " mins ");
        } else if (hours == 1) {
            tvDue.setText("Due in " + hours + " hour");
        } else if (hours > 1 && hours <= 24) {
            tvDue.setText("Due in " + hours + " hours ");
        } else if (days == 1) {
            tvDue.setText("Due in " + days + " day ");
        } else if (days > 1 && days <= 30) {
            tvDue.setText("Due in " + days + " days");
        } else if (month == 1) {
            tvDue.setText("Due in " + month + " month");
        } else if (month > 1 && month <= 12) {
            tvDue.setText("Due in " + month + " months");
        } else if (Year > 1) {
            tvDue.setText("Due in " + Year + "Year");
        } else if (min < -1 && min > -3) {
            tvDue.setText("overDue Just now");
        } else if (min < -1 && min > -60) {
            tvDue.setText("overDue " + Math.abs(min) + " mins ");
        } else if (hours == -1) {
            tvDue.setText("overDue " + Math.abs(hours) + " hour");
        } else if (hours < 1 && hours >= -24) {
            tvDue.setText("overDue " + Math.abs(hours) + " hours ");
        } else if (days == -1) {
            tvDue.setText("overDue  " + Math.abs(days) + " day ");
        } else if (days < -1 && days >= -30) {
            tvDue.setText("overDue  " + Math.abs(days) + " days");
        } else if (month == -1) {
            tvDue.setText("overDue " + Math.abs(month) + " month");
        } else if (month < -1 && month >= -12) {
            tvDue.setText("overDue " + Math.abs(month) + " months");
        } else if (Year < -1) {
            tvDue.setText("overDue " + Math.abs(Year) + "Year");
        }


        int indicatorValue = selectedTask.getTaskIndicator();

        if (indicatorValue == 0) {
            layoutMainIndicator.setVisibility(View.GONE);
        } else if (indicatorValue == 1) {
            ivNormalIndicator.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 2) {
            ivNormalIndicator.setVisibility(View.VISIBLE);
            ivNormalIndicator2.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 3) {
            ivNormalIndicator.setVisibility(View.VISIBLE);
            ivNormalIndicator2.setVisibility(View.VISIBLE);
            ivMediumIndicator.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 4) {
            ivNormalIndicator.setVisibility(View.VISIBLE);
            ivNormalIndicator2.setVisibility(View.VISIBLE);
            ivMediumIndicator.setVisibility(View.VISIBLE);
            ivMediumIndicator2.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 5) {
            ivNormalIndicator.setVisibility(View.VISIBLE);
            ivNormalIndicator2.setVisibility(View.VISIBLE);
            ivMediumIndicator.setVisibility(View.VISIBLE);
            ivMediumIndicator2.setVisibility(View.VISIBLE);
            ivJustBeforeProactiveIndicator.setVisibility(View.VISIBLE);
        } else if (indicatorValue == 6) {
            layoutNormal.setVisibility(View.GONE);
            layoutMedium.setVisibility(View.GONE);
            ivJustBeforeProactiveIndicator.setVisibility(View.GONE);
            ivProactiveIndicator.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        cleaningType = (CleaningType) (getArguments() != null ? getArguments().getSerializable("cleaningType") : null);
        date = getArguments().getString("date");
        time = getArguments().getString("time");
        howOften = getArguments().getString("howOften");
        selectedDays = getArguments().getStringArrayList("days");
        selectedTask = (Task) (getArguments() != null ? getArguments().getSerializable("selectedTask") : null);


        int addressId = getArguments().getInt("addressId");
        UserAddressHelper userAddressHelper = new UserAddressHelper(mContext.getApplicationContext());
        address = userAddressHelper.getAddressDetails(addressId, SessionHelper.getCurrentUser(mContext.getApplicationContext()));

        ArrayList<Contact> contactArrayList;
        contactArrayList = userAddressHelper.getContact(addressId, SessionHelper.getCurrentUser(mContext));

        if (contactArrayList.size() == 1) {
            tvPhone1.setText(contactArrayList.get(0).getPhone());
            phone = contactArrayList.get(0).getPhone();
        } else if (contactArrayList.size() > 1) {
            tvPhone1.setText(contactArrayList.get(0).getPhone());
            tvPhone2.setText(contactArrayList.get(1).getPhone());
            phone = contactArrayList.get(0).getPhone() + " ," + contactArrayList.get(1).getPhone();
        }

        tvAddressCaption.setText(address.getAddressCaption());
        tvStreet.setText(address.getBuildingNo() + " ," + address.getStreetAddress());
        tvCity.setText(address.getCity() + " ," + address.getZipcode() + " ," + address.getCountry());


        tvCleaningType.setText(cleaningType.getCategoryName());
        tvMaidsCount.setText("" + cleaningType.getNumberOfCleaner());

        tvPrice.setText("$" + cleaningType.getCategoryPricePerCleaning() + " per " + cleaningType.getHours());
        if (cleaningType.getDiscount() != -1) {
            tvDiscount.setText("- $" + cleaningType.getDiscount());
        } else {
            tvDiscount.setText("-$0");
        }


        double perItemPrice = cleaningType.getCategoryPricePerCleaning() - cleaningType.getDiscount();
        tvPerItemPrice.setText("$" + perItemPrice + " per " + cleaningType.getHours());

        tvArrivalTime.setText(time);
        tvArrivalDate.setText(date);

        tvHowOften.setText(howOften);
        tvHowOftenTop.setText(howOften);

        ivHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        switch (howOften) {
            case "One Time":
                subscriptionDiscount = 0;
                totalBill = (1 * perItemPrice) - subscriptionDiscount;
                break;
            case "2x per week":
                subscriptionDiscount = 0;
                tvHowOftenDays.setVisibility(View.VISIBLE);
                tvHowOftenDays.setText(selectedDays.get(0) + "," + selectedDays.get(1));

                totalBill = (2 * perItemPrice) - subscriptionDiscount;

                break;
            case "Every week":
                subscriptionDiscount = 0;

                totalBill = (7 * perItemPrice) - subscriptionDiscount;

                break;
            case "Every other week":
                subscriptionDiscount = 0;
                totalBill = (14 * perItemPrice) - subscriptionDiscount;

                break;
            case "Every Four weeks (i.e One month)":
                subscriptionDiscount = 0;
                totalBill = (30 * perItemPrice) - subscriptionDiscount;

                break;
            case "One year":

                totalBill = (365 * perItemPrice);


                subscriptionDiscount = (totalBill * 10) / 100;
                totalBill = totalBill - subscriptionDiscount;
                break;
        }
        tvSubsDiscount.setText("$" + subscriptionDiscount);
        tvTotalPrice.setText("$" + totalBill);


        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilInstruction.setErrorEnabled(false);
                String instruction = titInstruction.getText().toString();
                if (!TextUtils.isEmpty(instruction)) {
                    CartHelper cartHelper = new CartHelper(mContext);

                    long id = cartHelper.addToCart(cleaningType, address, phone, date, time, howOften, selectedDays, date, date, String.valueOf(totalBill), instruction, 0, 1, selectedTask.getTaskId());

                    if (id > -1) {

                        Toast.makeText(mContext, "Successfully added to cart", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(mContext, "problem while adding", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                } else {
                    tilInstruction.setErrorEnabled(true);
                    tilInstruction.setError("please Enter Something");
                }

            }
        });
        showTask();

    }
}
