package com.community.hundred.modules.dialog.entry;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.dialog.BaseCustomDialog;

// 会员开通dialog
public class OpenMemberDialog extends BaseCustomDialog implements View.OnClickListener {

    private Context mContext;

    private Button btnPay;
    private ImageView img_close;

    private TextView tv_tip;

    public OpenMemberDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_short_age;
    }

    @Override
    public void initView() {
        btnPay = findViewById(R.id.btn_pay);
        img_close = findViewById(R.id.img_close);
        btnPay.setOnClickListener(this);
        img_close.setOnClickListener(this);
        tv_tip = findViewById(R.id.tv_tip);
    }

    @Override
    public void initData() {
        tv_tip.setText("今日免费观看次数已用光，请开通会员！");
        btnPay.setText("去开通");
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public int showGravity() {
        return Gravity.CENTER;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_pay:
                dismiss();
                ARouter.getInstance().build(ActivityConstant.RECHARGE).navigation();
                break;
        }
    }
}
