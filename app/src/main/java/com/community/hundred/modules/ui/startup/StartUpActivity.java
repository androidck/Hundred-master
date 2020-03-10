package com.community.hundred.modules.ui.startup;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.network.permission.Permission;
import com.community.hundred.modules.ui.startup.presenter.StartUpPresenter;
import com.community.hundred.modules.ui.startup.presenter.view.IStartUpView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 启动页
@Route(path = ActivityConstant.START_UP)
public class StartUpActivity extends MyActivity<IStartUpView, StartUpPresenter> implements IStartUpView {

    @BindView(R.id.img_start)
    ImageView imgStart;
    private int empower;//默认0 全部授权

    @Override
    protected StartUpPresenter createPresenter() {
        return new StartUpPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start_up;
    }


    @Override
    protected void initView() {
        setEmpower();
    }

    //授权
    private void setEmpower() {
        XXPermissions.with(StartUpActivity.this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Permission.Group.LOCATION, Permission.Group.STORAGE, Permission.Group.CAMERA_GROUP) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            empower = 0;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //ARouter.getInstance().build("/ui/TestActivity").navigation();
                                    ARouter.getInstance().build(ActivityConstant.MAIN).navigation();
                                    finish();
                                }
                            }, 3000);
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            empower = 1;
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(StartUpActivity.this);
                        } else {
                            empower = 1;
                            toast("获取权限失败");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(StartUpActivity.this);
                        }
                    }
                });
    }

    @Override
    protected void initData() {
        mPresenter.startUp();
    }

    @Override
    public ImageView getImageView() {
        return imgStart;
    }

}
