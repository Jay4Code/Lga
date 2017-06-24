package com.lga.app.guide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * todo
 *
 * @author xiaojie
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFm;
    private ArrayList<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        mFm = fm;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
