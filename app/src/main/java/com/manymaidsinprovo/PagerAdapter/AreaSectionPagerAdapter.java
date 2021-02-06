package com.manymaidsinprovo.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.manymaidsinprovo.Fragments.AreaCopyFragment;
import com.manymaidsinprovo.Fragments.AreaInsideFragment;
import com.manymaidsinprovo.Fragments.AreaOutSideFragment;

public class AreaSectionPagerAdapter extends FragmentPagerAdapter {

    public AreaSectionPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {

            return "Inside";
        } else if (position == 1) {
            return "Outside";

        } else if (position == 2) {
            return "Copy";

        }
        return super.getPageTitle(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AreaInsideFragment();
        } else if (position == 1) {
            return new AreaOutSideFragment();
        } else if (position == 2) {
            return new AreaCopyFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
