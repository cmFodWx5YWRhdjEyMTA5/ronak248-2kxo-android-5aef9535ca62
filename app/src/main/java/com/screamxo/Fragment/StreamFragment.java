package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.apimodule.ApiBase.ApiBean.ProfileMedia.Posts;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Activity.ProfileActivity;
import com.screamxo.Adapter.ProfileStreamAdapter;
import com.screamxo.R;
import com.screamxo.Utils.EventData;
import com.screamxo.Utils.HidingScrollListener;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;

import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP_NEW_PROFILE;

/**
 * Created by Shubham Agarwal on 07/11/16.
 * <p>
 * Stream fragment used in profile page...
 */
public class StreamFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mrecyclerView;
    @BindView(R.id.bottom_tag_layout)
    RelativeLayout relativeWithBottomTag;
    @BindView(R.id.view_player)
    LinearLayout viewPlayer;
    @BindView(R.id.stream_no_data_layout)
    LinearLayout stream_no_data_layout;

    private Context context;
    private FetchrServiceBase mService;
    private ProfileStreamAdapter profileStreamAdapter;
    private Call<Posts> UserApiCall;
    private String uId;
    private ArrayList<Object> StreamList;
    private int pageCounter = 1;
    private boolean streamCall = false;
    private int totalCount = 0;
    private Preferences preferences;

    public StreamFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        ButterKnife.bind(this, view);
        initView();
        initControValue();

        if (context.getClass().getSimpleName().equals(ProfileActivity.class.getSimpleName())) {
            uId = ((ProfileFragment) ((ProfileActivity) context).getSupportFragmentManager().getFragments().get(0)).getUid();
        } else {
            uId = preferences.getUserId();
        }
//        uId = profileFragment.getUid();
        callUserInfoApi();
        return view;
    }

    private void initView() {
    }

    private static final String TAG = "StreamFragment";

    @SuppressWarnings("unused")
    @Subscribe
    public void onEventMainThread(EventData event) {
        switch (event.getCode()) {
            case EVENT_SCROLL_TO_TOP_NEW_PROFILE:
                if (getUserVisibleHint()) {
                    FloatingMenuButton floatingMenuButton = ((DrawerMainActivity) context).floatingButton;
                    if (!floatingMenuButton.isMenuOpen()) {
                        if (floatingMenuButton.getBackground().getConstantState() != null
                                && floatingMenuButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {
                            mrecyclerView.scrollToPosition(0);
                            floatingMenuButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                            floatingMenuButton.closeMenu();
                            return;
                        }
                    }
                }

                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initControValue() {

        mService = new FetchrServiceBase();
        StreamList = new ArrayList<>();
        preferences = new Preferences(context);
        relativeWithBottomTag.setVisibility(View.GONE);
        stream_no_data_layout.setVisibility(View.GONE);
        viewPlayer.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mrecyclerView.setLayoutManager(linearLayoutManager);


        mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mrecyclerView.addOnScrollListener(new HidingScrollListener() {
                    @Override
                    public void onHide() {
                        if (context.getClass() == DrawerMainActivity.class) {
                            ((DrawerMainActivity) context).hideViews();
                        }

                        if (context.getClass() == ProfileActivity.class) {
                            ((ProfileActivity) context).hideViews();
                        }
                    }

                    @Override
                    public void onShow() {
                        if (context.getClass() == DrawerMainActivity.class) {
                            ((DrawerMainActivity) context).showViews();
                        }

                        if (context.getClass() == ProfileActivity.class) {
                            ((ProfileActivity) context).showViews();
                        }
                    }
                });

                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (profileStreamAdapter.getItemCount() - 1)) {
                    if (streamCall && StreamList.size() < totalCount) {
                        streamCall = false;
                        callUserInfoApi();
                    }
                }
            }
        });
    }

    private void callUserInfoApi() {
        Map<String, String> map = new HashMap<>();

        // uid now static pls chnage when complete
        map.put("myid", uId);
        map.put("uid", uId);
        map.put("offset", "" + 0);
        map.put("limit", StaticConstant.LIMIT);
        map.put("posttype", "0");

        if (Utils.isInternetOn(context)) {

            UserApiCall = mService.getFetcherService(context).UserDetailData(map);

            UserApiCall.enqueue(new Callback<Posts>() {
                @Override
                public void onResponse(Call<Posts> call, Response<Posts> response) {
                    if (response.code() == StaticConstant.RESULT_OK && response.body() != null && response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                        streamCall = true;
                        if (pageCounter == 1) {
                            StreamList.clear();
                        }
                        pageCounter++;
                        totalCount = response.body().getResult().getCount();
                        StreamList.addAll(response.body().getResult().getPosts());
                        if (StreamList.size() == 0) {
                            stream_no_data_layout.setVisibility(View.VISIBLE);
                            mrecyclerView.setVisibility(View.GONE);
                        } else {
                            mrecyclerView.setVisibility(View.VISIBLE);
                            stream_no_data_layout.setVisibility(View.GONE);
                            setAdapter();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Posts> call, Throwable t) {
                    streamCall = true;
                }
            });
        } else {
            streamCall = true;
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setAdapter() {
        if (profileStreamAdapter == null) {
            profileStreamAdapter = new ProfileStreamAdapter(context, StreamList);
            mrecyclerView.setAdapter(profileStreamAdapter);
        } else {
            profileStreamAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (UserApiCall != null) {
            UserApiCall.cancel();
        }
    }
}
