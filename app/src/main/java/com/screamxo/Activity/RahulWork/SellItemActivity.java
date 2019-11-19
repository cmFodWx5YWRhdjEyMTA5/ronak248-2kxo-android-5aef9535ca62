package com.screamxo.Activity.RahulWork;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Adapter.SellItemAdapter;
import com.screamxo.Adapter.SellItemVPAdapter;
import com.screamxo.Fragment.SellItemFirstFragment;
import com.screamxo.Interface.Imagepath;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.PhotoPicker;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_POST_CONG_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Utils.StaticConstant.REQUEST_ITEM_CREATE;

public class SellItemActivity extends AppCompatActivity implements Imagepath, SellItemAdapter.OnImagePickedListener {
    public static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final String TAG = "SellItemActivity";
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.txtHeader)
    TextView txtHeader;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.imgone)
    ImageView imgone;
    @BindView(R.id.txtPhoto)
    TextView txtPhoto;
    @BindView(R.id.imgtwo)
    ImageView imgtwo;
    @BindView(R.id.txtDetails)
    TextView txtDetails;
    @BindView(R.id.imgthree)
    ImageView imgthree;
    @BindView(R.id.txtPrice)
    TextView txtPrice;
    @BindView(R.id.imgfour)
    ImageView imgfour;
    @BindView(R.id.txtFinish)
    TextView txtFinish;
    int pagerPosition = 0;

    PhotoPicker photoPicker;
    private int imagePos;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item2);
        ButterKnife.bind(this);
        photoPicker = new PhotoPicker(this, this, false);
        Preferences preferences = new Preferences(this);

        initView();
        setViewPagerAdapter();
        initFabIcon();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        txtHeader.setText("Sell Item");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setSelectedPage(int position) {
        if (position == 0) {
            setUIForFirstPage();
        } else if (position == 1) {
            setUIForSecondPage();
        } else if (position == 2) {
            setUIForThirdPage();
        } else if (position == 3) {
//          setUIForFourPage();
        }
    }

    private void initFabIcon() {
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
                    Utils.hideKeyboard(SellItemActivity.this);
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
                    Intent gotoNext = new Intent(SellItemActivity.this, UploadDataActivity.class);
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

        }
    }

    private void setUIForFirstPage() {
        imgone.setImageResource(R.drawable.vect_red_circle_selector);
        imgtwo.setImageResource(R.drawable.vect_gray_circle_selector);
        imgthree.setImageResource(R.drawable.vect_gray_circle_selector);
        imgfour.setImageResource(R.drawable.vect_gray_circle_selector);
        txtPhoto.setTextColor(getResources().getColor(R.color.colorRed));
        txtDetails.setTextColor(getResources().getColor(R.color.colorGray));
        txtPrice.setTextColor(getResources().getColor(R.color.colorGray));
        txtFinish.setTextColor(getResources().getColor(R.color.colorGray));
        viewpager.setCurrentItem(0, true);
    }

    private void setUIForSecondPage() {
        imgone.setImageResource(R.drawable.vect_gray_circle_selector);
        imgtwo.setImageResource(R.drawable.vect_red_circle_selector);
        imgthree.setImageResource(R.drawable.vect_gray_circle_selector);
        imgfour.setImageResource(R.drawable.vect_gray_circle_selector);
        txtPhoto.setTextColor(getResources().getColor(R.color.colorGray));
        txtDetails.setTextColor(getResources().getColor(R.color.colorRed));
        txtPrice.setTextColor(getResources().getColor(R.color.colorGray));
        txtFinish.setTextColor(getResources().getColor(R.color.colorGray));
        viewpager.setCurrentItem(1, true);
    }

    private void setUIForThirdPage() {
        imgone.setImageResource(R.drawable.vect_gray_circle_selector);
        imgtwo.setImageResource(R.drawable.vect_gray_circle_selector);
        imgthree.setImageResource(R.drawable.vect_red_circle_selector);
        imgfour.setImageResource(R.drawable.vect_gray_circle_selector);
        txtPhoto.setTextColor(getResources().getColor(R.color.colorGray));
        txtDetails.setTextColor(getResources().getColor(R.color.colorGray));
        txtPrice.setTextColor(getResources().getColor(R.color.colorRed));
        txtFinish.setTextColor(getResources().getColor(R.color.colorGray));
        viewpager.setCurrentItem(2, true);
    }

    private void setViewPagerAdapter() {
        SellItemVPAdapter mVeiePagerAdapter = new SellItemVPAdapter(SellItemActivity.this, getSupportFragmentManager());
        viewpager.setAdapter(mVeiePagerAdapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        pagerPosition = 0;
                        break;
                    case 1:
                        pagerPosition = 1;
                        break;
                    case 2:
                        pagerPosition = 2;
                        break;
                    case 3:
                        pagerPosition = 3;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewpager.setOnTouchListener((arg0, arg1) -> true);
    }

    @OnClick(R.id.imgBack)
    public void onBackPressed() {
        switch (pagerPosition) {
            case 0:
                finish();
                break;
            case 1:
                viewpager.setCurrentItem(0, true);
                break;
            case 2:
                viewpager.setCurrentItem(1, true);
                break;
            case 3:
                viewpager.setCurrentItem(2, true);
                break;
        }
    }

    private boolean getIsFileExist() {
        @SuppressLint("RestrictedApi")
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment != null && fragment instanceof SellItemFirstFragment) {
                return ((SellItemFirstFragment) fragment).getIsFileExist(imagePos);
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Utils.printIntentData(data);

        if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
            setResult(RESULT_OK, returnIntent);
            finish();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case StaticConstant.REQUEST_CAPTURE:
            case StaticConstant.REQUEST_GALLERY:
            case UCrop.REQUEST_CROP:
                if (photoPicker != null) {
                    photoPicker.onResult(requestCode, resultCode, data);
                }
                break;

            case REQUEST_ITEM_CREATE:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(this, PostCongActivity.class);
                    intent.putExtra("itemid", BundleUtils.getIntentExtra(data, "itemid", ""));
                    intent.putExtra("item_name", BundleUtils.getIntentExtra(data, "item_name", ""));
                    intent.putExtra("item_image", BundleUtils.getIntentExtra(data, "item_image", null));
                    startActivityForResult(intent, REQ_CODE_POST_CONG_ACTIVITY_RESULTS);
                    finish();
                }
                break;

            case REQ_CODE_POST_CONG_ACTIVITY_RESULTS:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        if (data.getExtras().containsKey("POST_ANOTHER")) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("POST_ANOTHER", data.getExtras().containsKey("POST_ANOTHER"));
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    } else {
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    public void imagePathfromCamara(File file) {
        updateProductImage(file);
    }

    @Override
    public void imagePathfromGallery(File file) {
        updateProductImage(file);
    }

    @Override
    public void imagepathfromCropImage(File file) {
        updateProductImage(file);
    }

    @SuppressLint("RestrictedApi")
    public void updateProductImage(File file) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment != null && fragment instanceof SellItemFirstFragment) {
                ((SellItemFirstFragment) fragment).updateProductImage(imagePos, file);
            }
        }
    }

    @Override
    public void onImagePicked(int position) {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        imagePos = position;
                        photoPicker.ImagePickker(getIsFileExist());
                    }
                });
    }

    public Fragment getFragmentFromViewpager(int position) {
        return ((Fragment) (viewpager.getAdapter().instantiateItem(viewpager, position)));
    }
}
