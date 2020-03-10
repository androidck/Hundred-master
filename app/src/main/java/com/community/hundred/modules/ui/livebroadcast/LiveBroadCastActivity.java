package com.community.hundred.modules.ui.livebroadcast;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.adapter.LiveBroadCastActivityAdapter;
import com.community.hundred.modules.ui.livebroadcast.entry.LiveBroadCastEntry;
import com.community.hundred.modules.ui.main.fragment.entry.AnchorEntry;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;
import retrofit2.http.Path;

// 直播列表
@Route(path = ActivityConstant.LIVE_BROAD_CAST)
public class LiveBroadCastActivity extends MyActivity {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;

    private View headView;
    private LiveBroadCastActivityAdapter adapter;
    private List<AnchorEntry.ZhuboBean> list = new ArrayList<>();

    @Autowired
    String address;

    @Autowired
    String imgUrl;

    @Autowired
    String title;

    @Autowired
    String cList;

    private TextView tv_title;
    private CircleImageView img_url;



    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_broad_cast_list;
    }

    @Override
    protected void initView() {
        setTitle(title);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        headView = getLayoutInflater().inflate(R.layout.view_header_live_activity, recyclerView, false);
        recyclerView.addHeaderView(headView);
        tv_title = headView.findViewById(R.id.tv_title);
        img_url = headView.findViewById(R.id.imgUrl);
        adapter = new LiveBroadCastActivityAdapter(this, list);
        recyclerView.setAdapter(adapter);
        Glide.with(this)
                .load(imgUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)//图片加载失败后，显示的图片
                .into(img_url);
        tv_title.setText(title + "\t直播平台");
        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.LIVE_BROAD_CAST_DETAILS)
                    .withString("videoUrl", list.get(position).getAddress()).navigation();
        });
    }

    @Override
    protected void initData() {
        getAnchor();
    }

    // 获取主播
    public void getAnchor() {
        showLoading();
        OkHttp.getAsync(cList + address, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                showComplete();
                AnchorEntry entry = new Gson().fromJson(result, AnchorEntry.class);
                list.addAll(entry.getZhubo());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showComplete();
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }


}
