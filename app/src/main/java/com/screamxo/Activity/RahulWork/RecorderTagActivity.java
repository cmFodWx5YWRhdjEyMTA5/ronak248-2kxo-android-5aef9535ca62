package com.screamxo.Activity.RahulWork;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.apimodule.ApiBase.ApiBean.PostMediaBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Interface.Imagepath;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.FileUtils;
import com.screamxo.Utils.PhotoPicker;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.videocompression.MediaController;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.animation.enumerators.AnimationType;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.listeners.FloatingMenuStateChangeListener;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;

import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Utils.StaticConstant.MEDIA_0;
import static com.screamxo.Utils.StaticConstant.POST_TYPE_IMAGE;
import static com.screamxo.Utils.StaticConstant.POST_TYPE_MUSIC;
import static com.screamxo.Utils.StaticConstant.POST_TYPE_RECORD;
import static com.screamxo.Utils.StaticConstant.POST_TYPE_VIDEO;

public class RecorderTagActivity extends AppCompatActivity implements View.OnClickListener, Imagepath {
    private static final String TAG = "RecorderTagActivity";

    @BindView(R.id.edTitle)
    EditText edTitle;
    @BindView(R.id.caption_et)
    EditText caption_et;
    @BindView(R.id.imgCancel)
    ImageView imgCancel;
    @BindView(R.id.imgRecord)
    ImageView imgRecord;
    @BindView(R.id.imgTick)
    ImageView imgTick;
    @BindView(R.id.img_custom_photo)
    ImageView imgCustomPhoto;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.img_waves)
    ImageView img_waves;

    Preferences preferences;
    Context context;
    LinkedHashMap<String, File> fileLinkedHashMap;
    String[] type;
    FloatingMenuButton floatingButton;
    FloatingSubButton sbflSetting, sbflHome, subFriend, sbSearch, sbWorld, sbProfile, sbChat, sbSocial;
    private FetchrServiceBase mService;
    private Call<PostMediaBean> postMediaCall;
    private String filePath;
    private PhotoPicker photoPicker;
    private File coverImage;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_tag);
        Utils.printIntentData(getIntent());
        ButterKnife.bind(this);
        init();
        initData();
        initFabIcon();
        if (uploadingFrom().equalsIgnoreCase("record")) {
            img_waves.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.ico_waveform)
                    .into(img_waves);
            playMedia();
            //  imgRecord.setImageResource(R.drawable.ic_mic_record);
        } else if (uploadingFrom().equalsIgnoreCase("music")) {
            img_waves.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.ico_waveform)
                    .into(img_waves);
            playMedia();
            //  imgRecord.setImageResource(R.mipmap.headphone_placeholder);
        } else {
            img_waves.setVisibility(View.GONE);
        }
    }

    private void init() {
        context = RecorderTagActivity.this;
        preferences = new Preferences(context);
        type = new String[2];
        mService = new FetchrServiceBase();
        fileLinkedHashMap = new LinkedHashMap<>();
        photoPicker = new PhotoPicker(context, this);
    }

    private void playMedia() {
        File fileMedia = new File(BundleUtils.getIntentExtra(getIntent(), "songUrl", ""));

        if (fileMedia != null) {
            mp = new MediaPlayer();
            try {
                mp.setDataSource(fileMedia.getPath());
                mp.prepare();
                mp.start();
                mp.setLooping(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    Utils.hideKeyboard(RecorderTagActivity.this);
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
                    Intent gotoNext = new Intent(RecorderTagActivity.this, UploadDataActivity.class);
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

    void callPostMediaApi(String postType) {
        if (fileLinkedHashMap.get(MEDIA_0) == null) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.msg_invalid_file_format), DialogBox.DIALOG_FAILURE, null);
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("postedby", preferences.getUserId());
        map.put("posttype", postType);
        map.put("posttitle", caption_et.getText().toString());

        MultipartBody.Part body = null;
        if ((postType == POST_TYPE_RECORD || postType == POST_TYPE_MUSIC) && coverImage != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), coverImage);
            body = MultipartBody.Part.createFormData("audioimage", coverImage.getName(), requestBody);
        }

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            postMediaCall = mService.getFetcherService(context, true, new Boolean[]{false})
                    .PostMedia(new RequestBodyConveter().converRequestBodyFromMap(map)
                            , new RequestBodyConveter().converRequestBodyFromMapImage(fileLinkedHashMap, type), body);
            postMediaCall.enqueue(new Callback<PostMediaBean>() {
                @Override
                public void onResponse(Call<PostMediaBean> call, Response<PostMediaBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Utils.showToast(context, response.body().getMsg());
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("FRAGMENT_INDEX", 1);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<PostMediaBean> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private String uploadingFrom() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("from")) {
            return getIntent().getExtras().getString("from");
        }
        return "";
    }

    private String getFileUrl() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("songUrl") && getIntent().getExtras().getString("songUrl") != null) {
            return getIntent().getExtras().getString("songUrl");
        }
        return "";
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            if (uploadingFrom().equalsIgnoreCase("record")) {
                fileLinkedHashMap.put(MEDIA_0, new File(BundleUtils.getIntentExtra(getIntent(), "songUrl", "")));
                type[0] = "4";
            } else if (uploadingFrom().equalsIgnoreCase("music")) {
                fileLinkedHashMap.put(MEDIA_0, FileUtils.getFile(context, Uri.parse(BundleUtils.getIntentExtra(getIntent(), "songUrl", ""))));
                type[0] = "3";
            } else if (uploadingFrom().equalsIgnoreCase("video")) {
                filePath = Uri.parse(BundleUtils.getIntentExtra(getIntent(), "songUrl", "")).toString();
                progreessbar.setVisibility(View.VISIBLE);
                new VideoCompressor().execute();
            } else {
                fileLinkedHashMap.put(MEDIA_0, new File(BundleUtils.getIntentExtra(getIntent(), "songUrl", "")));
                type[0] = "2";
            }
        }
    }

    @OnClick({R.id.imgCancel, R.id.imgTick, R.id.img_custom_photo, R.id.imgRecord})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgCancel:
                if (mp!=null)
                    mp.stop();
                finish();
                break;
            case R.id.imgTick:
                if (uploadingFrom().equalsIgnoreCase("record")) {
                    callPostMediaApi(POST_TYPE_RECORD);
                } else if (uploadingFrom().equalsIgnoreCase("music")) {
                    callPostMediaApi(POST_TYPE_MUSIC);
                } else if (uploadingFrom().equalsIgnoreCase("video")) {
                    callPostMediaApi(POST_TYPE_VIDEO);
                } else {
                    callPostMediaApi(POST_TYPE_IMAGE);
                }
                break;
            case R.id.img_custom_photo:
            case R.id.imgRecord:

                RxPermissions.getInstance(this)
                        .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                photoPicker.ImagePickker();
                            }
                        });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (postMediaCall != null) {
            postMediaCall.cancel();
        }
        if (mp!=null){
            mp.stop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (photoPicker != null) {
            photoPicker.onResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void imagePathfromCamara(File file) {
        coverImage = file;
        if (file != null)
            Picasso.with(context)
                    .load(file)
                    .into(imgCustomPhoto);
    }

    @Override
    public void imagePathfromGallery(File file) {
        coverImage = file;
        if (file != null)
            Picasso.with(context)
                    .load(file)
                    .into(imgCustomPhoto);
    }

    @Override
    public void imagepathfromCropImage(File file) {
        coverImage = file;
        if (file != null)
            Picasso.with(context)
                    .load(file)
                    .into(imgCustomPhoto);
    }

    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaController.getInstance().convertVideo(filePath);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                fileLinkedHashMap.put(MEDIA_0, new File(MediaController.cachedFile.getPath()));
                type[0] = "1";
            }
            progreessbar.setVisibility(View.GONE);
        }
    }
}
