package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivSplash;
    private Animation smallToBig,bottomToUp;
    private TextView tvAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplash = findViewById(R.id.ivSplash);
        tvAppName= findViewById(R.id.tvAppName);

        smallToBig = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.small_to_big);
        bottomToUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_to_up);

        ivSplash.setAnimation(smallToBig);
        tvAppName.setAnimation(bottomToUp);

        gotoHome();
    }

    private void gotoHome() {

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2800);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {

                    /*int userRole = SessionHelper.getUserRole(SplashActivity.this);
                    boolean isGuest = SessionHelper.getGuest(SplashActivity.this);

                    if (userRole == 0) {
                        startActivity(new Intent(SplashActivity.this, UserHomeActivity.class));
                        finish();
                    } else if (userRole == 1) {
                        startActivity(new Intent(SplashActivity.this, AdminHomeActivity.class));
                        finish();
                    } else {
                        if (isGuest) {
                            startActivity(new Intent(SplashActivity.this, UserHomeActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }

                    }*/

                    boolean onBoardFinished=PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getBoolean("onBoardFinished",false);

                    if(!onBoardFinished){
                        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    }
                }
            }
        };
        timer.start();
    }
}
