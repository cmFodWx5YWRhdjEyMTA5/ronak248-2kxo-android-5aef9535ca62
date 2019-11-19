package com.screamxo.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.screamxo.Activity.RahulWork.SellItemActivity;
import com.screamxo.Fragment.SellItemFirstFragment;
import com.screamxo.Fragment.SellItemFourFragment;
import com.screamxo.Fragment.SellItemSecondFragment;
import com.screamxo.Fragment.SellItemThreeFragment;

/**
 * Created by parangat on 17/5/17.
 */

public class SellItemVPAdapter extends FragmentPagerAdapter {
    private SellItemActivity mActivity;

    public SellItemVPAdapter(SellItemActivity homeOwnerEditJob, FragmentManager fragmentManager) {
        super(fragmentManager);
        mActivity = homeOwnerEditJob;
    }

    @Override
    public int getCount() {
        int NUM_ITEMS = 4;
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SellItemFirstFragment.newInstance(0, "SellItemFirstFragment");
            case 1:
                return SellItemSecondFragment.newInstance(1, "SellItemSecondFragment");
            case 2:
                return SellItemThreeFragment.newInstance(2, "SellItemThreeFragment");
            case 3:
                return SellItemFourFragment.newInstance(3, "SellItemFourFragment");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mActivity.setSelectedPage(position);
    }
}
