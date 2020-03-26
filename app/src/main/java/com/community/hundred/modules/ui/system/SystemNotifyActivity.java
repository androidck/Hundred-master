package com.community.hundred.modules.ui.system;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.MessageAdapter;
import com.community.hundred.modules.adapter.SystemNotifyAdapter;
import com.community.hundred.modules.ui.livebroadcast.entry.SystemEntry;
import com.community.hundred.modules.ui.livebroadcast.presenter.MessagePresenter;
import com.community.hundred.modules.ui.livebroadcast.presenter.view.IMessageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 系统通知
@Route(path = ActivityConstant.SYSTEM_NOTIFY)
public class SystemNotifyActivity extends MyActivity<IMessageView, MessagePresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private SystemNotifyAdapter adapter;

    private List<SystemEntry> systemEntries = new ArrayList<>();

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_notify;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SystemNotifyAdapter(this, systemEntries);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void initData() {
        getSystem();
    }

    // 获取系消息
    public void getSystem() {
        mPresenter.getSystemInfo();
        mPresenter.setOnSystemInfoListener(list -> {
            systemEntries.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
