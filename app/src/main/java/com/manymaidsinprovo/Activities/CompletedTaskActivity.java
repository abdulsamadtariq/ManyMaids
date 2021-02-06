package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.manymaidsinprovo.Adapter.CompletedTaskAdapter;
import com.manymaidsinprovo.Adapter.ToDoTaskAdapter;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.Model.UserM;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.util.ArrayList;

public class CompletedTaskActivity extends AppCompatActivity {

    private Spinner spPeriod, spGroupBy, spAssignedTo;
    private RecyclerView rvTodo;
    private TaskHelper taskHelper;
    private ArrayList<UserM> userMArrayList;
    private CompletedTaskAdapter completedTaskAdapter;
    private AdView adView;
    private ArrayList<Task> taskArrayList;
    private int dueWithInNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
        setTitle("Completed");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spPeriod = findViewById(R.id.spPeriod);
        spGroupBy = findViewById(R.id.spGroupBy);
        spAssignedTo = findViewById(R.id.spAssignedTo);
        rvTodo = findViewById(R.id.rvCompleted);
        taskHelper = new TaskHelper(CompletedTaskActivity.this);
        taskArrayList = new ArrayList<>();



        userMArrayList = taskHelper.getUser();
        if (userMArrayList.size() > 0) {

            ArrayAdapter<UserM> userAdapter = new ArrayAdapter<UserM>(getApplicationContext(), R.layout.spinner_item, userMArrayList);

            spAssignedTo.setAdapter(userAdapter);
        }

        loadAdmobAdd();


        fetchToDoItems();


        spPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchToDoItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spGroupBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchToDoItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spAssignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchToDoItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadAdmobAdd() {

        adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
    }
    private void fetchToDoItems() {

        taskArrayList.clear();
        String dueWithin = spPeriod.getSelectedItem().toString().trim();
        String groupBy = spGroupBy.getSelectedItem().toString().trim();
        String userName = spAssignedTo.getSelectedItem().toString().trim();

        checkDue(dueWithin);
        taskArrayList = taskHelper.getCompletedTask(dueWithInNo, groupBy, userName);

        completedTaskAdapter = new CompletedTaskAdapter(taskArrayList);

        rvTodo.setAdapter(completedTaskAdapter);
        rvTodo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private void checkDue(String dueWithin) {

        switch (dueWithin) {
            case "Today":
                dueWithInNo = 1;
                break;
            case "1 Day":
                dueWithInNo = 2;
            case "2 Days":
                dueWithInNo = 3;
                break;
            case "3 Days":
                dueWithInNo = 4;
            case "4 Days":
                dueWithInNo = 5;
                break;
            case "1 week":
                dueWithInNo = 8;
            case "2 weeks":
                dueWithInNo = 15;
                break;
            case "All":
                dueWithInNo = 0;
                break;

        }
    }

}
