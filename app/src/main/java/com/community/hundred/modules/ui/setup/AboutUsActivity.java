package com.community.hundred.modules.ui.setup;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.setup.presenter.AboutPresenter;
import com.community.hundred.modules.ui.setup.presenter.view.IAboutView;
import com.hjq.bar.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

// 关于我们
@Route(path = ActivityConstant.ABOUT_US)
public class AboutUsActivity extends MyActivity<IAboutView, AboutPresenter> {

    @Override
    protected AboutPresenter createPresenter() {
        return new AboutPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }


    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
    }


    @Override
    protected void initData() {
       mPresenter.getAbout();
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }
}
