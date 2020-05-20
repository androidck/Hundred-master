package com.community.hundred.modules.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.dialog.BaseCustomDialog;
import com.community.hundred.common.web.ShareBrowserActivity;
import com.community.hundred.common.web.SonicJavaScriptInterface;
import com.community.hundred.modules.manager.LoginUtils;

// 分享dialog
public class ShareDialog extends BaseCustomDialog implements View.OnClickListener {

    private Context mContext;

    private Button btnPay;
    private ImageView img_close;
    private TextView tv_tip;

    private OnItemClickListener onItemClickListener;

    public ShareDialog(@NonNull Context context, OnItemClickListener onItemClickListener) {
        super(context);
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_share;
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
        tv_tip.setText("今日免费观看次数已用光，请分享");
        btnPay.setText("去分享");
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
                onItemClickListener.onEscClick(1);
                break;
            case R.id.btn_pay:
                dismiss();
                Intent intent = new Intent(getContext(), ShareBrowserActivity.class);
                intent.putExtra(ShareBrowserActivity.PARAM_URL, HttpConstant.SHARE_URL + LoginUtils.getInstance().getUid());
                intent.putExtra(ShareBrowserActivity.PARAM_TITLE, "全民理财");
                intent.putExtra(ShareBrowserActivity.PARAM_MODE, 1);
                intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
                mContext.startActivity(intent);
                break;
        }
    }

    public interface OnItemClickListener {
        void onEscClick(int position);
    }
}
