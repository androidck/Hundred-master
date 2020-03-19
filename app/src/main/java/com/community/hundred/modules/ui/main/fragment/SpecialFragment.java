package com.community.hundred.modules.ui.main.fragment;

import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.adapter.MyViewPageAdapter;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.view.GiftItemView;
import com.community.hundred.modules.eventbus.SendGiftWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.ranking.RankingFragment;
import com.community.hundred.modules.ui.main.fragment.specialchild.CircleNewChildFragment;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

// 论坛
public class SpecialFragment extends MyLazyFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.gift_item_first)
    GiftItemView giftItemFirst;
    @BindView(R.id.img_send_post)
    ImageView imgSendPost;
    @BindView(R.id.img_message)
    ImageView imgMessage;


    private List<String> title;
    private List<Fragment> fragmentList;

    private MyViewPageAdapter adapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_special;
    }


    @Override
    protected void initView() {
        title = new ArrayList<>();
        fragmentList = new ArrayList<>();
        title.add("关注");
        title.add("圈子");
        title.add("最新");
        title.add("推荐");
        title.add("排行");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        for (int i = 0; i < title.size(); i++) {
            if (i == 4) {
                fragmentList.add(RankingFragment.getInstance());
            }
            fragmentList.add(CircleNewChildFragment.getInstance(i));
        }
        adapter = new MyViewPageAdapter(getChildFragmentManager(), getContext(), fragmentList, title);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    imgSendPost.setVisibility(View.GONE);
                } else {
                    imgSendPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    // 注册在主线程更新ui
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendGiftWrap(SendGiftWrap wrap) {
        giftItemFirst.setGift(wrap.entry);
        giftItemFirst.addNum(1);
    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    protected boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }


    public static SpecialFragment getInstance() {
        SpecialFragment fragment = new SpecialFragment();
        return fragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.img_message, R.id.img_send_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_message:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.MESSAGE).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.img_send_post:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.SEND_POST).navigation();
                } else {
                    notLogin();
                }
                break;
        }
    }
}
