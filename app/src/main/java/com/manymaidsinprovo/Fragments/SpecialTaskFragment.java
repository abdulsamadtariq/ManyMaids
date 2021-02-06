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
public class SpecialTaskFragment extends Fragment {

    private NumberPicker npSpecial;
    private Context mContext;
    private OnTaskSpecialSelectionListener mListener;
    private Area area;
    private String[] data;

    public SpecialTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        return inflater.inflate(R.layout.fragment_special_task, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        area = (Area) getArguments().getSerializable("selectedArea");
        npSpecial = view.findViewById(R.id.npSpecial);

        addData();
        npSpecial.setMinValue(0);
        npSpecial.setMaxValue(data.length - 1);

        npSpecial.setDisplayedValues(data);
        npSpecial.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npSpecial.setWrapSelectorWheel(false);

        npSpecial.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int pos = numberPicker.getValue();
                String datum = data[pos];
                mListener.onTaskChangeData(datum);
            }
        });
    }

    public interface OnTaskSpecialSelectionListener {
        void onTaskChangeData(CharSequence data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnTaskSpecialSelectionListener) {
            mListener = (OnTaskSpecialSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAreaOutSelectionListener ");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    private void addData() {
        switch (String.valueOf(area.getAreaName())) {
            case "Kitchen":
                data = new String[]{" ","Take out garbage","Lay the table","Do the dishes","Clean stainless steel","Dishwasher|Salt","Dishwasher|Rinse aid","Clean toaster","Clean microwave","Descale coffee maker","Descale electric kettle","Organize Pantry","Clean Pantry","Oil cutting boards","Oil table tops","Change water filter","Clean behind[X]"};
                break;
            case "Living Room":
                data = new String[]{" ", "Vacuum pillows", "Wash blankets", "Furniture|Vacuum", "Furniture|Wash cover", "Curtains|Vacuum", "Curtains|Wash", "Clean ceiling fan", "clean fireplace", "Declutter/Discard", "Custom"};
                break;
            case "Dining Room":
                data = new String[]{" ","Curtains|Vacuum","Curtains|Wash","Clean ceiling fan","clean fireplace"};
                break;
            case "Bedroom":
                data = new String[]{" ","Clean under bed","Clean mirror","Clean closet doors","Flip mattress","Seasonal wardrobe change","Curtains|Vacuum","Curtains|Wash","Clean ceiling fan"};
                break;
            case "Bathroom":
                data = new String[]{" ","Clean bathtub","Clean trash can","Change toothbrush","Tiles | Mold prevention","Tiles|Descale","Clean drains","Curtains|Wash","Clean ceiling fan"};
                break;
            case "Toilet":
                data = new String[]{" ","Clean trash can","Change toothbrush"};
                break;
            case "Children":
                data = new String[]{" ","Clean under bed","Clean closet/cabinet doors","Clean mirror","Clean closet doors","Flip mattress","Seasonal wardrobe change","Curtains|Vacuum","Curtains|Wash","Clean ceiling fan"};
                break;
            case "Office":
                data = new String[]{" ","File paper","Shred papers","Zero inbox","Bills & bookkeeping","Curtains|Vacuum","Curtains|Wash"};
                break;
            case "Entrance":
                data = new String[]{" ","Vacuum stairway","Clean stairway railing","Polish shoes","Seasonal coat/shoe change","Seasonal wardrobe change","Curtains|Vacuum","Curtains|Wash","Clean ceiling fan"};
                break;
            case "Laundry":
                data = new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Clean sink","Organize closet & cabinets","Clean closet &cabinets","Clean walls"};
                break;
            case "Basement":
                data = new String[]{" ","Furnace check","Mold check","Declutter/Discard"};
                break;
            case "Attic":
                data = new String[]{" ","Roof check","Mold check","Check mouse traps","Declutter/Discard"};
                break;
            case "General":
                data = new String[]{" ","Test smoke/gas detectors","Humidifier filters","Dust blinds","Furniture|Vacuum","Furniture|Wash covers","Curtains|Vacuum","Curtains|Wash"};
                break;
            case "Garage":
                data = new String[]{" ","Furnace maintenance","Trailer maintenance","Refill Salt","Lubricate garage door","Clean walls","Declutter all storage","Lawn mower-maintenance"};
                break;
            case "Garden":
                data = new String[]{" ","Clean up BBQ","Lawn|Fertilize","Lawn|Trim edges","Prune plants and trees","Rotate Compost","Garden furniture |Oil","Garden furniture |Store away"};
                break;
            case "House":
                data = new String[]{" ","Air Conditioner","Surveillance|Check","Deck maintenance","Pool|Test water/chlorine","Pool| Broom","Pool| Empty filter","Pool|Scrub tiles","Secure water appliances against frost"};
                break;
            default:
                data = new String[]{" ","Test smoke/gas detectors","Humidifier filters","Dust blinds","Furniture|Vacuum","Furniture|Wash covers","Curtains|Vacuum","Curtains|Wash"};
        }
    }

}
