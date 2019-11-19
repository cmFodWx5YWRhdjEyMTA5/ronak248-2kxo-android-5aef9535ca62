package com.screamxo.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.apimodule.ApiBase.ApiBean.ChatBean;
import com.example.apimodule.ApiBase.ApiBean.Message;
import com.example.apimodule.ApiBase.ApiBean.Notification;
import com.example.apimodule.ApiBase.ApiBean.NotificationBean;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Adapter.NetWorkAdapter;
import com.screamxo.Adapter.PleaserAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;

import static com.screamxo.FireBasePush.MyFirebaseMessagingService.ACTION_CHAT_MESSAGE;
import static com.screamxo.FireBasePush.MyFirebaseMessagingService.ACTION_NETWORK_BROADCAST;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP_NEW;

/**
 * Created by Shubham Agarwal on 13/01/17.
 */

@SuppressLint("ValidFragment")
public class PleaserFragment extends Fragment {
    private static final String TAG = "PleaserFragment";
    public static Boolean isPleasureOpened = false;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.lny_no_data)
    LinearLayout lnyNoData;
    PleaserAdapter pleaserAdapter;
    NetWorkAdapter netWorkAdapter;
    Call<ChatBean> friendBeanCall;
    Call<NotificationBean> notificationBeanCall;
    ArrayList<Notification> notificationArrayList;

    ArrayList<Message> messageArrayList;
    String type;
    int pageCounter = 0, totalNotification = 0, totalChatCount = 0;
    int notificationPageCounter = 0;
    String LIMIT = StaticConstant.LIMIT;
    Preferences preferences;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private FetchrServiceBase mService;
    private boolean shopBoolean = false, chatBoolean = false;
    private BroadcastReceiver networkBroadcastReciever;
    private BroadcastReceiver chatBroadCastReciever;
    private boolean isVisibleToUser;

    @SuppressLint("ValidFragment")
    public PleaserFragment(String type) {
//        type: 0 = Pleaser 1 = Business 2 = Network
        this.type = type;
    }

    PleaserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_only, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setAdapter();
        initListner();
//        type: 0 = Pleaser 1 = Business 2 = Network
    }

    private void init() {
        messageArrayList = new ArrayList<>();
        notificationArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        mService = new FetchrServiceBase();
        preferences = new Preferences(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initListner() {
        StaticConstant.CHAT_SCREEN = false;
        networkBroadcastReciever = new BroadcastReceiver() {
            private static final String TAG = "networkBroadcastReciever";

            @SuppressLint("LongLogTag")
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (type.equals("2")) {
                        notificationPageCounter = 0;
                        callGetNetworkNotification();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        chatBroadCastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!type.equals("2")) {
                    pageCounter = 0;
                    callGetChatFrdList();
                }
            }
        };

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.addOnScrollListener(new HidingScrollListener() {
                    @Override
                    public void onHide() {
                        if (context.getClass() == DrawerMainActivity.class) {
                            ((DrawerMainActivity) context).hideViews();
                        }
                    }

                    @Override
                    public void onShow() {
                        if (context.getClass() == DrawerMainActivity.class) {
                            ((DrawerMainActivity) context).showViews();
                        }
                    }
                });

                switch (type) {
                    case "2":
                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (netWorkAdapter.getItemCount() - 1)) {
                            if (shopBoolean && notificationArrayList.size() < totalNotification) {
                                shopBoolean = false;
                                callGetNetworkNotification();
                            }
                        }
                        break;
                    case "1":
                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (pleaserAdapter.getItemCount() - 1)) {
                            if (chatBoolean && messageArrayList.size() < totalChatCount) {
                                chatBoolean = false;
                                callGetChatFrdList();
                            }
                        }
                        break;

                    case "0":
                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (pleaserAdapter.getItemCount() - 1)) {
                            if (chatBoolean && messageArrayList.size() < totalChatCount) {
                                chatBoolean = false;
                                callGetChatFrdList();
                            }
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context = context;
    }

    private void setAdapter() {
        if (messageArrayList.size() == 0) {
            lnyNoData.setVisibility(View.VISIBLE);
        } else {
            lnyNoData.setVisibility(View.GONE);
        }

        if (pleaserAdapter == null) {
            pleaserAdapter = new PleaserAdapter(context, messageArrayList);
            recyclerView.setAdapter(pleaserAdapter);
            pleaserAdapter.type = type;
        } else {
            pleaserAdapter.type = type;
            pleaserAdapter.notifyDataSetChanged();
        }
    }

    private void setAdapterNetwork() {
        if (notificationArrayList.size() == 0) {
            lnyNoData.setVisibility(View.VISIBLE);
        } else {
            lnyNoData.setVisibility(View.GONE);
        }

        if (netWorkAdapter == null) {
            netWorkAdapter = new NetWorkAdapter(context, notificationArrayList);
            recyclerView.setAdapter(netWorkAdapter);
            netWorkAdapter.type = type;
        } else {
            netWorkAdapter.type = type;
            netWorkAdapter.notifyDataSetChanged();
        }
    }

    private void callGetChatFrdList() {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("offset", "" + pageCounter);
            map.put("limit", LIMIT);
            if (!(new Preferences(context).getUserId().isEmpty())) {
                map.put("uid", new Preferences(context).getUserId());
            }

            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }

            switch (type) {
                case "0":
                    friendBeanCall = mService.getFetcherService(context).getFrdChatHeader(map);
                    break;
                case "1":
                    friendBeanCall = mService.getFetcherService(context).getByerSellerChatHeader(map);
                    break;
            }

            friendBeanCall.enqueue(new Callback<ChatBean>() {
                @Override
                public void onResponse(Call<ChatBean> call, Response<ChatBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        switch (response.body().getStatus()) {
                            case StaticConstant.STATUS_1:
                                totalChatCount = response.body().getResult().getCount();
                                if (pageCounter == 0) {
                                    messageArrayList.clear();
                                }
                                pageCounter++;
                                chatBoolean = true;
                                messageArrayList.addAll(response.body().getResult().getMessages());
                                setAdapter();
                                break;
                            case StaticConstant.STATUS_0:
                                Utils.unAuthentication(context);
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatBean> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void callGetNetworkNotification() {
        if (Utils.isInternetOn(context)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("offset", "" + notificationPageCounter);
            map.put("limit", "25");
            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }

            notificationBeanCall = mService.getFetcherService(context).getNetworkNotification(map);
            notificationBeanCall.enqueue(new Callback<NotificationBean>() {
                @Override
                public void onResponse(Call<NotificationBean> call, Response<NotificationBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    shopBoolean = true;
                    if (response.code() == StaticConstant.RESULT_OK) {
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            if (notificationPageCounter == 0) {
                                notificationArrayList.clear();
                            }
                            totalNotification = response.body().getResult().getTotalcount();
                            notificationArrayList.addAll(response.body().getResult().getNotifications());
                            notificationPageCounter = notificationArrayList.size();

                            setAdapterNetwork();
                        } else {
                            Utils.showToast(context, response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationBean> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                    shopBoolean = true;
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEventMainThread(EventData event) {
        switch (event.getCode()) {
            case EVENT_SCROLL_TO_TOP_NEW:
                FloatingMenuButton floatingMenuButton = ((DrawerMainActivity) context).floatingButton;
                if (floatingMenuButton.getBackground().getConstantState() != null
                        && floatingMenuButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {

                    recyclerView.scrollToPosition(0);
                    netWorkAdapter.notifyDataSetChanged();
                    pleaserAdapter.notifyDataSetChanged();

                    floatingMenuButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                    floatingMenuButton.closeMenu();
                    return;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isPleasureOpened = true;
        try {
            EventBus.getDefault().register(this);
            context.registerReceiver(networkBroadcastReciever, new IntentFilter(ACTION_NETWORK_BROADCAST));
            context.registerReceiver(chatBroadCastReciever, new IntentFilter(ACTION_CHAT_MESSAGE));
            if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
                switch (type) {
                    case "0":
                        pageCounter = 0;
                        callGetChatFrdList();
                        break;
                    case "1":
                        pageCounter = 0;
                        callGetChatFrdList();
                        break;
                    case "2":
                        notificationPageCounter = 0;
                        callGetNetworkNotification();
                        break;
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        isPleasureOpened = false;
        EventBus.getDefault().unregister(this);
        context.unregisterReceiver(networkBroadcastReciever);
        context.unregisterReceiver(chatBroadCastReciever);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StaticConstant.CHAT_SCREEN = false;
    }
}
