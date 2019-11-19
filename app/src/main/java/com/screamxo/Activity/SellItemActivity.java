package com.screamxo.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.CategoryList.Result;
import com.example.apimodule.ApiBase.ApiBean.ItemCategories;
import com.example.apimodule.ApiBase.ApiBean.ItemCategoriesBean;
import com.example.apimodule.ApiBase.ApiBean.ItemDetailResult;
import com.example.apimodule.ApiBase.ApiBean.SellItemBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.google.gson.Gson;
import com.screamxo.Activity.RahulWork.ConditionActivity;
import com.screamxo.Adapter.BottomSheetAdapter;
import com.screamxo.Adapter.SellIEdittemAdapter;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.Imagepath;
import com.screamxo.Others.ImageDownloaderTask;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.Others.OnBottomSheetItemClickListener;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.NumFilter;
import com.screamxo.Utils.PhotoPicker;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.Validations;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellItemActivity extends AppCompatActivity implements Imagepath, CommonMethod, SellIEdittemAdapter.OnImagePickedListener {
    private static final String TAG = "SellItemActivity";
    private static final int RC_PICK_CONDITION = 201;
    public static int MAX_IMAGE_COUNT = 4; // Fixme
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.edt_item_name)
    EditText edtItemName;
    @BindView(R.id.edt_item_descripion)
    EditText edtItemDescripion;
    @BindView(R.id.edt_item_quantity)
    EditText edtItemQuantity;
    @BindView(R.id.edt_item_price)
    EditText edtItemPrice;
    @BindView(R.id.edt_you_will_earn)
    EditText edtYouWillEarn;
    @BindView(R.id.edt_shipping_cost)
    EditText edtShippingCost;
    @BindView(R.id.edt_separate_keyword)
    EditText edtSeparateKeyword;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.img_info)
    ImageView imgInfo;
    @BindView(R.id.transperent_view)
    LinearLayout transperentView;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.condition_ll)
    LinearLayout condition_ll;
    @BindView(R.id.txtCondition)
    TextView txtCondition;
    @BindView(R.id.imgQuestion)
    ImageView imgQuestion;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.tv_category)
    TextView tv_category;
    PhotoPicker photoPicker;
    ArrayList<ItemCategories> itemCategories;
    ArrayList<String> imag_array;
    String type[] = {"2", "2", "2", "2"};
    int imagePos = 0;
    Map<String, String> map;
    HashMap<String, File> fileArray;
    Call<ItemCategoriesBean> itemCategoriesBeanCall;
    Call<SellItemBean> updateItem;
    RequestBodyConveter requestbodyconverter;
    Preferences preferences;
    String updateItemRemovePhotoJason = "[";
    private Context context;
    private File[] imageFiles, imageUpdateFiles;
    private SellIEdittemAdapter sellitemadapter;
    private String categoryId;
    private Preferences mpreferences;
    private FetchrServiceBase mService;
    private ItemDetailResult itemDetailResult;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);
        ButterKnife.bind(this);
        init();
        initControlValue();
        initControl();
//        callApiGetItemCategory();
    }

    public void init() {
        context = this;
        map = new HashMap<>();
        preferences = new Preferences(context);
        fileArray = new HashMap<>();
        itemCategories = new ArrayList<>();
        Validations mvalidation = new Validations();
        mpreferences = new Preferences(context);
        requestbodyconverter = new RequestBodyConveter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        photoPicker = new PhotoPicker(context, this);
        imageFiles = new File[MAX_IMAGE_COUNT];
        imageUpdateFiles = new File[MAX_IMAGE_COUNT];
        mService = new FetchrServiceBase();
//        StaticConstant.SELL_CONDITION = "";
    }

    @SuppressLint("SetTextI18n")
    public void initControlValue() {
        edtItemPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {


                    if (!edtItemPrice.getText().toString().replaceAll(",", "").equals("")) {
                        Double commition = Double.parseDouble(edtItemPrice.getText().toString().replaceAll(",", ""))
                                - ((Double.parseDouble(edtItemPrice.getText().toString().replaceAll(",", ""))) * 0.10);
                        edtYouWillEarn.setText("" + (round(commition, 2)));
                    } else {
                        edtYouWillEarn.setText("");
                    }

                    String dta = s.toString();
                    int dotIndex = dta.indexOf(".");
                    if (s.toString().contains(".")) {
                        edtItemPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(dotIndex + 3)});
                        edtItemPrice.setSelection(edtItemPrice.length());
                    } else {
                        edtItemPrice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                        edtItemPrice.setSelection(edtItemPrice.length());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtShippingCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String dta = s.toString();
                    int dotIndex = dta.indexOf(".");
                    if (s.toString().contains(".")) {
                        edtShippingCost.setFilters(new InputFilter[]{new InputFilter.LengthFilter(dotIndex + 3)});
                        edtShippingCost.setSelection(edtShippingCost.length());
                    } else {
                        edtShippingCost.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                        edtShippingCost.setSelection(edtShippingCost.length());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        txtToolbarTitle.setText(getString(R.string.txt_edt_item));
        imgToolbarLeftIcon.setImageResource(R.mipmap.ico_up);
        imgToolbarLeftIcon.setRotation(-90);
        imgToolbarRightIcon.setVisibility(View.GONE);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("beanObject")) {
            itemDetailResult = getIntent().getExtras().getParcelable("beanObject");
            setValueInActivity();
        }
        //   setAdapter();
    }

    public void initControl() {
        imgToolbarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        condition_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConditionPicker();
            }
        });

        ll_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryPicker();
            }
        });

        imgQuestion.setOnClickListener(view -> startActivityForResult(new Intent(this, ConditionActivity.class), RC_PICK_CONDITION));

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPopUpWindow popUpWindowForSendInvite = new MyPopUpWindow(context, transperentView,
                        new String[]{getString(R.string.msg_get_amount_on_purchase)}, view, "SellItem");
                popUpWindowForSendInvite.show(view, MyPopUpWindow.PopUpPosition.RIGHT);
            }
        });
    }

    public void showCategoryPicker() {
        Result category = new Gson().fromJson(preferences.getNewCategories(), Result.class);
        ArrayList<String> inputList = new ArrayList<>();
        ArrayList<String> inputIds = new ArrayList<>();
        for (int i = 0; i < category.getCategories().size(); i++) {
            if (!(category.getCategories().get(i).getCategoryName().equalsIgnoreCase("All")
                    || category.getCategories().get(i).getCategoryName().equalsIgnoreCase("Trending"))) {
                inputList.add(category.getCategories().get(i).getCategoryName());
                inputIds.add(String.valueOf(category.getCategories().get(i).getId()));
            }
        }

        @SuppressLint("InflateParams") View modalbottomsheet = getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Select Category");
        rv_container.setAdapter(new BottomSheetAdapter(context, inputList, inputIds, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                showSubcategoryPicker(input, id);
            }
        }));
    }

    private void showSubcategoryPicker(String input, @Nullable String id) {
        Log.d(TAG, "showSubcategoryPicker input: " + input);
        Log.d(TAG, "showSubcategoryPicker id: " + id);
        Result category = new Gson().fromJson(preferences.getNewCategories(), Result.class);
        ArrayList<String> inputList = new ArrayList<>();
        ArrayList<String> inputIds = new ArrayList<>();
        for (int i = 0; i < category.getSubcategories(id).size(); i++) {
            if (!(category.getSubcategories(id).get(i).getCategoryName().equalsIgnoreCase("All")
                    || category.getSubcategories(id).get(i).getCategoryName().equalsIgnoreCase("Trending"))) {
                inputList.add(category.getSubcategories(id).get(i).getCategoryName());
                inputIds.add(String.valueOf(category.getSubcategories(id).get(i).getId()));
            }
        }

        @SuppressLint("InflateParams") View modalbottomsheet = getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Select Subcategory");
        rv_container.setAdapter(new BottomSheetAdapter(context, inputList, inputIds, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                tv_category.setText(input);
                categoryId = id;
            }
        }));
    }

    public void showConditionPicker() {
        @SuppressLint("InflateParams") View modalbottomsheet = getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);
        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("New");
        inputList.add("Like New");
        inputList.add("Refurbished");
        inputList.add("Fair");
        inputList.add("Poor");

        tv_title.setText("Select Condition");
        rv_container.setAdapter(new BottomSheetAdapter(context, inputList, null, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @android.support.annotation.Nullable String id) {
                dialog.dismiss();
                txtCondition.setText(input);
            }
        }));

    }

    @SuppressLint("SetTextI18n")
    private void setValueInActivity() {
        Log.d(TAG, "setValueInActivity: ");
        imag_array = new ArrayList<String>();
        for (int i = 0; i < itemDetailResult.getMedia().size(); i++) {
            if (itemDetailResult.getMedia().get(i).getMediaUrl() != null && !itemDetailResult.getMedia().get(i).getMediaUrl().equals("")) {
                try {
                    imag_array.add(itemDetailResult.getMedia().get(i).getMediaUrl());
                    downLoadImage(itemDetailResult.getMedia().get(i).getMediaUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        setAdapter();
        edtItemName.setText(itemDetailResult.getItemName());
        edtItemDescripion.setText(itemDetailResult.getItemDescription());
        edtItemQuantity.setText("" + itemDetailResult.getItemQtyRemained());
        txtCondition.setText("" + itemDetailResult.getItem_condition());
        tv_category.setText("" + itemDetailResult.getItemCategoryName());
        edtItemPrice.setText(itemDetailResult.getItemPrice());

        if (itemDetailResult.getItemShippingCost().equals("0")) {
            edtShippingCost.setText("Free Shipping");
        } else
            edtShippingCost.setText(itemDetailResult.getItemShippingCost());
        edtSeparateKeyword.setText(itemDetailResult.getItemTags());
        categoryId = itemDetailResult.getItemCategoryId();
    }

    private void downLoadImage(String path) {
        ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(context, this);
        imageDownloaderTask.execute(path);
    }

    private void setAdapter() {
        Log.d(TAG, "setAdapter: ");
        if (sellitemadapter == null) {
            sellitemadapter = new SellIEdittemAdapter(context, imag_array);
            recyclerView.setAdapter(sellitemadapter);
        } else {
            sellitemadapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");

        switch (requestCode) {
            case RC_PICK_CONDITION:
                switch (resultCode) {
                    case RESULT_OK:
                        txtCondition.setText(BundleUtils.getIntentExtra(data, "SELL_CONDITION", ""));
                        break;
                }
                break;

            case StaticConstant.REQUEST_CAPTURE:
            case StaticConstant.REQUEST_GALLERY:
            case UCrop.REQUEST_CROP:
                if (photoPicker != null) {
                    photoPicker.onResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    @Override
    public void imagePathfromCamara(File path) {
        Log.d(TAG, "imagePathfromCamara: ");
    }

    @Override
    public void imagePathfromGallery(File path) {
        Log.d(TAG, "imagePathfromGallery: ");
        StringBuilder stringBuilder = new StringBuilder();

        if (itemDetailResult.getMedia().size() > imagePos && itemDetailResult.getMedia().get(imagePos) != null) {
            stringBuilder.append("{\"img_id\":").append(itemDetailResult.getMedia().get(imagePos).getId()).append("},");
            itemDetailResult.getMedia().get(imagePos).setMediaUrl(null);
        }

        updateItemRemovePhotoJason = updateItemRemovePhotoJason + stringBuilder.toString();
        imageFiles[imagePos] = null;
        imageUpdateFiles[imagePos] = null;
        setAdapter();
    }

    @Override
    public void imagepathfromCropImage(File path) {
        Log.d(TAG, "imagepathfromCropImage: ");
        StringBuilder stringBuilder = new StringBuilder();

        if (itemDetailResult != null && itemDetailResult.getMedia().size() > imagePos && itemDetailResult.getMedia().get(imagePos) != null) {
            stringBuilder.append("{\"img_id\":").append(itemDetailResult.getMedia().get(imagePos).getId()).append("},");
        }

        updateItemRemovePhotoJason = updateItemRemovePhotoJason + stringBuilder.toString();
        imageFiles[imagePos] = path;
        imageUpdateFiles[imagePos] = path;
        imag_array.set(imagePos, "" + path);
        setAdapter();
    }

//    private void callApiGetItemCategory() {
//        Log.d(TAG, "callApiGetItemCategory: ");
//        if (Utils.isInternetOn(context)) {
//            itemCategoriesBeanCall = mService.getFetcherService(context).GetItemCategory(map);
//            itemCategoriesBeanCall.enqueue(new Callback<ItemCategoriesBean>() {
//                @Override
//                public void onResponse(Call<ItemCategoriesBean> call, Response<ItemCategoriesBean> response) {
//
//                    if (response.code() == StaticConstant.RESULT_OK) {
//                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
//
//                            itemCategories = response.body().getResult().getCategories();
//                            categoryId = itemCategories.get(0).getId();
//                            itemCategories.remove(0);
//
//                            for (int i = 0; i < response.body().getResult().getCategories().size(); i++) {
//                                if (response.body().getResult().getCategories().get(i).getCategoryName().equalsIgnoreCase("trending")) {
//                                    itemCategories.remove(i);
//                                }
//                            }
//
//                            ArrayList<String> itemcategoryNames = new ArrayList<>();
//                            for (int i = 0; i < itemCategories.size(); i++) {
//                                itemcategoryNames.add(i, itemCategories.get(i).getCategoryName());
//                            }
//                            setSpinnerValue(itemcategoryNames);
//
//                        } else {
//                            Utils.showToast(context, response.body().getMsg());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ItemCategoriesBean> call, Throwable t) {
//                    Utils.showToast(context, t.toString());
//                }
//            });
//
//        } else {
//            Utils.showToast(context, context.getString(R.string.toast_no_internet));
//        }
//    }

//    private void setSpinnerValue(ArrayList<String> itemcategories) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemcategories);
//        spinnerItemCategory.setAdapter(adapter);
//    }

    public boolean isValidation() {
        if (edtItemName.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_enter_item_name), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtItemDescripion.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_enter_item_description), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtItemQuantity.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_enter_item_quantity), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtItemPrice.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_enter_item_price), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtYouWillEarn.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_enter_item_actual_price), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtShippingCost.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_enter_item_shipping_coast), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else if (edtSeparateKeyword.getText().toString().equals("")) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), getResources().getString(R.string.msg_enter_keywords), DialogBox.DIALOG_FAILURE, null);
            return false;
        } else {
            return true;
        }
    }


    @OnClick({R.id.btn_update, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                callUpdateItemApi();
                break;
            case R.id.btn_delete:
                callDeleteApi();
                break;
        }
    }

    private void callUpdateItemApi() {
        Log.d(TAG, "callUpdateItemApi: ");
        for (int i = 0; i < MAX_IMAGE_COUNT; i++) {
            if (imageUpdateFiles[i] != null) {
                fileArray.put("media[" + i + "]", imageUpdateFiles[i]);
            }
        }

        int count = 0;

        for (int i = 0; i < itemDetailResult.getMedia().size(); i++) {
            if (itemDetailResult.getMedia().get(i).getMediaUrl() != null) {
                count++;
            }
        }
        if (fileArray.size() == 0 && count == 0) {
            Utils.showToast(context, "Please select item image");
        } else if (isValidation()) {

            HashMap<String, String> map = new HashMap<>();

            StringBuilder stringBuilder = new StringBuilder();
            if (updateItemRemovePhotoJason.length() > 2) {
                stringBuilder.append(updateItemRemovePhotoJason);
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                updateItemRemovePhotoJason = stringBuilder.toString();
            }
            updateItemRemovePhotoJason = updateItemRemovePhotoJason + "]";

            map.put("itemname", edtItemName.getText().toString());
            map.put("itemdesc", edtItemDescripion.getText().toString());
            //            map.put("itemprice", edtYouWillEarn.getText().toString().replaceAll(",", ""));// that seller will get
            map.put("itemprice", edtItemPrice.getText().toString().replaceAll(",", ""));// that seller will get
            if (edtShippingCost.getText().toString().equals("Free Shipping"))
                map.put("itemshipcost", "0");
            else
                map.put("itemshipcost", edtShippingCost.getText().toString().replaceAll(",", ""));


            map.put("itemcreatedby", mpreferences.getUserId());
            map.put("category", categoryId);
            map.put("item_condition", txtCondition.getText().toString());
            map.put("itemtags", edtSeparateKeyword.getText().toString());
            map.put("itemquantity", edtItemQuantity.getText().toString().replaceAll(",", ""));
            map.put("itemactualprice", edtItemPrice.getText().toString().replaceAll(",", ""));
            map.put("itemid", "" + itemDetailResult.getItemId());
            map.put("itemcreatedby", preferences.getUserId());
            map.put("removemedia", updateItemRemovePhotoJason);

            progreessbar.setVisibility(View.VISIBLE);
            updateItem = mService.getFetcherService(context).UpdateItem(requestbodyconverter.converRequestBodyFromMap(map), requestbodyconverter.converRequestBodyFromMapImage(fileArray, type));
            updateItem.enqueue(new Callback<SellItemBean>() {
                @Override
                public void onResponse(Call<SellItemBean> call, Response<SellItemBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("IS_ITEM_UPDATED", true);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SellItemBean> call, Throwable t) {
                    t.printStackTrace();
                    Utils.showToast(context, t.toString());
                    progreessbar.setVisibility(View.GONE);
                }
            });

        }
    }

    private void callDeleteApi() {
        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);
            Map<String, String> params = new HashMap<>();
            params.put("itemid", "" + itemDetailResult.getItemId());
            params.put("itemcreatedby", preferences.getUserId());

            itemCategoriesBeanCall = mService.getFetcherService(context).DeleteItem(params);
            itemCategoriesBeanCall.enqueue(new Callback<ItemCategoriesBean>() {
                @Override
                public void onResponse(Call<ItemCategoriesBean> call, Response<ItemCategoriesBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("IS_ITEM_DELETED", true);
                            returnIntent.putExtra("MESSAGE", response.body().getMsg());
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemCategoriesBean> call, Throwable t) {
                    t.printStackTrace();
                    progreessbar.setVisibility(View.GONE);
                    Utils.showToast(context, t.toString());
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void commonMethod(String type, File... file) {
        Log.d(TAG, "commonMethod: ");
        imageFiles[imagePos] = file[0];
        imagePos++;
        setAdapter();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (itemCategoriesBeanCall != null) {
            itemCategoriesBeanCall.cancel();
        }

        if (updateItem != null) {
            updateItem.cancel();
        }
//        StaticConstant.SELL_CONDITION = "";
    }

    @Override
    public void onImagePicked(int position) {
        Log.d(TAG, "onImagePicked: ");
        RxPermissions.getInstance(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        imagePos = position;
                        photoPicker.ImagePickker(getIsFileExist());
                    }
                });
    }

    public boolean getIsFileExist() {
        return (imageFiles[imagePos] != null);
    }
}
