package com.manymaidsinprovo.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.manymaidsinprovo.Fragments.AdminHomeFragment;
import com.manymaidsinprovo.Fragments.AdminNotificationFragment;
import com.manymaidsinprovo.Fragments.AdminOrderFragment;
import com.manymaidsinprovo.Fragments.AdminUserFragment;
import com.manymaidsinprovo.Fragments.CleaningTypeFragment;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdminHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private TextView tvName, tvEmail;
    private Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flContent);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        ////////body starts from here
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);

        User user = SessionHelper.getCurrentUser(AdminHomeActivity.this);
        String name = user.getFirstName() + " " + user.getLastName();
        tvName.setText(name);
        tvEmail.setText(user.getEmail());


        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AdminHomeFragment()).commitAllowingStateLoss();
        navigationView.getMenu().getItem(0).setChecked(true);
        setTitle(R.string.app_name);
        toolbar.setSubtitle("Home");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {

            if (!(currentFragment instanceof AdminNotificationFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AdminNotificationFragment()).commitAllowingStateLoss();
            }
            Toast.makeText(this, "Your notifications", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            toolbar.setSubtitle("Home");
            if (!(currentFragment instanceof AdminHomeFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AdminHomeFragment()).commitAllowingStateLoss();
            }

        } else if (id == R.id.nav_cleaningType) {
            toolbar.setSubtitle("Cleaning Packages");
            if (!(currentFragment instanceof CleaningTypeFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new CleaningTypeFragment()).commitAllowingStateLoss();
            }
        } else if (id == R.id.nav_orders) {
            toolbar.setSubtitle("Bookings");

            if (!(currentFragment instanceof AdminOrderFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AdminOrderFragment()).commitAllowingStateLoss();
            }
        } else if (id == R.id.nav_users) {
            toolbar.setSubtitle("Users");
            if (!(currentFragment instanceof AdminUserFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new AdminUserFragment()).commitAllowingStateLoss();
            }
        } else if (id == R.id.nav_logout) {
            SessionHelper.logout(AdminHomeActivity.this);
            startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
