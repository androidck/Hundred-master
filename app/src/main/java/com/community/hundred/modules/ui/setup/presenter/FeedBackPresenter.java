package com.community.hundred.modules.ui.setup.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.setup.presenter.view.IFeedBackView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

// 意见反馈
public class FeedBackPresenter extends BasePresenter<IFeedBackView> {

    public FeedBackPresenter(MyActivity context) {
        super(context);
    }

    // 意见反馈
    public void feedBack(String content) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("content", content);
        OkHttp.postAsync(HttpConstant.yijianURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if ("1".equals(response.getSta())) {
                    toast(response.getMsg());
                    prContext.finish();
                }
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
