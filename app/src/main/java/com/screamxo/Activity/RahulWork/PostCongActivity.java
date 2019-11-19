package com.screamxo.Activity.RahulWork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.ImageUtils;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_BOOST_ACTIVITY_RESULTS;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;

public class PostCongActivity extends AppCompatActivity {
    private static final String TAG = "PostCongActivity";

    @BindView(R.id.imgView)
    ImageView imgView;
    String itemImage;

    @BindView(R.id.item_name_tv)
    TextView item_name_tv;

    @BindView(R.id.ico_back)
    ImageView ico_back;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        Utils.printIntentData(getIntent());
        setContentView(R.layout.activity_post_cong);
        ButterKnife.bind(this);
        initData();
        initFabIcon();
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("item_image")) {
                itemImage = getIntent().getStringExtra("item_image");
                Picasso.with(PostCongActivity.this)
                        .load(itemImage)
                        .placeholder(R.mipmap.img_placeholder)
                        .error(R.mipmap.img_placeholder)
                        .fit()
                        .centerCrop()
                        .into(imgView);

                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageUtils.customChoosePhoto(PostCongActivity.this, itemImage);
                    }
                });
            }
            if (getIntent().getExtras().containsKey("item_name")) {
                item_name_tv.setText(getIntent().getStringExtra("item_name"));
            }
        }

        ico_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    Utils.hideKeyboard(PostCongActivity.this);
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
                    Intent gotoNext = new Intent(PostCongActivity.this, UploadDataActivity.class);
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
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

//    public void funShare(View view) {
//        new LongOperation().execute("");
//    }

    public void boostItem(View view) {
        Intent gotoActivity = new Intent(this, BoostActivity.class);
        gotoActivity.putExtra("image", BundleUtils.getIntentExtra(getIntent(), "item_image", ""));
        gotoActivity.putExtra("boost_type", String.valueOf(StaticConstant.BOOST_TYPE_SHOP_ITEM));
        gotoActivity.putExtra("itemid", BundleUtils.getIntentExtra(getIntent(), "itemid", ""));
        startActivityForResult(gotoActivity, REQ_CODE_BOOST_ACTIVITY_RESULTS);
        finish();
    }

    public void funAnother(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("POST_ANOTHER", true);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

//    class LongOperation extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                OutputStream output;
//                Bitmap bitmap = Picasso.with(PostCongActivity.this).load(itemImage).get();
//
//                File filepath = Environment.getExternalStorageDirectory();
//                File dir = new File(filepath.getAbsolutePath() + "/screamxoImage/");
//                dir.mkdirs();
//                File file = new File(dir, "share.jpeg");
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("image/jpeg");
//                output = new FileOutputStream(file);
//                // Compress into png format image from 0% - 100%
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
//                output.flush();
//                output.close();
//                Uri uri = Uri.fromFile(file);
//                share.putExtra(Intent.EXTRA_STREAM, uri);
//                share.putExtra(Intent.EXTRA_TEXT, "2KXO");
//                startActivity(Intent.createChooser(share, "Share Image"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//        }
//    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("POST_ANOTHER", false);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
