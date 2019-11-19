package com.screamxo.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.apimodule.ApiBase.ApiBean.PostMediaBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.gesture.Swipe;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Adapter.HomeViewPagerAdapter;
import com.screamxo.Others.CustomViewPager;
import com.screamxo.R;
import com.screamxo.Utils.BundleUtils;
import com.screamxo.Utils.DialogBox;
import com.screamxo.Utils.EventData;
import com.screamxo.Utils.FileUtils;
import com.screamxo.Utils.MyApplication;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.RequestBodyConveter;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.videocompression.MediaController;
import com.screamxo.compressor.CompressListener;
import com.screamxo.compressor.Compressor;
import com.screamxo.compressor.InitListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_CAPTION_ARG;
import static com.sandrios.sandriosCamera.internal.ui.preview.PreviewDashboardActivity.FILE_PATH_ARG;
import static com.screamxo.Activity.DrawerMainActivity.REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP_NEW;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP_NEW_PROFILE;
import static com.screamxo.Utils.EventData.EVENT_TOGGLE_SEARCH;
import static com.screamxo.Utils.StaticConstant.MEDIA_0;
import static com.screamxo.Utils.StaticConstant.POST_TYPE_IMAGE;
import static com.screamxo.Utils.StaticConstant.POST_TYPE_MUSIC;
import static com.screamxo.Utils.StaticConstant.POST_TYPE_RECORD;

/**
 * Created by Krunal.Kevadiya on 20/02/17.
 */
public class DashboardPagerFragment extends Fragment {
    private static final int CAPTURE_MEDIA = 368;
    private static final String TAG = "DashboardPagerFragment";
    @BindView(R.id.view_pager)
    public CustomViewPager viewPager;
    public Fragment fragment;
    public Swipe swipe = new Swipe();
    boolean isAttatched = true;
    @BindView(R.id.lnr_top)
    LinearLayout lnr_top;
    MyPageAdapter pageAdapter;
    RequestBodyConveter requestbodyconverter;
    LinkedHashMap<String, File> fileArray;
    String type[];
    Preferences preferences;
    /*Variables related to posting MEDIA FILE*/
    String[] postMediaType;
    String fromData = "";
    LinkedHashMap<String, File> fileLinkedHashMap;
    NotificationCompat.Builder builder;
    int i = 0;
    Boolean isCompressed = false;
    private Context context;
    private FetchrServiceBase mService;
    private String filePath;

    //    static int  currentItemPos = 1;
    public static DashboardPagerFragment newInstance(String pos)
    {
        Bundle args = new Bundle();
        args.putString("CURRENT_ITEM_POSTION", pos);
        DashboardPagerFragment dashboardPagerFragment = new DashboardPagerFragment();
        dashboardPagerFragment.setArguments(args);
        return dashboardPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        initData();
        return inflater.inflate(R.layout.fragment_dash_board_pager, container, false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if (newConfig.orientation == ORIENTATION_PORTRAIT) {
            lnr_top.setVisibility(View.VISIBLE);
        } else {
            lnr_top.setVisibility(View.GONE);
        }
        super.onConfigurationChanged(newConfig);
    }

    private void initData()
    {
        requestbodyconverter = new RequestBodyConveter();
        fileArray = new LinkedHashMap<>();
        type = new String[1];
        mService = new FetchrServiceBase();
        preferences = new Preferences(getActivity());
    }

    public void initSearch(String data)
    {
        try
        {
            fragment = pageAdapter.getItem(1);
            if (fragment instanceof DashboardFragment)
            {
                EventBus.getDefault().post(new EventData(EVENT_TOGGLE_SEARCH, data));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(preferences.getUserToken()) && TextUtils.isEmpty(preferences.getUserId()) && false)
        {
            fragment = new DashboardFragment();
            HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getChildFragmentManager());
            homeViewPagerAdapter.addFragment(fragment, "");
            viewPager.setAdapter(homeViewPagerAdapter);
        }
        else
            {
            RxPermissions.getInstance(getActivity()).request(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean)
                {
                    if (aBoolean)
                    {
                        pageAdapter = new MyPageAdapter(getChildFragmentManager());
                        viewPager.setAdapter(pageAdapter);
//                    fragment = pageAdapter.getItem(currentItemPos);
                        //  fragment = new DashboardFragment();
//                   fragment =  pageAdapter.getItem(currentItemPos);
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                            {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                Utils.hideKeyboard(context);
                                StaticConstant.count = position;
                                try {
                                    if (!((DrawerMainActivity) context).floatingButton.isMenuOpen()) {
                                        ((DrawerMainActivity) context).floatingButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                fragment = pageAdapter.getItem(position);
                                if (fragment instanceof DashboardFragment) {
                                    ((DrawerMainActivity) context).setVisibility(1, View.VISIBLE);
                                    lnr_top.setVisibility(View.VISIBLE);
                                } else if (fragment instanceof SocialFragment) {
                                    ((DrawerMainActivity) context).setVisibility(4, View.GONE);
                                    lnr_top.setVisibility(View.GONE);
                                } else {
                                    ((DrawerMainActivity) context).setVisibility(-1, View.GONE);
                                    lnr_top.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });

                        int currentItemPos = 1;
                        if (pageAdapter.getCount() > 1) {
                            viewPager.setOffscreenPageLimit(2);
                        }

                        try {
                            currentItemPos = Integer.parseInt(BundleUtils.getBundleExtra(getArguments(), "CURRENT_ITEM_POSTION", "1"));
                            viewPager.setCurrentItem(currentItemPos);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void check_Page() {
        if (viewPager.getCurrentItem() == 1) {
            EventBus.getDefault().post(new EventData(EVENT_SCROLL_TO_TOP, ""));
        } else if (viewPager.getCurrentItem() == 0) {
            EventBus.getDefault().post(new EventData(EVENT_SCROLL_TO_TOP_NEW, ""));
        } else {
            EventBus.getDefault().post(new EventData(EVENT_SCROLL_TO_TOP_NEW_PROFILE, ""));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == CAPTURE_MEDIA || requestCode == REQ_CODE_UPLOAD_DATA_ACTIVITY_RESULTS) && resultCode == RESULT_OK) {
            postMedia(BundleUtils.getIntentExtra(data, FILE_PATH_ARG, ""), BundleUtils.getIntentExtra(data, FILE_CAPTION_ARG, ""));
        }

        @SuppressLint("RestrictedApi")
        List<Fragment> childFragmentList = getChildFragmentManager().getFragments();
        if (childFragmentList != null) {
            for (Fragment childFrag : childFragmentList) {
                if (childFrag != null && childFrag instanceof SocialFragment && childFrag.isVisible()) {
                    childFrag.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    private void postMedia(String path, String postCaption) {
        Toast.makeText(getActivity(), "Media captured.", Toast.LENGTH_SHORT).show();
        if (path.contains("mp4") || path.contains("mkv")) {
            fromData = "video";
        }

        postMediaType = new String[2];
        Uri myUri = Uri.parse(path);
        fileLinkedHashMap = new LinkedHashMap<>();
        fileLinkedHashMap.clear();
        if (fromData.equalsIgnoreCase("record")) {
            fileLinkedHashMap.put(MEDIA_0, new File(path));
            postMediaType[0] = "4";
            callPostMediaApi(POST_TYPE_RECORD, postCaption);
        } else if (fromData.equalsIgnoreCase("music")) {
            fileLinkedHashMap.put(MEDIA_0, FileUtils.getFile(context, myUri));
            postMediaType[0] = "3";
            callPostMediaApi(POST_TYPE_MUSIC, postCaption);
        } else if (fromData.equalsIgnoreCase("video")) {

            filePath = myUri.toString();
            File cacheFile = new File(Environment.getExternalStorageDirectory(),
                    "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4");

            Compressor com;

            String[] cmd = {"-i", filePath,
                    "-c:v",
                    "libx264",
                    "-crf",
                    "23",
                    "-preset",
                    "ultrafast",
                    "-tune",
                    "fastdecode",
                    "-strict",
                    "-3",
                    "-c:a",
                    "aac",
                    "-b:a",
                    "128k",
                    "-movflags",
                    "+faststart",
                    "-vf",
                    "scale=-2:720",
                    cacheFile.getPath()};

            builder = new NotificationCompat.Builder(getActivity(), "131");
            builder.setAutoCancel(false);


            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(context, Uri.fromFile(new File(filePath)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            i = (int) Long.parseLong(time) / 1000;


            i = i + (i / 3);
            retriever.release();
            updateNotification();
            com = new Compressor(getActivity());
            com.loadBinary(new InitListener() {
                @Override
                public void onLoadSuccess() {
                    com.execCommand(cmd, new CompressListener() {
                        @Override
                        public void onExecSuccess(String message) {
                            fileLinkedHashMap.put(MEDIA_0, new File(cacheFile.getPath()));
                            postMediaType[0] = "1";
//                            Toast.makeText(context, getString(R.string.txt_uploading_video), Toast.LENGTH_SHORT).show();
                            callPostMediaApi(StaticConstant.POST_TYPE_VIDEO, postCaption);
                            Log.i("success", message);
                            isCompressed = true;
                            MyApplication.getInstance().showNoti("Uploading video", "Uploading", R.drawable.upload, 121, builder);
                        }

                        @Override
                        public void onExecFail(String reason) {
//                            fileLinkedHashMap.put(MEDIA_0, new File(filePath));
//                            postMediaType[0] = "1";
//                            Toast.makeText(context, getString(R.string.txt_uploading_video), Toast.LENGTH_SHORT).show();
//                            callPostMediaApi(StaticConstant.POST_TYPE_VIDEO, postCaption);
                            Log.i("fail", reason);
                        }

                        @Override
                        public void onExecProgress(String message) {
                            Log.i("progress", message);
                        }
                    });
                }

                @Override
                public void onLoadFail(String reason) {
//                    fileLinkedHashMap.put(MEDIA_0, new File(filePath));
//                    postMediaType[0] = "1";
//                    Toast.makeText(context, getString(R.string.txt_uploading_video), Toast.LENGTH_SHORT).show();
//                    callPostMediaApi(StaticConstant.POST_TYPE_VIDEO, postCaption);
                    Log.i("fail", reason);
                }
            });

//            new VideoCompressor(postCaption).execute();
        } else {
            fileLinkedHashMap.put(MEDIA_0, new File(path));
            postMediaType[0] = "2";
            callPostMediaApi(POST_TYPE_IMAGE, postCaption);
        }
    }

    void updateNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyApplication.getInstance().showNoti("Uploading video", getTime(i) + " left for compressing", R.drawable.compressing, 121, builder);
                if (i > 0 && !isCompressed) {
                    i--;
                    updateNotification();
                }
            }
        }, 1000);

    }

    private String getTime(int data) {
        if (data < 59)
            return "about " + data + " seconds";
        else {
            long minutes = TimeUnit.SECONDS.toMinutes(data);
            return "about " + minutes + " minutes";
        }
    }

    void callPostMediaApi(String postType, String postCaption) {

        if (fileLinkedHashMap.get(MEDIA_0) == null) {
            DialogBox.showDialog(context, context.getString(R.string.app_name), "Invalid file format", DialogBox.DIALOG_FAILURE, null);
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("postedby", preferences.getUserId());
        map.put("posttype", postType);
        map.put("posttitle", postCaption);

        if (Utils.isInternetOn(context)) {
            Call<PostMediaBean> postMediaCall = mService.getFetcherService(context).PostMedia(new RequestBodyConveter().converRequestBodyFromMap(map), new RequestBodyConveter().converRequestBodyFromMapImage(fileLinkedHashMap, postMediaType), null);

//            Utils.showToast(context, getString(R.string.txt_uploading_media));

            postMediaCall.enqueue(new Callback<PostMediaBean>()
            {
                @Override
                public void onResponse(Call<PostMediaBean> call, Response<PostMediaBean> response) {
                    if (builder != null) {
                        builder.setAutoCancel(true);
                        MyApplication.getInstance().showNoti("Uploading video", "Upload Done", R.drawable.compressing, 121, builder);
                    }

                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (isAttatched) {
                                Utils.showToast(context, response.body().getMsg());
                                Toast.makeText(MyApplication.getInstance(), getString(R.string.txt_refreshing_media), Toast.LENGTH_SHORT).show();
                                fragment = (Fragment) pageAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                                if (fragment != null && fragment instanceof DashboardFragment)
                                {
                                    ((DashboardFragment) fragment).loadDashboardData(false);
                                }
                            }
                        } else {
                            Utils.showToast(MyApplication.getInstance(), response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostMediaBean> call, Throwable t)
                {
                    MyApplication.getInstance().cancleNotification(121);
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    public Fragment getFragmentFromViewpager(@SuppressWarnings("SameParameterValue") int position)
    {
        try {
            return ((Fragment) (pageAdapter.instantiateItem(viewPager, position)));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        isAttatched = false;
    }

    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
        File cacheFile = new File(Environment.getExternalStorageDirectory(),
                "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4");
        private String postCaption;

        VideoCompressor(String postCaption) {
            this.postCaption = postCaption;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Boolean doInBackground(Void... voids) {


            return MediaController.getInstance().convertVideo(filePath, cacheFile.getPath());
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                fileLinkedHashMap.put(MEDIA_0, cacheFile);
                postMediaType[0] = "1";
                Toast.makeText(context, getString(R.string.txt_uploading_video), Toast.LENGTH_SHORT).show();
                callPostMediaApi(StaticConstant.POST_TYPE_VIDEO, postCaption);
            }
        }
    }

    public class MyPageAdapter extends FragmentStatePagerAdapter {
        private static final int CAPTURE_MEDIA = 368;
        MotionEvent event;

        MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount()
        {
            if (!TextUtils.isEmpty(preferences.getUserToken()) && !TextUtils.isEmpty(preferences.getUserId()))
                return 3;
            else
                return 1;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (TextUtils.isEmpty(preferences.getUserToken()) && TextUtils.isEmpty(preferences.getUserId()))
                        fragment = new DashboardFragment();
                    else
                        fragment = new SocialFragment();
                    return fragment;

                case 1:
                    fragment = new DashboardFragment();
                    return fragment;

                case 2:
                    fragment = new SandriosCamera(getActivity(), CAPTURE_MEDIA).setShowPicker(true).setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                            .enableImageCropping(true)
                            .launchCamera(
                                    (status, path, caption) -> {
                                        if (status == RESULT_OK) {
                                            viewPager.setCurrentItem(1);
                                            postMedia(path, caption);
                                        }
                                    });

                    return fragment;
                default:
                    fragment = new DashboardFragment();
                    return fragment;
            }
        }
    }
}