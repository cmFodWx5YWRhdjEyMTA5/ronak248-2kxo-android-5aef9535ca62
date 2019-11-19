package com.screamxo.Utils;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.screamxo.Interface.CommonMethod;
import com.screamxo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Agarwal on 15/02/17.
 */

@SuppressLint("ValidFragment")
public class DialogFragmentForItemQuantity extends DialogFragment {
    private ListView listview;
    private TextView txtcancel;
    CommonMethod commonMethod;
    ArrayList<String> stringArrayListQuantity;

    @SuppressLint("ValidFragment")
    public DialogFragmentForItemQuantity(CommonMethod commonMethod, ArrayList<String> stringArrayListQuantity, Context context) {
        this.commonMethod = commonMethod;
        this.stringArrayListQuantity = stringArrayListQuantity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_fragment_item_quantity, null);
        getDialog().setTitle("Time");
        listview = view.findViewById(R.id.listview_time_dialog);
        txtcancel = view.findViewById(R.id.txt_cancel);
        TextView txtDone = view.findViewById(R.id.txt_done);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels * 0.40));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationUpDown;

        List<String> time = new ArrayList<>(stringArrayListQuantity);

        ArrayAdapter<String> timeadapter = new ArrayAdapter<>(getActivity(), R.layout.item_quantity_text_dialog_fragment, R.id.list_time_textview, time);
        listview.setAdapter(timeadapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (stringArrayListQuantity.size() != 0)
                    commonMethod.commonMethod(stringArrayListQuantity.get(i));
                getDialog().cancel();
            }
        });

      /*  txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonMethod.commonMethod(stringArrayListQuantity.get(i));
                getDialog().cancel();
            }
        });*/
        txtcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

    }
}
