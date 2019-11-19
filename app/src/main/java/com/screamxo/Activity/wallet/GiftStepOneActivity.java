package com.screamxo.Activity.wallet;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.screamxo.Activity.TermsAndUseActivity;
import com.screamxo.Interface.DialogInterfaceAction;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.baseClass.BaseActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GiftStepOneActivity extends BaseActivity {
    @BindView(R.id.txt_instant)
    TextView txt_instant;
    @BindView(R.id.txt_basic)
    TextView txt_basic;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.constraint_keyboard)
    ConstraintLayout constraint_keyboard;
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
    @BindView(R.id.img_done)
    TextView imgBack;
    @BindView(R.id.txt_send_amount)
    TextView txtSendAmount;
    @BindView(R.id.txt_other_amount)
    TextView txtOtherAmount;
    private String moneyToTranser;
    @BindView(R.id.txt_balance)
    TextView txt_balance;
    private StringBuilder strAmount;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_gift_step_one;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        strAmount = new StringBuilder();
        Preferences preferences = new Preferences(this);
        if (getIntent() != null && Objects.requireNonNull(getIntent().getExtras()).containsKey("money")) {
            moneyToTranser = getIntent().getExtras().getString("money");
        }
        img_back.setOnClickListener(view -> finish());
        txt_basic.setOnClickListener(view -> {
            try {
                if (txtSendAmount.getText().toString().equals("00")) {
                    Toast.makeText(context, getString(R.string.txt_please_eneter_money_transfer), Toast.LENGTH_LONG).show();
                }else if (Double.parseDouble(txtSendAmount.getText().toString()+"."+txtOtherAmount.getText().toString()) < 1.0) {
                    Toast.makeText(context, getString(R.string.txt_minimum_amount_should_1), Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(txtSendAmount.getText().toString()) > Integer.parseInt(preferences.getAmount())) {
                    Toast.makeText(context, getString(R.string.txt_dont_have_enough_amoun), Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(txtSendAmount.getText().toString()) > 9999) {
                    Toast.makeText(context, getString(R.string.txt_cannot_transfer_more_than_999), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(GiftStepOneActivity.this, GiftActivity.class);
                    intent.putExtra("money", txtSendAmount.getText().toString() + "." + txtOtherAmount.getText().toString());
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        txt_instant.setOnClickListener(view -> {
            DialogBox.showDialog(context, context.getString(R.string.app_name), "Coming Soon...", DialogBox.DIALOG_SUCESS, new DialogInterfaceAction() {
                @Override
                public void dialogAction() {

                }
            });
        });

        txt_balance.setOnClickListener(view -> {
            if (constraint_keyboard.getVisibility() == View.GONE){
                constraint_keyboard.setVisibility(View.VISIBLE);
                imgBack.setVisibility(View.VISIBLE);
            }
            else{
                constraint_keyboard.setVisibility(View.GONE);
                imgBack.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({R.id.txt_one, R.id.txt_two, R.id.txt_three, R.id.txt_four, R.id.txt_five, R.id.txt_six, R.id.txt_seven,
            R.id.txt_eight, R.id.txt_nine, R.id.txt_dot, R.id.txt_zero, R.id.img_done,R.id.img_back1,R.id.img_gift,R.id.txt_price})
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
            case R.id.img_done:
                constraint_keyboard.setVisibility(View.GONE);
                imgBack.setVisibility(View.GONE);
                break;
            case R.id.img_back1:
                if (strAmount.length() > 0)
                    countAmount("back");
                break;
            case R.id.img_gift:
            case R.id.txt_price:
                Intent intent=  new Intent(GiftStepOneActivity.this,TermsAndUseActivity.class);
                intent.putExtra("screenType","refer friend");
                startActivity(intent);
                break;
        }
    }

    void countAmount(String digit) {
        String[] split = strAmount.toString().split(" ");
        if (split.length > 1 && !digit.equalsIgnoreCase("back")) {
            if (split[1].length() > 2) {
                return;
            }
        }
        if (strAmount.length() > 0 && digit.equalsIgnoreCase("back")) {
            strAmount.deleteCharAt(strAmount.length() - 1);
        } else if (!(strAmount.length() == 0 && (digit.equalsIgnoreCase("0") || digit.equalsIgnoreCase("."))))
            strAmount.append(digit);
        getAmount(strAmount.toString());
    }

    private void getAmount(String amount) {

        String[] strings = amount.split(" ");
        if (strings.length > 1) {
            txtSendAmount.setText(strings[0]);
            String s = strings[1];
            if (strings[1].length() > 2) {
                s = String.valueOf(strings[1].charAt(0)) + String.valueOf(strings[1].charAt(1));
            }
            txtOtherAmount.setText(s);
        } else {
            txtSendAmount.setText(amount.trim());
            txtOtherAmount.setText("00");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
