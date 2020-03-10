package com.community.hundred.modules.ui.setup.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.setup.presenter.view.IAboutView;

import java.io.IOException;

import okhttp3.Request;

// 关于我们
public class AboutPresenter extends BasePresenter<IAboutView> {

    public AboutPresenter(MyActivity context) {
        super(context);
    }


    // 关于我们
    public void getAbout() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.AboutURL, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("aboutView", result);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }
}
