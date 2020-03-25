package com.community.hundred.modules.ui.livebroadcast;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.MessageAdapter;
import com.community.hundred.modules.adapter.ReceivedGiftAdapter;
import com.community.hundred.modules.adapter.SystemNotifyAdapter;
import com.community.hundred.modules.ui.livebroadcast.entry.MyMessageEntry;
import com.community.hundred.modules.ui.livebroadcast.entry.SystemEntry;
import com.community.hundred.modules.ui.livebroadcast.presenter.MessagePresenter;
import com.community.hundred.modules.ui.livebroadcast.presenter.view.IMessageView;
import com.community.hundred.modules.ui.user.entry.GiftEntry;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 消息
@Route(path = ActivityConstant.MESSAGE)
public class MessageActivity extends MyActivity<IMessageView, MessagePresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_my_xx)
    RadioButton tvMyXx;
    @BindView(R.id.tv_my_hf)
    RadioButton tvMyHf;
    @BindView(R.id.tv_my_lw)
    RadioButton tvMyLw;
    @BindView(R.id.tv_my_xt)
    RadioButton tvMyXt;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    // 系统消息适配器
    private SystemNotifyAdapter systemNotifyAdapter;
    private List<SystemEntry> systemEntries = new ArrayList<>();
    private List<GiftEntry> giftEntries = new ArrayList<>();
    private List<MyMessageEntry> list = new ArrayList<>();
    private List<MyMessageEntry> hfList = new ArrayList<>();

    private ReceivedGiftAdapter receivedGiftAdapter;

    private MessageAdapter adapter, hfAdapter;

    private int checkId = 0;

    int p = 1;

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }


    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(this, list);
        recyclerView.setAdapter(adapter);
        refresh.setOnRefreshListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    if (checkId == 0) {
                        p++;
                        getMyMessage();
                    } else if (checkId == 1) {
                        p++;
                        getReply();
                    }
                    refreshLayout.finishLoadMore();
                }, 200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    if (checkId == 0) {
                        p = 1;
                        list.clear();
                        getMyMessage();
                    } else if (checkId == 1) {
                        p = 1;
                        hfList.clear();
                    }
                    refreshLayout.finishRefresh();
                }, 200);
            }
        });

    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initData() {
        getMyMessage();
    }

    // 获取我的消息
    public void getMyMessage() {
        mPresenter.getMyMessage(p);
        mPresenter.setOnMessageListener(list1 -> {
            if (list1.size() == 0) {
                showEmpty();
            } else {
                showComplete();
                list.addAll(list1);
                adapter.notifyDataSetChanged();
            }
        });
    }

    // 获取我的回复
    public void getReply() {
        mPresenter.getReply(p);
        mPresenter.setOnMessageListener(list1 -> {
            if (list1.size() == 0) {
                showEmpty();
            } else {
                showComplete();
                hfList.addAll(list1);
                hfAdapter.notifyDataSetChanged();
            }
        });
    }

    // 获取系消息
    public void getSystem() {
        mPresenter.getSystemInfo();
        mPresenter.setOnSystemInfoListener(list -> {
            systemEntries.clear();
            systemEntries.addAll(list);
            systemNotifyAdapter.notifyDataSetChanged();
        });
    }

    // 我的礼物
    public void myGift() {
        mPresenter.getMyGiftList();
        mPresenter.setOnDataListener(list -> {
            giftEntries.addAll(list);
            receivedGiftAdapter.notifyDataSetChanged();
        });

    }


    @OnClick({R.id.tv_my_xx, R.id.tv_my_hf, R.id.tv_my_lw, R.id.tv_my_xt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_my_xx:
                checkId = 0;
                list.clear();
                getMyMessage();
                break;
            case R.id.tv_my_hf:
                checkId = 1;
                hfList.clear();
                hfAdapter = new MessageAdapter(this, hfList);
                recyclerView.setAdapter(hfAdapter);
                getReply();
                break;
            case R.id.tv_my_lw:
                checkId = 2;
                receivedGiftAdapter = new ReceivedGiftAdapter(this, giftEntries);
                recyclerView.setAdapter(receivedGiftAdapter);
                giftEntries.clear();
                myGift();
                break;
            case R.id.tv_my_xt:
                checkId = 3;
                systemNotifyAdapter = new SystemNotifyAdapter(this, systemEntries);
                recyclerView.setAdapter(systemNotifyAdapter);
                getSystem();
                break;
        }
    }
}
