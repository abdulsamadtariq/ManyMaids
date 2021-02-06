package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskNoteFragment extends Fragment {


    private EditText etTaskNote;
    private Task task;
    private TaskHelper taskHelper;
    private Context mContext;

    public TaskNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        task = (Task) (getArguments() != null ? getArguments().getSerializable("selectedTask") : null);
        etTaskNote = view.findViewById(R.id.etTaskNote);
        taskHelper = new TaskHelper(mContext);

        etTaskNote.setText(task.getNote());
        etTaskNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                taskHelper.updateTaskNote(task.getTaskId(),charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
