package com.community.hundred.modules.ui.user;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.util.RegexUtils;
import com.community.hundred.modules.ui.user.presenter.RegisterPresenter;
import com.community.hundred.modules.ui.user.presenter.view.IRegisterView;
import com.hjq.bar.TitleBar;
import com.hjq.widget.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


// 注册
@Route(path = ActivityConstant.REGISTER)
public class RegisterActivity extends MyActivity<IRegisterView, RegisterPresenter> implements IRegisterView {
    @BindView(R.id.view_title)
    TitleBar viewTitle;
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
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.for_got_pwd)
    TextView forGotPwd;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;

    private boolean isPswVisible;

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }


    @Override
    protected void initView() {
        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append("注册即表示同意用户注册协议");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        };
        style.setSpan(clickableSpan1, 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreement.setText(style);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.tv_get_code, R.id.btn_login, R.id.tv_register, R.id.for_got_pwd,R.id.is_show_pwd})
    public void onViewClicked(View view) {
        if (isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_get_code:
                if (!RegexUtils.checkMobile(edPhone.getText().toString().trim().replace(" ", ""))) {
                    toast("请输入正确的手机号");
                } else {
                    mPresenter.sendMsg(edPhone.getText().toString().trim().replace(" ", ""));
                }
                break;
            case R.id.btn_login:
                isVerification();
                break;
            case R.id.tv_register:
                ARouter.getInstance().build(ActivityConstant.LOGIN).navigation();
                break;
            case R.id.for_got_pwd:
                ARouter.getInstance().build(ActivityConstant.FOR_GET_PWD).navigation();
                break;
            case R.id.is_show_pwd:
                setPswVisible();
                break;
        }
    }

    @Override
    public TextView sendMsg() {
        return tvGetCode;
    }

    @Override
    public void isVerification() {
        super.isVerification();
        if (!RegexUtils.checkMobile(edPhone.getText().toString().trim().replace(" ", ""))){
            toast("请输入正确的手机号");
        }else if (TextUtils.isEmpty(etCode.getText().toString().trim())){
            toast("请输入短信验证码");
        }else if (TextUtils.isEmpty(edPwd.getText().toString().trim())){
            toast("请输入密码");
        }else {
            register();
        }
    }

    public void register(){
        mPresenter.register(edPhone.getText().toString().trim().replace(" ", ""),
                edPwd.getText().toString().trim(),
                etCode.getText().toString().trim());
    }

    private void setPswVisible() {
        //定义全局变量，isPswVisible ：密码是否显示
        isPswVisible = !isPswVisible;
        if (isPswVisible) {
            //设置图片，隐藏或者显示图片
            isShowPwd.setImageResource(R.mipmap.icon_show_pwd);
            //显示密码方法一
            HideReturnsTransformationMethod method2 = HideReturnsTransformationMethod.getInstance();
            edPwd.setTransformationMethod(method2);
        } else {
            //设置图片，隐藏或者显示图片
            isShowPwd.setImageResource(R.mipmap.icon_hide_pwd);
            //隐藏密码方法一
            PasswordTransformationMethod method1 = PasswordTransformationMethod.getInstance();
            edPwd.setTransformationMethod(method1);
        }
        //切换后将EditText光标置于末尾
        edPwd.setSelection(edPwd.getText().toString().length());

    }
}
