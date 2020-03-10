package com.community.hundred.modules.ui.setup;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.setup.presenter.MyCollectPresenter;
import com.community.hundred.modules.ui.setup.presenter.view.IMyCollectView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

// 我的收藏
@Route(path = ActivityConstant.MY_COLLECT)
public class MyCollectActivity extends MyActivity<IMyCollectView, MyCollectPresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    @Override
    protected MyCollectPresenter createPresenter() {
        return new MyCollectPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initData() {
        getMyCollectList();
    }

    public void getMyCollectList(){
        mPresenter.getMyCollectList();
    }


}
