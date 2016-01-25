package com.hao1987.android.gorilla.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

    private HashMap<Integer, Fragment> mFragments;
    private List<String> mTitles;

    public TabAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new HashMap<Integer, Fragment>();
        mTitles = new ArrayList<String>();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).toString().toUpperCase();
    }

    public void addTab(Fragment newFragment, int tabId, String tabTitle) {
        mFragments.put(tabId, newFragment);
        mTitles.add(tabId, tabTitle);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
