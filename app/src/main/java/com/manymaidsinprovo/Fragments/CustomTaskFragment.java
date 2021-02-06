package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.manymaidsinprovo.Model.Area;
import com.manymaidsinprovo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomTaskFragment extends Fragment {

    private NumberPicker npCustom;
    private Context mContext;
    private OnCustomTaskSelectionListener mListener;
    private Area area;


    public CustomTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext=getContext();
        return inflater.inflate(R.layout.fragment_custom_task, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        area = (Area) getArguments().getSerializable("selectedArea");

        npCustom=view.findViewById(R.id.npCustom);
        final String[]  data=new String[]{" ","Kitchen","Living Room","Dining Room","Bedroom","Bathroom","Toilet","Children","Office","Entrance","Laundry","Basement ","Attic","General","Custom"};
        npCustom.setMinValue(0);
        npCustom.setMaxValue(data.length-1);

        npCustom.setDisplayedValues(data);
        npCustom.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npCustom.setWrapSelectorWheel(false);

        npCustom.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int pos=numberPicker.getValue();
                String datum=data[pos];
                mListener.onTaskChangeData(datum);
            }
        });
    }

    public interface OnCustomTaskSelectionListener {
        void onTaskChangeData(CharSequence data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnCustomTaskSelectionListener){
            mListener=(OnCustomTaskSelectionListener)context;
        }else {
            throw new RuntimeException(context.toString()+" must implement OnAreaSelectionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener=null;
    }

}
