package com.screamxo.Fragment;

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
import android.widget.ListView;
import android.widget.TextView;

import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;
import com.screamxo.Utils.StaticConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("ValidFragment")
public class DialogTimerFragment extends DialogFragment {
    private static final String TAG = "DialogFragments";
    CommonMethod commonMethod;
    private ListView listview;
    private TextView txtcancel;

    @SuppressLint("ValidFragment")
    public DialogTimerFragment(CommonMethod commonMethod) {
        this.commonMethod = commonMethod;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_timer_fragment, null);
        getDialog().setTitle(R.string.txt_time);
        listview = view.findViewById(R.id.listview_time_dialog);
        txtcancel = view.findViewById(R.id.cancle);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels * 0.70));
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
                "30 Mins",
                "1 Hour",
                "4 Hours",
                "8 Hours",
                "12 Hours",
                "16 Hours",
                "20 hours",
                "24 Hours"
        };

        List<String> time = new ArrayList<>(Arrays.asList(TimeArray));
        ArrayAdapter<String> timeadapter = new ArrayAdapter<String>(getActivity(), R.layout.list_dialog_fragment_popup,
                R.id.list_time_textview_popup, time) {
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
        });
        txtcancel.setOnClickListener(view -> getDialog().dismiss());
    }
}


