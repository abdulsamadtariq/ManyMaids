package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AreaTopFragment extends Fragment {


    private LinearLayout layoutToHide, layoutAreaTop;
    private EditText etAreaName;
    private ImageView ivArea;
    private Animation bounceAnim;
    private Animation bounceBackAnim;
    private Context mContext;
    private TaskHelper areaHelper;
    private Button btnCreateArea;
   // private OnAreaCreation mListener;

    public AreaTopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_area_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        etAreaName = view.findViewById(R.id.etAreaName);
        ivArea = view.findViewById(R.id.ivArea);
        layoutToHide = view.findViewById(R.id.layoutToHide);
        layoutAreaTop = view.findViewById(R.id.layoutAreaTop);
        btnCreateArea = view.findViewById(R.id.btnCreateArea);
        bounceAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_anim);

        areaHelper = new TaskHelper(mContext);

        layoutToHide.setVisibility(View.VISIBLE);
        layoutToHide.setAnimation(bounceAnim);
        etAreaName.setEnabled(false);


        btnCreateArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String areaName = etAreaName.getText().toString();

                if (TextUtils.isEmpty(areaName)) {
                    return;
                } else {
                    if (areaHelper.isAreaAlready(areaName)) {
                        Toast.makeText(mContext, "This Area already Present.please enter different", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long insert = areaHelper.addArea(areaName, 0);
                    if (insert > -1) {
                        //mListener.areaCreated(insert);
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                }
            }
        });
    }

    public void updateEditText(CharSequence selectedArea) {
        if (selectedArea != null && selectedArea.length() > 1 && !selectedArea.equals("Custom")) {
            etAreaName.setEnabled(true);
            layoutAreaTop.setVisibility(View.VISIBLE);
            bounceBackAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_back_anim);
            layoutToHide.setAnimation(bounceBackAnim);
            layoutToHide.setVisibility(View.GONE);
            etAreaName.setText(selectedArea);


            switch (String.valueOf(selectedArea)) {
                case "Kitchen":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.kitchen_blue_line));
                    break;
                case "Living Room":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.living_room_blue_line));
                    break;
                case "Dining Room":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.dining_room_blue_line));
                    break;
                case "Bedroom":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.bedroom_blue_line));
                    break;
                case "Bathroom":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.bathroom_blue_line));
                    break;
                case "Toilet":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.toilet_blue_line));
                    break;
                case "Children":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.kids_room_blue_line));
                    break;
                case "Office":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.home_office_blue_line));
                    break;
                case "Entrance":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.entrance_blue_line));
                    break;
                case "Laundry":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.laundry_blue_line));
                    break;
                case "Basement":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.basement_blue_line));
                    break;
                case "Attic":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.attic_blue_line));
                    break;
                case "General":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.inside_blue_line));
                    break;
                case "Garage":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.garage_blue_line));
                    break;
                case "Garden":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.garden_blue_line));
                    break;
                case "House":
                    ivArea.setImageDrawable(getResources().getDrawable(R.drawable.house_blue_line));
                    break;
            }


        } else if (selectedArea.equals("Custom")) {
            etAreaName.setEnabled(true);
            layoutAreaTop.setVisibility(View.VISIBLE);
            bounceBackAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_back_anim);
            layoutToHide.setAnimation(bounceBackAnim);
            layoutToHide.setVisibility(View.GONE);
            etAreaName.setText("");
            etAreaName.requestFocus();
            ivArea.setImageDrawable(getResources().getDrawable(R.drawable.inside_blue_line));
        } else {
            etAreaName.setEnabled(false);
            bounceAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_anim);
            layoutToHide.setAnimation(bounceAnim);
            layoutToHide.setVisibility(View.VISIBLE);
            etAreaName.setText("");
        }
    }

 /*   public interface OnAreaCreation {
        void areaCreated(long id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        if (context instanceof OnAreaCreation) {
            mListener = (OnAreaCreation) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAreaOutSelectionListener ");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener=null;
    }*/
}
