package com.screamxo.baseClass;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import rx.Subscription;

public abstract class BaseActivity extends AppCompatActivity {
    private ViewDataBinding bindObject;
    public Subscription subscription;
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        context = this;
        ButterKnife.bind(this);
        bindObject = DataBindingUtil.setContentView(this, getLayoutResId());
        init();
    }

    public abstract int getLayoutResId();

    public abstract void init();

    public ViewDataBinding getBindObject() {
        return bindObject;
    }
}
