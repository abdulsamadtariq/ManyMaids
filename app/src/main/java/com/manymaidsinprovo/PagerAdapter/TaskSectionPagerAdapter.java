package com.manymaidsinprovo.PagerAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.manymaidsinprovo.Fragments.AreaCopyFragment;
import com.manymaidsinprovo.Fragments.AreaInsideFragment;
import com.manymaidsinprovo.Fragments.AreaOutSideFragment;
import com.manymaidsinprovo.Fragments.BasicTaskFragment;
import com.manymaidsinprovo.Fragments.CustomTaskFragment;
import com.manymaidsinprovo.Fragments.SpecialTaskFragment;
import com.manymaidsinprovo.Model.Area;

public class TaskSectionPagerAdapter extends FragmentPagerAdapter {

    private BasicTaskFragment basicTaskFragment;
    private SpecialTaskFragment specialTaskFragment;
    private CustomTaskFragment customTaskFragment;
    private Area area;

    public TaskSectionPagerAdapter(@NonNull FragmentManager fm, Area area) {
        super(fm);
        this.area = area;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {

            return "Basic";
        } else if (position == 1) {
            return "Special";

        } else if (position == 2) {
            return "Custom";

        }
        return super.getPageTitle(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedArea", area);

        basicTaskFragment = new BasicTaskFragment();
        specialTaskFragment = new SpecialTaskFragment();
        customTaskFragment = new CustomTaskFragment();

        basicTaskFragment.setArguments(bundle);
        specialTaskFragment.setArguments(bundle);
        customTaskFragment.setArguments(bundle);

        if (position == 0) {
            return basicTaskFragment;
        } else if (position == 1) {
            return specialTaskFragment;
        } else if (position == 2) {
            return customTaskFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
