package com.community.hundred.modules.ui.bigimg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.community.hundred.R;
import com.community.hundred.common.aop.CheckNet;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.other.IntentKey;
import com.community.hundred.common.util.StatusBarUtil;
import com.community.hundred.modules.adapter.ImagePagerAdapter;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import com.rd.PageIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/03/05
 *    desc   : 查看大图
 */
public final class ImageActivity extends MyActivity {

    @BindView(R.id.vp_image_pager)
    ViewPager mViewPager;
    @BindView(R.id.pv_image_indicator)
    PageIndicatorView mIndicatorView;

    public static void start(Context context, String url) {
        ArrayList<String> images = new ArrayList<>(1);
        images.add(url);
        start(context, images);
    }

    public static void start(Context context, ArrayList<String> urls) {
        start(context, urls, 0);
    }

    @CheckNet
    public static void start(Context context, ArrayList<String> urls, int index) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(IntentKey.PICTURE, urls);
        intent.putExtra(IntentKey.INDEX, index);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMode(this, false, R.color.black);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    protected void initView() {
        mIndicatorView.setViewPager(mViewPager);
    }


    @Override
    protected void initData() {
        ArrayList<String> images = getStringArrayList(IntentKey.PICTURE);
        int index = getInt(IntentKey.INDEX);
        if (images != null && images.size() > 0) {
            mViewPager.setAdapter(new ImagePagerAdapter(this, images));
            if (index != 0 && index <= images.size()) {
                mViewPager.setCurrentItem(index);
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}