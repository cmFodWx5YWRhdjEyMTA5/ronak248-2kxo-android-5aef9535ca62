package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.RahulWork.CartCheckoutActivity;
import com.screamxo.Activity.cart.CartFragment;
import com.screamxo.Activity.cart.GetItemCount;
import com.screamxo.Activity.cart.SaveItemFragment;
import com.screamxo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.screamxo.Activity.cart.CartAdapter.qty;

/**
 * Created by SONY on 18-03-2018.
 */

@SuppressLint("ValidFragment")
public class NewCartFragment extends Fragment implements GetItemCount {
    public CartFragment cartFragment;
    public SaveItemFragment saveItemFragment;
    Context context;
    @BindView(R.id.txtCartTool)
    TextView txtCartTool;
    @BindView(R.id.imgCross)
    ImageView imgCross;
    @BindView(R.id.toolbarLinear)
    RelativeLayout toolbarLinear;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rl_Pager)
    RelativeLayout rlPager;
    @BindView(R.id.txtCheckout)
    TextView txtCheckout;
    ViewPagerAdapter viewPagerAdapter;
    boolean openSaved = false;

    @SuppressLint("ValidFragment")
    public NewCartFragment(boolean openSaved) {
        this.openSaved = openSaved;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_cart_layout, container, false);
        ButterKnife.bind(this, view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        setPager();

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof DrawerMainActivity)
                    ((DrawerMainActivity) getActivity()).clickCloseCart();
            }
        });

        txtCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartFragment != null && cartFragment.userdetailsArray.size() != 0) {
                    Intent i = new Intent(context, CartCheckoutActivity.class);
                    i.putExtra("screen", NewCartFragment.class.getSimpleName());
                    i.putExtra("qunty", qty);
                    ((Activity) context).startActivityForResult(i, 603);
                } else {
                    Toast.makeText(context, getString(R.string.txt_no_item_for_purchase), Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 603:
                    imgCross.performClick();
                    break;
            }
        }
    }

    private void setPager() {
        cartFragment = new CartFragment();
        saveItemFragment = new SaveItemFragment();
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(cartFragment, getString(R.string.txt_in_cart));
        viewPagerAdapter.addFragment(saveItemFragment, getString(R.string.txt_saved));

        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        txtCheckout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        txtCheckout.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (openSaved)
            viewpager.setCurrentItem(1);
    }

    public void setPagerPosition(int position) {
        viewpager.setCurrentItem(position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void setCartItem(String count) {
        tabs.getTabAt(0).setText(getString(R.string.txt_in_cart) + "(" + count + ")");
    }

    @Override
    public void setSaveItem(String count) {
        tabs.getTabAt(1).setText(getString(R.string.txt_saved) + "(" + count + ")");
    }
}
