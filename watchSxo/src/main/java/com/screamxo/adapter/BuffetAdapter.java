package com.screamxo.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.screamxo.CustomFragment;

import java.util.ArrayList;
import java.util.List;

public class BuffetAdapter extends FragmentGridPagerAdapter {
    private final Context mContext;
    private List<Row> mRows;

    public BuffetAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;

        mRows = new ArrayList<Row>();

        mRows.add(new Row(
                new CustomFragment(),
                new CustomFragment(),
                new CustomFragment(),
                new CustomFragment(),
                new CustomFragment(),
                new CustomFragment(),
                new CustomFragment(),
                new CustomFragment()));
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Row adapterRow = mRows.get(row);
        return adapterRow.getColumn(col);
    }

    @Override
    public int getRowCount() {
        return mRows.size();
    }

    @Override
    public int getColumnCount(int rowNum) {
        return mRows.get(rowNum).getColumnCount();
    }

    private class Row {
        final List<Fragment> columns = new ArrayList<Fragment>();

        public Row(Fragment... fragments) {
            for (Fragment f : fragments) {
                add(f);
            }
        }

        public void add(Fragment f) {
            columns.add(f);
        }

        Fragment getColumn(int i) {
            return columns.get(i);
        }

        public int getColumnCount() {
            return columns.size();
        }
    }
}