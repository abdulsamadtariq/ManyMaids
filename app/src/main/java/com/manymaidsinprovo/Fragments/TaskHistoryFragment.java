package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manymaidsinprovo.Activities.CompletedTaskActivity;
import com.manymaidsinprovo.Adapter.CompletedTaskAdapter;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskHistoryFragment extends Fragment {

    private Task task;
    private RecyclerView rvTodo;
    private TaskHelper taskHelper;
    private ArrayList<Task> taskArrayList;
    private CompletedTaskAdapter completedTaskAdapter;
    private Context mContext;

    public TaskHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        rvTodo = view.findViewById(R.id.rvCompleted);

        taskHelper = new TaskHelper(mContext);
        taskArrayList = new ArrayList<>();

        task = (Task) (getArguments() != null ? getArguments().getSerializable("selectedTask") : null);


        fetchHistoryItems();
    }

    private void fetchHistoryItems() {

        taskArrayList.clear();
        taskArrayList = taskHelper.getTaskHistoryWithId(task.getTaskId());

        completedTaskAdapter = new CompletedTaskAdapter(taskArrayList);

        rvTodo.setAdapter(completedTaskAdapter);
        rvTodo.setLayoutManager(new LinearLayoutManager(mContext.getApplicationContext()));

    }

}
