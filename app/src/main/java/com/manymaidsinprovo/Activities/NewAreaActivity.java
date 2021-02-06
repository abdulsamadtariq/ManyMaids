package com.manymaidsinprovo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.manymaidsinprovo.Fragments.AreaCopyFragment;
import com.manymaidsinprovo.Fragments.AreaInsideFragment;
import com.manymaidsinprovo.Fragments.AreaOutSideFragment;
import com.manymaidsinprovo.Fragments.AreaTopFragment;
import com.manymaidsinprovo.PagerAdapter.AreaSectionPagerAdapter;
import com.manymaidsinprovo.R;

public class NewAreaActivity extends AppCompatActivity implements AreaInsideFragment.OnAreaSelectionListener, AreaOutSideFragment.OnAreaOutSelectionListener, AreaCopyFragment.OnAreaCopySelectionListener {


    private TabLayout tbAreaChoose;
    private ViewPager viewPagerAreaBottom;
    private AreaSectionPagerAdapter sectionPagerAdapter;
    private AreaTopFragment areaTopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_area);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("New Area");


        tbAreaChoose = findViewById(R.id.tbAreaChoose);
        viewPagerAreaBottom = findViewById(R.id.viewPagerAreaBottom);

        FragmentManager fm = getSupportFragmentManager();
        sectionPagerAdapter = new AreaSectionPagerAdapter(fm);

        viewPagerAreaBottom.setAdapter(sectionPagerAdapter);
        tbAreaChoose.setupWithViewPager(viewPagerAreaBottom);

        areaTopFragment = new AreaTopFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flAreaTop, areaTopFragment)
                .commitNowAllowingStateLoss();
    }

    @Override
    public void onAreaChangeData(CharSequence data) {
        areaTopFragment.updateEditText(data);
    }

}
