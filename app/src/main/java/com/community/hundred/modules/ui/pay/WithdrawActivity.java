package com.community.hundred.modules.ui.pay;


import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.pay.presenter.WithdrawPresenter;
import com.community.hundred.modules.ui.pay.presenter.view.IWithdrawView;
import com.google.gson.Gson;
import com.hjq.widget.view.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

// 提现
@Route(path = ActivityConstant.WITHDRAW)
public class WithdrawActivity extends MyActivity<IWithdrawView, WithdrawPresenter> {

    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_zong)
    TextView tvZong;
    @BindView(R.id.tv_ktx)
    TextView tvKtx;
    @BindView(R.id.et_name)
    ClearEditText etName;
    @BindView(R.id.ed_alipay_account)
    ClearEditText edAlipayAccount;
    @BindView(R.id.ed_withdraw_money)
    ClearEditText edWithdrawMoney;
    @BindView(R.id.ed_pwd)
    ClearEditText edPwd;
    @BindView(R.id.is_show_pwd)
    ImageView isShowPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected WithdrawPresenter createPresenter() {
        return new WithdrawPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getMoney();
    }

    @Override
    public void isVerification() {
        super.isVerification();
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            toast("请输入收款人姓名");
        } else if (TextUtils.isEmpty(edAlipayAccount.getText().toString().trim())) {
            toast("请输入收款人支付宝账号");
        } else if (TextUtils.isEmpty(edWithdrawMoney.getText().toString().trim())) {
            toast("请输入提现金额");
        } else if (TextUtils.isEmpty(edPwd.getText().toString().trim())) {
            toast("请输入安全码");
        } else {
            // 提现
            String money = edWithdrawMoney.getText().toString().trim();
            String account = edAlipayAccount.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String aqm = edPwd.getText().toString().trim();
            mPresenter.withDraw(money, account, name, aqm);
        }
    }

    //获取金额
    public void getMoney() {
        mPresenter.getUserMoney();
        mPresenter.setOnMoneyDataListener(entry -> {
            tvMoney.setText(entry.getMoney());
            tvZong.setText(entry.getZong());
            tvKtx.setText(entry.getKtxian());
        });
    }


    @OnClick({R.id.is_show_pwd, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.is_show_pwd:
                break;
            case R.id.btn_login:
                isVerification();
                break;
        }


    }


}
