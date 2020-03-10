package com.community.hundred.modules.ui.user;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.FollowAdapter;
import com.community.hundred.modules.ui.user.entry.FollowEntry;
import com.community.hundred.modules.ui.user.presenter.FollowPresenter;
import com.community.hundred.modules.ui.user.presenter.view.IFollowView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

// 我的关注
@Route(path = ActivityConstant.FOLLOW)
public class FollowActivity extends MyActivity<IFollowView, FollowPresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private FollowAdapter adapter;

    private List<FollowEntry> list = new ArrayList<>();

    @Override
    protected FollowPresenter createPresenter() {
        return new FollowPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FollowAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void initData() {
        mPresenter.getMyFollowList();
        mPresenter.setOnFollowListener(followEntries -> {
            list.addAll(followEntries);
            adapter.notifyDataSetChanged();
        });
    }

}
