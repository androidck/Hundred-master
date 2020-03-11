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
import com.community.hundred.modules.dialog.UpdateDialog;
import com.community.hundred.modules.dialog.entry.GiftEntry;
import com.community.hundred.modules.eventbus.UpdateWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.setup.entry.UpdateEntry;
import com.community.hundred.modules.ui.setup.presenter.SetupPresenter;
import com.community.hundred.modules.ui.setup.presenter.view.ISetupView;
import com.hjq.bar.TitleBar;
import com.hjq.widget.layout.SettingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 设置
@Route(path = ActivityConstant.SETUP)
public class SetupActivity extends MyActivity<ISetupView, SetupPresenter> {

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

    private boolean isForceUpdate;

    @Override
    protected SetupPresenter createPresenter() {
        return new SetupPresenter(this);
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

        EventBus.getDefault().register(this);
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

    @OnClick({R.id.item_user_data, R.id.item_security_code, R.id.item_pwd_update, R.id.item_clear_catch, R.id.item_version_code, R.id.btn_login_out, R.id.item_check_update})
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
            case R.id.item_check_update:
                // 检查更新
                mPresenter.checkUpdate();
                break;


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateWrap(UpdateWrap updateWrap) {
        UpdateEntry entry = updateWrap.entry;
        if (entry.getSta() == 1) {
            isForceUpdate = true;
        } else {
            isForceUpdate = false;
        }
        new UpdateDialog.Builder(this)
                // 版本名
                .setVersionName(entry.getBanbenhao())
                // 是否强制更新
                .setForceUpdate(isForceUpdate)
                // 更新日志
                .setUpdateLog(entry.getShuoming())
                // 下载 url
                .setDownloadUrl(entry.getApk())
                .show();
    }

    /*  */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().isRegistered(null);
    }
}
