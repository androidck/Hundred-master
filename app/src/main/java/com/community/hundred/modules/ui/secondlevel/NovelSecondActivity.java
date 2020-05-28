package com.community.hundred.modules.ui.secondlevel;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.adapter.MyHomeViewPageAdapter;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.main.fragment.homechild.HomeChildNewFragment;
import com.community.hundred.modules.ui.main.fragment.presenter.HomePresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IHomeView;
import com.community.hundred.modules.ui.secondlevel.fragment.NovelSecondFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 小说二级
@Route(path = ActivityConstant.NOV_SECOND)
public class NovelSecondActivity extends MyActivity<IHomeView, HomePresenter> {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private MyHomeViewPageAdapter adapter;
    private List<MyLazyFragment> fragmentList;
    private List<String> titleList;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_novel_second;
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
        getClassify();
    }

    // 获取分类
    public void getClassify() {
        setTitle("全部视频");
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        mPresenter.getHomeClassify();
        mPresenter.setOnHomeClassifyListener(list -> {
            for (int i = 0; i < list.size(); i++) {
                titleList.add(list.get(i).getName());
                fragmentList.add(NovelSecondFragment.getInstance(list.get(i).getId()));
            }
            adapter = new MyHomeViewPageAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragmentList.get(position).getTitleInit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
