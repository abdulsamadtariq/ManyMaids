package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manymaidsinprovo.Adapter.CustomMonthAdapter;
import com.manymaidsinprovo.Adapter.CustomTimePickerAdapter;
import com.manymaidsinprovo.Dialouge.BottomSheetUserAssignment;
import com.manymaidsinprovo.Model.Area;
import com.manymaidsinprovo.Model.CustomTimePicker;
import com.manymaidsinprovo.Model.Month;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TaskTopFragment extends Fragment {

    private LinearLayout layoutToHide, layoutAreaTop;
    private EditText etTaskName;
    private Animation bounceAnim;
    private Animation bounceBackAnim;
    private Context mContext;

    private ArrayList<CustomTimePicker> monthsList;
    private RecyclerView rvMonths;
    private CustomMonthAdapter customMonthAdapter;
    private TextView tvSeasonal, tvAlwaysActive;
    private boolean isSeasonal = false;
    private ImageButton ibDecrease, ibIncrease, ibDaysDecrease, ibDaysIncrease, ibAddUser;
    private TextView tvDaysQuantity, tvQuantity, tvAll;
    private Button btnCreateTask;
    private Button btnUpdateTask;
    private TaskHelper taskHelper;
    private Area area;
    private Task task;
    private ArrayList<CustomTimePicker> monthArrayList;

    public TaskTopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        return inflater.inflate(R.layout.fragment_task_top, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTaskName = view.findViewById(R.id.etTaskName);
        layoutToHide = view.findViewById(R.id.layoutToHide);
        layoutAreaTop = view.findViewById(R.id.layoutTaskTop);
        monthsList = new ArrayList<>();
        rvMonths = view.findViewById(R.id.rvMonths);
        tvAlwaysActive = view.findViewById(R.id.tvAlwaysActive);
        tvAll = view.findViewById(R.id.tvAll);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        tvDaysQuantity = view.findViewById(R.id.tvDaysQuantity);
        ibIncrease = view.findViewById(R.id.ibIncrease);
        ibDecrease = view.findViewById(R.id.ibDecrease);
        ibDaysDecrease = view.findViewById(R.id.ibDaysDecrease);
        ibDaysIncrease = view.findViewById(R.id.ibDaysIncrease);
        ibAddUser = view.findViewById(R.id.ibAddUser);
        tvSeasonal = view.findViewById(R.id.tvSeasonal);
        btnCreateTask = view.findViewById(R.id.btnCreateTask);
        btnUpdateTask = view.findViewById(R.id.btnUpdateTask);

        bounceAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_anim);
        rvMonths.setNestedScrollingEnabled(false);

        layoutToHide.setVisibility(View.VISIBLE);
        layoutToHide.setAnimation(bounceAnim);
        etTaskName.setEnabled(false);
        taskHelper = new TaskHelper(mContext);


        area = (Area) (getArguments() != null ? getArguments().getSerializable("selectedArea") : null);
        task = (Task) (getArguments() != null ? getArguments().getSerializable("selectedTask") : null);

        dataHandlingOfTask();

        if (task != null) {
            etTaskName.setText(task.getTaskName());
            layoutToHide.setVisibility(View.GONE);
            etTaskName.setEnabled(true);
            btnCreateTask.setVisibility(View.GONE);
            btnUpdateTask.setVisibility(View.VISIBLE);

            btnUpdateTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateTask();
                }
            });


            ibAddUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BottomSheetUserAssignment bottomSheetUserAssignment = new BottomSheetUserAssignment();
                    bottomSheetUserAssignment.show(getChildFragmentManager(), "");
                }
            });

        }

    }

    private void dataHandlingOfTask() {

        tvSeasonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isSeasonal) {
                    isSeasonal = true;
                    tvSeasonal.setSelected(true);
                    tvSeasonal.setTextColor(getResources().getColor(R.color.white));
                    rvMonths.setVisibility(View.VISIBLE);
                    tvAlwaysActive.setVisibility(View.GONE);
                } else {
                    isSeasonal = false;
                    tvSeasonal.setSelected(false);
                    tvSeasonal.setTextColor(getResources().getColor(R.color.Black));
                    rvMonths.setVisibility(View.GONE);
                    tvAlwaysActive.setVisibility(View.VISIBLE);
                }
            }
        });


        ibIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                if (quantity <= 7) {
                    int newQty = quantity + 1;
                    tvQuantity.setText(String.valueOf(newQty));
                }
            }
        });
        ibDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                if (quantity > 1) {
                    int newQty = quantity - 1;
                    tvQuantity.setText(String.valueOf(newQty));
                }
            }
        });
        ibDaysIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String day = tvDaysQuantity.getText().toString();
                if (day.equals("Day")) {
                    tvDaysQuantity.setText("Week");
                } else if (day.equals("Week")) {
                    tvDaysQuantity.setText("Month");
                } else if (day.equals("Month")) {
                    tvDaysQuantity.setText("Year");
                }
            }
        });
        ibDaysDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String day = tvDaysQuantity.getText().toString();
                if (day.equals("Year")) {
                    tvDaysQuantity.setText("Month");
                } else if (day.equals("Week")) {
                    tvDaysQuantity.setText("Day");
                } else if (day.equals("Month")) {
                    tvDaysQuantity.setText("Week");
                }

            }
        });

        ibAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetUserAssignment bottomSheetUserAssignment = new BottomSheetUserAssignment();
                bottomSheetUserAssignment.show(getChildFragmentManager(), "");
            }
        });

        final CustomTimePicker c1 = new CustomTimePicker();
        c1.setTime("Jan");
        monthsList.add(c1);
        CustomTimePicker c2 = new CustomTimePicker();
        c2.setTime("Feb");
        monthsList.add(c2);
        CustomTimePicker c3 = new CustomTimePicker();
        c3.setTime("Mar");
        monthsList.add(c3);
        CustomTimePicker c4 = new CustomTimePicker();
        c4.setTime("Apr");
        monthsList.add(c4);
        CustomTimePicker c5 = new CustomTimePicker();
        c5.setTime("May");
        monthsList.add(c5);
        CustomTimePicker c6 = new CustomTimePicker();
        c6.setTime("Jun");
        monthsList.add(c6);
        CustomTimePicker c7 = new CustomTimePicker();
        c7.setTime("July");
        monthsList.add(c7);
        CustomTimePicker c8 = new CustomTimePicker();
        c8.setTime("Aug");
        monthsList.add(c8);
        CustomTimePicker c9 = new CustomTimePicker();
        c9.setTime("Sep");
        monthsList.add(c9);
        CustomTimePicker c10 = new CustomTimePicker();
        c10.setTime("Oct");
        monthsList.add(c10);
        CustomTimePicker c11 = new CustomTimePicker();
        c11.setTime("Nov");
        monthsList.add(c11);
        CustomTimePicker c12 = new CustomTimePicker();
        c12.setTime("Dec");
        monthsList.add(c12);

        customMonthAdapter = new CustomMonthAdapter(monthsList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                boolean isSelected = view.isSelected();

                if (isSelected) {
                    view.setSelected(false);
                } else {

                    view.setSelected(true);
                }

            }
        });


        rvMonths.setAdapter(customMonthAdapter);
        rvMonths.setLayoutManager(new GridLayoutManager(mContext, 6));


        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createTask();
            }
        });


    }

    private void createTask() {
        String taskName = etTaskName.getText().toString();
        if (TextUtils.isEmpty(taskName)) {
            return;
        } else {

            long startTime = System.currentTimeMillis();

            int qty = Integer.parseInt(tvQuantity.getText().toString());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());
            String startDate = sdf.format(startTime);

            String endDate;
            try {
                calendar.setTime(sdf.parse(startDate));

            } catch (ParseException e) {
                e.printStackTrace();
            }


            String day = tvDaysQuantity.getText().toString();


            if (day.equals("Day")) {
                calendar.add(Calendar.DAY_OF_MONTH, qty);
            } else if (day.equals("Week")) {
                calendar.add(Calendar.WEEK_OF_MONTH, qty);
            } else if (day.equals("Month")) {
                calendar.add(Calendar.MONTH, qty);

            } else if (day.equals("Year")) {
                calendar.add(Calendar.YEAR, qty);

            }
            endDate = sdf.format(calendar.getTime());
            // Toast.makeText(mContext, "startdate"+startDate+"new Date "+ endDate, Toast.LENGTH_SHORT).show();

            long insert = taskHelper.addTask(taskName, startDate, endDate, area.getId(), tvAll.getText().toString(), qty, day, 0, 1);


            if (insert > -1) {
                monthArrayList = new ArrayList<>();
                if (isSeasonal) {
                    for (int i = 0; i < monthsList.size(); i++) {
                        if (monthsList.get(i).isSelected()) {
                            CustomTimePicker customTimePicker = monthsList.get(i);
                            monthArrayList.add(customTimePicker);
                        }
                    }

                    if (monthArrayList.size() > 0) {

                        for (int i = 0; i < monthArrayList.size(); i++) {
                            taskHelper.addToSeasonTask(monthArrayList.get(i).getTime(), insert);
                        }
                    }
                }

                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                Toast.makeText(mContext, "Error occurred", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void updateTask() {
        String taskName = etTaskName.getText().toString();
        if (TextUtils.isEmpty(taskName)) {
            return;
        } else {

            long startTime = System.currentTimeMillis();

            int qty = Integer.parseInt(tvQuantity.getText().toString());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());
            String startDate = sdf.format(startTime);

            String endDate;
            try {
                calendar.setTime(sdf.parse(startDate));

            } catch (ParseException e) {
                e.printStackTrace();
            }


            String day = tvDaysQuantity.getText().toString();


            if (day.equals("Day")) {
                calendar.add(Calendar.DAY_OF_MONTH, qty);
            } else if (day.equals("Week")) {
                calendar.add(Calendar.WEEK_OF_MONTH, qty);
            } else if (day.equals("Month")) {
                calendar.add(Calendar.MONTH, qty);

            } else if (day.equals("Year")) {
                calendar.add(Calendar.YEAR, qty);

            }
            endDate = sdf.format(calendar.getTime());
            // Toast.makeText(mContext, "startdate"+startDate+"new Date "+ endDate, Toast.LENGTH_SHORT).show();

            long insert = taskHelper.updateTask(task.getTaskId(), taskName, startDate, endDate, tvAll.getText().toString(), qty, day, 0, 1);


            if (insert > -1) {
                monthArrayList = new ArrayList<>();
                if (isSeasonal) {
                    for (int i = 0; i < monthsList.size(); i++) {
                        if (monthsList.get(i).isSelected()) {
                            CustomTimePicker customTimePicker = monthsList.get(i);
                            monthArrayList.add(customTimePicker);
                        }
                    }

                    if (monthArrayList.size() > 0) {

                        for (int i = 0; i < monthArrayList.size(); i++) {
                            taskHelper.addToSeasonTask(monthArrayList.get(i).getTime(), insert);
                        }
                    }
                }

                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                Toast.makeText(mContext, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void updateEditText(CharSequence selectedArea) {
        if (selectedArea != null && selectedArea.length() > 1 && !selectedArea.equals("Custom")) {
            etTaskName.setEnabled(true);
            layoutAreaTop.setVisibility(View.VISIBLE);
            bounceBackAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_back_anim);
            layoutToHide.setAnimation(bounceBackAnim);
            layoutToHide.setVisibility(View.GONE);
            etTaskName.setText(selectedArea);

        } else if (selectedArea.equals("Custom")) {
            etTaskName.setEnabled(true);
            layoutAreaTop.setVisibility(View.VISIBLE);
            bounceBackAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_back_anim);
            layoutToHide.setAnimation(bounceBackAnim);
            layoutToHide.setVisibility(View.GONE);
            etTaskName.setText("");
            etTaskName.requestFocus();
        } else {
            etTaskName.setEnabled(false);
            bounceAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_anim);
            layoutToHide.setAnimation(bounceAnim);
            layoutToHide.setVisibility(View.VISIBLE);
            etTaskName.setText("");
        }
    }

    public void updateUserAssign(CharSequence selectedUser) {
            if(tvAll!=null && selectedUser!=null){
            tvAll.setText(selectedUser);
            }
           //tvAll.setText(selectedUser);
    }
}
