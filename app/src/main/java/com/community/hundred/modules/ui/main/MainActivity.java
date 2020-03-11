package com.community.hundred.modules.ui.main;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.helper.ActivityStackManager;
import com.community.hundred.common.helper.DoubleClickHelper;
import com.community.hundred.common.other.KeyboardWatcher;
import com.community.hundred.modules.dialog.ShortageDialog;
import com.community.hundred.modules.dialog.UpdateDialog;
import com.community.hundred.modules.ui.main.fragment.ForumNewFragment;
import com.community.hundred.modules.ui.main.fragment.HomeNewFragment;
import com.community.hundred.modules.ui.main.fragment.LiveBroadcastFragment;
import com.community.hundred.modules.ui.main.fragment.MineFragment;
import com.community.hundred.modules.ui.main.fragment.SpecialFragment;
import com.community.hundred.modules.ui.main.presenter.MainPresenter;
import com.community.hundred.modules.ui.main.presenter.view.IMainView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.widget.layout.NoScrollViewPager;

import butterknife.BindView;

// 主页
@Route(path = ActivityConstant.MAIN)
public class MainActivity extends MyActivity<IMainView, MainPresenter> implements BottomNavigationView.OnNavigationItemSelectedListener, KeyboardWatcher.SoftKeyboardStateListener {

    @BindView(R.id.vp_home_pager)
    NoScrollViewPager vpHomePager;
    @BindView(R.id.bv_home_navigation)
    BottomNavigationView bvHomeNavigation;

    // 组件页 布局
    private BaseFragmentAdapter<MyLazyFragment> mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
        // 不使用图标默认变色
        bvHomeNavigation.setItemIconTintList(null);
        bvHomeNavigation.setOnNavigationItemSelectedListener(this);
        KeyboardWatcher.with(this)
                .setListener(this);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(HomeNewFragment.getInstance());// 首页
        mPagerAdapter.addFragment(ForumNewFragment.getInstance());// 专栏
        mPagerAdapter.addFragment(SpecialFragment.getInstance());// 论坛
        mPagerAdapter.addFragment(LiveBroadcastFragment.getInstance());// 直播
        mPagerAdapter.addFragment(MineFragment.getInstance());// 我的
        vpHomePager.setAdapter(mPagerAdapter);
        // 限制页面数量
        vpHomePager.setOffscreenPageLimit(mPagerAdapter.getCount());

        mPresenter.notice();
    }


    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }


    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            //移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 进行内存优化，销毁掉所有的界面
                    ActivityStackManager.getInstance().finishAllActivities();
                }
            }, 300);
        } else {
            toast(R.string.home_exit_hint);
        }
    }

    protected void onDestroy() {
        vpHomePager.setAdapter(null);
        bvHomeNavigation.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                mPagerAdapter.setCurrentItem(HomeNewFragment.class);
                return true;
            case R.id.menu_forum:
                mPagerAdapter.setCurrentItem(ForumNewFragment.class);
                return true;
            case R.id.menu_special:
                mPagerAdapter.setCurrentItem(SpecialFragment.class);
                return true;
            case R.id.menu_live_broadcast:
                mPagerAdapter.setCurrentItem(LiveBroadcastFragment.class);
                return true;
            case R.id.menu_mine:
                mPagerAdapter.setCurrentItem(MineFragment.class);
                return true;
        }
        return false;
    }


    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        bvHomeNavigation.setVisibility(View.GONE);
    }

    @Override
    public void onSoftKeyboardClosed() {
        bvHomeNavigation.setVisibility(View.VISIBLE);
    }
}
