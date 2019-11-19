package com.screamxo.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.screamxo.Activity.RahulWork.SellItemActivity;
import com.screamxo.Adapter.SellItemAdapter;
import com.screamxo.R;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.StaticConstant;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class SellItemFirstFragment extends Fragment {
    public static int MAX_IMAGE_COUNT = 4; // fixme
    private static final String TAG = "SellItemFirstFragment";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btnAddDetails)
    Button btnAddDetails;
    @BindView(R.id.camera_iv)
    ImageView cameraIv;
    Unbinder unbinder;

    private Context context;
    private SellItemAdapter sellitemadapter;
    public static File[] imageFiles;

    public SellItemFirstFragment() {
        // Required empty public constructor
        imageFiles = new File[MAX_IMAGE_COUNT];
        StaticConstant.imageFiles_new = new File[MAX_IMAGE_COUNT];
    }

    public static SellItemFirstFragment newInstance(int page, String title) {
        return new SellItemFirstFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell_item_first, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        initView();
        initControlValues();
        super.onResume();
    }

    private void initControlValues() {
        setAdapter();
        btnAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImagesSelected()) {
                    ((SellItemActivity) getActivity()).setSelectedPage(1);
                } else {
                    DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_please_select_atleast) + MAX_IMAGE_COUNT + getString(R.string.txt_item_image), DialogBox.DIALOG_FAILURE, null);
                }
            }
        });
    }

    private void initView() {
        context = getActivity();
    }

    private void setAdapter() {
        if (sellitemadapter == null || recyclerView.getAdapter() == null) {
            sellitemadapter = new SellItemAdapter(context, imageFiles);
            recyclerView.setAdapter(sellitemadapter);
        } else {
            sellitemadapter.notifyDataSetChanged();
        }

        for (File imageFile : imageFiles) {
        }
    }

    @OnClick({R.id.camera_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_iv:
                if (isImagesSelected()) {
                    DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_product_image_added_sucessfully), DialogBox.DIALOG_SUCESS, null);
                } else {
                    pickImage();
                }
                break;
        }
    }

    public void pickImage() {
        RxPermissions.getInstance(context)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, SellItemActivity.REQUEST_IMAGE_CAPTURE);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SellItemActivity.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempImageUri = getImageUri(context.getApplicationContext(), photo);
                String realPath = getRealPathFromURI(tempImageUri);
                if (realPath != null) {
                    File imageFile = new File(realPath);
                    imageFiles[getCurrentImageHolderPosition()] = imageFile;
                    StaticConstant.imageFiles_new[getCurrentImageHolderPosition()] = imageFile;
                    setAdapter();
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateProductImage(int position, File file) {
        try {
            imageFiles[position] = file;
            StaticConstant.imageFiles_new[position] = file;
        } catch (Exception ignored) {
        }
        setAdapter();
    }

//    public void removePhoto(int position) {
//        imageFiles[position] = null;
//    }

    private int getCurrentImageHolderPosition() {
        for (int i = 0; i < imageFiles.length; i++) {
            if (imageFiles[i] == null) {
                return i;
            }
        }

        return 0;
    }

    private boolean isImagesSelected() {
        for (File imageFile : imageFiles) {
            if (imageFile == null) {
                return false;
            }
        }
        return true;
    }

    public boolean getIsFileExist(int imagePos) {
        return (imageFiles[imagePos] != null);
    }
}
