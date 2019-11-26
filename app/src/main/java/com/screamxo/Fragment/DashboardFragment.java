package com.screamxo.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.DashBoardBean;
import com.example.apimodule.ApiBase.ApiBean.DashBoardResult;
import com.example.apimodule.ApiBase.ApiBean.Posts.Posts;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.screamxo.Activity.DrawerMainActivity;
import com.screamxo.Adapter.DashboardAdapter;
import com.screamxo.Interface.CommonMethod;
import com.screamxo.Others.CustomLinearLayoutManager;
import com.screamxo.Others.MyPopUpWindow;
import com.screamxo.R;
import com.screamxo.Utils.EventData;
import com.screamxo.Utils.HidingScrollListener;
import com.screamxo.Utils.Preferences;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;
import com.screamxo.Utils.ViewUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP;
import static com.screamxo.Utils.EventData.EVENT_SCROLL_TO_TOP_tranding;
import static com.screamxo.Utils.EventData.EVENT_TOGGLE_SEARCH;
import static com.screamxo.Utils.StaticConstant.LIMIT;
import static com.screamxo.Utils.StaticConstant.RESULT_OK;

public class DashboardFragment extends Fragment implements CommonMethod
{

    public static final int ID = R.id.Dashboard_ID;
    public Boolean streamCall = false;
    @BindView(R.id.recycler_view)
    public RecyclerView mrecyclerView;
    @BindView(R.id.rel_landscape)
    public LinearLayout rellandscape;
    @BindView(R.id.progreessbar)
    public ProgressBar progreessbar;
    String TAG = DashboardFragment.class.getSimpleName();
    @BindView(R.id.toolbar_edt_search)
    EditText toolbar_edt_search;
    @BindView(R.id.rl_list)
    RelativeLayout rl_list;
    @BindView(R.id.rl_search_main)
    RelativeLayout rl_search_main;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    @BindView(R.id.txtCancel)
    TextView txtCancel;
    @BindView(R.id.iv_fire)
    ImageView iv_fire;

    Call<DashBoardBean> DashBoardApiCall;
    Call<Posts> StreamApiCall;
    Call<DashBoardBean> StreamApiCallMore;

    int totalCount = 0;
    int indexOfStreamSelect;
    private Context context;
    private DashboardAdapter dashboardAdapter;
    private FetchrServiceBase mService;
    private String uId;
    private int pageCounter = 1;
    private DashBoardResult dashBoardResult;
    private boolean isVisibleToUser;

    public DashboardFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        ButterKnife.bind(this, view);
        init();

        if (context != null && context.getClass() == DrawerMainActivity.class && ((DrawerMainActivity) context).getApidata() != null) {
            dashBoardResult = ((DrawerMainActivity) context).getApidata();
            setAdapter();
        }
        Utils.logFirebaseToken();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEventMainThread(EventData event)
    {
        switch (event.getCode())
        {
            case EVENT_SCROLL_TO_TOP:
                try {
//                    if (getUserVisibleHint()) {
                    FloatingMenuButton floatingMenuButton = ((DrawerMainActivity) context).floatingButton;
//                        if (!floatingMenuButton.isMenuOpen()) {
                    if (floatingMenuButton.getBackground().getConstantState() != null
                            && floatingMenuButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.ic_float_menu_up).getConstantState())) {
                        mrecyclerView.scrollToPosition(0);
                        floatingMenuButton.setBackground(getResources().getDrawable(R.mipmap.menu));

                        if (!floatingMenuButton.isMenuOpen()) {
                            floatingMenuButton.closeMenu();
                            floatingMenuButton.setBackground(getResources().getDrawable(R.mipmap.menu));

                        }
                        floatingMenuButton.setBackground(getResources().getDrawable(R.mipmap.menu));
                        return;
                    }
                }
                catch (Exception ignored)
                {

                }

                break;
            case EVENT_TOGGLE_SEARCH:
                if (ViewUtils.isVisible(rl_search_main)) {
                    hideSearchToolbar();
                } else {
                    showSearchBar();
                }
                break;
            case EVENT_SCROLL_TO_TOP_tranding:
                callMoreStreamApi(true, StaticConstant.from_posi);
                break;

            default:
                //   callMoreStreamApi(true, event.getData());
                break;
        }
    }

    public void showSearchBar()
    {
        rl_search.setVisibility(View.GONE);
        rl_search_main.setVisibility(View.VISIBLE);
        iv_fire.setVisibility(View.VISIBLE);
        toolbar_edt_search.requestFocus();
        Utils.showKeyboard(context, toolbar_edt_search);
    }

    public void hideSearchToolbar() {
        toolbar_edt_search.setText("");
        rl_search.setVisibility(View.GONE);
        rl_search_main.setVisibility(View.GONE);
        iv_fire.setVisibility(View.GONE);
        Utils.hideKeyboard(context);
    }

    private void init()
    {
        mService = new FetchrServiceBase();
        Preferences preferences = new Preferences(context);
        dashBoardResult = new DashBoardResult();

        mrecyclerView.setLayoutManager(new CustomLinearLayoutManager(context));

        if (!preferences.getUserId().isEmpty() && !preferences.getStripeCustomerId().isEmpty()) {
            uId = preferences.getUserId();
        } else {
            uId = null;
        }

        mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                if (((CustomLinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() == (dashboardAdapter.getItemCount() - 1)) {
                    if (streamCall && dashBoardResult.getStreampost().size() < totalCount && dashboardAdapter.getItemCount() != 3) {
                        streamCall = false;
                        String userfilterid = "2"; // 2 is public
                        if (dashboardAdapter != null && dashboardAdapter.commentFilterPopupWindow != null) {
                            MyPopUpWindow.CheckboxResponse checkboxResponse = dashboardAdapter.commentFilterPopupWindow.strfilter.get(MyPopUpWindow.checkboxposition);
                            userfilterid = checkboxResponse.getId();
                        }
                        callMoreStreamApi(false, userfilterid);
                    }
                }

                mrecyclerView.addOnScrollListener(new HidingScrollListener() {
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
            }
        });

        toolbar_edt_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (toolbar_edt_search.getText().toString().length() > 0) {
                    callDashBoardApi(toolbar_edt_search.getText().toString(), true, false, false);
                }
                StaticConstant.SEARCHSTRING = toolbar_edt_search.getText().toString();
            }
        });

        rl_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                rl_search_main.setVisibility(View.VISIBLE);
                toolbar_edt_search.requestFocus();
                rl_search.setVisibility(View.GONE);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_search.setVisibility(View.VISIBLE);
                rl_search_main.setVisibility(View.GONE);
                Utils.hideKeyboard(context);
            }
        });

        iv_fire.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                loadDashboardData(true);
                Utils.hideKeyboard(context);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void commonMethod(String type, File... files) {
        if (type.equals("scroll")) {
            mrecyclerView.scrollToPosition(0);
        } else {
            indexOfStreamSelect = Integer.parseInt(type);
        }
    }

    public void callFromActivity(int visibility)
    {
        if (dashboardAdapter != null)
        {
            dashboardAdapter.hideToprecyclerview(visibility);
        }
    }

    void callDashBoardApi(String query, boolean isSearch, boolean isFiring, boolean isFirst)
    {

        Map<String, String> map = new HashMap<>();
        if (isFirst)
        {
            map.put("userfiltertype", "2");
        }
        if (isSearch)
        {
            pageCounter = 1;
            map.put("searchstring", query);
            map.put("userfiltertype", "2");
        }
        if (isFiring) {
            map.put("firing_mode", "1");
        }
        if (!TextUtils.isEmpty(uId)) {
            map.put("myid", uId);
            map.put("uid", uId);
        }
        map.put("limit", LIMIT);
        map.put("offset", "" + 0);

        if (Utils.isInternetOn(context))
        {
            progreessbar.setVisibility(View.VISIBLE);
            DashBoardApiCall = mService.getFetcherService(context).getDashboardEventsData(map);
            DashBoardApiCall.enqueue(new Callback<DashBoardBean>()
            {
                @Override
                public void onResponse(Call<DashBoardBean> call, Response<DashBoardBean> response) {
                    Log.d("Responce", "Success");
                    streamCall = true;
                    if (response.code() == RESULT_OK) {
                        if (response.body() != null) {
                            progreessbar.setVisibility(View.GONE);
                            dashBoardResult = response.body().getResult();
                            setAdapter();
                            pageCounter++;
                        }
                    } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                        Utils.unAuthentication(context);
                    }
                }

                @Override
                public void onFailure(Call<DashBoardBean> call, Throwable t)
                {
                    Log.d("Responce", "false");
                    streamCall = true;
                    progreessbar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    /* userFilterType  // 2 is public */
    public void callMoreStreamApi(boolean isSearched, String userFilterType)
    {

        if (isSearched)
        {
            pageCounter = 1;
        }

        Map<String, String> map = new HashMap<>();
        map.put("uid", uId);
        map.put("limit", LIMIT);
        map.put("offset", "" + 0);
        map.put("myid", uId);
        map.put("userfiltertype", userFilterType); // public private friend etc

        if (StaticConstant.from_posi.equals("1")) {
            map.put("friends_mode", "1");
        }

        if (toolbar_edt_search.getText().toString().length() > 0) {
            map.put("searchstring", toolbar_edt_search.getText().toString());
        } else {
            map.put("searchstring", "");
        }

        if (Utils.isInternetOn(context)) {
            progreessbar.setVisibility(View.VISIBLE);

//            commented and changed by amit, because stream filter was not working
//            if (StaticConstant.from_posi.equalsIgnoreCase("3")) {

            if (isSearched) {
                StreamApiCallMore = mService.getFetcherService(context).getDashboardEventsData(map);
                StreamApiCallMore.enqueue(new retrofit2.Callback<DashBoardBean>() {
                    @Override
                    public void onResponse(Call<DashBoardBean> call, Response<DashBoardBean> response) {
                        if (response.code() == StaticConstant.RESULT_OK && response.body() != null) {
                            progreessbar.setVisibility(View.GONE);
                            dashBoardResult.getStreampost().clear();
                            dashBoardResult.getStreampost().addAll(response.body().getResult().getStreampost());
                            setAdapter();
                            pageCounter++;
                        }
                    }

                    @Override
                    public void onFailure(Call<DashBoardBean> call, Throwable t) {
                        progreessbar.setVisibility(View.GONE);
                        streamCall = true;
                    }
                });
            } else {
                StreamApiCall = mService.getFetcherService(context).StreamMore(map);
                StreamApiCall.enqueue(new Callback<Posts>() {
                    @Override
                    public void onResponse(Call<Posts> call, Response<Posts> response) {
                        streamCall = true;
                        progreessbar.setVisibility(View.GONE);
                        if (response.code() == RESULT_OK && response.body() != null) {
                            dashBoardResult.getStreampost().addAll(response.body().getResult().getPosts());
                            setAdapter();
                            pageCounter++;
                        } else if (response.code() == StaticConstant.UNAUTHORIZE) {
                            Utils.unAuthentication(context);
                        }
                    }

                    @Override
                    public void onFailure(Call<Posts> call, Throwable t) {
                        progreessbar.setVisibility(View.GONE);
                        streamCall = true;
                    }
                });
            }
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setAdapter()
    {
        dashboardAdapter = new DashboardAdapter(context, this, dashBoardResult, DashboardFragment.this);
        mrecyclerView.setAdapter(dashboardAdapter);
    }

    public void loadDashboardData(boolean isFiring)
    {
        pageCounter = 1;
        callDashBoardApi("", false, isFiring, true);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if (dashboardAdapter != null)
        {
            dashboardAdapter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (dashboardAdapter != null) {
            dashboardAdapter.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        if (dashboardAdapter != null && !DrawerMainActivity.refresh) {

            dashboardAdapter.f();
            dashboardAdapter.onResume();
        } else {
            DrawerMainActivity.refresh = false;
            loadFirstDashboardData(false);

        }

    }

    public void loadFirstDashboardData(boolean isFiring) {
        pageCounter = 1;
        callDashBoardApi("", false, isFiring, true);
        Log.e("HOgya", "asasas");
    }

    @Override
    public void onDestroy() {
        if (dashboardAdapter != null) {
            dashboardAdapter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dashboardAdapter != null) {
            dashboardAdapter.onConfigurationChanged(newConfig);
        }
    }
}