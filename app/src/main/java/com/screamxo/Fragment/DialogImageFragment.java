package com.screamxo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.screamxo.Adapter.ImageVideoAdapter;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.TouchImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by parangat on 24/11/17.
 */

public class DialogImageFragment extends Fragment {
    private static final String TAG = "DialogImageFragment";
    @BindView(R.id.iv_image)
    TouchImageView iv_image;

    @BindView(R.id.iv_share)
    ImageView iv_share;

    @BindView(R.id.iv_cancel)
    ImageView iv_cancel;

    private Context context;

    public static DialogImageFragment newInstance(String imageUrl) {
        Log.d(TAG, "newInstance: " + imageUrl);
        Bundle args = new Bundle();
        args.putString("image_url", imageUrl);
        DialogImageFragment fragment = new DialogImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_viewpage_dialog_image, container, false);
//        swipeDismissDialog = new SwipeDismissDialog.Builder(getContext()).setView(view).build().show();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: " + BundleUtils.getBundleExtra(getArguments(), "image_url", null));
     /*   Picasso.with(context)
                .load(BundleUtils.getBundleExtra(getArguments(), "image_url", "https://s3-us-west-2.amazonaws.com/scremax/postsmedia/281f2ff0-7c5f-11e8-bbc2-3521e809f6a0.jpg"))
                .error(R.mipmap.add_icon)
                .placeholder(R.mipmap.img_placeholder)
               // .fit()
               // .centerCrop()
                .into(iv_image);*/

        Picasso picasso = new Picasso.Builder(context)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        //Here your log
                        exception.printStackTrace();
//                        Toast.makeText(context,exception.toString(),Toast.LENGTH_LONG).show();
                    }
                })
                .build();

        Glide.with(context)
                .load(BundleUtils.getBundleExtra(getArguments(), "image_url", "https://s3-us-west-2.amazonaws.com/scremax/postsmedia/281f2ff0-7c5f-11e8-bbc2-3521e809f6a0.jpg"))
                .placeholder(R.mipmap.img_placeholder).error(R.mipmap.img_placeholder)
                .into(iv_image);


        /*picasso.load(BundleUtils.getBundleExtra(getArguments(), "image_url", "https://s3-us-west-2.amazonaws.com/scremax/postsmedia/281f2ff0-7c5f-11e8-bbc2-3521e809f6a0.jpg"))
                .error(R.mipmap.img_placeholder)
                .placeholder(R.mipmap.img_placeholder)
                .into(iv_image);*/

//        iv_image.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return gesture.onTouchEvent();
//            }
//        });
    /*    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ImageDialogFragment.class.getSimpleName());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_up_from_bottom,0);
        transaction.addToBackStack("");
        transaction.replace(R.id.framlayout,fragment);*/
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ImageDialogFragment.class.getSimpleName());
                    if (fragment != null && fragment instanceof ImageDialogFragment) {
                        ((ImageDialogFragment) fragment).dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, (BundleUtils.getBundleExtra(getArguments(), "image_url", null)));
                    sendIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(sendIntent, "Send to"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
