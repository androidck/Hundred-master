package com.community.hundred.modules.ui.secondlevel;

import android.os.Bundle;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.adapter.MyViewPageAdapter;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.secondlevel.entry.SecondTabEntry;
import com.community.hundred.modules.ui.secondlevel.fragment.SecondLevelFragment;
import com.google.android.material.tabs.TabLayout;
import com.hjq.widget.view.ClearEditText;
import com.samluys.filtertab.FilterInfoBean;
import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.FilterTabConfig;
import com.samluys.filtertab.FilterTabView;
import com.samluys.filtertab.listener.OnSelectResultListener;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 二级分类
@Route(path = ActivityConstant.SECOND_LEVEL)
public class SecondLevelActivity extends MyActivity implements OnSelectResultListener {

    @BindView(R.id.ly_screen)
    AutoRelativeLayout lyScreen;
    @BindView(R.id.ftb_filter)
    FilterTabView ftbFilter;
    private PopupWindow popupwindow;

    @BindView(R.id.ed_content)
    ClearEditText edContent;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private MyViewPageAdapter adapter;

    private List<String> titleList;
    private List<Fragment> fragmentList;

    @Autowired
    String title;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_level;
    }

    @Override
    protected void initView() {
        setLeftTitle(title);
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            titleList.add("tab" + i);
            fragmentList.add(SecondLevelFragment.getInstance());
        }

        adapter = new MyViewPageAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        FilterInfoBean bean1 = new FilterInfoBean("", FilterTabConfig.FILTER_TYPE_SINGLE_SELECT, getTicketType());
        ftbFilter.addFilterItem("" ,bean1.getFilterData(), bean1.getPopupType(), 0);
        ftbFilter.setOnSelectResultListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }


    @OnClick(R.id.ly_screen)
    public void onViewClicked() {

    }

    @Override
    public void onSelectResult(FilterResultBean resultBean) {

    }


    private List<SecondTabEntry> getTicketType() {
        List<SecondTabEntry> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new SecondTabEntry(i, "tab" + (i + 1), 0));
        }
        return list;
    }


}
