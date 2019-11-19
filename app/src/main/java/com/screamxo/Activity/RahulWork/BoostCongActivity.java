package com.screamxo.Activity.RahulWork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class BoostCongActivity extends AppCompatActivity {
    private static final String TAG = "BoostCongActivity";
    @BindView(R.id.imgView)
    ImageView imgView;
    String itemImage, item_name;
    @BindView(R.id.txt_item)
    TextView txt_item;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private com.example.apimodule.ApiBase.FetchrServiceBase mservice;
    private Preferences preferences = new Preferences(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost_cong);
        ButterKnife.bind(this);
        mservice = new com.example.apimodule.ApiBase.FetchrServiceBase();
        initData();
        initFabIcon();
    }

    private void initData() {
        if (getIntent().getExtras() != null) {

            itemImage = getIntent().getStringExtra("image");
            item_name = getIntent().getStringExtra("name");
            if (item_name != null)
                txt_item.setText(item_name.replaceAll("[-+.^:,@]", ""));
            if ((itemImage.contains("voice"))
                    || (itemImage.contains("audio"))) {
                Picasso.with(BoostCongActivity.this)
                        .load(R.mipmap.headphone_placeholder)
                        .placeholder(R.mipmap.headphone_placeholder)
                        .error(R.mipmap.headphone_placeholder)
                        .fit()
                        .centerCrop()
                        .into(imgView);
            } else {

                Picasso.with(BoostCongActivity.this)
                        .load(itemImage)
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .fit()
                        .centerCrop()
                        .into(imgView);
            }

            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageUtils.customChoosePhoto(BoostCongActivity.this, itemImage);
                }
            });
        }
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
                    Utils.hideKeyboard(BoostCongActivity.this);
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
                    Intent gotoNext = new Intent(BoostCongActivity.this, UploadDataActivity.class);
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

    public void funDone(View view) {
        callSucessBoostApi();
        finish();
    }

    public void callSucessBoostApi() {
//TODO debug for params

        if (BundleUtils.getIntentExtra(getIntent(), "boost_type", "").equals("2"))
            itemImage = BundleUtils.getIntentExtra(getIntent(), "videourl", "");
        else
            itemImage = BundleUtils.getIntentExtra(getIntent(), "image", "");

        String link = "http://apis.2kxo.com/item/itemdetail/" + BundleUtils.getIntentExtra(getIntent(), "itemid", "");
        if (item_name == null)
            item_name = " ";
        if (Utils.isInternetOn(this)) {
            Call<JsonElement> uploadBoostCall =
                    mservice.getBoostFetcherService(this, false)
                            .uploadBoost(getIntent().getStringExtra("member_uaserName"), 1, 1
                                    , link, item_name, itemImage, Integer.parseInt(getIntent().getStringExtra("boost_type"))
                                    , getIntent().getStringExtra("no_users"), getIntent().getStringExtra("no_days"));
            uploadBoostCall.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    android.widget.Toast.makeText(BoostCongActivity.this, "sucess", android.widget.Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(retrofit2.Call<JsonElement> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(this, getString(R.string.toast_no_internet));
        }
    }


    public void funShare(View view) {
        new LongOperation().execute("");
    }

    public void funAnother(View view) {
        finish();
    }

    class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OutputStream output;
                Bitmap bitmap = Picasso.with(BoostCongActivity.this).load(itemImage).get();

                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath.getAbsolutePath() + "/screamxoImage/");
                dir.mkdirs();
                File file = new File(dir, "share.jpeg");
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                output = new FileOutputStream(file);
                // Compress into png format image from 0% - 100%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                output.flush();
                output.close();
                Uri uri = Uri.fromFile(file);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, "2KXO");
                startActivity(Intent.createChooser(share, "Share Image"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


    }
}
