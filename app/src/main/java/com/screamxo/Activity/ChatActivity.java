package com.screamxo.Activity;

import android.Manifest;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.ChatBean;
import com.example.apimodule.ApiBase.ApiBean.Item;
import com.example.apimodule.ApiBase.ApiBean.Message;
import com.example.apimodule.ApiBase.ApiBean.Otheruser;
import com.example.apimodule.ApiBase.ApiBean.SendMsgBean;
import com.example.apimodule.ApiBase.ApiBean.cart.model.CartBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.Preferences;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.screamxo.Activity.RahulWork.CartCheckoutActivity;
import com.screamxo.Activity.RahulWork.CustomWebViewFragment;
import com.screamxo.Activity.RahulWork.UploadDataActivity;
import com.screamxo.Adapter.ChatAdapter;
import com.screamxo.Emoji.EmojiLayoutFragment;
import com.screamxo.Emoji.Emojix;
import com.screamxo.Fragment.NewCartFragment;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.EmojiTextInterface;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogFragments;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
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

import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_PATH_ARG;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.FireBasePush.MyFirebaseMessagingService.ACTION_CHAT_MESSAGE;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, CommonMethod, EmojiTextInterface, TextWatcher, android.support.v4.app.FragmentManager.OnBackStackChangedListener {

    private static final int CAPTURE_MEDIA = 368;
    private static final String TAG = "ChatActivity";
    public String otherUserName = "", otherUserPhoto = "", itemId;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.lny_send_msg)
    LinearLayout lnySendMsg;
    @BindView(R.id.img_emoji)
    ImageView imgEmoji;
    @BindView(R.id.edt_msg)
    EditText edtMsg;
    @BindView(R.id.img_timer)
    ImageView imgTimer;
    @BindView(R.id.img_item)
    ImageView imgItem;
    @BindView(R.id.txt_item_name)
    TextView txtItemName;
    @BindView(R.id.txt_item_rate)
    TextView txtItemRate;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.lny_item_header)
    LinearLayout lnyItemHeader;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    int firstVisibleItem;
    int lastVisibleItem;
    Context context;
    @BindView(R.id.frame_emoji)
    FrameLayout frameEmoji;
    int pageCounter = 0, totalMsgCount = 0;
    String otherUid = "0";
    String LIMIT = "5000", msgDeleteTimer = null;
    Call<ChatBean> chatBeanCall;
    Call<SendMsgBean> sendMsgBeanCall;
    ArrayList<Message> messageArrayList;
    Otheruser otherUserData;
    LinkedHashMap<String, File> fileArray;
    RequestBodyConveter requestbodyconverter;
    String type[];
    Boolean iscameFromSellerChat = false;
    EmojiTextInterface emojiTextInterface;
    int textLenght = 0;
    CommonMethod commonMethod;
    @BindView(R.id.img_User)
    ImageView imgUser;
    @BindView(R.id.ico_back)
    ImageView ico_back;
    @BindView(R.id.txt_username)
    TextView txt_username;
    @BindView(R.id.ico_camera_chat)
    ImageView ico_camera_chat;

    com.screamxo.Utils.Preferences preferences;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private ChatAdapter chatAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FetchrServiceBase mService;
    private BroadcastReceiver broadcastReceiver;
    private int heightProfile;
    private int widthProfile;
    private String filePath = "";
    private MenuItem cameraMenuItem;

    public static Bitmap loadBitmap(String filePath, int requiredWidth, int requiredHeight) {

        BitmapFactory.Options options = getOptions(filePath, requiredWidth, requiredHeight);

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static BitmapFactory.Options getOptions(String filePath, int requiredWidth, int requiredHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        // setting inJustDecodeBounds to true
        // ensures that we are able to measure
        // the dimensions of the image,without
        // actually allocating it memory
        options.inJustDecodeBounds = true;

        // decode the file for measurement
        BitmapFactory.decodeFile(filePath, options);

        // obtain the inSampleSize for loading a
        // scaled down version of the image.
        // options.outWidth and options.outHeight
        // are the measured dimensions of the
        // original image
        options.inSampleSize = getScale(options.outWidth, options.outHeight, requiredWidth, requiredHeight);

        // set inJustDecodeBounds to false again
        // so that we can now actually allocate the
        // bitmap some memory
        options.inJustDecodeBounds = false;

        return options;

    }

    public static int getScale(int originalWidth, int originalHeight, final int requiredWidth,
                               final int requiredHeight) {
        // a scale of 1 means the original dimensions
        // of the image are maintained
        int scale = 1;

        // calculate scale only if the height or width of
        // the image exceeds the required value.
        if ((originalWidth > requiredWidth) || (originalHeight > requiredHeight)) {
            // calculate scale with respect to
            // the smaller dimension
            if (originalWidth < originalHeight)
                scale = Math.round((float) originalWidth / requiredWidth);
            else
                scale = Math.round((float) originalHeight / requiredHeight);
        }

        return scale;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d(TAG, "onCreate: ");
        Utils.printIntentData(getIntent());
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        StaticConstant.CHAT_SCREEN = true;
        StaticConstant.selectedTimePos = -1;
        init();
        initControlValue();
        initControlListner();
        callgetChatData();
        initFabIcon();
    }

    private void init() {
        commonMethod = this;
        context = this;
        preferences = new com.screamxo.Utils.Preferences(context);
        BitmapDrawable bdUser = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.pic_holder_dashboard);
        heightProfile = bdUser.getBitmap().getHeight();
        widthProfile = bdUser.getBitmap().getWidth();
        mService = new FetchrServiceBase();
        emojiTextInterface = this;
        type = new String[1];
        fileArray = new LinkedHashMap<>();
        messageArrayList = new ArrayList<>();
        requestbodyconverter = new RequestBodyConveter();
        linearLayoutManager = new LinearLayoutManager(context);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("userProfile")) {
            if (!TextUtils.isEmpty(getIntent().getExtras().getString("userProfile"))) {
                Picasso.with(context)
                        .load(getIntent().getExtras().getString("userProfile"))
                        .placeholder(R.mipmap.ic_user)
                        .error(R.mipmap.ic_user)
                        .transform(new CircleTransform())
                        .into(imgUser);
            }
        }

    }

    private void initControlListner() {
        edtMsg.addTextChangedListener(this);
        edtMsg.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                fileArray.clear();

                if (!TextUtils.isEmpty(edtMsg.getText().toString().trim()))
                    callSendMsgApi();
                handled = true;
            }
            return handled;
        });

        ico_back.setOnClickListener(view -> finish());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (firstVisibleItem == 0 && totalMsgCount > messageArrayList.size()) {
                    callgetChatData();
                }
            }
        });
        imgTimer.setOnClickListener(this);
    }

    private void initControlValue() {
        Log.d(TAG, "initControlValue: ");
        Utils.printIntentData(getIntent());
        recyclerView.setLayoutManager(linearLayoutManager);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("otherUid")) {
            otherUid = getIntent().getExtras().getString("otherUid");
            if (otherUid == null)
                otherUid = getIntent().getExtras().getInt("otherUid") + "";
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("username")) {
            otherUserName = getIntent().getExtras().getString("username");
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("itemId")) {
            itemId = getIntent().getExtras().getString("itemId");
            iscameFromSellerChat = true;
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("message")) {
            edtMsg.setText(getIntent().getExtras().getString("message"));
        }

        /*in case of sending question directly..*/
        if (!isTopic() && edtMsg.getText().toString().length() > 0) {
            Utils.hideKeyboard(ChatActivity.this);
            if (edtMsg.getText().toString().contains("http")) {
                if (edtMsg.getText().toString().contains(".mp4")) {
                    type[0] = "1";

                    downloadimage(itemId, edtMsg.getText().toString(), ".mp4");
                } else if ((edtMsg.getText().toString().contains(".png"))
                        || (edtMsg.getText().toString().contains(".jpg"))
                        || (edtMsg.getText().toString().contains(".jpeg"))
                        || (edtMsg.getText().toString().contains(".gif"))) {
                    type[0] = "2";
                    String ext = "";
                    if (edtMsg.getText().toString().contains(".png")) {
                        ext = ".png";
                    } else if (edtMsg.getText().toString().contains(".jpg")) {
                        ext = ".jpg";
                    } else if (edtMsg.getText().toString().contains(".jpeg")) {
                        ext = ".jpeg";
                    } else if (edtMsg.getText().toString().contains(".gif")) {
                        ext = ".gif";
                    }
                    downloadimage(itemId, edtMsg.getText().toString(), ext);
                } else {
                    fileArray.clear();

                    if (!TextUtils.isEmpty(edtMsg.getText().toString().trim()))
                        callSendMsgApi();
                }
            } else {
                fileArray.clear();
                if (!TextUtils.isEmpty(edtMsg.getText().toString().trim()))
                    callSendMsgApi();
            }
        }
        setTitle();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pageCounter = 0;
                callgetChatData();
            }
        };

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemId != null)
                    callMoveToCartApi(itemId, "1");
            }
        });
    }

    private void downloadimage(String itemId, String url_item, String ext) {
        Log.i("CHAT ATTACHMENT", "INSIDE DOWNLOAD IMAGE+++++" + url_item);

        fileArray.clear();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/req_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ext;
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();

        try {

            URL url = new URL(url_item);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            Utilss.CopyStream(is, os);
            os.close();
            conn.disconnect();

        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
        }
        fileArray.put("media", file);

        if (!TextUtils.isEmpty(edtMsg.getText().toString().trim()) || (fileArray.size() != 0 && file != null))
            callSendMsgApi();
        filePath = "";

    }

    private void setTitle() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null && (fragment instanceof CustomWebViewFragment)) {
            txt_username.setText("2KXO");
            return;
        } else {
            try {
                if (!BundleUtils.getIntentExtra(getIntent(), "username", "").isEmpty()) {
                    txt_username.setText(BundleUtils.getIntentExtra(getIntent(), "username", ""));
                } else {
                    txt_username.setText(BundleUtils.getIntentExtra(getIntent(), "fullname", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ico_camera_chat.setOnClickListener(view -> {
            RxPermissions.getInstance(ChatActivity.this)
                    .request(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            new SandriosCamera(ChatActivity.this, CAPTURE_MEDIA)
                                    .setShowPicker(true)
                                    .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                                    .enableImageCropping(true)
                                    .launchCamera();
                        }
                    });
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
                    Utils.hideKeyboard(ChatActivity.this);
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
                    Intent gotoNext = new Intent(ChatActivity.this, UploadDataActivity.class);
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
            sbSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.floating_chat));
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

    private void callMoveToCartApi(String id, String qty) {
        Map<String, String> map = new HashMap<>();
        com.screamxo.Utils.Preferences preference = new com.screamxo.Utils.Preferences(context);
        map.put("user_id", preference.getUserId());
        map.put("item_id", id);
        map.put("cart_qty", qty);

        Call<CartBean> cartBeanCall = mService.getFetcherService(context).addToCart(map);
        if (Utils.isInternetOn(context)) {
            cartBeanCall.enqueue(new Callback<CartBean>() {
                @Override
                public void onResponse(Call<CartBean> call, Response<CartBean> response) {
                    Intent i = new Intent(context, CartCheckoutActivity.class);
                    i.putExtra("screen", NewCartFragment.class.getSimpleName());
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<CartBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @SuppressWarnings("unused")
    private boolean isTopic() {
        return getIntent().getExtras() != null && getIntent().getExtras().containsKey("isTopic") && getIntent().getExtras().getBoolean("isTopic");
    }

    private void setAdapter(boolean isReceive) {
        if (isReceive)
            Collections.reverse(messageArrayList);
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(context, messageArrayList, otherUserData);
            recyclerView.setAdapter(chatAdapter);
            recyclerView.scrollToPosition(messageArrayList.size() - 1);
        } else {
            chatAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageArrayList.size() - 1);
        }
    }

    void callgetChatData() {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("offset", "" + pageCounter);
            map.put("limit", LIMIT);
            map.put("otherid", "" + otherUid);

            if (iscameFromSellerChat) {
                map.put("itemid", itemId);
            }

            chatBeanCall = mService.getFetcherService(context).getChatData(map);
            chatBeanCall.enqueue(new Callback<ChatBean>() {
                @Override
                public void onResponse(Call<ChatBean> call, Response<ChatBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("screen"))
                            if (getIntent().getStringExtra("screen").equals("sendmoney")) {
                                String amount = getIntent().getStringExtra("amount");
                                amount = "I donated $" + amount;
                                callSendMsgApi(amount);
                            }
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (pageCounter == 0) {
                                messageArrayList.clear();
                                try {

                                    if (iscameFromSellerChat && response.body().getResult().getItem().getIspurchased().equals("0")) {
                                        setItemDetail(response.body().getResult().getItem());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            otherUserData = response.body().getResult().getOtheruser();
                            messageArrayList.addAll(response.body().getResult().getMessages());
                            if (messageArrayList.size() > 0) {
                                if (response.body().getResult() != null && response.body().getResult().getOtheruser() != null)
                                    otherUserPhoto = response.body().getResult().getOtheruser().getUserphoto();
                                pageCounter++;
                                totalMsgCount = response.body().getResult().getCount();
                                setAdapter(true);
                            }
                           else if(iscameFromSellerChat){
                               callSendMsgApi("Send tracking");
                            }

                        }


                    }
                }

                @Override
                public void onFailure(Call<ChatBean> call, Throwable t) {
                    if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("screen"))
                        if (getIntent().getStringExtra("screen").equals("sendmoney")) {
                            String amount = getIntent().getStringExtra("amount");
                            amount = "I donated $" + amount;
                            callSendMsgApi(amount);
                        }
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setItemDetail(Item item) {
        lnyItemHeader.setVisibility(View.VISIBLE);
        Log.d(TAG, "setItemDetail: " + item.getName());
        txtItemName.setText(item.getName());

        if (item.getItemprice() == null || Utils.getFormattedPrice(item.getItemprice()) == null) {
            txtItemRate.setVisibility(View.GONE);
        } else {
            txtItemRate.setText(Utils.getFormattedPrice(item.getItemprice()));
        }
        if (item.getMedia_thumb() != null && !item.getMedia_thumb().equals("")) {
            Picasso.with(context)
                    .load(item.getMedia_thumb())
                    .error(R.mipmap.pic_holder_dashboard)
                    .placeholder(R.mipmap.pic_holder_dashboard)
                    .resize(widthProfile, heightProfile)
                    .centerCrop()
                    .into(imgItem);
        }

    }

    @Override
    public void commonMethod(String type, File... File) {
//      type = return Time select
        msgDeleteTimer = type;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode: " + requestCode);
        Log.d(TAG, "onActivityResult resultCode: " + resultCode);

        if (data != null && data.getExtras() != null && data.getExtras().containsKey("FRAGMENT_INDEX")) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("FRAGMENT_INDEX", data.getExtras().getInt("FRAGMENT_INDEX"));
            setResult(RESULT_OK, returnIntent);
            finish();
            return;
        }

        if (requestCode == CAPTURE_MEDIA && resultCode == RESULT_OK) {
            filePath = data.getStringExtra(FILE_PATH_ARG);
            File f;
            Log.d(TAG, "onActivityResult filePath: " + filePath);
            fileArray.clear();
            if (filePath.contains("mp4")) {
                type[0] = "1";
                f = new File(filePath);
            } else {
                type[0] = "2";
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(new File(filePath).getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                int angle = 0;

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }
                Matrix mat = new Matrix();
                mat.postRotate(angle);
                StaticConstant.photo = loadBitmap(new File(filePath).getAbsolutePath(), 400, 400);
                StaticConstant.photo = Bitmap.createBitmap(StaticConstant.photo, 0, 0, StaticConstant.photo.getWidth(),
                        StaticConstant.photo.getHeight(), mat, true);

                f = StaticConstant.saveImage(StaticConstant.photo);
            }

            fileArray.put("media", f);

            if ((fileArray.size() != 0 && f != null) || !TextUtils.isEmpty(edtMsg.getText().toString().trim()))
                callSendMsgApi();
            filePath = "";
        }
    }

    public void callDeleteChatMsg(int position) {
        if (Utils.isInternetOn(context)) {

            HashMap<String, String> map = new HashMap<>();
            map.put("offset", "" + pageCounter);
            map.put("limit", LIMIT);
            map.put("otherid", "" + otherUid);
            map.put("messageid", messageArrayList.get(position).getMessageid());

            if (iscameFromSellerChat) {
                map.put("itemid", itemId);
            }

            messageArrayList.remove(position);
            setAdapter(true);

            chatBeanCall = mService.getFetcherService(context).deleteMsg(map);

            chatBeanCall.enqueue(new Callback<ChatBean>() {
                @Override
                public void onResponse(Call<ChatBean> call, Response<ChatBean> response) {
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ChatBean> call, Throwable t) {
                    t.printStackTrace();
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callSendMsgApi() {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();

            map.put("fromid", "" + new Preferences(context).getUserId());
            map.put("toid", "" + otherUid);
            map.put("messagetype", "1");
            map.put("messagedetail", "" + edtMsg.getText().toString());

            if (msgDeleteTimer != null) {
                map.put("messagetiming", msgDeleteTimer);
            }

            if (iscameFromSellerChat) {
                map.put("itemid", itemId);
            }

            edtMsg.setText("");
            progreessbar.setVisibility(View.VISIBLE);
            sendMsgBeanCall = mService.getFetcherService(context).sendMsg(requestbodyconverter.converRequestBodyFromMap(map),
                    requestbodyconverter.converRequestBodyFromMapImage(fileArray, type));
            sendMsgBeanCall.enqueue(new Callback<SendMsgBean>() {
                @Override
                public void onResponse(Call<SendMsgBean> call, Response<SendMsgBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            fileArray.clear();
                            msgDeleteTimer = null;
                            messageArrayList.add(response.body().getResult());
                            setAdapter(false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SendMsgBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callSendMsgApi(String message) {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();

            map.put("fromid", "" + new Preferences(context).getUserId());
            map.put("toid", "" + otherUid);
            map.put("messagetype", "1");
            map.put("messagedetail", "" + message);

            if (msgDeleteTimer != null) {
                map.put("messagetiming", msgDeleteTimer);
            }

            if (iscameFromSellerChat) {
                map.put("itemid", itemId);
            }

            edtMsg.setText("");
            progreessbar.setVisibility(View.VISIBLE);
            sendMsgBeanCall = mService.getFetcherService(context).sendMsg(requestbodyconverter.converRequestBodyFromMap(map),
                    requestbodyconverter.converRequestBodyFromMapImage(fileArray, type));
            sendMsgBeanCall.enqueue(new Callback<SendMsgBean>() {
                @Override
                public void onResponse(Call<SendMsgBean> call, Response<SendMsgBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            fileArray.clear();
                            msgDeleteTimer = null;
                            messageArrayList.add(response.body().getResult());
                            setAdapter(false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SendMsgBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @OnClick({R.id.img_emoji, R.id.img_timer})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_emoji:
                addEmojiFragment();
                break;
            case R.id.img_timer:
                FragmentManager fm = getFragmentManager();
                DialogFragments dialogFragment = new DialogFragments(commonMethod);
                dialogFragment.show(fm, "Time Fragment");
                break;
        }
    }

    private void addEmojiFragment() {
        @SuppressWarnings("unused")
        View view = this.getCurrentFocus();
        if (frameEmoji.getChildCount() > 0) {
            frameEmoji.removeAllViews();
            imgEmoji.setImageResource(R.mipmap.like);

        } else {
            Fragment fragment = new EmojiLayoutFragment(emojiTextInterface);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_emoji, fragment, "");
            fragmentTransaction.commit();
            imgEmoji.setImageResource(R.drawable.vect_key_board);
            Utils.hideKeyboard(this);
        }
    }

    // emoji custome listner for TextView and EditText
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase, hashTag -> {
        }));
    }

    @Override
    public void getEmojiText(String emojiText) {
        String s = edtMsg.getText().toString().concat(emojiText);
        edtMsg.setText(s);
        edtMsg.setSelection(s.length());
    }

    private void deleteEmoji() {
        String text = edtMsg.getText().toString();

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
        edtMsg.setText(s);
        Log.e("setText " + s, "removeString: " + edtMsg.getText().toString());
        edtMsg.setSelection(s.length());
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

    @Override
    protected void onDestroy() {
        StaticConstant.CHAT_SCREEN = false;
        StaticConstant.selectedTimePos = -1;
        chatBeanCall.cancel();
        context.unregisterReceiver(broadcastReceiver);
        if (chatAdapter != null) {
            chatAdapter.onDestroy();
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        context.registerReceiver(broadcastReceiver, new IntentFilter(ACTION_CHAT_MESSAGE));
        fileArray.clear();
        if (filePath.contains("mp4")) {
            type[0] = "2";
        } else {
            type[0] = "1";
        }
    }

    @Override
    public void onBackPressed() {
        StaticConstant.CHAT_SCREEN = false;

        if (isFinishing()) {
            return;
        }

        if (BundleUtils.getIntentExtra(getIntent(), "from_notification", false)) {
            Intent gotoHome = new Intent(context, DrawerMainActivity.class);
            startActivity(gotoHome);
            finish();
            return;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null && fragment instanceof CustomWebViewFragment) {
            android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            fragmentManager.popBackStack(CustomWebViewFragment.class.getSimpleName(), android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (chatAdapter != null) {
            chatAdapter.onConfigurationChanged();
        }
    }

    @Override
    public void onBackStackChanged() {
        setTitle();
    }
}
