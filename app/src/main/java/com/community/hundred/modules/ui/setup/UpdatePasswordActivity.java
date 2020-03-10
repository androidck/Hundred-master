package com.community.hundred.modules.ui.setup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.util.RegexUtils;
import com.community.hundred.modules.ui.setup.presenter.UpdatePasswordPresenter;
import com.community.hundred.modules.ui.setup.presenter.view.IUpdatePasswordView;
import com.hjq.widget.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 修改密码
@Route(path = ActivityConstant.UPDATE_PWD)
public class UpdatePasswordActivity extends MyActivity<IUpdatePasswordView, UpdatePasswordPresenter> implements IUpdatePasswordView {

    @BindView(R.id.ed_phone)
    ClearEditText edPhone;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_code)
    ClearEditText etCode;
    @BindView(R.id.ed_pwd)
    ClearEditText edPwd;
    @BindView(R.id.is_show_pwd)
    ImageView isShowPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected UpdatePasswordPresenter createPresenter() {
        return new UpdatePasswordPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_pwd;
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
    public void isVerification() {
        super.isVerification();
        if (!RegexUtils.checkMobile(edPhone.getText().toString().trim().replace(" ", ""))) {
            toast("请输入正确的手机号");
        } else if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
            toast("请输入短信验证码");
        } else if (TextUtils.isEmpty(edPwd.getText().toString().trim())) {
            toast("请输入密码");
        } else {
            String phone = edPhone.getText().toString().trim().replace(" ", "");
            String code = etCode.getText().toString().trim();
            String pwd = edPwd.getText().toString().trim();
            mPresenter.updatePwd(phone, code, pwd);
        }
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.tv_get_code, R.id.is_show_pwd, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                if (!RegexUtils.checkMobile(edPhone.getText().toString().trim().replace(" ", ""))) {
                    toast("请输入正确的手机号");
                } else {
                    mPresenter.sendCode(edPhone.getText().toString().trim().replace(" ", ""));
                }
                break;
            case R.id.is_show_pwd:
                break;
            case R.id.btn_login:
                isVerification();
                break;
        }
    }

    @Override
    public TextView getSendCode() {
        return tvGetCode;
    }
}
