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
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.util.ArrayList;

public class AreaCopyFragment extends Fragment {


    private NumberPicker npCopy;
    private Context mContext;
    private TaskHelper areaHelper;
    private ArrayList<Area> areaArrayList;
    private OnAreaCopySelectionListener mListener;
    private String[]  data;

    public AreaCopyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext=getContext();

        return inflater.inflate(R.layout.fragment_area_custom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        npCopy=view.findViewById(R.id.npCopy);
        areaHelper=new TaskHelper(mContext);

        areaArrayList=new ArrayList<>();

        areaArrayList=areaHelper.getArea();

        data=new String[areaArrayList.size()+ 1];

        data[0]=" ";
        for (int i=0;i<areaArrayList.size();i++){
            data[i+1]=areaArrayList.get(i).getAreaName();
        }


        npCopy.setMinValue(0);
        npCopy.setMaxValue(data.length-1);

        npCopy.setDisplayedValues(data);
        npCopy.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npCopy.setWrapSelectorWheel(false);

        npCopy.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int pos=numberPicker.getValue();
                String datum=data[pos];
                mListener.onAreaChangeData(datum);
               // Toast.makeText(mContext, "data:"+datum, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public interface OnAreaCopySelectionListener {
        void onAreaChangeData(CharSequence data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnAreaCopySelectionListener){
            mListener=(OnAreaCopySelectionListener)context;
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
