package com.community.hundred.modules.ui.pay.fragment;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;

// 会员充值
public class MemberRechargeFragment extends MyLazyFragment {
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recharge;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public static MemberRechargeFragment getInstance() {
        MemberRechargeFragment fragment = new MemberRechargeFragment();
        return fragment;
    }
}
