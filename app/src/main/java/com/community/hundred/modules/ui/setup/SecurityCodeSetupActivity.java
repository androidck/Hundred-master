package com.community.hundred.modules.ui.setup;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.setup.presenter.SecurityCodePresenter;
import com.community.hundred.modules.ui.setup.presenter.view.ISecurityCodeView;
import com.jyn.vcview.VerificationCodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


// 安全码设置
@Route(path = ActivityConstant.SECURITY_CODE)
public class SecurityCodeSetupActivity extends MyActivity<ISecurityCodeView, SecurityCodePresenter> {
    @BindView(R.id.ed_security_code)
    VerificationCodeView edSecurityCode;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;

    private String str;

    @Override
    protected SecurityCodePresenter createPresenter() {
        return new SecurityCodePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_security_code_setup;
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        edSecurityCode.setOnCodeFinishListener(content -> {
            str = content;
        });
    }


    @Override
    protected void initData() {
    }

    @Override
    public void isVerification() {
        super.isVerification();
        if (TextUtils.isEmpty(str)) {
            toast("安全码不能为空");
        } else if (str.length() < 6) {
            toast("请输入6位安全码");
        } else {
            mPresenter.setSecurityCode(str);
        }
    }

    @OnClick(R.id.btn_login_out)
    public void onViewClicked() {
        isVerification();
    }


}
