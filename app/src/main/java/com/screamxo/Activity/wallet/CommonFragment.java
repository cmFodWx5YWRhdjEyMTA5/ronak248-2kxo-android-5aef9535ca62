package com.screamxo.Activity.wallet;

import android.support.v4.app.FragmentTransaction;

import com.screamxo.Fragment.wallet.WalletHistoryFragment;
import com.screamxo.Fragment.wallet.WalletSendFragment;
import com.screamxo.Interface.SendWallet;
import com.screamxo.R;
import com.screamxo.baseClass.BaseFragment;

public class CommonFragment extends BaseFragment {
    WalletSendFragment walletSendFragment;
    SendWallet sendWallet;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_common;
    }

    @Override
    public void init() {
        walletSendFragment = new WalletSendFragment();
        setSendfrag();
    }

    public void setInterface(SendWallet anInterface) {
        sendWallet = anInterface;
    }

    public void setSendfrag(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framlayout, walletSendFragment, "");
        fragmentTransaction.commit();
        walletSendFragment.setInterface(sendWallet);
    }

    public void callHistry(String s)
    {
        WalletHistoryFragment walletHistoryFragment = new WalletHistoryFragment(s);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framlayout,walletHistoryFragment , "213");
        fragmentTransaction.commit();
    }
}
