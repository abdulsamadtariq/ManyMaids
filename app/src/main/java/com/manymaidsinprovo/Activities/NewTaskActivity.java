package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.manymaidsinprovo.Dialouge.BottomSheetUserAssignment;
import com.manymaidsinprovo.Fragments.AreaTopFragment;
import com.manymaidsinprovo.Fragments.BasicTaskFragment;
import com.manymaidsinprovo.Fragments.CustomTaskFragment;
import com.manymaidsinprovo.Fragments.SpecialTaskFragment;
import com.manymaidsinprovo.Fragments.TaskTopFragment;
import com.manymaidsinprovo.Model.Area;
import com.manymaidsinprovo.PagerAdapter.AreaSectionPagerAdapter;
import com.manymaidsinprovo.PagerAdapter.TaskSectionPagerAdapter;
import com.manymaidsinprovo.R;

public class NewTaskActivity extends AppCompatActivity implements BasicTaskFragment.OnTaskSelectionListener, SpecialTaskFragment.OnTaskSpecialSelectionListener, CustomTaskFragment.OnCustomTaskSelectionListener, BottomSheetUserAssignment.OnUserSelectionListener {

    private TabLayout tbTaskChoose;
    private ViewPager viewPagerTaskBottom;
    private TaskSectionPagerAdapter taskSectionPagerAdapter;
    private TaskTopFragment taskTopFragment;
    private Area area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("New Task");


        tbTaskChoose = findViewById(R.id.tbTaskChoose);
        viewPagerTaskBottom = findViewById(R.id.viewPagerTaskBottom);

        taskTopFragment = new TaskTopFragment();
        area = (Area) getIntent().getSerializableExtra("selectedArea");

        FragmentManager fm = getSupportFragmentManager();
        taskSectionPagerAdapter = new TaskSectionPagerAdapter(fm, area);


        viewPagerTaskBottom.setAdapter(taskSectionPagerAdapter);
        tbTaskChoose.setupWithViewPager(viewPagerTaskBottom);

        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedArea", area);

        getSupportActionBar().setSubtitle(area.getAreaName());

        taskTopFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flTaskTop, taskTopFragment)
                .commitNowAllowingStateLoss();
    }

    @Override
    public void onTaskChangeData(CharSequence data) {
        taskTopFragment.updateEditText(data);
    }

    @Override
    public void onUserChangeData(CharSequence data) {
        taskTopFragment.updateUserAssign(data);
    }
}
