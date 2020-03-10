package com.community.hundred.modules.ui.classify;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.ClassifyAdapter;
import com.community.hundred.modules.adapter.ClassifyMyAdapter;
import com.community.hundred.modules.ui.classify.entry.ClassifyChildEntry;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

// 所有分类
@Route(path = ActivityConstant.CLASSIFY)
public class ClassifyActivity extends MyActivity {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    View headView;

    private ClassifyAdapter adapter;

    RecyclerView headerRecyclerView;

    private ClassifyMyAdapter headerAdapter;

    private List<ClassifyChildEntry> myList = new ArrayList<>();
    private List<ClassifyChildEntry> allList = new ArrayList<>();


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classify;
    }

    @Override
    protected void initView() {
        for (int i = 0; i < getData().size(); i++) {
            myList.add(getData().get(i));
        }

        for (int i = 0; i < getData().size(); i++) {
            allList.add(getData().get(i));
        }
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);
        headView = getLayoutInflater().inflate(R.layout.view_header_my_classify, recyclerView, false);
        recyclerView.addHeaderView(headView);
        adapter = new ClassifyAdapter(this, allList);
        recyclerView.setAdapter(adapter);
        headerRecyclerView = headView.findViewById(R.id.recyclerView);
        GridLayoutManager headManager = new GridLayoutManager(this, 4);
        headerRecyclerView.setLayoutManager(headManager);
        headerAdapter = new ClassifyMyAdapter(this, myList);
        headerRecyclerView.setAdapter(headerAdapter);
        getTitleBar().getRightView().setTextColor(Color.parseColor("#FFD428"));
        setRightTitle("编辑");
        adapter.setOnLongItemClickListener(position -> {
            setRightTitle("完成");
            isMyHideShowSymbol(1);
            isAllHideShowSymbol(1);
        });

        // 我的分类长按事件
        headerAdapter.setOnLongItemClickListener(position -> {
            setRightTitle("完成");
            isMyHideShowSymbol(1);
            isAllHideShowSymbol(1);
        });

        // 点击删除
        headerAdapter.setOnItemClickListener(position -> {
            if (myList.get(position).isEdit == true) {
                myList.remove(myList.get(position));
                headerAdapter.notifyItemRemoved(position);
                headerAdapter.notifyDataSetChanged();
            } else {
                ARouter.getInstance().build(ActivityConstant.SECOND_LEVEL)
                        .withString("title", myList.get(position).getName())
                        .navigation();
            }
        });

    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        if ("完成".equals(getTitleBar().getRightView().getText())) {
            isMyHideShowSymbol(2);
            isAllHideShowSymbol(2);
            setRightTitle("编辑");
        } else {
            isMyHideShowSymbol(1);
            isAllHideShowSymbol(1);
            setRightTitle("完成");
        }

    }

    // 隐藏显示
    public void isMyHideShowSymbol(int type) {
        for (ClassifyChildEntry entry : myList) {
            if (type == 1) {
                if (entry.isEdit == false) {
                    entry.setEdit(true);
                }
            } else {
                if (entry.isEdit == true) {
                    entry.setEdit(false);
                }
            }
        }
        headerAdapter.notifyDataSetChanged();
    }

    // 隐藏显示
    public void isAllHideShowSymbol(int type) {
        for (ClassifyChildEntry entry : allList) {
            if (type == 1) {
                if (entry.isEdit == false) {
                    entry.setEdit(true);
                }
            } else {
                if (entry.isEdit == true) {
                    entry.setEdit(false);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    public List<ClassifyChildEntry> getData() {
        List<ClassifyChildEntry> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ClassifyChildEntry entry = new ClassifyChildEntry();
            entry.setId("" + i);
            entry.setName("item" + i);
            entry.setFatherId("item" + (i + 1));
            list.add(entry);
        }
        return list;
    }


}
