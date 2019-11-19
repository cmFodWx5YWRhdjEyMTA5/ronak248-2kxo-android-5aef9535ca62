package com.screamxo.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.screamxo.Adapter.BottomSheetAdapter;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Interface.DialogInterfaceAction;
import com.screamxo.Interface.FriendActionInterface;
import com.screamxo.Interface.ImagePicckerDialog;
import com.screamxo.Others.OnBottomSheetItemClickListener;
import com.screamxo.R;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 10/10/16.
 */
public class DialogBox {

    private static final String TAG = "DialogBox";

    public static final int DIALOG_SUCESS = 0;
    public static final int DIALOG_FAILURE = 1;

    public static void showDialog(Context ctx, String title, String message, int type, @Nullable final DialogInterfaceAction dialogInterfaceAction) {
        Dialog dialogphoto = new Dialog(ctx);
        dialogphoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogphoto.setContentView(R.layout.dialog_message);
        TextView title_tv = dialogphoto.findViewById(R.id.title_tv);
        TextView txtValidation = dialogphoto.findViewById(R.id.txtValidation);
        ImageView iv_image = dialogphoto.findViewById(R.id.iv_image);
        if (type == DIALOG_FAILURE) {
            iv_image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_cancel_tiffany_blue));
        } else {
            iv_image.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_tick_circle_tiffany_blue));
        }
        title_tv.setText(Html.fromHtml(title));
        txtValidation.setText(message);
        Button btnOk = dialogphoto.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogphoto.dismiss();
                if (dialogInterfaceAction != null) {
                    dialogInterfaceAction.dialogAction();
                }
            }
        });
        dialogphoto.show();
    }

    @SuppressWarnings("WeakerAccess")
    public static void showAlertImagePicker(Activity activity, final ImagePicckerDialog imageDialogInteface) {
        Log.d(TAG, "showAlertImagePicker: ");

        @SuppressLint("InflateParams") View modalbottomsheet = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Media");

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("Camera");
        inputList.add("Gallery");

        ArrayList<Integer> imageIds = new ArrayList<>();
        imageIds.add(R.drawable.ico_camera_chat);
        imageIds.add(R.drawable.gallery_image);

        rv_container.setAdapter(new BottomSheetAdapter(activity, inputList, null, imageIds, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Camera")) {
                    if (imageDialogInteface != null) {
                        imageDialogInteface.OnclickCamara();
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Gallery")) {
                    if (imageDialogInteface != null) {
                        imageDialogInteface.OnclickGallery();
                    }
                    return;
                }
            }
        }));
    }

    /*no use... */
    public static void showAlertFriendAction(Activity activity, final FriendActionInterface friendactioninterface, String frdType) {
        //                    frdType=   0=sent,1=accpeted,2=rejected,3=blocked,4=unbloked,5=unfriend,6=cancelled
        @SuppressLint("InflateParams") View modalbottomsheet = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Choose Action");

        ArrayList<String> inputList = new ArrayList<>();
        switch (frdType) {
            case "0":
                inputList.add("Block");
                inputList.add("Add friend");
                inputList.add("Cancel");
                break;

            case "1":
                inputList.add("Block");
                inputList.add("Un Friend");
                inputList.add("Message");
                break;

            case "2":
                inputList.add("Block");
                inputList.add("Cancel request");
                inputList.add("Cancel");
                break;

            case "3":
                inputList.add("Block");
                inputList.add("Accept");
                inputList.add("Reject");
                break;
        }

        rv_container.setAdapter(new BottomSheetAdapter(activity, inputList, null, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Block")) {
                    if (friendactioninterface != null) {
                        friendactioninterface.onClickBlock();
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Un Friend") || input.equalsIgnoreCase("Add friend")
                        || input.equalsIgnoreCase("Cancel request") || input.equalsIgnoreCase("Accept")) {
                    if (friendactioninterface != null) {
                        friendactioninterface.onClickUnFriend();
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Cancel") || input.equalsIgnoreCase("Message")
                        || input.equalsIgnoreCase("Reject")) {
                    if (friendactioninterface != null) {
                        friendactioninterface.onClickMessage();
                    }
                    return;
                }

            }
        }));
    }

    public static void showDownloadImageDialog(Activity activity, final CommonMethod commonMethod, int menuId) {

        @SuppressLint("InflateParams") View modalbottomsheet = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Media");

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("Download");
        inputList.add("Delete");
        inputList.add("Cancel");

        ArrayList<Integer> imageIds = new ArrayList<>();
        imageIds.add(R.drawable.ic_download);
        imageIds.add(R.drawable.vect_delete);
        imageIds.add(R.drawable.vect_close);

        rv_container.setAdapter(new BottomSheetAdapter(activity, inputList, null, imageIds, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Download")) {
                    if (commonMethod != null) {
                        commonMethod.commonMethod("3");
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Delete")) {
                    if (commonMethod != null) {
                        commonMethod.commonMethod("1");
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Cancel")) {
                    return;
                }

            }
        }));
    }

    public static void showAlertDeletemsg(Activity activity, final CommonMethod commonMethod, int menuId) {
        @SuppressLint("InflateParams") View modalbottomsheet = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Media");

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("Copy");
        inputList.add("Delete");
        inputList.add("Cancel");

        ArrayList<Integer> imageIds = new ArrayList<>();
        imageIds.add(R.drawable.vect_copy);
        imageIds.add(R.drawable.vect_delete);
        imageIds.add(R.drawable.vect_close);

        rv_container.setAdapter(new BottomSheetAdapter(activity, inputList, null, imageIds, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Copy")) {
                    if (commonMethod != null) {
                        commonMethod.commonMethod("2");
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Delete")) {
                    if (commonMethod != null) {
                        commonMethod.commonMethod("1");
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Cancel")) {
                    return;
                }

            }
        }));
    }

    public static void showAlertImagePickerUpdate(Activity activity, final ImagePicckerDialog imageDialogInteface) {
        Log.d(TAG, "showAlertImagePickerUpdate: ");

        @SuppressLint("InflateParams") View modalbottomsheet = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Media");

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("Camera");
        inputList.add("Gallery");
        inputList.add("Remove Photo");

        ArrayList<Integer> imageIds = new ArrayList<>();
        imageIds.add(R.drawable.ico_camera_chat);
        imageIds.add(R.drawable.gallery_image);
        imageIds.add(R.drawable.vect_close);

        rv_container.setAdapter(new BottomSheetAdapter(activity, inputList, null, imageIds, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Camera")) {
                    if (imageDialogInteface != null) {
                        imageDialogInteface.OnclickCamara();
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Gallery")) {
                    if (imageDialogInteface != null) {
                        imageDialogInteface.OnclickGallery();
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Remove Photo")) {
                    if (imageDialogInteface != null) {
                        imageDialogInteface.OnclickNoImage();
                    }
                    return;
                }

            }
        }));
    }

    public static void showTrackingDetails(Context context, String courierCompanyName, String trackingNumber) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tracking_details);
        TextView courier_company_name_tv = dialog.findViewById(R.id.courier_company_name_tv);
        TextView tracking_no_tv = dialog.findViewById(R.id.tracking_no_tv);
        TextView done_tv = dialog.findViewById(R.id.done_tv);


        courier_company_name_tv.setText(Html.fromHtml(courierCompanyName));
        tracking_no_tv.setText(trackingNumber);

        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public interface OnShopItemOptionClickListener {
        void onEditItem();

        void onClickPurchaseHistory();

        void shareItem();

        void sendMessage();
        void reportPost();
    }

    public static void showMyItemOptions(Context context, boolean hasPurchasedData) {
        DialogBox.OnShopItemOptionClickListener onShopItemOptionClickListener = (OnShopItemOptionClickListener) context;
        @SuppressLint("InflateParams") View modalbottomsheet = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Choose Action");

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("Edit Item");

        if (hasPurchasedData) {
            inputList.add("Purchase History");
        }

        rv_container.setAdapter(new BottomSheetAdapter(context, inputList, null, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Edit Item")) {
                    onShopItemOptionClickListener.onEditItem();
                    return;
                }

                if (input.equalsIgnoreCase("Purchase History")) {
                    onShopItemOptionClickListener.onClickPurchaseHistory();
                    return;
                }
            }
        }));
    }

    public static void showOtherItemOptions(Context context) {
        DialogBox.OnShopItemOptionClickListener onShopItemOptionClickListener = (OnShopItemOptionClickListener) context;
        @SuppressLint("InflateParams") View modalbottomsheet = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);

        tv_title.setText("Choose Action");

        ArrayList<String> inputList = new ArrayList<>();
        inputList.add("Report Post");
        inputList.add("Share Item");
        inputList.add("Send Message");

        rv_container.setAdapter(new BottomSheetAdapter(context, inputList, null, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Share Item")) {
                    onShopItemOptionClickListener.shareItem();
                    return;
                }

                if (input.equalsIgnoreCase("Send Message")) {
                    onShopItemOptionClickListener.sendMessage();
                    return;
                }
                if (input.equalsIgnoreCase("Report Post")) {
                    onShopItemOptionClickListener.reportPost();
                    return;
                }
            }
        }));
    }

    public static void showStreamPostOption(Context context, int postUserId) {

        OnStreamPostOptionClickListener onStreamPostOptionClickListener = (OnStreamPostOptionClickListener) context;
        Preferences preferences = new Preferences(context);

        @SuppressLint("InflateParams") View modalbottomsheet = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_view, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_title = modalbottomsheet.findViewById(R.id.tv_title);
        RecyclerView rv_container = modalbottomsheet.findViewById(R.id.rv_container);
        tv_title.setVisibility(View.GONE);
//        tv_title.setText("Choose Action");

        ArrayList<String> inputList = new ArrayList<>();
        if (preferences.getUserId().equals(String.valueOf(postUserId))) {
            inputList.add("Delete");
        } else {
            inputList.add("Report Post");
        }
        inputList.add("Share");

        rv_container.setAdapter(new BottomSheetAdapter(context, inputList, null, null, new OnBottomSheetItemClickListener() {
            @Override
            public void onItemClick(String input, @Nullable String id) {
                dialog.dismiss();
                if (input.equalsIgnoreCase("Delete") || input.equalsIgnoreCase("Report Post")) {
                    if (preferences.getUserId().equals(String.valueOf(postUserId))) {
                        onStreamPostOptionClickListener.onDeletePost();
                    } else {
                        onStreamPostOptionClickListener.onReportPost();
                    }
                    return;
                }

                if (input.equalsIgnoreCase("Share")) {
                    onStreamPostOptionClickListener.onSharePost();
                    return;
                }
            }
        }));
    }

    public interface OnStreamPostOptionClickListener {
        void onDeletePost();

        void onSharePost();

        void onReportPost();
    }

    public static void pickPaymentProcessorEmailDialog(Context context, String hint, String paymentProcessorType, OnPaymentProcessorEmailAddedListener onPaymentProcessorEmailAddedListener) {
        try {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_payment_processor_email);
            EditText et_email = dialog.findViewById(R.id.et_email);
            Button btn_submit = dialog.findViewById(R.id.btn_submit);
            et_email.setHint(hint);
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.hideKeyboard(context);
                    dialog.dismiss();
                    if (onPaymentProcessorEmailAddedListener != null) {
                        onPaymentProcessorEmailAddedListener.onSubmit(paymentProcessorType, et_email.getText().toString());
                    }
                }
            });
            dialog.show();

            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } catch (Exception ignored) {

        }

    }

    public interface OnPaymentProcessorEmailAddedListener {
        void onSubmit(String paymentProcessorType, String email);
    }
}
