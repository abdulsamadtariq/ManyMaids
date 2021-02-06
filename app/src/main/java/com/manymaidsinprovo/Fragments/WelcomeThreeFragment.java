package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.manymaidsinprovo.Activities.HomeActivity;
import com.manymaidsinprovo.Activities.WelcomeActivity;
import com.manymaidsinprovo.R;

public class WelcomeThreeFragment extends Fragment {

    private Button btnFinish;
    private LinearLayout rootWelcome;
    private Context mContext;
    private Animation fadeIn;

    public WelcomeThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        return inflater.inflate(R.layout.fragment_welcome_three, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnFinish = view.findViewById(R.id.btnFinish);
        rootWelcome = view.findViewById(R.id.rootWelcome);
        mContext = getContext();

        fadeIn = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.fade_in);
        rootWelcome.startAnimation(fadeIn);


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(mContext, "Finish", Toast.LENGTH_SHORT).show();
                PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean("onBoardFinished", true).apply();
                startActivity(new Intent(mContext.getApplicationContext(), HomeActivity.class));

               getActivity().finish();
            }
        });
    }

}
