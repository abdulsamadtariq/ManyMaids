package com.manymaidsinprovo.PagerAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.manymaidsinprovo.Fragments.BasicTaskFragment;
import com.manymaidsinprovo.Fragments.CustomTaskFragment;
import com.manymaidsinprovo.Fragments.SpecialTaskFragment;
import com.manymaidsinprovo.Fragments.TaskHistoryFragment;
import com.manymaidsinprovo.Fragments.TaskNoteFragment;
import com.manymaidsinprovo.Fragments.TaskStatusFragment;
import com.manymaidsinprovo.Fragments.TaskTopFragment;
import com.manymaidsinprovo.Model.Area;
import com.manymaidsinprovo.Model.Task;

public class TaskDetailSectionPagerAdapter extends FragmentPagerAdapter {

    private TaskStatusFragment taskStatusFragment;
    private TaskNoteFragment taskNoteFragment;
    private TaskHistoryFragment taskHistoryFragment;
    private TaskTopFragment taskTopFragment;
    private Task task;

    public TaskDetailSectionPagerAdapter(@NonNull FragmentManager fm, Task task) {
        super(fm);
        this.task = task;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {

            return "Status";
        } else if (position == 1) {
            return "Notes";

        } else if (position == 2) {
            return "Edit";

        } else if (position == 3) {
            return "History";
        }
        return super.getPageTitle(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedTask", task);

        taskStatusFragment = new TaskStatusFragment();
        taskNoteFragment = new TaskNoteFragment();
        taskTopFragment = new TaskTopFragment();
        taskHistoryFragment = new TaskHistoryFragment();

        taskStatusFragment.setArguments(bundle);
        taskNoteFragment.setArguments(bundle);
        taskTopFragment.setArguments(bundle);
        taskHistoryFragment.setArguments(bundle);

        if (position == 0) {
            return taskStatusFragment;
        } else if (position == 1) {
            return taskNoteFragment;
        } else if (position == 2) {
            return taskTopFragment;
        } else if (position == 3) {
            return taskHistoryFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
