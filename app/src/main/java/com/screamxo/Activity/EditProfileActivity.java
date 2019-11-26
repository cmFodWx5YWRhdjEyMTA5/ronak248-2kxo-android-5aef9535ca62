package com.screamxo.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.LoginBean;
import com.example.apimodule.ApiBase.ApiBean.StripeCreateCustomer.StripeCreateCustomerReponse;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.screamxo.Adapter.CountriesAdapter;
import com.screamxo.Adapter.StatesAdapter;
import com.screamxo.Interface.Imagepath;
import com.screamxo.Model.CountryModel;
import com.screamxo.Model.StateModel;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.CircleTransform;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.PhotoPicker;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.screamxo.Utils.ViewUtils;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditProfileActivity extends AppCompatActivity implements Imagepath {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    public String gender = "m", relation = "a", sexPref = "o", lat = "", lon = "";
    @BindView(R.id.countries_spinner)
    Spinner countries_spinner;
    @BindView(R.id.states_spinner)
    Spinner states_spinner;
    @BindView(R.id.edt_Username)
    EditText edt_Username;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.edt_firstname)
    EditText edtFirstname;
    @BindView(R.id.edt_lastname)
    EditText edtLastname;
    @BindView(R.id.edt_school_or_master)
    EditText edtSchoolOrMaster;
    @BindView(R.id.edt_job)
    EditText edtJob;
    @BindView(R.id.edt_city)
    EditText edtCity;
    @BindView(R.id.edt_hobby)
    EditText edtHobby;
    @BindView(R.id.rad_male)
    RadioButton radMale;
    @BindView(R.id.rad_female)
    RadioButton radFemale;
    @BindView(R.id.rad_transgender)
    RadioButton radTransgender;
    @BindView(R.id.rad_available)
    RadioButton radAvailable;
    @BindView(R.id.rad_unavailable)
    RadioButton radUnavailable;
    @BindView(R.id.rad_opposite)
    RadioButton radOpposite;
    @BindView(R.id.rad_same)
    RadioButton radSame;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.progreessbar)
    ProgressBar progressBar;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    Context context;
    HashMap<String, String> bean;
    PhotoPicker photoPicker;
    RequestBodyConveter requestbodyconverter;
    Call<LoginBean> editprofilebean;
    Map<String, File> fileArray;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    private Validations mValidations;
    private Preferences preferences;
    private FetchrServiceBase mService;
    private String type[] = {"2"};
    private Call<StripeCreateCustomerReponse> createStripeCustomerCall;
    boolean first = true;

    ArrayList<StateModel> newStatesList = new ArrayList<>();
    String selectedCountry, selectedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        init();
        initControlValue();
    }

    private void init()
    {
        context = this;
        bean = new HashMap<>();
        fileArray = new HashMap<>();
        photoPicker = new PhotoPicker(context, this);
        requestbodyconverter = new RequestBodyConveter();
        mValidations = new Validations();
        preferences = new Preferences(context);
        mService = new FetchrServiceBase();
    }

    public void initControlValue() {
        if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(EmailLoginActivity.class.getSimpleName())
                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(CommonLoginSignUpActivity.class.getSimpleName())) {
            imgToolbarLeftIcon.setVisibility(View.GONE);
        } else {
            imgToolbarLeftIcon.setVisibility(View.VISIBLE);
        }
        et_email.setVisibility((BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(CommonLoginSignUpActivity.class.getSimpleName())
                ||
                BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(EmailVerification.class.getSimpleName()))
                ? View.VISIBLE : View.GONE);

        txtToolbarTitle.setText(R.string.title_edit_profile);
        imgToolbarRightIcon.setVisibility(View.GONE);
        bean = preferences.getUserDetail();
        setValue();

        String countries = loadJsonFromFile("json/countries.json");
        String states = loadJsonFromFile("json/states.json");

        ArrayList<CountryModel> countriesList = new ArrayList<>();
        ArrayList<StateModel> statesList = new ArrayList<>();

        try {
            JSONArray json = new JSONArray(countries);
            for (int i = 0; i < json.length(); i++) {
                CountryModel model = new Gson().fromJson(json.get(i).toString(), CountryModel.class);
                countriesList.add(model);
            }
            countries_spinner.setAdapter(new CountriesAdapter(this, countriesList));

        } catch (Exception e) {
        }
        try {
            JSONArray json = new JSONArray(states);
            for (int i = 0; i < json.length(); i++) {
                StateModel model = new Gson().fromJson(json.get(i).toString(), StateModel.class);
                statesList.add(model);
            }

        } catch (Exception e) {
        }


        if (!bean.get("country").isEmpty()) {
            selectedCountry = bean.get("country");
            selectedState = bean.get("state");


            for (int i = 0; i < countriesList.size(); i++)
                if (countriesList.get(i).getName().equals(bean.get("country"))) {
                    countries_spinner.setSelection(i);

                    if (!bean.get("state").isEmpty()) {
                        for (StateModel state : statesList) {
                            if (state.getCountryId().equals(countriesList.get(i).getId())) {
                                newStatesList.add(state);
                            }
                        }
                        states_spinner.setAdapter(new StatesAdapter(EditProfileActivity.this, newStatesList));
                        for (int j = 0; j < newStatesList.size(); j++)
                            if (newStatesList.get(j).getName().equals(bean.get("state"))) {
                                states_spinner.setSelection(j);
                                break;
                            }
                    }
                    break;
                }
        } else {
            selectedCountry = "United States";
            for (int i = 0; i < countriesList.size(); i++)
                if (countriesList.get(i).getName().equals(selectedCountry)) {
                    countries_spinner.setSelection(i);
                    selectedState = "Alabama";
                    for (StateModel state : statesList) {
                        if (state.getCountryId().equals(countriesList.get(i).getId())) {
                            newStatesList.add(state);
                        }
                    }
                    states_spinner.setAdapter(new StatesAdapter(EditProfileActivity.this, newStatesList));
                    for (int j = 0; j < newStatesList.size(); j++)
                        if (newStatesList.get(j).getName().equals(selectedState)) {
                            states_spinner.setSelection(j);
                            break;
                        }

                }

        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countries_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedCountry = countriesList.get(position).getName();
                        newStatesList.clear();

                        for (StateModel state : statesList) {
                            if (state.getCountryId().equals(countriesList.get(position).getId())) {
                                newStatesList.add(state);
                            }

                        }
                        states_spinner.setAdapter(new StatesAdapter(EditProfileActivity.this, newStatesList));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                states_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedState = newStatesList.get(position).getName();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }, 1000);

        et_email.setText(preferences.getUserEmail());

    }

    public void setValue() {
        if (bean.get("avatar") != null && !bean.get("avatar").equals("")) {
            setAvatarImage(bean.get("avatar"));
        }
        setFnameLname(bean.get("name"));
        edt_Username.setText(mValidations.isNullThenEmpty(bean.get("username")));
        edtSchoolOrMaster.setText(mValidations.isNullThenEmpty(bean.get("school")));
        edtJob.setText(mValidations.isNullThenEmpty(bean.get("job")));
        edtCity.setText(mValidations.isNullThenEmpty(bean.get("address")));
        edtHobby.setText(mValidations.isNullThenEmpty(bean.get("hobby")));
        lon = bean.get("lon");
        lat = bean.get("lat");

        if (bean.get("gender") != null) {
            setGender(bean.get("gender"));
        }

        if (bean.get("realtionstatus") != null) {
            setRelationshipStatus(bean.get("realtionstatus"));
        }

        if (bean.get("sexpreference") != null) {
            setSexPreference(bean.get("sexpreference"));
        }
    }

    private void setFnameLname(String fullName) {
        edtFirstname.setText(mValidations.isNullThenEmpty(fullName));
//        if(BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(DrawerMainActivity.class.getSimpleName())
//                || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ProfileActivity.class.getSimpleName())){
//            edtFirstname.setText(mValidations.isNullThenEmpty(fullName));
//
//        }
//        else
//        {
//            String[] name = fullName.split(" ");
//            String firstname = name[0];
//            String lastname = "";
//            if (name.length > 1 && name[1] != null) {
//                lastname = name[1];
//            }
//
//            edtFirstname.setText(mValidations.isNullThenEmpty(firstname));
//            edtLastname.setText(mValidations.isNullThenEmpty(lastname));
//        }
    }

    private void setAvatarImage(String imageUrl) {
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.profile_pic_placeholder);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.mipmap.pic_holder_dashboard)
                .error(R.mipmap.pic_holder_dashboard)
                .resize(width, height)
                .centerCrop()
                .transform(new CircleTransform())
                .into(imgProfile);
    }

    private void setGender(String s) {
        switch (s) {
            case "m":
                radMale.setChecked(true);
                gender = "m";
                break;
            case "f":
                radFemale.setChecked(true);
                gender = "f";
                break;
            case "t":
                radTransgender.setChecked(true);
                gender = "t";
                break;
        }
    }

    private void setRelationshipStatus(String s) {
        switch (s) {
            case "a":
                radAvailable.setChecked(true);
                relation = "a";
                break;
            case "u":
                radUnavailable.setChecked(true);
                relation = "u";
                break;
        }
    }

    private void setSexPreference(String s) {
        switch (s) {
            case "o":
                radOpposite.setChecked(true);
                sexPref = "o";
                break;
            case "s":
                radSame.setChecked(true);
                sexPref = "s";
                break;
        }
    }

    public void onUpdateclick(View view) {
        if (isValidation()) {
            Map<String, String> map = new HashMap<>();

            if (radMale.isChecked()) {
                gender = "m";
            } else if (radFemale.isChecked()) {
                gender = "f";
            } else {
                gender = "t";
            }

            if (radAvailable.isChecked()) {
                relation = "a";
            } else {
                relation = "u";
            }

            if (radOpposite.isChecked()) {
                sexPref = "o";
            } else {
                sexPref = "s";
            }

            map.put("fname", edtFirstname.getText().toString());
            map.put("country", selectedCountry);
            map.put("state", selectedState);
            map.put("fname", edtFirstname.getText().toString());
            map.put("lname", edtLastname.getText().toString());
            map.put("username", edt_Username.getText().toString());
            if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(DrawerMainActivity.class.getSimpleName())
                    || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(ProfileActivity.class.getSimpleName())) {
                map.put("email", preferences.getUserEmail());
            } else {
                map.put("email", et_email.getText().toString());
            }

            map.put("school", edtSchoolOrMaster.getText().toString());
            map.put("job", edtJob.getText().toString());
            map.put("city", edtCity.getText().toString());
            map.put("hobbies", edtHobby.getText().toString());
            map.put("gender", gender);
            map.put("realtionstatus", relation);
            map.put("sexpreference", sexPref);
//            map.put("uid", preferences.getUserId());
            map.put("lat", lat);
            map.put("lon", lon);
            map.put("country", selectedCountry);
            map.put("state", selectedState);

            if (Utils.isInternetOn(context)) {
                progressBar.setVisibility(View.VISIBLE);
                btnUpdate.setEnabled(false);
                editprofilebean = mService.getFetcherService(context).EditProfile(requestbodyconverter.converRequestBodyFromMap(map),
                        requestbodyconverter.converRequestBodyFromMapImage(fileArray, type));

                editprofilebean.enqueue(new Callback<LoginBean>() {
                    @Override
                    public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                        progressBar.setVisibility(View.GONE);
                        btnUpdate.setEnabled(true);
                        if (response.code() == StaticConstant.RESULT_OK) {
                            if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                                if (response.body().getResult().getPhoto() != null) {
                                    map.put("avatar", response.body().getResult().getPhoto());
                                }
                                preferences.setUserDetail(map);
                                /*in case of social login */
                                if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(CommonLoginSignUpActivity.class.getSimpleName()) && (response.body().getResult().getStripeCustomerId() == null || response.body().getResult().getStripeCustomerId().isEmpty())) {
                                    createStripeCustomer();
                                    return;
                                }

                                if (BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(EmailLoginActivity.class.getSimpleName())
                                        || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(CommonLoginSignUpActivity.class.getSimpleName())
                                        || BundleUtils.getIntentExtra(getIntent(), "screen", "").equalsIgnoreCase(EmailVerification.class.getSimpleName())) {
                                    Intent intent = new Intent(context, DrawerMainActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
                                DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginBean> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        btnUpdate.setEnabled(true);
                    }
                });

            } else {
                Utils.showToast(context, context.getString(R.string.toast_no_internet));
            }
        }
    }

    private void createStripeCustomer() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", preferences.getUserId());
        map.put("email", preferences.getUserEmail());
        map.put("description", preferences.getUserName());  // need to put username

        if (Utils.isInternetOn(context)) {
            progressBar.setVisibility(View.VISIBLE);
            btnUpdate.setEnabled(false);
            createStripeCustomerCall = mService.getFetcherService(context).createStripeCustomer(map);
            createStripeCustomerCall.enqueue(new retrofit2.Callback<StripeCreateCustomerReponse>() {
                @Override
                public void onResponse(Call<StripeCreateCustomerReponse> call, Response<StripeCreateCustomerReponse> response) {
                    progressBar.setVisibility(View.GONE);
                    btnUpdate.setEnabled(true);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            preferences.setStripeCustomerId(response.body().getResult().getStripeCustomerId());
                            /*only called from social login*/
                            Intent intent = new Intent(context, DrawerMainActivity.class);
                            startActivity(intent);
                        } else {
                            DialogBox.showDialog(context, context.getString(R.string.app_name), response.body().getMsg(), DialogBox.DIALOG_FAILURE, null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StripeCreateCustomerReponse> call, Throwable t) {
                    t.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    btnUpdate.setEnabled(true);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public boolean isValidation() {


        String imageProfile = null;
        if (bean != null) {
            imageProfile = bean.get("avatar");
        }
        if (fileArray.size() == 0 && (imageProfile.contains("null") || imageProfile.equals(""))) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.msg_add_profile_pic), DialogBox.DIALOG_FAILURE, null);
            return false;
        }

        if (edt_Username.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.msd_uname_required), DialogBox.DIALOG_FAILURE, null);
            return false;
        }

        if (ViewUtils.isVisible(et_email) && et_email.getText().toString().isEmpty()) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.msg_email_required), DialogBox.DIALOG_FAILURE, null);
            return false;
        }

        if (edtFirstname.getText().toString().equals("")) {
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

    @OnClick(R.id.img_toolbar_left_icon)
    public void onClick() {
        finish();
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
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.pic_holder_dashboard);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        fileArray.clear();
        fileArray.put("photo", file);
        Log.d(TAG, "imagePathfromCamara: " + file.toString());
        Picasso.with(context)
                .load(file)
                .placeholder(R.mipmap.pic_holder_dashboard)
                .error(R.mipmap.pic_holder_dashboard)
                .resize(width, height)
                .centerCrop()
                .transform(new CircleTransform())
                .into(imgProfile);
    }

    @Override
    public void imagePathfromGallery(File file) {
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.pic_holder_dashboard);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        fileArray.clear();
        fileArray.put("photo", file);
        Log.d(TAG, "imagePathfromCamara" + file.toString());

        Picasso.with(context)
                .load(file)
                .placeholder(R.mipmap.pic_holder_dashboard)
                .error(R.mipmap.pic_holder_dashboard)
                .resize(width, height)
                .centerCrop()
                .transform(new CircleTransform())
                .into(imgProfile);
    }

    @Override
    public void imagepathfromCropImage(File file) {
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.pic_holder_dashboard);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        fileArray.clear();
        fileArray.put("photo", file);
        Log.d(TAG, "imagePathfromCrop" + file.toString());

        Picasso.with(context)
                .load(file)
                .placeholder(R.mipmap.pic_holder_dashboard)
                .error(R.mipmap.pic_holder_dashboard)
                .resize(width, height)
                .centerCrop()
                .transform(new CircleTransform())
                .into(imgProfile);
    }

    @Override
    protected void onDestroy() {
        if (createStripeCustomerCall != null) {
            createStripeCustomerCall.cancel();
        }

        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private String loadJsonFromFile(String path) {
        String json = null;
        try {
            InputStream in = this.getAssets().open(path);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            json = new String(buffer, "UTF-8");
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
