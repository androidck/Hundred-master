package com.community.hundred.modules.ui.user;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.ReceivedGiftAdapter;
import com.community.hundred.modules.ui.user.entry.GiftEntry;
import com.community.hundred.modules.ui.user.presenter.ReceivedGiftPresenter;
import com.community.hundred.modules.ui.user.presenter.view.IReceivedGiftView;
import com.nostra13.universalimageloader.utils.L;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 收到礼物
@Route(path = ActivityConstant.RECEIVED_GIFT)
public class ReceivedGiftActivity extends MyActivity<IReceivedGiftView, ReceivedGiftPresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private List<GiftEntry> giftEntries = new ArrayList<>();

    private ReceivedGiftAdapter adapter;

    @Override
    protected ReceivedGiftPresenter createPresenter() {
        return new ReceivedGiftPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_received;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReceivedGiftAdapter(this, giftEntries);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        getMyGift();
    }

    public void getMyGift() {
        mPresenter.getMyGiftList();
        mPresenter.setOnDataListener(list -> {
            giftEntries.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }


}
