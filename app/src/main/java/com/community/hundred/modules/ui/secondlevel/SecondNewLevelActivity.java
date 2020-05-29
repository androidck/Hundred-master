package com.community.hundred.modules.ui.secondlevel;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.adapter.MyHomeViewPageAdapter;
import com.community.hundred.common.adapter.MyViewPageAdapter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.main.fragment.homechild.HomeChildNewFragment;
import com.community.hundred.modules.ui.main.fragment.presenter.HomePresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IHomeView;
import com.community.hundred.modules.ui.secondlevel.entry.SecondTabEntry;
import com.community.hundred.modules.ui.secondlevel.fragment.SecondLevelFragment;
import com.community.hundred.modules.ui.secondlevel.presenter.SecondLevelPresenter;
import com.community.hundred.modules.ui.secondlevel.presenter.view.ISecondLevelView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
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
@Route(path = ActivityConstant.SECOND_NEW_LEVEL)
public class SecondNewLevelActivity extends MyActivity<IHomeView, HomePresenter> implements OnSelectResultListener {

    @BindView(R.id.ly_screen)
    AutoRelativeLayout lyScreen;
    @BindView(R.id.ftb_filter)
    FilterTabView ftbFilter;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;



    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private MyViewPageAdapter adapter;

    private List<String> titleList;
    private List<Fragment> fragmentList;

    @Autowired
    String title;

    @Autowired
    String id;

    private List<SecondTabEntry> tabEntries;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second_level;
    }

    @Override
    protected void initView() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        tabEntries = new ArrayList<>();

    }

    @Override
    protected void initData() {
        mPresenter.getHomeClassify();
        mPresenter.setOnHomeClassifyListener(list -> {
            for (int i = 0; i < list.size(); i++) {
                titleList.add(list.get(i).getName());
                fragmentList.add(HomeChildNewFragment.getInstance(list.get(i).getId(), new Gson().toJson(list.get(i).getEr()), list.get(i).getName()));
                tabEntries.add(new SecondTabEntry(i, list.get(i).getName(), 0, i));
            }
            adapter = new MyViewPageAdapter(getSupportFragmentManager(), SecondNewLevelActivity.this, fragmentList, titleList);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        });

        FilterInfoBean bean1 = new FilterInfoBean("", FilterTabConfig.FILTER_TYPE_SINGLE_SELECT, tabEntries);
        ftbFilter.addFilterItem("", bean1.getFilterData(), bean1.getPopupType(), 0);
        ftbFilter.setOnSelectResultListener(this);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }


    @Override
    public void onSelectResult(FilterResultBean resultBean) {
        viewPager.setCurrentItem(resultBean.getPosition());
        tabLayout.getTabAt(resultBean.getPosition()).select();
    }


    @OnClick({R.id.tv_content, R.id.ly_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_content:
                ARouter.getInstance().build(ActivityConstant.SEARCH)
                        .withString("name", "推荐")
                        .navigation();
                break;
            case R.id.ly_screen:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
