package com.screamxo.Activity.wallet;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.screamxo.R;

@SuppressLint("ValidFragment")
public class MonthFragment extends DialogFragment {
    private static final String TAG = "DialogFragments";
    MonthSelection commonMethod;
    private ListView listview;
    private TextView txtcancel;
    private LinearLayout linearLayout;

    @SuppressLint("ValidFragment")
    public MonthFragment(MonthSelection commonMethod) {
        this.commonMethod = commonMethod;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_month_fragment, null);
        getDialog().setTitle(getString(R.string.txt_time));
        //listview = view.findViewById(R.id.listview_time_dialog);
        txtcancel = view.findViewById(R.id.cancle);
        linearLayout = view.findViewById(R.id.container);
        //FrameLayout imageView = view.findViewById(R.id.title_container);
        //imageView.setVisibility(View.GONE);
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

        Integer[] TimeMin = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
        };
        String[] TimeArray = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };

        TextView textView = null;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 10, 20, 4);
        for (int i = 0; i < TimeArray.length; i++) {
            textView = new TextView(txtcancel.getContext());
            textView.setId(i);
            textView.setBackgroundColor(ContextCompat.getColor(textView.getContext(), R.color.colorf7f7f7));
            textView.setTextColor(Color.BLACK);
            textView.setPadding(0, 30, 0, 30);
            textView.setGravity(Gravity.CENTER);
            textView.setText(TimeArray[i]);
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // StaticConstant.selectedTimePos = i;
                    commonMethod.setMonth(TimeMin[v.getId()]);
                    getDialog().dismiss();
                }
            });
        }

       /* List<String> time = new ArrayList<>(Arrays.asList(TimeArray));
        ArrayAdapter<String> timeadapter = new ArrayAdapter<String>(getActivity(), R.layout.list_dialog_fragment_popup, R.id.list_time_textview_popup, time) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView != null) {
                    *//*if (position == StaticConstant.selectedTimePos) {
                        convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorlightgrey));
                    } else {
                        convertView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                    }*//*
                }
                return super.getView(position, convertView, parent);
            }
        };
        listview.setAdapter(timeadapter);
        listview.setSelection(StaticConstant.selectedTimePos);
        listview.setOnItemClickListener((adapterView, view, i, l) -> {
            StaticConstant.selectedTimePos = i;
            commonMethod.setMonth(TimeMin[i]);
            getDialog().dismiss();
        });*/
        txtcancel.setOnClickListener(view -> getDialog().dismiss());
    }
}
