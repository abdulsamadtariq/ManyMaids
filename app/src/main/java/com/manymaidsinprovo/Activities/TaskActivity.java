package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.manymaidsinprovo.Adapter.TaskAdapter;
import com.manymaidsinprovo.Model.Area;
import com.manymaidsinprovo.Model.CustomTimePicker;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {

    private ImageView ivHide, ivArea;
    private CardView cvAddTask;
    private boolean isAreaImageHidden = false;
    private Area area;
    private RecyclerView rvTask;
    private ArrayList<Task> taskArrayList;
    private TaskAdapter taskAdapter;
    private TaskHelper taskHelper;
    private LinearLayout layoutTaskInfo;
    private long diff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        area = (Area) getIntent().getSerializableExtra("selectedArea");
        if (area != null) {
            changeTheme();
            setTitle(area.getAreaName());
        }
        setContentView(R.layout.activity_task);

        ivHide = findViewById(R.id.ivHide);
        ivArea = findViewById(R.id.ivArea);
        cvAddTask = findViewById(R.id.cvAddTask);
        rvTask = findViewById(R.id.rvTask);
        layoutTaskInfo = findViewById(R.id.layoutTaskInfo);
        taskArrayList = new ArrayList<>();
        taskHelper = new TaskHelper(TaskActivity.this);

        setAreaImage();

        cvAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TaskActivity.this, NewTaskActivity.class);
                intent.putExtra("selectedArea", area);
                startActivity(intent);
            }
        });


        ivHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isAreaImageHidden) {
                    ivHide.setImageDrawable(getResources().getDrawable(R.drawable._bt_icon_chevron_down));
                    isAreaImageHidden = false;
                    ivArea.setVisibility(View.VISIBLE);
                } else {
                    isAreaImageHidden = true;
                    ivArea.setVisibility(View.GONE);
                    ivHide.setImageDrawable(getResources().getDrawable(R.drawable._bt_icon_chevron_up));
                }
            }
        });

        fetchTasks();
    }

    private void fetchTasks() {
        taskArrayList.clear();

        taskArrayList = taskHelper.getTask(area.getId());

        if (taskArrayList.size() == 0) {
            layoutTaskInfo.setVisibility(View.VISIBLE);
            taskHelper.updateAreaIndicator(0, area.getId());
            return;
        } else {
            layoutTaskInfo.setVisibility(View.GONE);

            updateIndicator();
        }

        taskAdapter = new TaskAdapter(taskArrayList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //item click

                Task task = taskArrayList.get(position);
                Intent intent = new Intent(TaskActivity.this, TaskDetailActivity.class);
                intent.putExtra("selectedTask", task);
                startActivity(intent);
            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //delete click

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                builder.setTitle("Warning")
                        .setMessage("The corresponding task will be deleted.\n Are you sure to delete?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                int deleteTask = taskHelper.deleteTask(taskArrayList.get(position).getTaskId());
                                if (deleteTask > -1) {
                                    ArrayList<Task> tempList = taskHelper.getTask(area.getId());
                                    taskArrayList.clear();
                                    taskArrayList.addAll(tempList);
                                    taskAdapter.notifyItemRemoved(position);

                                    if (taskArrayList.size() == 0) {
                                        layoutTaskInfo.setVisibility(View.VISIBLE);
                                    } else {
                                        layoutTaskInfo.setVisibility(View.GONE);
                                    }
                                }
                            }
                        })
                        .setNeutralButton("cancel", null)
                        .create().show();

            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //edit click

                Task task = taskArrayList.get(position);
                Intent intent = new Intent(TaskActivity.this, TaskDetailActivity.class);
                intent.putExtra("selectedTask", task);
                startActivity(intent);
            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //just did it click

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                builder.setTitle("Sure")
                        .setMessage("Are you sure you have completed the task?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Task task = taskArrayList.get(position);
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


                                switch (day) {
                                    case "Day":
                                        calendar.add(Calendar.DAY_OF_MONTH, qty);
                                        break;
                                    case "Week":
                                        calendar.add(Calendar.WEEK_OF_MONTH, qty);
                                        break;
                                    case "Month":
                                        calendar.add(Calendar.MONTH, qty);

                                        break;
                                    case "Year":
                                        calendar.add(Calendar.YEAR, qty);

                                        break;
                                }


                                endDate = sdf.format(calendar.getTime());
                                // Toast.makeText(mContext, "startdate"+startDate+"new Date "+ endDate, Toast.LENGTH_SHORT).show();

                                taskHelper.updateJustDidItTask(task.getTaskId(), startDate, endDate, 1, startDate, 1);
                               // taskHelper.updateJustDidItTask(1, "", "", 1, "", 1);
                                long insert=taskHelper.addToTaskHistory(task,1,startDate);


                                if (insert > -1) {
                                    ArrayList<Task> tempList = taskHelper.getTask(area.getId());
                                    taskArrayList.clear();
                                    taskArrayList.addAll(tempList);
                                    taskAdapter.notifyItemChanged(position);
                                    updateIndicator();

                                } else {
                                    Toast.makeText(TaskActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                }


                            }
                        })
                        .setNeutralButton("cancel", null)
                        .create().show();


            }
        });

        rvTask.setAdapter(taskAdapter);
        rvTask.setLayoutManager(new LinearLayoutManager(TaskActivity.this, RecyclerView.VERTICAL, false));
        rvTask.addItemDecoration(new DividerItemDecoration(TaskActivity.this, DividerItemDecoration.VERTICAL));

    }

    private void updateIndicator() {
        double sum = 0;
        for (Task task : taskArrayList) {
            sum += task.getTaskIndicator();
        }
        double avg = sum / taskArrayList.size();
        //  Toast.makeText(this, "sum:"+sum+" avg:"+avg, Toast.LENGTH_SHORT).show();
        taskHelper.updateAreaIndicator((int) avg, area.getId());
        ////////////////////////////////////////Calculating and updating Due/overDue///////////////////////////////////


        long currentTimeStamp = System.currentTimeMillis();
        //long endTime = Long.parseLong(dataSet.get(position).getEndTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault());

        for (Task task : taskArrayList) {
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
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (min == 1) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());

            } else if (min > 1 && min <= 60) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (hours == 1) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (hours > 1 && hours <= 24) {
                taskHelper.updateTaskIndicator(5, task.getTaskId());
            } else if (days == 1) {
                taskHelper.updateTaskIndicator(5, task.getTaskId());
            } else if (days > 1 && days <= 30) {
                taskHelper.updateTaskIndicator(4, task.getTaskId());
            } else if (month == 1) {
                taskHelper.updateTaskIndicator(3, task.getTaskId());
            } else if (month > 1 && month <= 12) {
                taskHelper.updateTaskIndicator(3, task.getTaskId());
            } else if (Year > 1) {
                taskHelper.updateTaskIndicator(2, task.getTaskId());
            } else if (min < -1 && min > -3) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (min < -1 && min > -60) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (hours == -1) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (hours < 1 && hours >= -24) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (days == -1) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (days < -1 && days >= -30) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (month == -1) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (month < -1 && month >= -12) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            } else if (Year < -1) {
                taskHelper.updateTaskIndicator(6, task.getTaskId());
            }

        }
        ArrayList<Task> tempList = taskHelper.getTask(area.getId());
        taskArrayList.clear();
        taskArrayList.addAll(tempList);

    }

    private void setAreaImage() {
        switch (String.valueOf(area.getAreaName())) {
            case "Kitchen":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.kitchen_blue_line));
                break;
            case "Living Room":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.living_room_blue_line));
                break;
            case "Dining Room":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.dining_room_blue_line));
                break;
            case "Bedroom":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.bedroom_blue_line));
                break;
            case "Bathroom":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.bathroom_blue_line));
                break;
            case "Toilet":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.toilet_blue_line));
                break;
            case "Children":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.kids_room_blue_line));
                break;
            case "Office":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.home_office_blue_line));
                break;
            case "Entrance":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.entrance_blue_line));
                break;
            case "Laundry":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.laundry_blue_line));
                break;
            case "Basement":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.basement_blue_line));
                break;
            case "Attic":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.attic_blue_line));
                break;
            case "General":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.inside_blue_line));
                break;
            case "Garage":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.garage_blue_line));
                break;
            case "Garden":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.garden_blue_line));
                break;
            case "House":
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.house_blue_line));
                break;
            default:
                ivArea.setImageDrawable(getResources().getDrawable(R.drawable.inside_blue_line));
        }

    }

    private void changeTheme() {


        switch (String.valueOf(area.getAreaName())) {
            case "Kitchen":
                setTheme(R.style.AreaTypeBeigeLighter);
                break;
            case "Living Room":
                setTheme(R.style.AreaTypeBlack);
                break;
            case "Dining Room":
                setTheme(R.style.AreaTypeBlackLighter);
                break;
            case "Bedroom":
                setTheme(R.style.AreaTypeDarkBlueLighter);
                break;
            case "Bathroom":
                setTheme(R.style.AreaTypeBrownLighter);
                break;
            case "Toilet":
                setTheme(R.style.AreaTypeBrownLighter);
                break;
            case "Children":
                setTheme(R.style.AreaTypeDeepPurple);
                break;
            case "Office":
                setTheme(R.style.AreaTypeFreshBlue);
                break;
            case "Entrance":
                setTheme(R.style.AreaTypeDeepPurpleLighter);
                break;
            case "Laundry":
                setTheme(R.style.AreaTypeLimeLighter);
                break;
            case "Basement":
                setTheme(R.style.AreaTypeGray);
                break;
            case "Attic":
                setTheme(R.style.AreaTypeLightGrayLighter);
                break;
            case "General":
                setTheme(R.style.AreaTypeLime);
                break;
            case "Garage":
                setTheme(R.style.AreaTypeLightBrown);
                break;
            case "Garden":
                setTheme(R.style.AreaTypeOrange);
                break;
            case "House":
                setTheme(R.style.AreaTypeLightBrownLighter);
                break;
            default:
                setTheme(R.style.AreaTypeTeal);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchTasks();
    }
}
