package com.community.hundred.modules.ui.setup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.BuildConfig;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.helper.CacheDataManager;
import com.community.hundred.modules.dialog.GiftDialog;
import com.community.hundred.modules.dialog.entry.GiftEntry;
import com.community.hundred.modules.manager.LoginUtils;
import com.hjq.bar.TitleBar;
import com.hjq.widget.layout.SettingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 设置
@Route(path = ActivityConstant.SETUP)
public class SetupActivity extends MyActivity {

    @BindView(R.id.item_user_data)
    SettingBar itemUserData;
    @BindView(R.id.item_security_code)
    SettingBar itemSecurityCode;
    @BindView(R.id.item_pwd_update)
    SettingBar itemPwdUpdate;
    @BindView(R.id.item_clear_catch)
    SettingBar itemClearCatch;
    @BindView(R.id.item_version_code)
    SettingBar itemVersionCode;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.tv_email)
    TextView tvEmail;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup;
    }


    @Override
    protected void initView() {
        getTitleBar().setLeftIcon(R.mipmap.bar_icon_back_white);
        if (LoginUtils.getInstance().isLogin()) {
            btnLoginOut.setVisibility(View.VISIBLE);
        } else {
            btnLoginOut.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        // 设置版本号
        itemClearCatch.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
        itemVersionCode.setRightText("V" + BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @OnClick({R.id.item_user_data, R.id.item_security_code, R.id.item_pwd_update, R.id.item_clear_catch, R.id.item_version_code, R.id.btn_login_out})
    public void onViewClicked(View view) {
        if (isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.item_user_data:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.MODIFY_USER_DATA).navigation();
                } else {
                    ARouter.getInstance().build(ActivityConstant.LOGIN).navigation();
                }
                break;
            case R.id.item_security_code:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.SECURITY_CODE).navigation();
                } else {
                    ARouter.getInstance().build(ActivityConstant.LOGIN).navigation();
                }
                break;
            case R.id.item_pwd_update:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.UPDATE_PWD).navigation();
                } else {
                    ARouter.getInstance().build(ActivityConstant.LOGIN).navigation();
                }
                break;
            case R.id.item_clear_catch:
                CacheDataManager.clearAllCache(this);
                postDelayed(() -> {
                    // 重新获取应用缓存大小
                    itemClearCatch.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
                }, 500);
                break;
            case R.id.item_version_code:
                break;
            case R.id.btn_login_out:
                loginOut();
                break;
        }
    }


}
