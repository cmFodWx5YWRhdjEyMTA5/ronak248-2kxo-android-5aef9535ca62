package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.screamxo.Activity.RahulWork.NewConfigurePaymentActivity;
import com.screamxo.Activity.RahulWork.SellItemActivity;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.NumFilter;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SellItemThreeFragment extends Fragment {
    private static final String TAG = "SellItemThreeFragment";

    @BindView(R.id.imgQuestion)
    ImageView imgQuestion;

    @BindView(R.id.ed_item_price)
    EditText ed_item_price;
    @BindView(R.id.ed_item_earn)
    EditText ed_item_earn;
    @BindView(R.id.ed_item_Shipping)
    EditText ed_item_Shipping;
    @BindView(R.id.ed_item_Keywords)
    EditText ed_item_Keywords;

    Unbinder unbinder;
    private Context context;
    private Preferences preferences;

    public SellItemThreeFragment() {
        // Required empty public constructor
    }

    public static SellItemThreeFragment newInstance(int page, String title) {
        return new SellItemThreeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell_item_three, container, false);
        unbinder = ButterKnife.bind(this, view);
        initControls();
        return view;
    }

    private void initControls() {
        imgQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, getString(R.string.txt_get_amount), Toast.LENGTH_SHORT).show();
            }
        });

        ed_item_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    if (!ed_item_price.getText().toString().replaceAll(",", "").equals("")) {
                        Double commition = Double.parseDouble(ed_item_price.getText().toString().replaceAll(",", ""))
                                - ((Double.parseDouble(ed_item_price.getText().toString().replaceAll(",", ""))) * 0.10);
                        ed_item_earn.setText("" + ((round(commition, 2))));
                    } else {
                        ed_item_earn.setText("");
                    }

                    String dta = s.toString();
                    int dotIndex = dta.indexOf(".");


                    if (s.toString().contains(".")) {
                        ed_item_price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(dotIndex + 3)});
                        ed_item_price.setSelection(ed_item_price.length());
                    } else {
                        ed_item_price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                        ed_item_price.setSelection(ed_item_price.length());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ed_item_Shipping.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {


                    String dta = s.toString();
                    int dotIndex = dta.indexOf(".");

                    if (s.toString().contains(".")) {
                        ed_item_Shipping.setFilters(new InputFilter[]{new InputFilter.LengthFilter(dotIndex + 3)});
                        ed_item_Shipping.setSelection(ed_item_Shipping.length());

                    } else {
                        ed_item_Shipping.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                        ed_item_Shipping.setSelection(ed_item_Shipping.length());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        preferences = new Preferences(context);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.btnDone)
    public void onClick(View view) {
        if (ed_item_price.getText().toString().isEmpty()) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_enter_item_price), DialogBox.DIALOG_FAILURE, null);
            return;
        }

        if (ed_item_Shipping.getText().toString().isEmpty()) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_enter_shipping_cost), DialogBox.DIALOG_FAILURE, null);
            return;
        }

        if (ed_item_Keywords.getText().toString().isEmpty()) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_enter_tags), DialogBox.DIALOG_FAILURE, null);
            return;
        }

        List<Fragment> fragmentList = ((AppCompatActivity) context).getSupportFragmentManager().getFragments();
        Intent intent = new Intent(context, NewConfigurePaymentActivity.class);
        intent.putExtra("fileArraymap", StaticConstant.imageFiles_new);
        HashMap<String, String> map = new HashMap<>();
        for (Fragment fragment : fragmentList) {
            if (fragment == null) {
                return;
            }

            if (fragment instanceof SellItemFirstFragment) {
                intent.putExtra("fileArraymap", StaticConstant.imageFiles_new);
            } else if (fragment instanceof SellItemSecondFragment) {
                map.put("itemname", ((SellItemSecondFragment) fragment).edItemName.getText().toString());
                map.put("item_condition", ((SellItemSecondFragment) fragment).txtCondition.getText().toString());
                map.put("category", String.valueOf(((SellItemSecondFragment) fragment).categoryId));
                map.put("itemquantity", ((SellItemSecondFragment) fragment).edItemQuantity.getText().toString().replaceAll(",", ""));
                map.put("itemdesc", ((SellItemSecondFragment) fragment).edItemDeacription.getText().toString());
            } else if (fragment instanceof SellItemThreeFragment) {
                map.put("itemprice", ((SellItemThreeFragment) fragment).ed_item_price.getText().toString().replaceAll(",", ""));
                map.put("itemactualprice", ((SellItemThreeFragment) fragment).ed_item_earn.getText().toString().replaceAll(",", ""));
                map.put("itemshipcost", String.valueOf(0));
                map.put("itemtags", ((SellItemThreeFragment) fragment).ed_item_Keywords.getText().toString());
            }
        }
        map.put("itemcreatedby", preferences.getUserId());
        for (Fragment fragment : fragmentList) {
            if (fragment != null && fragment instanceof SellItemFirstFragment) {
                HashMap<String, File> fileArrayMap = new HashMap<>(SellItemFirstFragment.MAX_IMAGE_COUNT);
                File[] imageFiles = ((SellItemFirstFragment) fragment).imageFiles;
                for (int i = 0; i < imageFiles.length; i++) {
                    fileArrayMap.put("media[" + i + "]", imageFiles[i]);
                }
                intent.putExtra("fileArraymap", fileArrayMap);
            }
        }
        intent.putExtra("mapString", map);
        intent.putExtra("screen", SellItemActivity.class.getSimpleName());
        Utils.printIntentData(intent);
        getActivity().startActivityForResult(intent, StaticConstant.REQUEST_ITEM_CREATE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: ");
//        switch (requestCode) {
//
//        }
//    }
}


