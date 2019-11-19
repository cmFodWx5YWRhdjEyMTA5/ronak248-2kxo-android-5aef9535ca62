package com.screamxo.Activity.RahulWork;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.screamxo.Activity.PostStreamActivity;
import com.screamxo.Activity.UploadMediaAcitvity;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_CAPTION_ARG;
import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_PATH_ARG;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_POST_STREAM_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_MEDIA_ACTIVITY_RESULTS;

public class UploadDataActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UploadDataActivity";

    @BindView(R.id.txtUpload)
    TextView txtUpload;
    @BindView(R.id.txtStream)
    TextView txtStream;
    @BindView(R.id.txtMedia)
    TextView txtMedia;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        ButterKnife.bind(this);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorfb4a4c));
        initFabIcon();
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
                    Utils.hideKeyboard(UploadDataActivity.this);
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
//                    Intent gotoNext = new Intent(UploadDataActivity.this, UploadDataActivity.class);
//                    startActivityForResult(gotoNext, REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS);
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
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_chat));
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatingButton.closeMenu();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("FRAGMENT_INDEX", 101);
                    setResult(RESULT_OK, returnIntent);
                    finish();
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

    @OnClick({R.id.frame_payload, R.id.frame_media, R.id.frame_blog})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame_media:
                Intent gotoNext = new Intent(UploadDataActivity.this, UploadMediaAcitvity.class);
                startActivityForResult(gotoNext, REQ_CODE_UPLOAD_MEDIA_ACTIVITY_RESULTS);
                break;

            case R.id.frame_payload:
                startActivityForResult(new Intent(UploadDataActivity.this,
                        com.screamxo.Activity.RahulWork.SellItemActivity.class), StaticConstant.REQUEST_SELL_ITEM);
                finish();
                break;

            case R.id.frame_blog:
                startActivityForResult(new Intent(UploadDataActivity.this,
                        PostStreamActivity.class), REQ_CODE_POST_STREAM_ACTIVITY_RESULTS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
                setResult(RESULT_OK, returnIntent);
                finish();
                return;
            }
        }

        switch (requestCode) {
            case StaticConstant.REQUEST_SELL_ITEM:
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("POST_ANOTHER")) {
                    Intent intent = new Intent(this, com.screamxo.Activity.RahulWork.SellItemActivity.class);
                    startActivityForResult(intent, StaticConstant.REQUEST_SELL_ITEM);
                    finish();
                    return;
                }
                finish();
                break;
            case REQ_CODE_UPLOAD_MEDIA_ACTIVITY_RESULTS:
                Intent resultIntent = new Intent();
                resultIntent.putExtra(FILE_PATH_ARG, BundleUtils.getIntentExtra(data, FILE_PATH_ARG, ""));
                resultIntent.putExtra(FILE_CAPTION_ARG, BundleUtils.getIntentExtra(data, FILE_CAPTION_ARG, ""));
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
