package com.community.hundred.modules.ui.post.fragment;

import android.os.Bundle;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.modules.ui.post.PeoplePostDetailsActivity;
import com.community.hundred.modules.ui.post.presenter.MySendPostPresenter;
import com.community.hundred.modules.ui.post.presenter.view.IMySendPostView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

// 动态
public class PeopleDynamicFragment extends MyLazyFragment<PeoplePostDetailsActivity, IMySendPostView, MySendPostPresenter> implements IMySendPostView {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private int x = 0;
    private int y = 10;

    @Override
    protected MySendPostPresenter createPresenter() {
        return new MySendPostPresenter(getAttachActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_people_dynamic;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getData();
    }

    public void getData() {
        String uid = getArguments().getString("uid");
        mPresenter.init(uid);
        mPresenter.getMySendPost(uid, x, y);
    }

    public static PeopleDynamicFragment getInstance(String uid) {
        PeopleDynamicFragment fragment = new PeopleDynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public SwipeRecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return refresh;
    }
}
