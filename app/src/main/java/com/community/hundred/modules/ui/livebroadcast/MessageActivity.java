package com.community.hundred.modules.ui.livebroadcast;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.MessageAdapter;
import com.community.hundred.modules.ui.livebroadcast.entry.MyMessageEntry;
import com.hjq.bar.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 消息
@Route(path = ActivityConstant.MESSAGE)
public class MessageActivity extends MyActivity {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private View headView;

    private MessageAdapter adapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }


    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headView = getLayoutInflater().inflate(R.layout.view_header_message, recyclerView, false);
        recyclerView.addHeaderView(headView);
        adapter = new MessageAdapter(this, getData());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initData() {

    }

    public List<MyMessageEntry> getData() {
        List<MyMessageEntry> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MyMessageEntry entry = new MyMessageEntry();
            entry.setUserHead("10");
            list.add(entry);
        }
        return list;
    }

}
