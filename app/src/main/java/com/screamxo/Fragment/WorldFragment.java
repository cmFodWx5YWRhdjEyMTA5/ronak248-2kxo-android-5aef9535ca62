package com.screamxo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apimodule.ApiBase.ApiBean.DashBoardBean;
import com.example.apimodule.ApiBase.ApiBean.Mediapost;
import com.example.apimodule.ApiBase.ApiBean.Streampost;
import com.example.apimodule.ApiBase.FetchrServiceBase;
import com.example.apimodule.ApiBase.Preferences;
import com.screamxo.Adapter.WorldAdapter;
import com.screamxo.R;
import com.screamxo.Utils.StaticConstant;
import com.screamxo.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.screamxo.Utils.StaticConstant.LIMIT;
import static com.screamxo.Utils.StaticConstant.RESULT_OK;

/**
 * Created by Shubham Agarwal on 10/02/17.
 * No usage of this class.. it's has been closed as per the instruction of Kamlesh sir
 */


@Deprecated
public class WorldFragment extends Fragment {


    @BindView(R.id.progreessbar)
    ProgressBar progreessbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.img_toolbar_left_icon)
    ImageView imgToolbarLeftIcon;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.img_toolbar_right_icon)
    ImageView imgToolbarRightIcon;
    Preferences preferences;
    int offset = 1;
    ArrayList<Streampost> streampostArrayList;
    ArrayList<Mediapost> mediapostArrayList;
    @BindView(R.id.toolbar_edt_search)
    EditText toolbar_edt_search;
    private FetchrServiceBase mService;
    private Context context;
    private Call<DashBoardBean> friendBeanCall;
    private WorldAdapter worldAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean streamCall = false;
    private Call<DashBoardBean> StreamApiCall;
    private int totalCount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        initControlValue();

        return view;
    }

    private void init() {
        preferences = new Preferences(context);
        streampostArrayList = new ArrayList<>();
        mediapostArrayList = new ArrayList<>();
        mService = new FetchrServiceBase();
        toolbar_edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (toolbar_edt_search.getText().toString().length() > 0) {
                    callGetWorlDataApi(toolbar_edt_search.getText().toString(), 1);
                }

                StaticConstant.WORLDSEARCH = toolbar_edt_search.getText().toString();
            }
        });


        txtToolbarTitle.setVisibility(View.GONE);
        toolbar_edt_search.setVisibility(View.VISIBLE);
        imgToolbarRightIcon.setImageDrawable(getResources().getDrawable(R.drawable.ico_user_new));
        imgToolbarRightIcon.setVisibility(View.VISIBLE);
        toolbar_edt_search.setHint(R.string.txt_world);
        imgToolbarLeftIcon.setVisibility(View.GONE);
        txtToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initControlValue() {
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (worldAdapter.getItemCount() - 1) && streamCall && streampostArrayList.size() < totalCount) {
                    streamCall = false;
                    callMoreStreamApi();
                }
            }
        });
    }

    private void callGetWorlDataApi(String searchedData, int postion) {
        offset = 1;
        if (Utils.isInternetOn(context)) {

            Map<String, String> map = new HashMap<>();
            map.put("offset", "" + offset);
            map.put("limit", LIMIT);
            map.put("uid", preferences.getUserId());
            map.put("myid", preferences.getUserId());
            map.put("posttype", "1");
            if (postion == 1) {
                map.put("searchstring", searchedData);
            }

            if (friendBeanCall != null) {
                friendBeanCall.cancel();
            }

            friendBeanCall = mService.getFetcherService(context).GetWorldData(map);

            friendBeanCall.enqueue(new Callback<DashBoardBean>() {
                @Override
                public void onResponse(Call<DashBoardBean> call, Response<DashBoardBean> response) {
                    progreessbar.setVisibility(View.GONE);
                    if (response.code() == StaticConstant.RESULT_OK) {
                        Utils.showToast(context, response.body().getMsg());
                        if (response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            streamCall = true;
                            offset++;
                            totalCount = response.body().getResult().getStreampostcount();
                            streampostArrayList.clear();
                            mediapostArrayList.clear();
                            streampostArrayList.addAll(response.body().getResult().getStreampost());
                            //         if (postion==0)
                            mediapostArrayList.addAll(response.body().getResult().getMediapost());
                            setAdapter();

                        } else if (response.body().getStatus().equals(StaticConstant.STATUS_0)) {
                            Utils.unAuthentication(context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<DashBoardBean> call, Throwable t) {
                    progreessbar.setVisibility(View.GONE);
                    if (!call.isCanceled()) {
                        Utils.showToast(context, t.toString());
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    private void setAdapter() {

        if (worldAdapter == null) {
            worldAdapter = new WorldAdapter(context, mediapostArrayList, streampostArrayList, totalCount);
            recyclerView.setAdapter(worldAdapter);
        } else if (StaticConstant.WORLDSEARCH.length() > 0) {
            worldAdapter = new WorldAdapter(context, mediapostArrayList, streampostArrayList, totalCount);
            recyclerView.setAdapter(worldAdapter);
        } else {
            worldAdapter.notifyDataSetChanged();
        }


    }


    void callMoreStreamApi() {
        Map<String, String> map = new HashMap<>();

        map.put("uid", preferences.getUserId());
        map.put("limit", LIMIT);
        map.put("offset", "" + offset);

        if (StaticConstant.SEARCHSTRING.length() > 0) {
            map.put("searchstring", "" + StaticConstant.SEARCHSTRING);
        }

        if (Utils.isInternetOn(context)) {
            StreamApiCall = mService.getFetcherService(context).GetMoreStream(map);

            StreamApiCall.enqueue(new Callback<DashBoardBean>() {
                @Override
                public void onResponse(Call<DashBoardBean> call, Response<DashBoardBean> response) {
                    streamCall = true;
                    if (response.code() == RESULT_OK) {
                        if (response.body() != null && response.body().getStatus().equals(StaticConstant.STATUS_1)) {
                            streampostArrayList.addAll(response.body().getResult().getStreampost());
                            setAdapter();
                            offset++;
                        }
                    }
                }

                @Override
                public void onFailure(Call<DashBoardBean> call, Throwable t) {
                    streamCall = true;
                }
            });
        } else {
            Utils.showToast(context, context.getString(R.string.toast_no_internet));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (StreamApiCall != null) {
            StreamApiCall.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callGetWorlDataApi("", 0);
    }
}
