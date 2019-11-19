package com.screamxo.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;
import com.screamxo.Interface.Imagepath;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.PhotoPicker;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.RoundedImageView;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignupActivity extends AppCompatActivity implements Imagepath {

    private static final String TAG = SignupActivity.class.getSimpleName();
    @BindView(R.id.img_profile)
    RoundedImageView imgProfile;
    @BindView(R.id.img_camera)
    ImageView imgCamera;
    @BindView(R.id.frame_profile)
    FrameLayout frameProfile;
    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_firstname)
    EditText edtFirstname;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_phoneno)
    EditText edtPhoneno;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.txt_terms_condition)
    TextView txtTermsCondition;
    @BindView(R.id.tv_signup)
    TextView tv_signup;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.title_signup_email)
    TextView titleSignupEmail;

    @BindView(R.id.txt_terms_of_use)
    LinearLayout txt_terms_of_use;

    @BindView(R.id.txt_privacy_policy)
    LinearLayout txt_privacy_policy;

    Context context;
    Map<String, RequestBody> map;
    PhotoPicker photoPicker;
    RequestBodyConveter requestbodyconverter;

    Map<String, File> fileArray;
    String type[] = {"2"};
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    private Validations mValidations;
    private Preferences preferences;
    private FetchrServiceBase mService;

    Call<LoginBean> signupbeancall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getToken();
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        init();
        initControlValue();
    }

    private void initControlValue() {
        titleSignupEmail.setOnClickListener(view -> finish());

        txt_terms_of_use.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, TermsAndUseActivity.class);
            intent.putExtra("screenType", "TermsUse");
            startActivity(intent);
        });

        txt_privacy_policy.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, TermsAndUseActivity.class);
            intent.putExtra("screenType", "PrivacyPolicy");
            startActivity(intent);
        });
    }

    private void init() {
        context = this;
        map = new HashMap<>();
        fileArray = new HashMap<>();
        photoPicker = new PhotoPicker(context, this);
        requestbodyconverter = new RequestBodyConveter();
        mValidations = new Validations();
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
    }

    public void onBackPressed() {
        finish();
    }

    public void onCreateAccountClick(View view) {
        if (isValidation()) {
            @SuppressLint("HardwareIds") String uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Map<String, String> map = new HashMap<>();
            map.put("fname", edtFirstname.getText().toString());
            map.put("uname", edtUsername.getText().toString());
            map.put("username", edtUsername.getText().toString());
            map.put("email", edtEmail.getText().toString());
//            map.put("email", "shubham.a+" + Utils.getRandomNumber(1, 9999) + "@parangat.com");
            map.put("phone", "+" + ccp.getSelectedCountryCode() + edtPhoneno.getText().toString());
            map.put("password", edtPassword.getText().toString());
            String token = FirebaseInstanceId.getInstance().getToken();
            if (TextUtils.isEmpty(token)) {
                token = "909953256435";
            }
            map.put("devicetoken", token);
            map.put("devicetype", "android");
            map.put("uniquestring", uniqueid);

            if (Utils.isInternetOn(context)) {
                progreessbar.setVisibility(View.VISIBLE);
                //btnSignup.setEnabled(false);
                tv_signup.setEnabled(false);
                signupbeancall = mService.getFetcherService(context).CustomSignUP(requestbodyconverter.converRequestBodyFromMap(map),
                        requestbodyconverter.converRequestBodyFromMapImage(fileArray, type));

                signupbeancall.enqueue(new Callback<LoginBean>() {
                    @Override
                    public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                        progreessbar.setVisibility(View.GONE);
                        //btnSignup.setEnabled(true);
                        tv_signup.setEnabled(true);

                        switch (response.code()) {
                            case StaticConstant.RESULT_OK:
                                preferences.setUserid("" + response.body().getResult().getId());
                                Intent intent = new Intent(context, EmailVerification.class);
                                intent.putExtra("screen", SignupActivity.class.getSimpleName());
//                                intent.putExtra("screen", SignupActivity.class.getSimpleName());
                                startActivity(intent);
                                finish();
                                break;
                            case StaticConstant.UNAUTHORIZE:
                                Utils.unAuthentication(context);
                                break;
                            case StaticConstant.BAD_REQUEST:
                                DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_uname_email_already_taken), DialogBox.DIALOG_FAILURE, null);
//                                Utils.showToast(context, getString(R.string.txt_uname_email_already_taken));
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginBean> call, Throwable t) {
                        progreessbar.setVisibility(View.GONE);
                        // btnSignup.setEnabled(true);
                        tv_signup.setEnabled(true);
                    }
                });

            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }
        }
    }


    public boolean isValidation() {
//        if (fileArray.size() == 0) {
//            DialogBox.showDialog(context, "Please add profile picture to continue.");
//            return false;
//        }

        if (TextUtils.isEmpty(edtUsername.getText().toString())) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_username_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (TextUtils.isEmpty(edtFirstname.getText().toString())) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_first_name_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (!validName()) {
            if (!edtFirstname.getText().toString().contains(" ")) {
                DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_last_name_null), DialogBox.DIALOG_FAILURE, null);
            } else {
                DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_enter_valid_name), DialogBox.DIALOG_FAILURE, null);
            }

            return false;
        } else if (!containsOneSpaces()) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_last_name_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_email_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (mValidations.checkEmail(edtEmail.getText().toString())) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_email_invalid), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (!TextUtils.isEmpty(edtPhoneno.getText().toString()) && mValidations.checkPhoneNo(edtPhoneno.getText().toString())) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_phone_number_invalid), DialogBox.DIALOG_FAILURE, null);
            return false;
        }/* else if (edtPhoneno.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_phone_number_invalid), DialogBox.DIALOG_FAILURE, null);
            return false;
        }*/ else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_password_null), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (mValidations.ischeckPassword(edtPassword.getText().toString())) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_password_not_valid), DialogBox.DIALOG_FAILURE, null);
            return false;
        }
        return true;
    }

    public boolean containsOneSpaces() {

        String nameRegex = "\\w+\\s\\w+$";
        CharSequence inputStr = edtFirstname.getText().toString();
        Pattern pattern = Pattern.compile(new String(nameRegex));
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();


    }

    Boolean validName() {
        String nameRegex = "^[\\p{L} .'-]+$";
        CharSequence inputStr = edtFirstname.getText().toString();
        Pattern pattern = Pattern.compile(new String(nameRegex));
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public void userProfileClick(View view) {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        photoPicker.ImagePickker();
                    }
                });
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
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        fileArray.clear();
        fileArray.put("photo", file);
        Log.d(TAG, "imagePathfromCamara: " + file.toString());
        Picasso.with(context)
                .load(file)
                .placeholder(R.mipmap.profile_pic_placeholder)
                .error(R.mipmap.profile_pic_placeholder)
                .resize(width, height)
                .centerCrop()
                .into(imgProfile);
    }

    @Override
    public void imagePathfromGallery(File file) {
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        fileArray.clear();
        fileArray.put("photo", file);
        Log.d(TAG, "imagePathfromCamara" + file.toString());

        Picasso.with(context)
                .load(file)
                .placeholder(R.mipmap.profile_pic_placeholder)
                .error(R.mipmap.profile_pic_placeholder)
                .resize(width, height)
                .centerCrop()
                .into(imgProfile);
    }

    @Override
    public void imagepathfromCropImage(File file) {
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        fileArray.clear();
        fileArray.put("photo", file);
        Log.d(TAG, "imagePathfromCamara" + file.toString());

        Picasso.with(context)
                .load(file)
                .placeholder(R.mipmap.profile_pic_placeholder)
                .error(R.mipmap.profile_pic_placeholder)
                .resize(width, height)
                .centerCrop()
                .into(imgProfile);
    }

    @Override
    protected void onDestroy() {
        if (signupbeancall != null) {
            signupbeancall.cancel();
        }
        super.onDestroy();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
