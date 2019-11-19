package com.screamxo.baseClass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.prefs.Preferences;

import butterknife.ButterKnife;
import rx.Subscription;

public abstract class BaseFragment extends Fragment {

//    private ViewDataBinding viewDataBinding;
    public Subscription subscription;
    public Preferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, view);
//        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        init();
        return view;
    }


   /* public ViewDataBinding getBindingObj() {
        return viewDataBinding;
    }*/

    public abstract int getLayoutResId();

    public abstract void init();
}