package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.manymaidsinprovo.Activities.WelcomeActivity;
import com.manymaidsinprovo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeOneFragment extends Fragment {


    private Button btnNext;
    private LinearLayout rootWelcome;
    private Animation fadeIn;
    private Context mContext;
    public WelcomeOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnNext=view.findViewById(R.id.btnNext);
        rootWelcome=view.findViewById(R.id.rootWelcome);
        mContext=getContext();

        fadeIn= AnimationUtils.loadAnimation(mContext.getApplicationContext(),R.anim.fade_in);
        rootWelcome.startAnimation(fadeIn);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                WelcomeActivity.fragmentManager.beginTransaction().replace(R.id.flWelcome,new WelcomeTwoFragment(),null)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
    }
}
