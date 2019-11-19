package com.screamxo.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.AddressBean;
import com.example.apimodule.ApiBase.ApiBean.ShippingAddress;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.Preferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class PaymentDetailActivity extends AppCompatActivity implements CommonMethod, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = "PaymentDetailActivity";

    public static final int PERM_ACCESS_LOCATION = 201;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;

    @BindView(R.id.btn_continue)
    Button btnContinue;
//    @BindView(R.id.txt_cost_value)
//    TextView txtCostValue;
//    @BindView(R.id.txt_shipping_cost_value)
//    TextView txtShippingCostValue;
//    @BindView(R.id.txt_total_cost_value)
//    TextView txtTotalCostValue;
//    @BindView(R.id.toolbar_edt_search)
//    EditText toolbarEdtSearch;

    @BindView(R.id.edt_street)
    EditText edtStreet;
    @BindView(R.id.edt_city)
    EditText edtCity;
    @BindView(R.id.edt_zipcode)
    EditText edtZipcode;
    @BindView(R.id.edt_state)
    EditText edtState;
    @BindView(R.id.country_picker)
    CountryCodePicker countryPicker;
    @BindView(R.id.rl_country_container)
    RelativeLayout rl_country_container;


//    @BindView(R.id.txt_item_quantity)
//    TextView txtItemQuantity;
//    @BindView(R.id.img_item_quantity)
//    ImageView imgItemQuantity;

    //    @BindView(R.id.txt_item_cost)
//    TextView txtItemCost;
//    @BindView(R.id.txt_shipping_cost)
//    TextView txtShippingCost;
//    @BindView(R.id.txt_total_cost)
//    TextView txtTotalCost;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    //    @BindView(R.id.frm_item_quantity)
//    FrameLayout frmItemQuantity;
//    @BindView(R.id.txt_quntity)
//    TextView txtQuntity;

    //    String itemcost, shippingcost, itemTotalquantity, itemid;
//    Double totalcost;
    Preferences preferences;
    Call<AddressBean> paypalbeanCall;

    ArrayList<String> strings;
    CommonMethod commonMethod;

    private Context context;
    private FetchrServiceBase mService;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;

    private static final long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private static final long FASTEST_INTERVAL = 2000; /* 2 sec */

    boolean isLocationRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        ButterKnife.bind(this);
        init();
        initControlValue();
        initControl();
        initFabIcon();
        buildGoogleApiClient();
    }

    public void init() {
        context = this;
        commonMethod = this;
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
        strings = new ArrayList<>();
        Utils.printIntentData(getIntent());
//        if (getIntent().getExtras() != null && (getIntent().getExtras().containsKey("itemcost") || getIntent().getExtras().containsKey("shippingcost")
//                || getIntent().getExtras().containsKey("itemquantiy") || getIntent().getExtras().containsKey("itemid"))) {
//            itemcost = getIntent().getExtras().getString("itemcost");
//            shippingcost = getIntent().getExtras().getString("shippingcost");
//            itemTotalquantity = getIntent().getExtras().getString("itemquantiy");
//            itemid = getIntent().getExtras().getString("itemid");
//            totalcost = (Double.parseDouble(itemcost)) + Double.parseDouble(shippingcost);
//        }
    }

    @SuppressLint("SetTextI18n")
    public void initControlValue() {
        txtToolbarTitle.setText("Shipping Address");
        imgToolbarLeftIcon.setImageResource(R.mipmap.ico_up);
        imgToolbarLeftIcon.setRotation(-90);
        imgToolbarRightIcon.setVisibility(View.GONE);

        rl_country_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                countryPicker.performClick();
            }
        });

//        if (Utils.getFormattedPrice(String.valueOf(itemcost)) != null) {
//            txtCostValue.setText(Utils.getFormattedPrice(String.valueOf(itemcost)));
//        }
//        if (Utils.getFormattedPrice(String.valueOf(shippingcost)) != null) {
//            txtShippingCostValue.setText(Utils.getFormattedPrice(String.valueOf(shippingcost)));
//        }
//        if (Utils.getFormattedPrice(String.valueOf(totalcost)) != null) {
//            txtTotalCostValue.setText(Utils.getFormattedPrice(String.valueOf(totalcost)));
//        }

    }

    private void initFabIcon() {
        Log.d(TAG, "initFabIcon: ");
        try {
            floatingButton = findViewById(R.id.my_floating_button);
            floatingButton.setStartAngle(0)
                    .setEndAngle(360)
                    .setAnimationType(AnimationType.EXPAND)
                    .setRadius(170)
                    .setAnchored(false)
                    .getAnimationHandler()
                    .setOpeningAnimationDuration(500)
                    .setClosingAnimationDuration(200)
                    .setLagBetweenItems(0)
                    .setOpeningInterpolator(new FastOutSlowInInterpolator())
                    .setClosingInterpolator(new FastOutLinearInInterpolator())
                    .shouldFade(true)
                    .shouldScale(true)
                    .shouldRotate(true);

            floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));

            floatingButton.setStateChangeListener(new FloatingMenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingMenuButton floatingMenuButton) {
                    Log.d(TAG, "onMenuOpened: ");
                    Utils.hideKeyboard(PaymentDetailActivity.this);
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.cross));
                }

                @Override
                public void onMenuClosed(FloatingMenuButton floatingMenuButton) {
                    floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                }
            });

            sbProfile = findViewById(R.id.sbProfile);
            sbProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 6);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbSocial = findViewById(R.id.sbSocial);
            sbSocial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 3);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbChat = findViewById(R.id.sbChat);
            sbChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent gotoNext = new Intent(PaymentDetailActivity.this, UploadDataActivity.class);
                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
                }
            });

            sbWorld = findViewById(R.id.sbWorld);
            sbWorld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 2);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbSearch = findViewById(R.id.sbSearch);
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                }
            });

            sbflSetting = findViewById(R.id.sbflSetting);
            sbflSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 7);
                    setResult(RESULT_OK, returnIntent);
                    finish();

                }
            });

            subFriend = findViewById(R.id.subFriend);
            subFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 5);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });

            sbflHome = findViewById(R.id.sbflHome);
            sbflHome.setOnClickListener(view -> {
                floatingButton.closeMenu();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            });

        } catch (Exception e) {
            Log.e(TAG, "initFabIcon: ", e);
        }
    }

    public void initControl() {
//        for (int i = 1; i <= Integer.parseInt(itemTotalquantity); i++) {
//            strings.add("" + i);
//        }

//        setSpinnerValue(strings);
        imgToolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        frmItemQuantity.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                FragmentManager fm = getFragmentManager();
//                DialogFragmentForItemQuantity dialogFragment = new DialogFragmentForItemQuantity(commonMethod, strings, context);
//                dialogFragment.show(fm, "Time Fragment");
//            }
//        });

//        txtQuntity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fm = getFragmentManager();
//                DialogFragmentForItemQuantity dialogFragment = new DialogFragmentForItemQuantity(commonMethod, strings, context);
//                dialogFragment.show(fm, "Time Fragment");
//            }
//        });

//        txtQuntity.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (Utils.getFormattedPrice(totalcost * Integer.parseInt(txtQuntity.getText().toString())) != null) {
//                    txtTotalCostValue.setText(Utils.getFormattedPrice(totalcost * Integer.parseInt(txtQuntity.getText().toString())));
//                }
//            }
//        });

        callGetAddreshApi();
    }

    private void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient: ");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void onClickContinue(View view) {
        if (isValidation()) {
            callPostAddress();
//            String shippingAdress = edtStreet.getText().toString() + ", " + edtCity.getText().toString() + ", " + edtZipcode.getText().toString()
//                    + ", " + edtState.getText().toString() + ", " + countryPicker.getSelectedCountryName();
//            Intent i = new Intent(context, NewConfigurePaymentActivity.class);
//            i.putExtra("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
//            i.putExtra("selected_quantity", txtQuntity.getText().toString());
//            i.putExtra("total_quantity", itemTotalquantity);
//            i.putExtra("item_cost", txtCostValue.getText().toString());
//            i.putExtra("item_shipping_cost", txtShippingCostValue.getText().toString());
//            i.putExtra("item_total_cost", txtTotalCostValue.getText().toString());
//            i.putExtra("shippingAdress", shippingAdress);
//            i.putExtra("screen", "paymentdetails");
//            i.putExtra("item_name", BundleUtils.getIntentExtra(getIntent(), "item_name", ""));
//            i.putExtra("item_media_thumb", BundleUtils.getIntentExtra(getIntent(), "item_media_thumb", ""));
//            startActivityForResult(i, REQ_CODE_CONFIGURE_PAYEMENT_RESULTS);
        }
    }

    void callPostAddress() {
        Map<String, Object> map = new HashMap<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("countryCode",countryPicker.getSelectedCountryNameCode());
        jsonObject.addProperty("Street", edtStreet.getText().toString());
        jsonObject.addProperty("City", edtCity.getText().toString());
        jsonObject.addProperty("Zipcode", edtZipcode.getText().toString());
        jsonObject.addProperty("State", edtState.getText().toString());
        jsonObject.addProperty("Country", countryPicker.getSelectedCountryName());
        map.put("address",jsonObject);

        /*ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setStreet(edtStreet.getText().toString());
        shippingAddress.setCity(edtCity.getText().toString());
        shippingAddress.setZipcode(edtZipcode.getText().toString());
        shippingAddress.setState(edtState.getText().toString());
        shippingAddress.setCountry(countryPicker.getSelectedCountryName());
        shippingAddress.setCountryCode(countryPicker.getSelectedCountryNameCode());

        RequestUpdateAddressBean requestUpdateAddressBean = new RequestUpdateAddressBean();
        requestUpdateAddressBean.setShippingAddress(shippingAddress);*/
       // map.put("Street", edtStreet.getText().toString());
       // map.put("City", edtCity.getText().toString());
       // map.put("Zipcode", edtZipcode.getText().toString());
      //  map.put("State", edtState.getText().toString());
       // map.put("Country", countryPicker.getSelectedCountryName());

        //map.put("uid", preferences.getUserId());

        if (Utils.isInternetOn(context)) {
            paypalbeanCall = mService.getFetcherService(context).postAddress(map);
            paypalbeanCall.enqueue(new Callback<AddressBean>() {
                @Override
                public void onResponse(Call<AddressBean> call, Response<AddressBean> response) {
                    finish();
                }

                @Override
                public void onFailure(Call<AddressBean> call, Throwable t) {    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    void callGetAddreshApi() {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());

        if (Utils.isInternetOn(context)) {
            paypalbeanCall = mService.getFetcherService(context).getAddress();
            paypalbeanCall.enqueue(new Callback<AddressBean>() {
                @Override
                public void onResponse(Call<AddressBean> call, Response<AddressBean> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.code()==StaticConstant.RESULT_OK){
                        ShippingAddress shippingAddress = response.body().getResult().getShippingAddress();
                        if (shippingAddress != null) {
                            if (shippingAddress.getStreet() != null) {
                                edtStreet.setText(shippingAddress.getStreet());
                            }
                            if (shippingAddress.getCity() != null) {
                                edtCity.setText(shippingAddress.getCity());
                            }
                            if (shippingAddress.getZipcode() != null) {
                                edtZipcode.setText(shippingAddress.getZipcode());
                            }
                            if (shippingAddress.getState() != null) {
                                edtState.setText(shippingAddress.getState());
                            }
                            if (shippingAddress.getCountry() != null) {
                                countryPicker.setCountryForNameCode(shippingAddress.getCountryCode() == null ? "" : shippingAddress.getCountryCode());
                            }

                    }}
                    else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
                    }
                }

                @Override
                public void onFailure(Call<AddressBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    isLocationRequested = true;
                    startLocationUpdates();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public void onBackPressed() {
        finish();
    }

    public boolean isValidation() {
        if (edtStreet.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.toast_no_street), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtCity.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.toast_no_city), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtZipcode.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.toast_no_zip), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtState.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.toast_no_state), DialogBox.DIALOG_FAILURE, null);
            return false;
        } /*else if (txtQuntity.getText().toString().equals("Select Qty") || txtQuntity.getText().equals("")) {
            Utils.showToast(context, getResources().getString(R.string.toast_no_quantity));
            return false;
        } */ else {
            return true;
        }
    }

    @OnClick(R.id.img_item_quantity)
    public void onClick() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode: " + requestCode);
        Log.d(TAG, "onActivityResult resultCode: " + resultCode);
        Utils.printIntentData(data);


        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                setResult(RESULT_OK, returnIntent);
                finish();
                return;
            }
        }

    }

    @Override
    public void commonMethod(String quantity, File... files) {
//        txtQuntity.setText(quantity);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {

        if (!isLocationRequested) {
            return;
        }

        Log.d(TAG, "startLocationUpdates: ");
        if (!isLocationEnabled()) {
            showAlert();
            return;
        }

        if (ContextCompat.checkSelfPermission(PaymentDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaymentDetailActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERM_ACCESS_LOCATION);
            return;
        }

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            Log.d(TAG, "startLocationUpdates: Location: " + Double.toString(mLocation.getLatitude()) + "," + Double.toString(mLocation.getLongitude()));
            countryPicker.setCountryForNameCode(getCountryNameCode(this, mLocation.getLatitude(), mLocation.getLongitude()));
            isLocationRequested = false;
        } else {
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_INTERVAL);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    public static String getCountryNameCode(Context context, double latitude, double longitude) {
        Log.d(TAG, "getCountryNameCode: ");
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;
            if (addresses != null && !addresses.isEmpty()) {
                Log.d(TAG, "getCountryNameCode: " + addresses.get(0).getCountryCode());
                return addresses.get(0).getCountryCode();
            }
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (isLocationRequested) {
            Log.d(TAG, "onLocationChanged: Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()));
            mLocation = location;
            countryPicker.setCountryForNameCode(getCountryNameCode(this, mLocation.getLatitude(), mLocation.getLongitude()));
            isLocationRequested = false;
        }
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult requestCode: " + requestCode);
        switch (requestCode) {
            case PERM_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                }
            }
            break;

        }
    }

    private boolean isLocationEnabled() {
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager != null && (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

}
