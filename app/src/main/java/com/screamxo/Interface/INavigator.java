package com.screamxo.Interface;

import android.os.Bundle;

/**
 * Created by dellparangat on 10/1/17.
 */
public interface INavigator {

    void navigate(int id, int layoutID, Bundle bundle);

    void showMessage(String message);

    void islogout(boolean status);

    void isMenuClick(int id);
}
