package com.screamxo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.CategoryList.Result;
import com.google.gson.Gson;
import com.screamxo.Adapter.SellItemCategoryAdapter;
import com.screamxo.Others.OnBottomSheetItemClickListener;
import com.screamxo.R;
import com.screamxo.Utils.Preferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SellItemCategoryActivity extends AppCompatActivity {

    @BindView(R.id.ico_back)
    ImageView icoBack;

    @BindView(R.id.rv_container)
    RecyclerView recyclerViewCategory;

    @BindView(R.id.txt_toolbar_title)
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item_category);
        ButterKnife.bind(this);
        init();
        initControlValue();
    }

    private void initControlValue() {
        icoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        Result category = new Gson().fromJson(new Preferences(this).getNewCategories(), Result.class);
        ArrayList<String> inputList = new ArrayList<>();
        ArrayList<String> inputIds = new ArrayList<>();
        txtTitle.setText(getString(R.string.txt_select_category));
        for (int i = 0; i < category.getCategories().size(); i++) {
            if (!(category.getCategories().get(i).getCategoryName().equalsIgnoreCase("All")
                    || category.getCategories().get(i).getCategoryName().equalsIgnoreCase("Trending"))) {
                inputList.add(category.getCategories().get(i).getCategoryName());
                inputIds.add(String.valueOf(category.getCategories().get(i).getId()));
            }
        }
        setCategoryAdapter(inputList, inputIds);
    }

    private void setCategoryAdapter(ArrayList<String> inputList, ArrayList<String> inputIds) {
        recyclerViewCategory.setAdapter(new SellItemCategoryAdapter(this, inputList, inputIds, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                showSubcategoryPicker(input, id);
            }
        }));
    }

    private void showSubcategoryPicker(String input, @Nullable String id) {
        Result category = new Gson().fromJson(new Preferences(this).getNewCategories(), Result.class);
        ArrayList<String> inputList = new ArrayList<>();
        ArrayList<String> inputIds = new ArrayList<>();
        for (int i = 0; i < category.getSubcategories(id).size(); i++) {
            if (!(category.getSubcategories(id).get(i).getCategoryName().equalsIgnoreCase("All")
                    || category.getSubcategories(id).get(i).getCategoryName().equalsIgnoreCase("Trending"))) {
                inputList.add(category.getSubcategories(id).get(i).getCategoryName());
                inputIds.add(String.valueOf(category.getSubcategories(id).get(i).getId()));
            }
        }

        txtTitle.setText(getString(R.string.txt_select_sub_category));
        setSubCategoryAdapter(inputList,inputIds);
    }

    private void setSubCategoryAdapter(ArrayList<String> inputList, ArrayList<String> inputIds) {
        recyclerViewCategory.setAdapter(new SellItemCategoryAdapter(this, inputList, inputIds, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                Intent intent = new Intent();
                intent.putExtra("Selected Category",input);
                intent.putExtra("categoryId",Integer.parseInt(id));
                setResult(RESULT_OK,intent);
                finish();
            }
        }));
    }
}
