package com.community.hundred.modules.ui.main.fragment.ranking;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.modules.adapter.RankingAdapter;
import com.community.hundred.modules.ui.main.fragment.entry.RankingEntry;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

// 排行
public class RankingFragment extends MyLazyFragment {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private View headView;

    private RankingAdapter adapter;

    private RadioGroup radioGroup;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ranking;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        headView = getLayoutInflater().inflate(R.layout.view_header_ranking, recyclerView, false);
        recyclerView.addHeaderView(headView);
        adapter = new RankingAdapter(mActivity, getData());
        recyclerView.setAdapter(adapter);

        radioGroup = headView.findViewById(R.id.ra_group);
    }

    @Override
    protected void initData() {

    }

    public List<RankingEntry> getData() {
        List<RankingEntry> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RankingEntry entry = new RankingEntry();
            entry.setNumber("" + i);
            list.add(entry);
        }
        return list;
    }

    public static RankingFragment getInstance() {
        RankingFragment fragment = new RankingFragment();
        return fragment;
    }
}
