package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manymaidsinprovo.Activities.TaskActivity;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskStatusFragment extends Fragment {

    private Task task;
    private TextView tvTaskName, tvHowOften, tvDue;
    private Button btnJustDidIt;
    private LinearLayout layoutMainIndicator, layoutNormal, layoutMedium, layoutItem;
    private ImageView ivNormalIndicator, ivNormalIndicator2, ivMediumIndicator, ivMediumIndicator2, ivJustBeforeProactiveIndicator, ivProactiveIndicator;
    private long diff;
    private Context mContext;
    private TaskHelper taskHelper;
    private LineChartView chart;


    public TaskStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        task = (Task) (getArguments() != null ? getArguments().getSerializable("selectedTask") : null);

        tvTaskName = view.findViewById(R.id.tvTaskName);
        tvHowOften = view.findViewById(R.id.tvHowOften);
        tvDue = view.findViewById(R.id.tvDue);
        btnJustDidIt = view.findViewById(R.id.btnJustDidIt);
        mContext = getContext();
        taskHelper = new TaskHelper(mContext);

        layoutMainIndicator = view.findViewById(R.id.layoutMainIndicator);
        layoutNormal = view.findViewById(R.id.layoutNormal);
        layoutMedium = view.findViewById(R.id.layoutMedium);
        layoutItem = view.findViewById(R.id.layoutItem);
        ivNormalIndicator = view.findViewById(R.id.ivNormalIndicator);
        ivNormalIndicator2 = view.findViewById(R.id.ivNormalIndicator2);
        ivMediumIndicator = view.findViewById(R.id.ivMediumIndicator);
        ivMediumIndicator2 = view.findViewById(R.id.ivMediumIndicator2);
        ivJustBeforeProactiveIndicator = view.findViewById(R.id.ivJustBeforeProactiveIndicator);
        ivProactiveIndicator = view.findViewById(R.id.ivProactiveIndicator);
        chart = view.findViewById(R.id.chartView);

        showChart();

        taskIndicatorValue();

        btnJustDidIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Sure")
                        .setMessage("Are you sure you have completed the task?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                long startTime = System.currentTimeMillis();

                                int qty = task.getHowOftenQty();

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());
                                String startDate = sdf.format(startTime);

                                String endDate;
                                try {
                                    calendar.setTime(sdf.parse(startDate));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                String day = task.getHowOftenDays();


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

                                long insert = taskHelper.updateJustDidItTask(task.getTaskId(), startDate, endDate, 1, startDate, 1);
                                taskHelper.addToTaskHistory(task,1,startDate);


                                if (insert > -1) {
                                    getActivity().finish();
                                }

                            }
                        })
                        .setNeutralButton("cancel", null)
                        .create().show();

            }
        });
    }

    private void showChart() {

        chart.setInteractive(true);

        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(0, 2));
        values.add(new PointValue(1, 4));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(3, 4));


        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        LineChartView chart = new LineChartView(mContext);
        chart.setLineChartData(data);
    }

    private void taskIndicatorValue() {
        int indicatorValue = task.getTaskIndicator();

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


        ///////

        long currentTimeStamp = System.currentTimeMillis();
        //long endTime = Long.parseLong(dataSet.get(position).getEndTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());

        try {
            Date endDate = sdf.parse(task.getEndTime());
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


        ////
        tvHowOften.setText(task.getHowOftenQty() + " " + task.getHowOftenDays());

        tvTaskName.setText(task.getTaskName());
    }
}
