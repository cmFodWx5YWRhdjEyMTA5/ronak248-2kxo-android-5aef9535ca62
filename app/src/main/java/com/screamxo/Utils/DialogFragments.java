package com.screamxo.Utils;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shubham.Agarwal on 08/12/16.
 */

@SuppressLint("ValidFragment")
public class DialogFragments extends DialogFragment {
    private static final String TAG = "DialogFragments";
    CommonMethod commonMethod;
    private ListView listview;
    private LinearLayout linearLayout;
    private TextView txtcancel;

    @SuppressLint("ValidFragment")
    public DialogFragments(CommonMethod commonMethod) {
        this.commonMethod = commonMethod;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_dialogfragment, null);
        getDialog().setTitle("Time");
      //  listview = view.findViewById(R.id.listview_time_dialog);
        txtcancel = view.findViewById(R.id.cancle);
        linearLayout = view.findViewById(R.id.container);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels * 0.65));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationUpDown;

        String[] TimeMin = {
                "30",
                "60",
                "240",
                "480",
                "720",
                "960",
                "1220",
                "1440"
        };
        String[] TimeArray = {
                "30 mins",
                "1 Hour",
                "4 Hours",
                "8 Hours",
                "12 Hours",
                "16 Hours",
                "20 hours",
                "24 Hours"
        };

        TextView textView = null;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,10,20,4);
        for (int i=0;i<TimeArray.length;i++){
            textView = new TextView(txtcancel.getContext());
            textView.setId(i);
            textView.setBackgroundColor(textView.getContext().getColor(R.color.colorf7f7f7));
            textView.setTextColor(Color.BLACK);
            textView.setPadding(0,30,0,30);
            textView.setGravity(Gravity.CENTER);
            textView.setText(TimeArray[i]);
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonMethod.commonMethod(TimeArray[v.getId()]);
                    getDialog().dismiss();
                }
            });
        }

        /*List<String> time = new ArrayList<>(Arrays.asList(TimeArray));
        ArrayAdapter<String> timeadapter = new ArrayAdapter<String>(getActivity(), R.layout.list_dialog,
                R.id.list_time, time) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView != null) {
                    if (position == StaticConstant.selectedTimePos) {
                        convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorlightgrey));
                    } else {
                        convertView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                    }
                }
                return super.getView(position, convertView, parent);
            }
        };
        listview.setAdapter(timeadapter);
        listview.setSelection(StaticConstant.selectedTimePos);
        listview.setOnItemClickListener((adapterView, view, i, l) -> {
            StaticConstant.selectedTimePos = i;
            commonMethod.commonMethod(TimeMin[i]);
            getDialog().dismiss();
        });*/
        txtcancel.setOnClickListener(view -> getDialog().dismiss());
    }
}

