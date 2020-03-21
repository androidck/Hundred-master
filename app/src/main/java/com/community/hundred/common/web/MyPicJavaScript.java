package com.community.hundred.common.web;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.common.constant.ActivityConstant;
import com.hjq.toast.ToastUtils;

// 调用js 方法处理
public class MyPicJavaScript {

    private Context mContext;
    private ClipboardManager cm;
    private ClipData mClipData;

    public MyPicJavaScript(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public void dianji(String url) {
        ARouter.getInstance().build(ActivityConstant.EXTENSION)
                .withString("shareUrl", url)
                .navigation();
    }

    @JavascriptInterface
    public void fuzhi(String url) {
        //获取剪贴板管理器：
        cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipData = ClipData.newPlainText("Label", url);
        cm.setPrimaryClip(mClipData);
        ToastUtils.show("链接已复制");
    }
}
