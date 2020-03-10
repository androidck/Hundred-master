package com.community.hundred.modules.ui.femalestar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.widget.XCollapsingToolbarLayout;
import com.community.hundred.modules.adapter.FemaleStarVideoAdapter;
import com.community.hundred.modules.ui.femalestar.entry.FemaleInfoEntry;
import com.community.hundred.modules.ui.femalestar.presenter.FemaleStarPresenter;
import com.community.hundred.modules.ui.femalestar.presenter.view.IFemaleView;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

// 女星详情
@Route(path = ActivityConstant.FEMALE_STAR)
public class FemaleStarActivity extends MyActivity<IFemaleView, FemaleStarPresenter> implements XCollapsingToolbarLayout.OnScrimsListener {
    @BindView(R.id.user_head)
    CircleImageView userHead;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.t_test_title)
    Toolbar tTestTitle;
    @BindView(R.id.ctl_test_bar)
    XCollapsingToolbarLayout ctlTestBar;
    @BindView(R.id.abl_test_bar)
    AppBarLayout ablTestBar;
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.btn_one)
    RadioButton btnOne;
    @BindView(R.id.btn_two)
    RadioButton btnTwo;
    @BindView(R.id.group)
    RadioGroup group;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_user_data)
    TextView tvUserData;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private FemaleStarVideoAdapter adapter;

    private int p = 1;
    @Autowired
    String pid;

    private String title;

    private String order;

    private List<FemaleInfoEntry> infoEntries = new ArrayList<>();


    @Override
    protected FemaleStarPresenter createPresenter() {
        return new FemaleStarPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_female_star;
    }

    @Override
    protected void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.setTitleBar(this, tTestTitle);
        //设置渐变监听
        ctlTestBar.setOnScrimsListener(this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        adapter = new FemaleStarVideoAdapter(this, infoEntries);
        recyclerView.setAdapter(adapter);
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    p++;
                    getFemaleStar(order);
                    refreshLayout.finishLoadMore();
                }, 200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    infoEntries.clear();
                    p = 1;
                    getFemaleStar(order);
                    refreshLayout.finishRefresh();
                }, 200);
            }
        });

        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.VIDEO_DETAILS)
                    .withString("videoId", infoEntries.get(position).getId())
                    .navigation();
        });
    }

    @Override
    protected void initData() {
        order = "1";
        getFemaleStar(order);
    }

    public void getFemaleStar(String order) {
        mPresenter.getFemaleStarDetails(pid, String.valueOf(p), order);
        mPresenter.setOnFemaleStarDetailsListener((entry, list) -> {
            infoEntries.addAll(list);
            title = entry.getName();
            Glide.with(this).load(HttpConstant.BASE_HOST + entry.getImage()).into(userHead);
            tvDesc.setText(entry.getDescription());
            tvUserData.setText("身高:" + entry.getShengao() + "CM\t\t" + "体重:" + entry.getTizhong() + "KG\t\t罩杯：" + entry.getZhaobei() + "杯");
            tvNickName.setText(title);
            adapter.notifyDataSetChanged();
        });
    }


    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        if (shown) {
            getStatusBarConfig().statusBarDarkFont(true).init();
            getTitleBar().setLeftIcon(R.mipmap.bar_icon_back_black);
            getTitleBar().getTitleView().setTextColor(Color.parseColor("#323232"));
            setTitle(title);
        } else {
            getStatusBarConfig().statusBarDarkFont(false).init();
            getTitleBar().setLeftIcon(R.mipmap.bar_icon_back_white);
            setTitle("");
        }
    }


    @OnClick({R.id.btn_one, R.id.btn_two})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_one:
                infoEntries.clear();
                p = 1;
                order = "1";
                getFemaleStar(order);
                break;
            case R.id.btn_two:
                infoEntries.clear();
                p = 1;
                order = "2";
                getFemaleStar(order);
                break;
        }
    }
}
