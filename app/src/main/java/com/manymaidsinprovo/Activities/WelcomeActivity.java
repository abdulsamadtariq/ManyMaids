package com.manymaidsinprovo.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.manymaidsinprovo.Fragments.WelcomeOneFragment;
import com.manymaidsinprovo.R;


public class WelcomeActivity extends AppCompatActivity {


    public static FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        fragmentManager=getSupportFragmentManager();
        if(findViewById(R.id.flWelcome) !=null){

            if(savedInstanceState !=null){
                return;
            }

            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.flWelcome,new WelcomeOneFragment(),null);
            fragmentTransaction.commitAllowingStateLoss();

        }



    }
}
