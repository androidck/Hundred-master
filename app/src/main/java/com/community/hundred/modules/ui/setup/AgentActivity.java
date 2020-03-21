package com.community.hundred.modules.ui.setup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;

import butterknife.BindView;
import butterknife.ButterKnife;


@Route(path = ActivityConstant.AGENT)
public class AgentActivity extends MyActivity {
    @BindView(R.id.iv_long_img)
    ImageView ivBig;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_agent;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.img_agent);
        setImageViewWideHigh(ivBig, bitmap);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }


    public void setImageViewWideHigh(ImageView imageView, Bitmap bitmap) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        // 获得bitmap宽高
        float bitWidth = bitmap.getWidth();
        float bithight = bitmap.getHeight();
        float bitScalew = bithight / bitWidth;
        // 获得屏幕宽高（有多种方式、用自己喜欢的）
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int imgWidth = outMetrics.widthPixels;
        int imgHight = outMetrics.heightPixels;
        // 根据需求展示长图、宽填满，高按比例设置
        params.width = (int) imgWidth;
        params.height = (int) (imgWidth * bitScalew);
        // ImageView 控件设置
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
