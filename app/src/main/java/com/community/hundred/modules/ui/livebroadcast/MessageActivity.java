package com.community.hundred.modules.ui.livebroadcast;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

    private ReceivedGiftAdapter receivedGiftAdapter;

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

    // 获取系消息
    public void getSystem() {
        mPresenter.getSystemInfo();
        mPresenter.setOnSystemInfoListener(list -> {
            systemEntries.clear();
            systemEntries.addAll(list);
            systemNotifyAdapter.notifyDataSetChanged();
        });
    }

    public void myGift() {
        mPresenter.getMyGiftList();
        mPresenter.setOnDataListener(list -> {
            giftEntries.addAll(list);
            receivedGiftAdapter.notifyDataSetChanged();
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_my_xx, R.id.tv_my_hf, R.id.tv_my_lw, R.id.tv_my_xt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_my_xx:
                showEmpty();
                break;
            case R.id.tv_my_hf:
                showEmpty();
                break;
            case R.id.tv_my_lw:
                receivedGiftAdapter = new ReceivedGiftAdapter(this, giftEntries);
                recyclerView.setAdapter(receivedGiftAdapter);
                giftEntries.clear();
                myGift();
                break;
            case R.id.tv_my_xt:
                systemNotifyAdapter = new SystemNotifyAdapter(this, systemEntries);
                recyclerView.setAdapter(systemNotifyAdapter);
                getSystem();
                break;
        }
    }
}
