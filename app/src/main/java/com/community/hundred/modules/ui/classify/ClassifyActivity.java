package com.community.hundred.modules.ui.classify;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.GroupClassifyAdapter;
import com.community.hundred.modules.eventbus.TabWrap;
import com.community.hundred.modules.ui.classify.entry.ClassifyChildEntry;
import com.community.hundred.modules.ui.classify.entry.ClassifyHeaderEntry;
import com.community.hundred.modules.ui.classify.presenter.ClassifyPresenter;
import com.community.hundred.modules.ui.classify.presenter.IClassifyView;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.layoutmanger.GroupedGridLayoutManager;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

// 所有分类
@Route(path = ActivityConstant.CLASSIFY)
public class ClassifyActivity extends MyActivity<IClassifyView, ClassifyPresenter> {


    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    private ArrayList<ClassifyHeaderEntry> arrayList = new ArrayList<>();

    private GroupClassifyAdapter adapter;

    @Override
    protected ClassifyPresenter createPresenter() {
        return new ClassifyPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classify;
    }

    @Override
    protected void initView() {
        adapter = new GroupClassifyAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        GroupedGridLayoutManager manager = new GroupedGridLayoutManager(this, 4, adapter);
        recyclerView.setLayoutManager(manager);
        // head 点击事件
        adapter.setOnHeaderClickListener((adapter, holder, groupPosition) -> {
            EventBus.getDefault().post(TabWrap.getInstance(groupPosition));
            finish();
        });

        // 子项点击事件
        adapter.setOnChildClickListener((adapter, holder, groupPosition, childPosition) -> {
            ClassifyChildEntry entry = arrayList.get(groupPosition).getSon().get(childPosition);
            ARouter.getInstance().build(ActivityConstant.SECOND_LEVEL)
                    .withString("title", entry.getName())
                    .withString("id", entry.getId())
                    .navigation();
        });
    }

    @Override
    protected void initData() {
        getClassify();
    }

    public void getClassify() {
        mPresenter.getClassify();
        mPresenter.setOnDataSourceListener(list -> {
            arrayList.addAll(list);
            adapter.notifyDataChanged();
        });
    }


}
