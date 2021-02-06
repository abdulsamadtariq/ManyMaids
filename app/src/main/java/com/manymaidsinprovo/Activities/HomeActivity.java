package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.manymaidsinprovo.Adapter.AreaAdapter;
import com.manymaidsinprovo.Model.Area;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.Notifications.ReminderBroadCast;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvAreas;
    private CardView cvAddArea;
    private ArrayList<Area> areaArrayList;
    private AreaAdapter areaAdapter;
    private LinearLayout layoutAreaInfo;
    private ImageView ivBooking, ivTodoList, ivSettings, ivCompletedList;
    private TextView tvUser;
    private ArrayList<Task> taskArrayList;
    private TaskHelper taskHelper;
    private View lineView, lineView1;
    private long diff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        cvAddArea = findViewById(R.id.cvAddArea);
        rvAreas = findViewById(R.id.rvAreas);
        layoutAreaInfo = findViewById(R.id.layoutAreaInfo);
        ivTodoList = findViewById(R.id.ivTodoList);
        ivBooking = findViewById(R.id.ivBookings);
        lineView = findViewById(R.id.lineView);
        lineView1 = findViewById(R.id.lineView1);
        ivSettings = findViewById(R.id.ivSettings);
        ivCompletedList = findViewById(R.id.ivCompletedList);
        tvUser = findViewById(R.id.tvUser);

        rvAreas.setNestedScrollingEnabled(false);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvAreas.getContext(), DividerItemDecoration.VERTICAL);
        rvAreas.addItemDecoration(dividerItemDecoration);

        areaArrayList = new ArrayList<>();
        taskHelper = new TaskHelper(HomeActivity.this);

        fetchArea();

        cvAddArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NewAreaActivity.class));

            }
        });

        ivTodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ToDoActivity.class));
            }
        });
        ivBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, UserBookingActivity.class));
            }
        });
        ivCompletedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CompletedTaskActivity.class));

            }
        });
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));

            }
        });
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, UserManagmentActivity.class));
            }
        });


        int taskCount = taskHelper.getNoOfTaskRemaining();
        if (taskCount == 0) {
            ReminderBroadCast.title = "Many Maids";
            ReminderBroadCast.text = "you have no task Yet.please add some";
            ReminderBroadCast.landingClass=HomeActivity.class;

        } else if (taskCount > 0) {
            ReminderBroadCast.title = "Many Maids";
            ReminderBroadCast.text = "you have " + taskCount + " remaining Task.";
            ReminderBroadCast.landingClass=HomeActivity.class;
        }

        boolean notification = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this)
                .getBoolean("notification", false);
        if (notification) {
            showNotification();
        }

        boolean userEnable = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this)
                .getBoolean("userEnable", false);
        if (!userEnable) {
            tvUser.setVisibility(View.GONE);
        }

    }

    private void showNotification() {
        Intent intent = new Intent(HomeActivity.this, ReminderBroadCast.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, intent, 0);

        long currentTime = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + 1000 * 2, pendingIntent);

    }

    private void fetchArea() {

        areaArrayList.clear();
        areaArrayList = taskHelper.getArea();
        if (areaArrayList.size() == 0) {
            layoutAreaInfo.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.GONE);
            ivTodoList.setVisibility(View.GONE);
            ivBooking.setVisibility(View.GONE);
            ivCompletedList.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
            lineView1.setVisibility(View.GONE);
        } else {
            layoutAreaInfo.setVisibility(View.GONE);
            ivTodoList.setVisibility(View.VISIBLE);
            ivBooking.setVisibility(View.VISIBLE);
            ivCompletedList.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            lineView1.setVisibility(View.VISIBLE);
            tvUser.setVisibility(View.VISIBLE);

            String selectedUser = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getString("selectedUser", null);
            if (selectedUser != null) {
                tvUser.setText(selectedUser);
            }

            taskArrayList = new ArrayList<>();
            taskArrayList.clear();

            taskHelper = new TaskHelper(HomeActivity.this);
            for (Area area : areaArrayList) {

                taskArrayList = taskHelper.getTask(area.getId());
                if (taskArrayList.size() == 0) {
                    taskHelper.updateAreaIndicator(0, area.getId());
                } else {
                    updateIndicator(area);
                }

                ArrayList<Task> tempList = taskHelper.getTask(area.getId());
                taskArrayList.clear();
                taskArrayList.addAll(tempList);
               // taskArrayList = taskHelper.getTask(area.getId());
                double sum = 0;
                for (Task task : taskArrayList) {
                    sum += task.getTaskIndicator();
                }
                double avg = sum / taskArrayList.size();
                taskHelper.updateAreaIndicator((int) avg, area.getId());
            }

            ArrayList<Area> tempList = taskHelper.getArea();
            areaArrayList.clear();
            areaArrayList.addAll(tempList);
        }


        areaAdapter = new AreaAdapter(areaArrayList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //whole card click
                Area area = areaArrayList.get(i);
                Intent intent = new Intent(HomeActivity.this, TaskActivity.class);
                intent.putExtra("selectedArea", area);
                startActivity(intent);

            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //onDelete

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Warning")
                        .setMessage("All the corresponding task will be deleted.\n Are you sure to delete?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                int deleteArea = taskHelper.deleteArea(areaArrayList.get(position).getId());
                                if (deleteArea > -1) {
                                    ArrayList<Area> tempList = taskHelper.getArea();
                                    areaArrayList.clear();
                                    areaArrayList.addAll(tempList);
                                    areaAdapter.notifyItemRemoved(position);

                                    if (areaArrayList.size() == 0) {
                                        layoutAreaInfo.setVisibility(View.VISIBLE);
                                        tvUser.setVisibility(View.GONE);
                                        ivTodoList.setVisibility(View.GONE);
                                        ivBooking.setVisibility(View.GONE);
                                        ivCompletedList.setVisibility(View.GONE);
                                        lineView.setVisibility(View.GONE);
                                        lineView1.setVisibility(View.GONE);

                                    } else {
                                        layoutAreaInfo.setVisibility(View.GONE);
                                        ivTodoList.setVisibility(View.VISIBLE);
                                        ivBooking.setVisibility(View.VISIBLE);
                                        ivCompletedList.setVisibility(View.VISIBLE);
                                        lineView.setVisibility(View.VISIBLE);
                                        lineView1.setVisibility(View.VISIBLE);
                                        tvUser.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        })
                        .setNeutralButton("cancel", null)
                        .create().show();
            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //onEdit

                Toast.makeText(HomeActivity.this, "Edit", Toast.LENGTH_SHORT).show();
            }
        });
        rvAreas.setAdapter(areaAdapter);
        rvAreas.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

    }


    private void updateIndicator(Area area) {
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


    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchArea();
    }
}
