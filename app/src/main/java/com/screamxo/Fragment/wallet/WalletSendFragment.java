package com.screamxo.Fragment.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Interface.SendWallet;
import com.screamxo.R;
import com.screamxo.baseClass.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WalletSendFragment extends BaseFragment {

    @BindView(R.id.txt_one)
    TextView txtOne;
    @BindView(R.id.txt_two)
    TextView txtTwo;
    @BindView(R.id.txt_three)
    TextView txtThree;
    @BindView(R.id.txt_four)
    TextView txtFour;
    @BindView(R.id.txt_five)
    TextView txtFive;
    @BindView(R.id.txt_six)
    TextView txtSix;
    @BindView(R.id.txt_seven)
    TextView txtSeven;
    @BindView(R.id.txt_eight)
    TextView txtEight;
    @BindView(R.id.txt_nine)
    TextView txtNine;
    @BindView(R.id.txt_dot)
    TextView txtDot;
    @BindView(R.id.txt_zero)
    TextView txtZero;
    @BindView(R.id.img_back)
    ImageView imgBack;
    Unbinder unbinder;
    SendWallet sendWallet;
    StringBuilder strAmount;
    int amount = 0;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_wallet_send;
    }

    @Override
    public void init() {
        strAmount = new StringBuilder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.txt_one, R.id.txt_two, R.id.txt_three, R.id.txt_four, R.id.txt_five, R.id.txt_six, R.id.txt_seven, R.id.txt_eight, R.id.txt_nine, R.id.txt_dot, R.id.txt_zero, R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_one:
                countAmount("1");
                break;
            case R.id.txt_two:
                countAmount("2");
                break;
            case R.id.txt_three:
                countAmount("3");
                break;
            case R.id.txt_four:
                countAmount("4");
                break;
            case R.id.txt_five:
                countAmount("5");
                break;
            case R.id.txt_six:
                countAmount("6");
                break;
            case R.id.txt_seven:
                countAmount("7");
                break;
            case R.id.txt_eight:
                countAmount("8");
                break;
            case R.id.txt_nine:
                countAmount("9");
                break;
            case R.id.txt_dot:
                if (!strAmount.toString().contains(" "))
                    countAmount(" ");
                break;
            case R.id.txt_zero:
                countAmount("0");
                break;
            case R.id.img_back:
                try {
                    if (strAmount.length() > 0)
                        countAmount(getString(R.string.txt_back));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    void countAmount(String digit) {
        String[] split = strAmount.toString().split(" ");
        if (split.length > 1 && !digit.equalsIgnoreCase(getString(R.string.txt_back))) {
            if (split[1].length() > 2) {
                return;
            }
        }
        if (strAmount.length() > 0 && digit.equalsIgnoreCase(getString(R.string.txt_back))) {
            strAmount.deleteCharAt(strAmount.length() - 1);
        } else if (!(strAmount.length() == 0 && (digit.equalsIgnoreCase("0") || digit.equalsIgnoreCase("."))))
            strAmount.append(digit);
        sendWallet.getAmount(strAmount.toString());
    }

    public void setInterface(SendWallet anInterface) {
        sendWallet = anInterface;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void reSetValue() {
        try {
            strAmount = new StringBuilder();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
