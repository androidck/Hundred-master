package com.community.hundred.common.util;

import android.app.Activity;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;

import java.util.List;

/**
 * 权限申请工具类
 */
public class PermissionUtils {

    private static volatile PermissionUtils instance=null;
    public static Activity mContext;

    public PermissionUtils(Activity context){
        this.mContext=context;
    }

    //单利模式
    public static PermissionUtils getInstance(Activity context){
        if (instance==null){
            synchronized (PermissionUtils.class){
                if (instance==null){
                    instance=new PermissionUtils(context);
                }
            }
        }
        return instance;
    }

    //请求权限
    public void requestPermission(String[]... permissions){
        XXPermissions.with(mContext)
                .permission(permissions) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            ToastUtils.show("获取权限成功");
                        }else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }
                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if(quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        }else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
    }


    //判断是否授权
    public boolean isHasPermission(String[]... permissions) {
        if (XXPermissions.isHasPermission(mContext, permissions)) {
            return true;
        }else {
            return false;
        }
    }

    //跳转到权限设置页面
    public void gotoPermissionSettings() {
        XXPermissions.gotoPermissionSettings(mContext);
    }
}
