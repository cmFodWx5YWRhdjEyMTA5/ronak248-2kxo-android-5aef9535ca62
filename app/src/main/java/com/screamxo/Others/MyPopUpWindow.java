package com.screamxo.Others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sandrios.sandriosCamera.internal.utils.Utils;
import com.screamxo.Adapter.RecyclerViewAdapter;
import com.screamxo.R;

import java.util.ArrayList;

public class MyPopUpWindow extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private static final String TAG = "MyPopUpWindow";
    public static int checkboxposition = 0;
    public ArrayList<CheckboxResponse> strfilter;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Context context;
    private View transparentView;
    private OnPopupItemClickListener onPopupItemClickListner;
    private AppCompatImageView arrowTopLeft, arrowTopCenter, arrowTopRight, arrowBottomLeft, arrowBottomCenter, arrowBottomRight;
    private int anchorViewWidth;

    /***
     * Constructor to initialize views
     *
     * @param context         context of activity or fragment
     * @param transparentView it's a view that we have to show as dim while open a window
     * @param str             whereArgs strings for popup textview text.
     */
    @SuppressLint("PrivateResource")
    public MyPopUpWindow(Context context, View transparentView, String[] str, View anchor, String screen_type) {
        super(context);

        this.context = context;
        this.transparentView = transparentView;

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_item_info, null);
        setContentView(view);

        LinearLayout view_container = view.findViewById(R.id.view_container);
        ScrollView sv_container = view.findViewById(R.id.sv_container);

        arrowTopLeft = view.findViewById(R.id.arrowTopLeft);
        arrowTopCenter = view.findViewById(R.id.arrowTopCenter);
        arrowTopRight = view.findViewById(R.id.arrowTopRight);
        arrowBottomLeft = view.findViewById(R.id.arrowBottomLeft);
        arrowBottomCenter = view.findViewById(R.id.arrowBottomCenter);
        arrowBottomRight = view.findViewById(R.id.arrowBottomRight);
        LinearLayout image_chooser_popup = view.findViewById(R.id.image_chooser_popup);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));

        setOnDismissListener(this);
        int five = context.getResources().getDimensionPixelSize(R.dimen._10sdp);
        int four = context.getResources().getDimensionPixelSize(R.dimen._10sdp);
        int forty = context.getResources().getDimensionPixelSize(R.dimen._40sdp);
        int maxWidthText = 0;

        switch (screen_type) {
            case "Dashboardshop":
                ViewGroup.LayoutParams layoutParams = sv_container.getLayoutParams();
                layoutParams.height = Utils.convertDipToPixels(context, 160);
                break;
        }

        if (screen_type.equalsIgnoreCase("commentFilter")) {
            setCommentAdapter();
        } else {
            for (int i = 0; i < str.length; i++) {
                if (maxWidthText < str[i].length()) {
                    maxWidthText = str[i].length();
                }

                TextView textView = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                if (screen_type.equalsIgnoreCase("ProfileRejectList")) {
                    setTint();
                    layoutParams.height = forty;
                    layoutParams.setMargins(0, 0, 0, 0);
                    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    textView.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));
                    if (Build.VERSION.SDK_INT >= 16) {
                        textView.setBackground(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    } else {
                        textView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_popup_selector));
                    }
                    textView.setTextColor(ContextCompat.getColor(context, R.color.tw__blue_default));
                    textView.setTextSize(11);
                    textView.setOnClickListener(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(str[i]);
                    textView.setWidth(600);
                    textView.setGravity(Gravity.CENTER);
                    textView.setId(i);
                    view_container.addView(textView);
                } else if (screen_type.equalsIgnoreCase("RejectList")) {
                    layoutParams.height = forty;
                    layoutParams.setMargins(0, 0, 0, 0);
                    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    textView.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));

                    if (i == 1) {
                        if (Build.VERSION.SDK_INT >= 16) {
                            textView.setBackground(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                        } else {
                            textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                        }
                        textView.setTextColor(Color.RED);
                        textView.setTextSize(13);
                    } else {
                        textView.setTextSize(11);
                    }
                    int padding = 0;
                    textView.setOnClickListener(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(str[i]);

                    textView.setWidth(600);
                    textView.setGravity(Gravity.CENTER);
                    textView.setId(i);
                    view_container.addView(textView);


                } else if (screen_type.equals("SellItem")) {
                    layoutParams.height = forty;
                    layoutParams.setMargins(five, four, five, four);
                    textView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);

                    textView.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));

                    if (Build.VERSION.SDK_INT >= 16) {
                        textView.setBackground(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    } else {
                        textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    }

                    int padding = 0;
                    textView.setPadding(padding, 0, padding, 0);
                    textView.setOnClickListener(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(str[i]);
                    textView.setWidth(400);
                    textView.setSingleLine(false);
                    textView.setId(i);
                    view_container.addView(textView);
                } else if (screen_type.equalsIgnoreCase("wallet")) {
                    layoutParams.height = forty;
                    layoutParams.setMargins(0, 0, 0, 0);
                    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    textView.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));

                    if (Build.VERSION.SDK_INT >= 16) {
                        textView.setBackground(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    } else {
                        textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    }

                    int padding = 0;
                    textView.setPadding(20, padding, 0, padding);
                    textView.setOnClickListener(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(str[i]);
                    textView.setTextSize(11);
                    textView.setWidth(400);
                    textView.setSingleLine(true);
                    textView.setId(i);
                    view_container.addView(textView);

                } else if (screen_type.equalsIgnoreCase("support")) {
                    layoutParams.height = forty;
                    layoutParams.setMargins(0, 0, 0, 0);
                    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    textView.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));
                    if (Build.VERSION.SDK_INT >= 16) {
                        textView.setBackground(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    } else {
                        textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    }

                    int padding = 0;
                    textView.setPadding(20, padding, 0, padding);
                    textView.setOnClickListener(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(str[i]);
                    textView.setTextSize(11);
                    textView.setWidth(330);
                    textView.setSingleLine(false);
                    textView.setId(i);
                    view_container.addView(textView);


                } else if (screen_type.equalsIgnoreCase("Dashboardshop")) {
                    layoutParams.height = forty - 50;
                    layoutParams.setMargins(0, 0, 0, 0);
                    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    textView.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));

                    int padding = 0;
                    textView.setPadding(40, padding, 0, padding);
                    textView.setPadding(30, padding, 0, padding);
                    textView.setOnClickListener(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(str[i]);
                    textView.setTextSize(11);
                    textView.setWidth(600);
                    textView.setHeight(10);
                    textView.setSingleLine(true);
                    textView.setId(i);

                    view_container.addView(textView);
                } else if (screen_type.equalsIgnoreCase("DashboardFragment")) {
                    Log.i("popup", "........height........" + forty);
                    layoutParams.height = forty - 50;
                    //layoutParams.setMargins(five,four,five,four);
                    layoutParams.setMargins(0, 0, 0, 0);
                    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    // textView.setGravity(Gravity.CENTER);

                    textView.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));

                    if (Build.VERSION.SDK_INT >= 16) {
                        textView.setBackground(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    } else {
                        textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    }

                    int padding = 0;
                    textView.setPadding(40, padding, 0, padding);
                    //  textView.setPadding(padding,0,padding,0);
                    textView.setPadding(30, padding, 0, padding);
                    //  textView.setPadding(padding,0,padding,0);
                    textView.setOnClickListener(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(str[i]);
                    textView.setTextSize(11);
                    textView.setWidth(300);
                    textView.setHeight(10);
                    textView.setSingleLine(true);
                    textView.setId(i);
                    view_container.addView(textView);
                } else {
                    layoutParams.height = forty;
                    //layoutParams.setMargins(five,four,five,four);
                    layoutParams.setMargins(0, 0, 0, 0);

                    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    // textView.setGravity(Gravity.CENTER);

                    textView.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));

                    if (Build.VERSION.SDK_INT >= 16) {
                        textView.setBackground(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    } else {
                        textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popup_selector));
                    }

                    int padding = 0;
                    textView.setPadding(40, padding, 0, padding);
                    //  textView.setPadding(padding,0,padding,0);
                    textView.setPadding(30, padding, 0, padding);
                    //  textView.setPadding(padding,0,padding,0);
                    textView.setOnClickListener(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(str[i]);
                    textView.setTextSize(11);
                    textView.setWidth(400);
                    textView.setHeight(10);
                    textView.setSingleLine(true);
                    textView.setId(i);
                    view_container.addView(textView);
                }

            }
        }
        image_chooser_popup.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        setWidth(image_chooser_popup.getMeasuredWidth());
        setHeight(image_chooser_popup.getMeasuredHeight());
        hideAllArrows();
        anchorViewWidth = anchor.getMeasuredWidth();
        int anchorViewHeight = anchor.getMeasuredHeight();
    }

    @SuppressLint("RestrictedApi")
    private void setTint() {
        arrowTopRight.setSupportBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.white));
    }

    public void setOnPopupItemClickListner(OnPopupItemClickListener onPopupItemClickListner) {
        this.onPopupItemClickListner = onPopupItemClickListner;
    }

    /***
     * @param anchor        it's anchor view
     * @param arrowPosition @param arrowPosition pass 1=left,2=center,3=right and will consider for top or bottom after creating popup
     */
    public void show(View anchor, PopUpPosition arrowPosition) {
        //    transparentView.setVisibility(View.VISIBLE);
        showAsDropDown(anchor, 0, 0);
//
        int ten = context.getResources().getDimensionPixelSize(R.dimen._10sdp);
        setMargins(arrowTopLeft, (anchorViewWidth / 2) - ten, 0, 0, 0);

        if (isAboveAnchor()) {
            findArrowPosition(arrowPosition);
            update(anchor, 0, -50, -1, -1);
        } else {
            findArrowPosition(arrowPosition);
            if (arrowPosition == PopUpPosition.RIGHT) {
                update(anchor, -100, 0, -1, -1);
            } else if (arrowPosition == PopUpPosition.CENTER) {
                update(anchor, -getWidth() / 2 + anchorViewWidth / 2, -15, -1, -1);
            } else {
                update(anchor, 0, -15, -1, -1);
            }
        }
    }

    public void showRightSide(View anchor, PopUpPosition arrowPosition) {
        /*int ten = context.getResources().getDimensionPixelSize(R.dimen.d_10);
        setMargins(arrowTopLeft,(anchorViewWidth/2)-ten,0,0,0);*/

        transparentView.setVisibility(View.VISIBLE);
        showAsDropDown(anchor, -getWidth() + anchorViewWidth / 2 - 15, 0);

        if (isAboveAnchor()) {
            update(anchor, -getWidth() + anchorViewWidth / 2 - 15, -50, -1, -1);
        } else {
            update(anchor, -getWidth() + anchorViewWidth / 2 - 15, 0, -1, -1);
        }
        findArrowPosition(arrowPosition);
    }

    public void showRightSideSetting(View anchor, PopUpPosition arrowPosition) {
        /*int ten = context.getResources().getDimensionPixelSize(R.dimen.d_10);
        setMargins(arrowTopLeft,(anchorViewWidth/2)-ten,0,0,0);*/

        transparentView.setVisibility(View.VISIBLE);
        showAsDropDown(anchor, -getWidth() + anchorViewWidth + 20, -40);

        if (isAboveAnchor()) {
            update(anchor, -getWidth() + anchorViewWidth + 20, -40, -1, -1);
        } else {
            update(anchor, -getWidth() + anchorViewWidth + 20, -40, -1, -1);
        }
        findArrowPosition(arrowPosition);
    }

    @Override
    public void onDismiss() {
        // transparentView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (onPopupItemClickListner != null) {
            onPopupItemClickListner.onPopupItemClick(v.getId());
        }
    }

    private void findArrowPosition(PopUpPosition position)
    {
        switch (position)
        {
            case LEFT:
                if (!isAboveAnchor()) {
                    arrowTopLeft.setVisibility(View.VISIBLE);
                    arrowBottomLeft.setVisibility(View.GONE);
                } else {
                    arrowBottomLeft.setVisibility(View.VISIBLE);
                    arrowTopLeft.setVisibility(View.GONE);
                }

                break;
            case CENTER:
                if (!isAboveAnchor()) {
                    arrowTopCenter.setVisibility(View.VISIBLE);
                    arrowBottomCenter.setVisibility(View.GONE);
                } else {
                    arrowBottomCenter.setVisibility(View.VISIBLE);
                    arrowTopCenter.setVisibility(View.GONE);
                }
                break;
            case RIGHT:
                if (!isAboveAnchor())
                {
                    arrowTopRight.setVisibility(View.VISIBLE);
                    arrowBottomRight.setVisibility(View.GONE);
                } else {
                    arrowBottomRight.setVisibility(View.VISIBLE);
                    arrowTopRight.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void hideAllArrows() {
        arrowTopLeft.setVisibility(View.GONE);
        arrowTopCenter.setVisibility(View.GONE);
        arrowTopRight.setVisibility(View.GONE);
        arrowBottomLeft.setVisibility(View.GONE);
        arrowBottomCenter.setVisibility(View.GONE);
        arrowBottomRight.setVisibility(View.GONE);
    }

    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    public void setCommentAdapter() {
        strfilter = new ArrayList<>();
        strfilter.add(new CheckboxResponse("Public", false, "2"));
        strfilter.add(new CheckboxResponse("Friends", false, "1"));
        strfilter.add(new CheckboxResponse("Private", false, "0"));
        strfilter.add(new CheckboxResponse("Trending", false, "3"));
        strfilter.get(checkboxposition).setFlag(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewAdapter = new RecyclerViewAdapter(context, strfilter, this);
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }

    public void setCount(int position) {
        Log.d(TAG, "setCount: " + position);
        checkboxposition = position;
        strfilter.clear();
        strfilter.add(new CheckboxResponse("Public", false, "2"));
        strfilter.add(new CheckboxResponse("Friends", false, "1"));
        strfilter.add(new CheckboxResponse("Private", false, "0"));
        strfilter.add(new CheckboxResponse("Trending", false, "3"));
        strfilter.get(position).setFlag(true);
        recyclerViewAdapter.notifyDataSetChanged();
        dismiss();
    }

    public enum PopUpPosition {
        LEFT, CENTER, RIGHT
    }

    public interface OnPopupItemClickListener {
        boolean onPopupItemClick(int position);
    }

    public class CheckboxResponse {
        String data = "";
        Boolean flag = false;
        String id = "";

        CheckboxResponse(String data, Boolean flag, String id) {
            this.data = data;
            this.flag = flag;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Boolean getFlag() {
            return flag;
        }

        public void setFlag(Boolean flag) {
            this.flag = flag;
        }
    }
}


