package com.community.hundred.modules.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.modules.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestFragment extends MyLazyFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.text_fragment;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setNestedScrollingEnabled(false);
        TestAdapter adapter = new TestAdapter(mActivity, getData());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    public List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("第" + i + "个Item");
        }
        return list;
    }
}
