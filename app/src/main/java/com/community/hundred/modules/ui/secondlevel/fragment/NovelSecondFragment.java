package com.community.hundred.modules.ui.secondlevel.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.HomeChildVideoAdapter;
import com.community.hundred.modules.ui.main.fragment.entry.HomeVideoEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.HomePresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IHomeView;
import com.community.hundred.modules.ui.secondlevel.NovelSecondActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

// 子页面
public class NovelSecondFragment extends MyLazyFragment<NovelSecondActivity, IHomeView, HomePresenter> {


    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private String id;// id
    private List<HomeVideoEntry> list = new ArrayList<>();
    private HomeChildVideoAdapter adapter;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getAttachActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_novel_second;
    }

    @Override
    protected void initView() {
        GridLayoutManager manager = new GridLayoutManager(mActivity, 2);
        recyclerView.setLayoutManager(manager);
        adapter = new HomeChildVideoAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.VIDEO_DETAILS)
                    .withString("videoId", list.get(position).getId())
                    .navigation();
        });

        id = getArguments().getString("id");
    }

    @Override
    protected void initData() {
        getVideoList();
    }

    public static NovelSecondFragment getInstance(String id) {
        NovelSecondFragment fragment = new NovelSecondFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void getVideoList() {
        mPresenter.getVideoList(id, 1);
        mPresenter.setOnHomeVideoListener(list1 -> {
            list.addAll(list1);
            adapter.notifyDataSetChanged();
        });
    }
}
