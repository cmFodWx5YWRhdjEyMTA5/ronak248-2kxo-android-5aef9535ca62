package com.screamxo.Activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Emoji.EmojiLayoutFragment;
import com.screamxo.Emoji.Emojix;
import com.screamxo.Emoji.tagging.OnHashTagClickListener;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.EmojiTextInterface;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogFragments;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class PostStreamActivity extends AppCompatActivity implements View.OnClickListener, CommonMethod, EmojiTextInterface, TextWatcher {

    private static final String TAG = "PostStreamActivity";

    @BindView(R.id.img_toolbar_right_icon)
    TextView imgToolbarRightIcon;
    @BindView(R.id.edt_stream_txt)
    EditText edtStreamTxt;
    @BindView(R.id.txt_user_name)
    TextView txtUserName;
    @BindView(R.id.img_user)
    ImageView imgUser;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_clock)
    ImageView imgClock;
    @BindView(R.id.ico_reaction)
    ImageView ico_reaction;

    @BindView(R.id.rl_BottomSheet)
    RelativeLayout rl_BottomSheet;

    @BindView(R.id.frame_emoji)
    FrameLayout frameEmoji;
    String msgDeleteTimer = null;
    Call<LoginBean> streampostcall;
    Map<String, String> map;
    int height, width;
    CommonMethod commonMethod;
    EmojiTextInterface emojiTextInterface;
    int textLenght = 0;
    private Context context;
    private Preferences preferences;
    private boolean isUpdate = false;
    private FetchrServiceBase mService;
    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;

    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;

    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(PostStreamActivity.this);
            if (heightDiff <= contentViewTop) {
                onHideKeyboard();
                Intent intent = new Intent("KeyboardWillHide");
                broadcastManager.sendBroadcast(intent);
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);

                Intent intent = new Intent("KeyboardWillShow");
                intent.putExtra("KeyboardHeight", keyboardHeight);
                broadcastManager.sendBroadcast(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_stream);
        ButterKnife.bind(this);
        init();
        initControlValue();
        attachKeyboardListeners();
        initFabIcon();
    }

    public void init() {
        context = this;
        commonMethod = this;
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
        map = new HashMap<>();
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
        emojiTextInterface = this;
    }

    protected void onShowKeyboard(int keyboardHeight) {
        rl_BottomSheet.setVisibility(View.VISIBLE);
    }

    protected void onHideKeyboard() {
        rl_BottomSheet.setVisibility(View.GONE);
    }

    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = findViewById(R.id.rootview);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        keyboardListenersAttached = true;
    }

    public void initControlValue() {
        txtUserName.setText(preferences.getUserFullName());
        setUserProfileImage();
    }

    private void setUserProfileImage() {
        Log.d(TAG, "setUserProfileImage: " + preferences.getUserProfile());
        if (preferences.getUserProfile() != null && !preferences.getUserProfile().isEmpty()) {
            Picasso.with(context)
                    .load(preferences.getUserProfile())
                    .placeholder(R.mipmap.user).centerCrop()
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .resize(width, height)
                    .into(imgUser);
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
                    Utils.hideKeyboard(PostStreamActivity.this);
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
                    Intent gotoNext = new Intent(PostStreamActivity.this, UploadDataActivity.class);
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

    @OnClick({R.id.img_toolbar_left_icon, R.id.img_clock, R.id.img_toolbar_right_icon, R.id.ico_reaction})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_toolbar_left_icon:
                onBackPressed();
                break;
            case R.id.img_toolbar_right_icon:
                callPostStreamApi();
                break;

            case R.id.img_clock:
                FragmentManager fm = getFragmentManager();
                DialogFragments dialogFragment = new DialogFragments(commonMethod);
                dialogFragment.show(fm, "Time Fragment");
                break;

            case R.id.ico_reaction:
                addEmojiFragment();
                break;
        }
    }

    private void addEmojiFragment() {
        if (frameEmoji.getChildCount() > 0) {
            frameEmoji.removeAllViews();
            setUserProfileImage();
        } else {
            Fragment fragment = new EmojiLayoutFragment(emojiTextInterface);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_emoji, fragment, "");
            fragmentTransaction.commit();
            ico_reaction.setImageResource(R.drawable.vect_key_board);
            Utils.hideKeyboard(this);
        }
    }

    @Override
    public void onBackPressed() {

        Intent returnintent = new Intent();
        returnintent.putExtra("update", isUpdate);
        setResult(RESULT_OK, returnintent);
        finish();
    }

    void callPostStreamApi() {

        Map<String, String> map = new HashMap<>();
        map.put("posttype", "0");
        map.put("posttitle", edtStreamTxt.getText().toString());
        map.put("postedby", preferences.getUserId());

        if (msgDeleteTimer != null) {
            map.put("posttiming", msgDeleteTimer);
        }

        if (Utils.isInternetOn(context)) {

            setViewEnableDisable(false);
            progressBar.setVisibility(View.VISIBLE);
            streampostcall = mService.getFetcherService(context).PostStream(map);
            streampostcall.enqueue(new Callback<LoginBean>() {
                @Override
                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {

                    setViewEnableDisable(true);
                    progressBar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            isUpdate = true;
                            Utils.showToast(context, response.body().getMsg());
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("FRAGMENT_INDEX", 1);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginBean> call, Throwable t) {
                    setViewEnableDisable(true);
                    progressBar.setVisibility(View.GONE);
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    void setViewEnableDisable(Boolean enable) {
        imgToolbarRightIcon.setEnabled(enable);
    }

    @Override
    public void commonMethod(String type, File... files) {
        msgDeleteTimer = type;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase, new OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {

            }
        }));
    }

    @Override
    protected void onDestroy() {

        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
        super.onDestroy();

    }

    @Override
    public void getEmojiText(String emojiText) {
        String s = edtStreamTxt.getText().toString().concat(emojiText);
        edtStreamTxt.setText(s);
        edtStreamTxt.setSelection(s.length());
    }

    private void deleteEmoji() {

        String text = edtStreamTxt.getText().toString();

        if (text.endsWith("[:angryface:")) {
            removeString(text, "\\[:angryface:");

        } else if (text.endsWith("[:bigsmile:")) {
            removeString(text, "\\[:bigsmile:");

        } else if (text.endsWith("[:cry:")) {
            removeString(text, "\\[:cry:");

        } else if (text.endsWith("[:dizzy:")) {
            removeString(text, "\\[:dizzy:");

        } else if (text.endsWith("[:rosy-chicks:")) {
            removeString(text, "\\[:rosy-chicks:");

        } else if (text.endsWith("[:tongue:")) {
            removeString(text, "\\[:tongue:");

        } else if (text.endsWith("[:wink:")) {
            removeString(text, "\\[:wink:");
        }
    }

    private void removeString(String text, String emoji) {

        Pattern p = Pattern.compile(emoji);
        Matcher m = p.matcher(text);
        int satrtIndex = 0, endIndex = 0;
        while (m.find()) {
            satrtIndex = m.start();
            endIndex = m.end();
        }
        String s = new StringBuilder().append(text).delete(satrtIndex, endIndex).toString();
        edtStreamTxt.setText(s);
        Log.e("setText " + s, "removeString: " + edtStreamTxt.getText().toString());
        edtStreamTxt.setSelection(s.length());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (textLenght > charSequence.length()) {
            deleteEmoji();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        textLenght = editable.length();
    }
}
