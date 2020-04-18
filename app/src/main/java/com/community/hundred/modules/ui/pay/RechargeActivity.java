package com.community.hundred.modules.ui.pay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.adapter.MyViewPageAdapter;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.adapter.PayTypeAdapter;
import com.community.hundred.modules.ui.pay.fragment.BalanceRechargeFragment;
import com.community.hundred.modules.ui.pay.fragment.MemberRechargeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 充值
@Route(path = ActivityConstant.RECHARGE)
public class RechargeActivity extends MyActivity {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<Fragment> fragmentList;
    private List<String> titleList;

    private MyViewPageAdapter adapter;



    @Autowired
    int selectIndex;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        titleList.add("余额充值");
        titleList.add("开通会员");
        fragmentList.add(BalanceRechargeFragment.getInstance());
        fragmentList.add(MemberRechargeFragment.getInstance());
        adapter = new MyViewPageAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(selectIndex);
        viewPager.setCurrentItem(selectIndex);
    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
