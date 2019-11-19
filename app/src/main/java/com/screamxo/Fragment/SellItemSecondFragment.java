package com.screamxo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.screamxo.Activity.RahulWork.ConditionActivity;
import com.screamxo.Activity.RahulWork.SellItemActivity;
import com.screamxo.Activity.SelectItemConditionActivity;
import com.screamxo.Activity.SellItemCategoryActivity;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class SellItemSecondFragment extends Fragment {
    private static final String TAG = "SellItemSecondFragment";
    @BindView(R.id.ed_item_name)
    EditText edItemName;
    @BindView(R.id.txtCondition)
    TextView txtCondition;
    @BindView(R.id.imgDropDown1)
    ImageView imgDropDown1;
    @BindView(R.id.txtCategory)
    TextView txtCategory;
    @BindView(R.id.imgDropDown)
    ImageView imgDropDown;
    @BindView(R.id.ed_item_Deacription)
    EditText edItemDeacription;
    @BindView(R.id.ed_item_Quantity)
    EditText edItemQuantity;
    @BindView(R.id.btnAddDetails)
    Button btnAddDetails;
    Unbinder unbinder;
    @BindView(R.id.rl_Condition)
    RelativeLayout rl_Condition;
    @BindView(R.id.rl_category)
    RelativeLayout rl_category;
    @BindView(R.id.imgQuestion)
    ImageView imgQuestion;
    private Context context;
    private static final int RC_PICK_CONDITION = 201;
    int categoryId;
    private Preferences preferences;

    public SellItemSecondFragment() {
        // Required empty public constructor
    }

    public static SellItemSecondFragment newInstance(int page, String title) {
        return new SellItemSecondFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell_item_second, container, false);
        unbinder = ButterKnife.bind(this, view);
        initControls();
        return view;
    }

    @Override
    public void onResume() {
//        if (!StaticConstant.SELL_CONDITION.equalsIgnoreCase("")) {
//            txtCondition.setText(StaticConstant.SELL_CONDITION);
//        }
        super.onResume();
        context = getActivity();
        preferences = new Preferences(context);
    }

    private void initControls() {
        btnAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edItemName.getText().toString().isEmpty()) {
                    DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_enter_item_name), DialogBox.DIALOG_FAILURE, null);
                    return;
                }

                if (txtCondition.getText().toString().isEmpty()) {
                    DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_enter_condition), DialogBox.DIALOG_FAILURE, null);
                    return;
                }

                if (txtCategory.getText().toString().isEmpty()) {
                    DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_enter_category), DialogBox.DIALOG_FAILURE, null);
                    return;
                }

                if (edItemDeacription.getText().toString().isEmpty()) {
                    DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_enter_description), DialogBox.DIALOG_FAILURE, null);
                    return;
                }

                if (edItemQuantity.getText().toString().isEmpty()) {
                    DialogBox.showDialog(context, context.getString(R.string.app_name), getString(R.string.txt_enter_quantity), DialogBox.DIALOG_FAILURE, null);
                    return;
                }

                ((SellItemActivity) getActivity()).setSelectedPage(2);
            }
        });

        rl_Condition.setOnClickListener(view -> {
            Intent intent = new Intent(context, SelectItemConditionActivity.class);
            startActivityForResult(intent, 1000);
            // showConditionPicker();
        });

        imgQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoNext = new Intent(getActivity(), ConditionActivity.class);
                startActivityForResult(gotoNext, RC_PICK_CONDITION);
            }
        });

        rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SellItemCategoryActivity.class);
                startActivityForResult(intent, 1001);
                // showCategoryPicker();
            }
        });
    }

   /* public void showCategoryPicker() {
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
    }*/

    /*private void showSubcategoryPicker(String input, @Nullable String id) {
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
                txtCategory.setText(input);
                try {
                    categoryId = Integer.parseInt(id);
                } catch (NumberFormatException ignored) {
                }
            }
        }));
    }*/

    @Override
    public void onDestroyView() {
//        StaticConstant.SELL_CONDITION = "";
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.printIntentData(data);

        switch (requestCode) {
            case RC_PICK_CONDITION:
                switch (resultCode) {
                    case RESULT_OK:
                        txtCondition.setText(BundleUtils.getIntentExtra(data, getString(R.string.txt_sell_condition), ""));
                        break;
                }
                break;
            case 1000:
                if (data != null && data.getExtras().containsKey("Selected Condition"))
                    txtCondition.setText(data.getExtras().getString("Selected Condition"));
                break;
            case 1001:
                if (data != null && data.getExtras().containsKey("Selected Category"))
                    txtCategory.setText(data.getExtras().getString("Selected Category"));
                if(data!=null && data.getExtras().containsKey("categoryId"))
                    categoryId = data.getExtras().getInt("categoryId");
                break;
        }
    }
}
