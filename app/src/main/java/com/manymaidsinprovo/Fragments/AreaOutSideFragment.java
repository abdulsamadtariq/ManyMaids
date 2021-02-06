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
import android.widget.Toast;

import com.manymaidsinprovo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AreaOutSideFragment extends Fragment {

    private NumberPicker npOutside;
    private Context mContext;
    private OnAreaOutSelectionListener  mListener;

    public AreaOutSideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext=getContext();

        return inflater.inflate(R.layout.fragment_area_out_side, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        npOutside=view.findViewById(R.id.npOutside);

        final String[]  data=new String[]{" ","Garage","Garden","House","Custom"};

        npOutside.setMinValue(0);
        npOutside.setMaxValue(data.length-1);

        npOutside.setDisplayedValues(data);
        npOutside.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npOutside.setWrapSelectorWheel(false);

        npOutside.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int pos=numberPicker.getValue();
                String datum=data[pos];
                mListener.onAreaChangeData(datum);
            }
        });
    }

    public interface OnAreaOutSelectionListener {
        void onAreaChangeData(CharSequence data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnAreaOutSelectionListener ){
            mListener=(OnAreaOutSelectionListener )context;
        }else {
            throw new RuntimeException(context.toString()+" must implement OnAreaOutSelectionListener ");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener=null;
    }
}
