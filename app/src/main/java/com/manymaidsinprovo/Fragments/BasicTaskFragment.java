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
public class BasicTaskFragment extends Fragment {

    private NumberPicker npBasic;
    private Context mContext;
    private OnTaskSelectionListener mListener;
    private Area area;
    private String[]  data;

    public BasicTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext=getContext();
        return inflater.inflate(R.layout.fragment_basic_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        area = (Area) getArguments().getSerializable("selectedArea");

        npBasic=view.findViewById(R.id.npBasic);
        addData();
        npBasic.setMinValue(0);
        npBasic.setMaxValue(data.length-1);

        npBasic.setDisplayedValues(data);
        npBasic.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npBasic.setWrapSelectorWheel(false);

        npBasic.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int pos=numberPicker.getValue();
                String datum=data[pos];
                mListener.onTaskChangeData(datum);
            }
        });
    }

    private void addData() {
        switch (String.valueOf(area.getAreaName())) {
            case "Kitchen":
                data=new String[]{" ","Dust(thorough)","Sweep floor","Mop floor","Wipe Counter","Clean sink","Organize &  wipe cupboards","Organize &  wipe drawers","Organize fridge","Organize freezer","Clean fridge","Clean freezer","Clean stove top","Clean hood above stove","Clean oven","Clean trash can","Change towels","Sharpen knives","Clean walls"};
                break;
            case "Living Room":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Clean Shelves & cabinets","Organize & clean bookshelves","Wipe electronics","Clean Walls","Tidy Up(quick)","Tidy Up(thorough)","Custom"};
                break;
            case "Dining Room":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Clean Shelves & cabinets","Organize & clean bookshelves","Wipe electronics","Clean Walls","Tidy Up(quick)","Tidy Up(thorough)","Custom"};
                break;
            case "Bedroom":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Change bed linen","Wash quilt and pillow","Clean bedside tables","Organize &  wipe closet","Clean walls","Tidy Up(quick)","Tidy up(thorough)"};
                break;
            case "Bathroom":
                data=new String[]{" ","Mop floor","Disinfect floor","Dust","Clean toilet","Clean sink","Clean mirrors","Change towels","Change bath mat","Shower|Clean fittings","Shower|Wash curtains","Shower|Clean glass","Clean Walls"};
                break;
            case "Toilet":
                data=new String[]{" ","Mop floor","Disinfect floor","Dust","Clean toilet","Clean sink","Clean mirrors","Change towels","Change bath mat","Shower|Clean fittings","Shower|Wash curtains","Shower|Clean glass","Clean Walls"};
                break;
            case "Children":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Change bed linen","Wash quilt and pillow","Clean closet","Clean -size check","Toys-age check","Clean walls","Tidy Up(quick)","Tidy up(thorough)"};
                break;
            case "Office":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Clean/disinfect hardware","Clean walls","Tidy Up(quick)","Tidy up(thorough)"};
                break;
            case "Entrance":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Organize shoe rack","Clean walls","Tidy Up(quick)","Tidy up(thorough)"};
                break;
            case "Laundry":
                data=new String[]{" ","Washing. One load.","Hang out.One load","Folding. One load","Iron 20 min.","DryClean Winter Coats","DryClean summer Coats","Clean dryer","Clean washing machine"};
                break;
            case "Basement":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Clean sink","Organize storage","Clean walls"};
                break;
            case "Attic":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Organize storage"};
                break;
            case "General":
                data=new String[]{" ","Clean all doors","Clean all doorknobs","Remove cobwebs","Sanitize light switches","Clean/Dust all lights","Vacuum house","Water plants","Garbage collection"};
                break;
            case "Garage":
                data=new String[]{" ","Dust(quick)","Dust(thorough)","Sweep floor","Vacuum Floor","Mop floor","Organize tools","Car|Wash Outside","Car|Wash inside","Car|Clean inside","Car|Seasonal|tire change","Bike|Check tires","Bike|Oil chain","Bike|Wash","Tidy Up(quick)","Tidy up(thorough)"};
                break;
            case "Garden":
                data=new String[]{" ","Weed garden","Weed front garden","Weed herb garden","Water garden","Water herbs","Water pots","Trim bushes","Trim hedges","Rake leaves","Lawn | Mow","Harvet ripe fruits","Tidy Up(quick)","Tidy up(thorough)"};
                break;
            case "House":
                data=new String[]{" ","Wash windows","Shake doormats","Sweep driveway & paths","Clean up gutters","Inspect roof","Trash can & recycle bins","Tidy Up(quick)","Tidy up(thorough)"};
                break;
            default:
                data=new String[]{" ","Clean all doors","Clean all doorknobs","Remove cobwebs","Sanitize light switches","Clean/Dust all lights","Vacuum house","Water plants","Garbage collection"};
        }
    }

    public interface OnTaskSelectionListener {
        void onTaskChangeData(CharSequence data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof BasicTaskFragment.OnTaskSelectionListener){
            mListener=(BasicTaskFragment.OnTaskSelectionListener)context;
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
