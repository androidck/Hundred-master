package com.community.hundred.modules.ui.main.fragment;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.adapter.LiveBroadcastAdapter;
import com.community.hundred.modules.ui.livebroadcast.entry.LiveBroadCastEntry;
import com.community.hundred.modules.ui.main.fragment.entry.LiveBroadCastPlatformEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Request;

// 直播
public class LiveBroadcastFragment extends MyLazyFragment {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private View headView;
    private LiveBroadcastAdapter adapter;

    private String cList;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private List<LiveBroadCastPlatformEntry.PingtaiBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_broadcast;
    }


    @Override
    protected void initView() {
        setTitle("直播");
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(manager);
        headView = getLayoutInflater().inflate(R.layout.view_head_live_broadcast, recyclerView, false);
        recyclerView.addHeaderView(headView);
        adapter = new LiveBroadcastAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.LIVE_BROAD_CAST)
                    .withString("address", list.get(position).getAddress())
                    .withString("imgUrl", list.get(position).getXinimg())
                    .withString("title", list.get(position).getTitle())
                    .withString("cList", cList)
                    .navigation();
        });
        refresh.setEnableRefresh(false);
        refresh.setEnableLoadMore(false);
    }


    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    protected boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initData() {
        getLiveBroadCast();
    }

    public static LiveBroadcastFragment getInstance() {
        LiveBroadcastFragment fragment = new LiveBroadcastFragment();
        return fragment;
    }

    // 获取直播
    private void getLiveBroadCast() {
        showLoading();
        OkHttp.getAsync(HttpConstant.liveURL, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                showComplete();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                LiveBroadCastEntry entry = new Gson().fromJson(object.toString(), LiveBroadCastEntry.class);
                getVideoList(entry.getList());
                cList = entry.getClist();

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showComplete();
            }
        });
    }

    public void getVideoList(String url) {
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                LiveBroadCastPlatformEntry entry = new Gson().fromJson(result, LiveBroadCastPlatformEntry.class);
                list.addAll(entry.getPingtai());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(Request request, IOException e) {

            }
        });
    }


}
