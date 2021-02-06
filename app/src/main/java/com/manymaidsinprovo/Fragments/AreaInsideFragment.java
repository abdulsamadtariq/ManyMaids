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

import com.manymaidsinprovo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AreaInsideFragment extends Fragment {


    private NumberPicker npInside;
    private Context mContext;
    private OnAreaSelectionListener mListener;

    public AreaInsideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext=getContext();
        return inflater.inflate(R.layout.fragment_area_inside, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        npInside=view.findViewById(R.id.npInside);
        final String[]  data=new String[]{" ","Kitchen","Living Room","Dining Room","Bedroom","Bathroom","Toilet","Children","Office","Entrance","Laundry","Basement ","Attic","General","Custom"};
        npInside.setMinValue(0);
        npInside.setMaxValue(data.length-1);

        npInside.setDisplayedValues(data);
        npInside.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npInside.setWrapSelectorWheel(false);

        npInside.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int pos=numberPicker.getValue();
                String datum=data[pos];
                mListener.onAreaChangeData(datum);
            }
        });
    }

    public interface OnAreaSelectionListener {
        void onAreaChangeData(CharSequence data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnAreaSelectionListener){
            mListener=(OnAreaSelectionListener)context;
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
