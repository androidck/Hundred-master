package com.community.hundred.modules.ui.post;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.modules.adapter.CircleAdapter;
import com.community.hundred.modules.eventbus.CommentWrap;
import com.community.hundred.modules.eventbus.SpecialWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.post.presenter.MySendPostPresenter;
import com.community.hundred.modules.ui.post.presenter.SendPostPresenter;
import com.community.hundred.modules.ui.post.presenter.view.IMySendPostView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 我的发布
@Route(path = ActivityConstant.MY_SEND_POST)
public class MySendPostActivity extends MyActivity<IMySendPostView, MySendPostPresenter> implements IMySendPostView {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;


    private int x = 0;
    private int y = 10;

    @Override
    protected MySendPostPresenter createPresenter() {
        return new MySendPostPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_send_post;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        getData();
    }

    public void getData() {
        mPresenter.init(LoginUtils.getInstance().getUid());
        mPresenter.getMySendPost(LoginUtils.getInstance().getUid(),x, y);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    public SwipeRecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return refresh;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentWrap(CommentWrap commentWrap) {
        if (commentWrap.code == 200) {
            refresh.autoRefresh();
        }
    }
}
