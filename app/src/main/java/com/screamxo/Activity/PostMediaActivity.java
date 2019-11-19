package com.screamxo.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.PostMediaBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.AudioRecorder.AudioResetRecorder;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.Imagepath;
import com.screamxo.R;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.DialogFragments;
import com.screamxo.Utils.FileUtils;
import com.screamxo.Utils.PhotoPicker;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.VideoPicker;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.screamxo.Utils.StaticConstant.MEDIA_0;

/*THIS CLASS IS NOT USED ANYWHERE IN THE PROJECT...*/
@Deprecated
public class PostMediaActivity extends AppCompatActivity implements Imagepath, CommonMethod {

    private static final String TAG = PostMediaActivity.class.getSimpleName();
    @BindView(R.id.img_voice)
    ImageView imgVoice;

    @BindView(R.id.img_music)
    ImageView imgMusic;
    Preferences preferences;

    @BindView(R.id.chronometer)
    Chronometer mTimer;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.progreessbar)
    ProgressBar progressBar;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.txt_username)
    TextView txtUserName;
    @BindView(R.id.img_profile_pic)
    ImageView mimgUser;
    @BindView(R.id.img_clock)
    ImageView imgClock;
    LinkedHashMap<String, File> fileLinkedHashMap;
    String[] type;
    PhotoPicker photoPicker;
    VideoPicker videoPicker;
    ImageView mimgprofile, mimgedit;
    ImageView cancle;
    String mStrtitle = "";
    int height, width;
    CommonMethod commonMethod;
    private Context context;
    private boolean isUpdate = false;
    private MediaRecorder mediaRecorder;
    private FetchrServiceBase mService;
    private Boolean audioImageFile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_media);
        ButterKnife.bind(this);
        init();
        initControlValue();
        initControlListener();
    }

    public void init() {
        context = this;
        type = new String[2];
        commonMethod = this;
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
        photoPicker = new PhotoPicker(context, this);

        videoPicker = new VideoPicker(context, this::setUriForVideo);
        fileLinkedHashMap = new LinkedHashMap<>();
    }

    public void initControlValue() {

        txtToolbarTitle.setText(R.string.title_post_media);
        imgToolbarRightIcon.setVisibility(View.GONE);

        txtUserName.setText(preferences.getUserName());
        try {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.user);
            height = bd.getBitmap().getHeight();
            width = bd.getBitmap().getWidth();
        } catch (Exception ignored) {

        }
        if (preferences.getUserProfile() != null && !preferences.getUserProfile().equals("")) {
            Picasso.with(context)
                    .load(preferences.getUserProfile())
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .transform(new CircleTransform())
                    .resize(width, height)
                    .into(mimgUser);
        }
    }

    public void initControlListener() {
        imgClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DialogFragments dialogFragment = new DialogFragments(commonMethod);
                dialogFragment.show(fm, "Time Fragment");
            }
        });
    }

    @OnClick(R.id.img_toolbar_left_icon)
    public void onClick() {
        onBackPressed();
    }

    public void onClickPhoto(View view) {

        RxPermissions.getInstance(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        audioImageFile = false;
                        photoPicker.ImagePickker();
                    }
                });
    }

    public void onClickVideo(View view) {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        audioImageFile = false;
                        videoPicker.Videopicker();
                    }
                });
    }

    public void onVoicClick(View view) {
        {
            if (mediaRecorder == null) {
                RxPermissions.getInstance(context)
                        .request(Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                {
                                    mediaRecorder = new MediaRecorder();
                                    mediaRecorder = AudioResetRecorder.resetRecorderforMcq("normal1");
                                    mTimer.setVisibility(View.VISIBLE);
                                    mTimer.setBase(SystemClock.elapsedRealtime());
                                    mTimer.start();
                                    mediaRecorder.start();
                                }
                            }
                        });
            } else {

                mediaRecorder.stop();
                mediaRecorder.release();
                mTimer.stop();
                String time = mTimer.getText().toString();
                //fileLinkedHashMap.clear();

               /* audioFile = new File(AudioResetRecorder.getAudioPathforPostMedia().toString());
                openAlertDialog();

                mTimer.setVisibility(View.GONE);

                mediaRecorder = null;*/

                try {
                    fileLinkedHashMap.clear();
                    fileLinkedHashMap.put("media[0]", new File(AudioResetRecorder.getAudioPathforPostMedia().toString()));
                    type[0] = "4";
//                    audioFile = new File(AudioResetRecorder.getAudioPathforPostMedia().toString());
                    openAlertDialog();
                } catch (NullPointerException ignored) {

                }
                mTimer.setVisibility(View.GONE);
                mediaRecorder = null;
            }
        }
    }

    public void onClickMusic(View view) {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent_upload, "Play the song with"), 1);
    }

    private void openAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(R.layout.audio_dialog, null);
        builder.setView(v);
        final AlertDialog ad = builder.show();

       /* ImageView mimgprofile, mimgedit;
        ImageView cancle = (ImageView) v.findViewById(R.id.imageView4);
        final EditText mtitle = (EditText) v.findViewById(R.id.editText);*/

//        ImageView mimgprofile, mimgedit;
        cancle = v.findViewById(R.id.imageView4);
        EditText medttitle = v.findViewById(R.id.editText);

        ImageView mpost = v.findViewById(R.id.btn_create_post);
        //  mimgedit = (ImageView) v.findViewById(R.id.imageView3);
        mimgprofile = v.findViewById(R.id.imageView2);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                audioImageFile = false;
                ad.dismiss();
            }
        });

        mpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "art created successfully", Toast.LENGTH_LONG).show();

                mStrtitle = medttitle.getText().toString();
                callPostMediaApi("3");
                ad.dismiss();
//                Toast.makeText(context, "art created successfully", Toast.LENGTH_LONG).show();
            }
        });

       /* mimgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxPermissions.getInstance(context)
                        .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                audioImageFile = true;
                                photoPicker.ImagePickker();
                            }
                        });
            }
        });*/

        mimgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxPermissions.getInstance(context)
                        .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                audioImageFile = true;
                                photoPicker.ImagePickker();
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (photoPicker != null && (requestCode == StaticConstant.REQUEST_GALLERY || requestCode == StaticConstant.REQUEST_CAPTURE || requestCode == UCrop.REQUEST_CROP)) {
            photoPicker.onResult(requestCode, resultCode, data);
        }
        if (videoPicker != null && (requestCode == StaticConstant.REQUEST_VIDEO || requestCode == StaticConstant.REQUEST_VIDEO_CAPTURE)) {
            videoPicker.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //the selected audio.
                Uri uri = data.getData();

                fileLinkedHashMap.clear();
                fileLinkedHashMap.put("media[0]", FileUtils.getFile(context, data.getData()));
                type[0] = "3";

                callPostMediaApi("4");
                Log.e("URI: ", "audio captured sussessfully==>" + uri);
            }
        }
    }

    @Override
    public void imagePathfromCamara(File file) {
   /*     BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        if (!audioImageFile) {
            fileLinkedHashMap.clear();
            fileLinkedHashMap.put("media[0]", file);
            type[0] = "2";
        } else {
            fileLinkedHashMap.put("audioimage", file);
            type[1] = "2";
        }
        Picasso.with(context)
                .load(file)
                .placeholder(R.mipmap.profile_pic_placeholder)
                .error(R.mipmap.profile_pic_placeholder)
                .resize(width, height)
                .centerCrop()
                .into(imgPhoto);*/
    }

    @Override
    public void imagePathfromGallery(File file) {

  /*      BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        if (!audioImageFile) {
            fileLinkedHashMap.clear();
            fileLinkedHashMap.put("media[0]", file);
            type[0] = "2";
        } else {
            fileLinkedHashMap.put("audioimage", file);
            type[1] = "2";
        }

        //imgPhoto.setColorFilter(ContextCompat.getColor(context,R.color.colorBlack));
        Picasso.with(context)
                .load(file)
                .placeholder(R.mipmap.profile_pic_placeholder)
                .error(R.mipmap.profile_pic_placeholder)
                .resize(width, height)
                .centerCrop()
                .into(imgPhoto);*/
    }

    @Override
    public void imagepathfromCropImage(File file) {

        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);

        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        if (mimgprofile != null && audioImageFile) {
            Picasso.with(context)
                    .load(file)
                    .placeholder(R.mipmap.profile_pic_placeholder)
                    .error(R.mipmap.profile_pic_placeholder)
                    .resize(width, height)
                    .centerCrop()
                    .into(mimgprofile);
        } else {
            Picasso.with(context)
                    .load(file)
                    .placeholder(R.mipmap.profile_pic_placeholder)
                    .error(R.mipmap.profile_pic_placeholder)
                    .resize(width, height)
                    .centerCrop()
                    .into(imgPhoto);
        }

        if (!audioImageFile) {
            fileLinkedHashMap.clear();
            fileLinkedHashMap.put("media[0]", file);
            type[0] = "2";
            callPostMediaApi("2");
        } else {
            fileLinkedHashMap.put("audioimage", file);
            type[1] = "2";
            audioImageFile = false;
        }
        //imgPhoto.setColorFilter(ContextCompat.getColor(context,R.color.colorBlack));
    }

    private void setUriForVideo(File selectedImage, Uri uri) {
        //  profileVideo = selectedImage;
        fileLinkedHashMap.clear();

        // fileLinkedHashMap.put("media", selectedImage);

        fileLinkedHashMap.put("media[0]", selectedImage);
        type[0] = "1";
        callPostMediaApi("1");
        // Bitmap thumb = ThumbnailUtils.createVideoThumbnail(profileVideo.getAbsolutePath(),
        //         MediaStore.Images.Thumbnails.MICRO_KIND);
        //   imgPhoto.setImageBitmap(thumb);
        //   imgPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("update", isUpdate);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    void callPostMediaApi(String postType) {

        if (fileLinkedHashMap.get(MEDIA_0) == null) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), "Invalid file format", DialogBox.DIALOG_FAILURE, null);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("postedby", preferences.getUserId());
        map.put("posttype", postType);
        map.put("posttitle", mStrtitle);

        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);

            Call<PostMediaBean> postMediaCall = mService.getFetcherService(context).PostMedia(new RequestBodyConveter().converRequestBodyFromMap(map)
                    , new RequestBodyConveter().converRequestBodyFromMapImage(fileLinkedHashMap, type),null);

            postMediaCall.enqueue(new Callback<PostMediaBean>() {
                @Override
                public void onResponse(Call<PostMediaBean> call, Response<PostMediaBean> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            isUpdate = true;
                            Utils.showToast(context, response.body().getMsg());
                            onBackPressed();

                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<PostMediaBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void commonMethod(String type, File... files) {

    }
}
