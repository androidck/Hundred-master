package com.community.hundred.modules.ui.secondlevel.fragment;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.modules.adapter.ScreenMenuAdapter;
import com.community.hundred.modules.adapter.SecondLevelAdapter;
import com.community.hundred.modules.entry.MenuEntry;
import com.community.hundred.modules.ui.secondlevel.entry.SecondEntry;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

// 二级分类子页面
public class SecondLevelFragment extends MyLazyFragment {


    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private View header;

    private SwipeRecyclerView screenRecyclerView;

    private ScreenMenuAdapter headAdapter;
    private SecondLevelAdapter adapter;

    private List<MenuEntry> list = new ArrayList<>();

    private int checkedPosition;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_second_level;
    }

    @Override
    protected void initView() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        header = getLayoutInflater().inflate(R.layout.view_header_screen, recyclerView, false);
        screenRecyclerView = header.findViewById(R.id.recyclerView);
        recyclerView.addHeaderView(header);
        adapter = new SecondLevelAdapter(getContext(), getVideo());
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < getData().size(); i++) {
            if (i == 0) {
                getData().get(i).setSelect(true);
            } else
                getData().get(i).setSelect(false);
            list.add(getData().get(i));
        }

        GridLayoutManager headerManager = new GridLayoutManager(getContext(), 3);
        screenRecyclerView.setLayoutManager(headerManager);
        headAdapter = new ScreenMenuAdapter(getContext(), list);
        screenRecyclerView.setAdapter(headAdapter);
        headAdapter.setOnItemClickListener(position -> {
            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
                    list.get(i).setSelect(true);
                } else {
                    list.get(i).setSelect(false);
                }
            }
            checkedPosition = position;
            headAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void initData() {

    }

    // 初始化菜单数据
    public List<MenuEntry> getData() {
        List<MenuEntry> list = new ArrayList<>();
        list.add(new MenuEntry("1", "综合排序", true));
        list.add(new MenuEntry("2", "最多播放", false));
        list.add(new MenuEntry("3", "最近更新", false));
        return list;
    }

    public List<SecondEntry> getVideo() {
        List<SecondEntry> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SecondEntry entry = new SecondEntry();
            entry.setId("" + i);
            entry.setVideoTitle("视频" + i);
            entry.setTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            entry.setPlayCount("123");
            list.add(entry);
        }
        return list;
    }

    public static SecondLevelFragment getInstance() {
        SecondLevelFragment fragment = new SecondLevelFragment();
        return fragment;
    }


}
