package com.community.hundred.modules.ui.setup;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.setup.presenter.ActivationPresenter;
import com.community.hundred.modules.ui.setup.presenter.view.IActivationView;
import com.hjq.widget.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


// 激活码兑换
@Route(path = ActivityConstant.ACTIVATION_CODE)
public class ActivationCodeActivity extends MyActivity<IActivationView, ActivationPresenter> implements IActivationView {
    @BindView(R.id.tv_exchange)
    TextView tvExchange;
    @BindView(R.id.ed_jhm)
    ClearEditText edJhm;

    @Override
    protected ActivationPresenter createPresenter() {
        return new ActivationPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_activation_code;
    }


    @Override
    protected void initView() {
        getTitleBar().setLeftIcon(R.mipmap.bar_icon_back_white);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void isVerification() {
        super.isVerification();
        if (TextUtils.isEmpty(edJhm.getText().toString().trim())){
            toast("请输入你的激活码");
        }else {
            mPresenter.setActivation(edJhm.getText().toString().trim());
        }
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }




    @OnClick(R.id.tv_exchange)
    public void onViewClicked() {
        isVerification();
    }

    @Override
    public ClearEditText getEditText() {
        return edJhm;
    }
}
