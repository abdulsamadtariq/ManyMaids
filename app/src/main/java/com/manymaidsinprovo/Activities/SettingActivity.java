package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private Switch sNotification, sUserEnable, sAssignment;
    private TextView tvAbout, tvFeedBack, tvFaq, tvSorting, tvUserManager;
    private TextView tvUserName,tvEmail,tvLogin;
    private CircleImageView civUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sNotification = findViewById(R.id.sNotification);
        sAssignment = findViewById(R.id.sAssignment);
        sUserEnable = findViewById(R.id.sUserEnable);
        tvAbout = findViewById(R.id.tvAbout);
        tvFeedBack = findViewById(R.id.tvFeedBack);
        tvFaq = findViewById(R.id.tvFaq);
        tvSorting = findViewById(R.id.tvSorting);
        tvUserManager = findViewById(R.id.tvUserManager);
        tvUserName=findViewById(R.id.tvUserName);
        tvEmail=findViewById(R.id.tvEmail);
        tvLogin=findViewById(R.id.tvLogin);
        civUser=findViewById(R.id.civProfile);


        boolean isNotification=PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).getBoolean("notification",false);
        if(isNotification){
            sNotification.setChecked(true);
        }else {
            sNotification.setChecked(false);
        }

        boolean isUser=SessionHelper.isUserLoggedIn(SettingActivity.this);
        if(isUser){
           tvUserName.setVisibility(View.VISIBLE);
           tvEmail.setVisibility(View.VISIBLE);
           civUser.setVisibility(View.VISIBLE);


            User currentUser =SessionHelper.getCurrentUser(SettingActivity.this);
            tvUserName.setText(currentUser.getFirstName()+" "+currentUser.getLastName());
            tvEmail.setText(currentUser.getEmail());

            if (currentUser.getImageLink() != null && currentUser.getImageLink().length()>0) {
                try {

                    Picasso.get().load(ApiUrl.uploadsFolder + currentUser.getImageLink()).placeholder(R.drawable.ic_profile_gray).into(civUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
           if(currentUser.getUserRole()==1){
               tvLogin.setText("Go-to Admin");

               tvLogin.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       startActivity(new Intent(SettingActivity.this, AdminHomeActivity.class));
                       finish();
                   }
               });

           }else {

               tvLogin.setText("View profile");

               tvLogin.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       startActivity(new Intent(SettingActivity.this, UserProfileActivity.class));
                       finish();
                   }
               });
           }
        }else {
            tvUserName.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
            civUser.setVisibility(View.GONE);

            tvLogin.setText("Login");

            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                    finish();
                }
            });
        }
        tvSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showChangeSortingDialog();
            }
        });
        tvUserManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, UserManagmentActivity.class));
            }
        });

        sNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit().putBoolean("notification", true)
                            .apply();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit().putBoolean("notification", false)
                            .apply();
                }
            }
        });

        sUserEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit().putBoolean("userEnable", true)
                            .apply();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit().putBoolean("userEnable", false)
                            .apply();
                }
            }
        });
        sAssignment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit().putBoolean("userAssignment", true)
                            .apply();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit().putBoolean("userAssignment", false)
                            .apply();
                }
            }
        });


        String sorting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("sorting", null);
        if (sorting != null) {
            tvSorting.setText(sorting);
        } else {
            tvSorting.setText(R.string.defaulta);
        }
    }

    private void showChangeSortingDialog() {
        String[] langItems = {"Default", "Days", "Indicator"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("Choose Sorting priority.")
                .setSingleChoiceItems(langItems, -1, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //English
                            tvSorting.setText(R.string.defaulta);
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                    .edit().putString("sorting", getResources().getString(R.string.defaulta))
                                    .apply();
                            recreate();

                        } else if (which == 1) {

                            //Urdu
                            tvSorting.setText(R.string.days);
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                    .edit().putString("sorting", getResources().getString(R.string.days))
                                    .apply();
                            recreate();
                        } else if (which == 2) {
                            //Arabic
                            tvSorting.setText(R.string.indicator);
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                    .edit().putString("sorting", getResources().getString(R.string.indicator))
                                    .apply();
                            recreate();
                        }
                        dialog.dismiss();
                    }
                });
        builder.show();

    }
}

