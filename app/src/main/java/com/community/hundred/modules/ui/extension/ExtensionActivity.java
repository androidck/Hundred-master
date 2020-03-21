package com.community.hundred.modules.ui.extension;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.util.QRCodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

// 分享得VIP
@Route(path = ActivityConstant.EXTENSION)
public class ExtensionActivity extends MyActivity {
    @BindView(R.id.tv_big_img)
    ImageView tvBigImg;
    @BindView(R.id.tv_qr_code)
    ImageView tvQrCode;

    private Bitmap bitmap;

    @Autowired
    String shareUrl;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_extension;
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
    protected void initData() {
        bitmap = QRCodeUtils.createQRCodeBitmap(shareUrl, 400, 400, "UTF-8", "H", "1", Color.BLACK, Color.WHITE);
        tvQrCode.setImageBitmap(bitmap);
    }

}
