package com.community.hundred.modules.ui.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.adapter.MyHomeViewPageAdapter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.KeyConstant;
import com.community.hundred.modules.eventbus.GradualWrap;
import com.community.hundred.modules.eventbus.TabWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.fragment.homechild.HomeChildNewFragment;
import com.community.hundred.modules.ui.main.fragment.presenter.HomePresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IHomeView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

// 首页父类
public class HomeNewFragment extends MyLazyFragment<MainActivity, IHomeView, HomePresenter> implements HomeChildNewFragment.OnScrollChangeListener {
    @BindView(R.id.tv_toorbar)
    TextView tvToorbar;
    @BindView(R.id.tv_bannerbg)
    TextView tvBannerbg;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.img_surface)
    ImageView imgSurface;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ly_one)
    AutoLinearLayout lyOne;
    @BindView(R.id.tv_search_one)
    TextView tvSearchOne;
    @BindView(R.id.tv_search_two)
    TextView tvSearchTwo;
    @BindView(R.id.ly_two)
    AutoLinearLayout lyTwo;
    @BindView(R.id.ly_top)
    AutoLinearLayout lyTop;
    @BindView(R.id.img_classify)
    ImageView imgClassify;

    private MyHomeViewPageAdapter adapter;
    private List<MyLazyFragment> fragmentList;
    private List<String> titleList;


    private int scrollYNew, imageHeightNew;

    private int checkPosition;

    private int redCode, greenCode, blueCode;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(mActivity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_new;
    }

    @Override
    protected void initView() {
        HomeChildNewFragment.setListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    // 获取分类
    public void getClassify() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        mPresenter.getHomeClassify();
        mPresenter.setOnHomeClassifyListener(list -> {
            for (int i = 0; i < list.size(); i++) {
                titleList.add(list.get(i).getName());
                fragmentList.add(HomeChildNewFragment.getInstance(list.get(i).getId(), new Gson().toJson(list.get(i).getEr()), list.get(i).getName()));
            }
            adapter = new MyHomeViewPageAdapter(getChildFragmentManager(), getContext(), fragmentList, titleList);
            viewPager.setAdapter(adapter);
            tabs.setupWithViewPager(viewPager);
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragmentList.get(position).getTitleInit();
                checkPosition = position;
                if (position == 0) {
                    lyOne.setVisibility(View.VISIBLE);
                    lyTwo.setVisibility(View.GONE);
                } else {
                    lyOne.setVisibility(View.GONE);
                    lyTwo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        getClassify();
    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }


    public static HomeNewFragment getInstance() {
        HomeNewFragment fragment = new HomeNewFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onScrollChange(int imageHeight, int scrollY) {
        scrollYNew = scrollY;
        imageHeightNew = imageHeight;
        if (scrollY <= 0) {//顶部
            tvBannerbg.setBackgroundColor(Color.argb(255, redCode, greenCode, blueCode));
            tvToorbar.setBackgroundColor(Color.argb(255, redCode, greenCode, blueCode));
            tvToorbar.setTextColor(getResources().getColor(android.R.color.white));
            tabs.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.white));
            tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
            imgSurface.setImageResource(R.mipmap.icon_surface_white);
            imgAdd.setImageResource(R.mipmap.icon_add_white);
            imgClassify.setImageResource(R.mipmap.icon_classify_white);
        } else if (scrollY <= imageHeight) {
            float scale = (float) scrollY / imageHeight;
            float alpha = (255 * (1 - scale));
            tvBannerbg.setBackgroundColor(Color.argb((int) alpha, redCode, greenCode, blueCode));
            tvToorbar.setBackgroundColor(Color.argb((int) alpha, redCode, greenCode, blueCode));
        } else {
            //滑动到banner下面设置普通颜色
            tvBannerbg.setBackgroundColor(getResources().getColor(android.R.color.white));
            tvToorbar.setBackgroundColor(getResources().getColor(android.R.color.white));
            tvToorbar.setTextColor(getResources().getColor(R.color.black));
            tabs.setTabTextColors(getResources().getColor(R.color.textColor), getResources().getColor(R.color.textColor));
            tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
            imgSurface.setImageResource(R.mipmap.icon_surface_gray);
            imgAdd.setImageResource(R.mipmap.icon_add_gray);
            imgClassify.setImageResource(R.mipmap.icon_classify_gray);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGradualWrap(GradualWrap wrap) {
        if (wrap != null) {
            if (KeyConstant.HOME_TITLE.equals(wrap.title)) {
                if (scrollYNew <= 0) {
                    tvBannerbg.setBackgroundColor(wrap.vibrantColor);
                    tvToorbar.setBackgroundColor(wrap.vibrantColor);
                }
                int rgb = wrap.vibrantColor;
                int r = (rgb & 16711680) >> 16;
                int g = (rgb & 65280) >> 8;
                int b = (rgb & 255);

                redCode = r;
                greenCode = g;
                blueCode = b;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTabWrap(TabWrap wrap) {
        int position = wrap.position;
        viewPager.setCurrentItem(position);
        tabs.getTabAt(position).select();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @OnClick({R.id.tv_search, R.id.tv_search_one, R.id.tv_search_two, R.id.img_surface, R.id.img_add, R.id.img_classify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                ARouter.getInstance().build(ActivityConstant.SEARCH)
                        .withString("name", titleList.get(checkPosition))
                        .navigation();
                break;
            case R.id.tv_search_one:
                ARouter.getInstance().build(ActivityConstant.SEARCH)
                        .withString("name", titleList.get(checkPosition))
                        .navigation();
                break;
            case R.id.tv_search_two:
                ARouter.getInstance().build(ActivityConstant.NOV_SECOND).navigation();
                break;
            case R.id.img_surface:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.HISTORY_LOOK).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.img_add:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.MY_COLLECT)
                            .navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.img_classify:
                ARouter.getInstance().build(ActivityConstant.CLASSIFY)
                        .navigation();
                break;
        }
    }
}
