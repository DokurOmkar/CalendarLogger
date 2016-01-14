package com.omkardokur.calendarlogger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by omkardokur on 1/3/16.
 */
public class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0){
            fragment = new CallsFragment();
        }
        if(position==1){
            fragment = new SMSFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
