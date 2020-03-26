package com.community.hundred.modules.ui.main.fragment;

import android.graphics.Color;
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
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.fragment.entry.BannerEntry;
import com.community.hundred.modules.ui.main.fragment.forumchild.ForumChildNewFragment;
import com.community.hundred.modules.ui.main.fragment.presenter.ForumPresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IForumView;
import com.google.android.material.tabs.TabLayout;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ForumNewFragment extends MyLazyFragment<MainActivity, IForumView, ForumPresenter> implements ForumChildNewFragment.OnScrollChangeListener {

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
    @BindView(R.id.ly_top)
    AutoLinearLayout lyTop;

    private MyHomeViewPageAdapter adapter;
    private List<MyLazyFragment> fragmentList;
    private List<String> titleList;

    private int scrollYNew, imageHeightNew;

    private int checkPosition;

    private List<BannerEntry> bannerList;

    private int redCode, greenCode, blueCode;


    @Override
    protected ForumPresenter createPresenter() {
        return new ForumPresenter(mActivity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forum_new;
    }

    @Override
    protected void initView() {
        ForumChildNewFragment.setListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void initData() {
        getForumTab();
    }

    // 获取专栏tab标签
    public void getForumTab() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        mPresenter.getForumClassify();
        mPresenter.setOnForumClassifyListener(list -> {
            for (int i = 0; i < list.size(); i++) {
                titleList.add(list.get(i).getName());
                fragmentList.add(ForumChildNewFragment.getInstance(list.get(i).getId()));
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static ForumNewFragment getInstance() {
        ForumNewFragment fragment = new ForumNewFragment();
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
            imgAdd.setImageResource(R.mipmap.icon_collection_white);
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
            imgAdd.setImageResource(R.mipmap.icon_collection_gray);
        }
    }


    @OnClick(R.id.tv_search)
    public void onViewClicked() {
        ARouter.getInstance().build(ActivityConstant.SEARCH)
                .withString("name", "专栏")
                .navigation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGradualWrap(GradualWrap wrap) {
        if (wrap != null) {
            if (KeyConstant.FORUM_TITLE.equals(wrap.title)) {
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

    @OnClick({R.id.img_surface, R.id.img_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_surface:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.HISTORY_LOOK).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.img_add:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.MY_COLLECT).navigation();
                } else {
                    notLogin();
                }
                break;
        }
    }
}
