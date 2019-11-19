package com.screamxo.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class WalletPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private int selectType = 1;

    public WalletPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (selectType == 0) {
                return mFragmentList.get(0);
            } else if (selectType == 1) {
                return mFragmentList.get(3);
            }
        }
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return 3;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    public void setSelectType(int type) {
        selectType = type;
        notifyAll();
    }
}
