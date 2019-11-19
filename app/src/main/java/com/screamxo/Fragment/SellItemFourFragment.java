package com.screamxo.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.screamxo.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemFourFragment extends Fragment {

    Unbinder unbinder;

    public SellItemFourFragment() {
        // Required empty public constructor
    }

    public static SellItemFourFragment newInstance(int page, String title) {
        return new SellItemFourFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_item_four, container, false);
        unbinder = ButterKnife.bind(this, view);
        initControls();
        return view;
    }

    private void initControls() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
