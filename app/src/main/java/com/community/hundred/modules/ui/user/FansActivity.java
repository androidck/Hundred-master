package com.community.hundred.modules.ui.user;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.FansAdapter;
import com.community.hundred.modules.adapter.FollowAdapter;
import com.community.hundred.modules.ui.user.entry.FansEntry;
import com.community.hundred.modules.ui.user.entry.FollowEntry;
import com.community.hundred.modules.ui.user.presenter.FansPresenter;
import com.community.hundred.modules.ui.user.presenter.view.IFansView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

// 我的粉丝
@Route(path = ActivityConstant.FANS)
public class FansActivity extends MyActivity<IFansView, FansPresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private FansAdapter adapter;

    private List<FansEntry> fansEntries = new ArrayList<>();

    @Override
    protected FansPresenter createPresenter() {
        return new FansPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FansAdapter(this, fansEntries);
        recyclerView.setAdapter(adapter);
    }


    protected void initData() {
        getFans();
    }

    public void getFans() {
        mPresenter.getMyFansList();
        mPresenter.setOnDataListener(list -> {
            fansEntries.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

}
