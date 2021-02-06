package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.manymaidsinprovo.Dialouge.BottomSheetUserAssignment;
import com.manymaidsinprovo.Fragments.TaskTopFragment;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.PagerAdapter.TaskDetailSectionPagerAdapter;
import com.manymaidsinprovo.R;

public class TaskDetailActivity extends AppCompatActivity implements BottomSheetUserAssignment.OnUserSelectionListener {

    private TabLayout tbTaskDetails;
    private ViewPager vpTaskDetails;
    private TaskDetailSectionPagerAdapter detailSectionPagerAdapter;
    private TaskTopFragment taskTopFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Task");
        Task task = (Task) getIntent().getSerializableExtra("selectedTask");
        getSupportActionBar().setSubtitle(task.getTaskName());


        tbTaskDetails = findViewById(R.id.tbTaskDetails);
        vpTaskDetails = findViewById(R.id.vpTaskDetails);


        detailSectionPagerAdapter = new TaskDetailSectionPagerAdapter(getSupportFragmentManager(), task);

        vpTaskDetails.setAdapter(detailSectionPagerAdapter);
        tbTaskDetails.setupWithViewPager(vpTaskDetails);


        taskTopFragment = new TaskTopFragment();


    }

    @Override
    public void onUserChangeData(CharSequence data) {
        taskTopFragment.updateUserAssign(data);
    }
}
