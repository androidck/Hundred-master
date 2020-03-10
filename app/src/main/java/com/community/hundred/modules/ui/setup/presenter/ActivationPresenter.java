package com.community.hundred.modules.ui.setup.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.setup.presenter.view.IActivationView;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Request;

// 激活码兑换
public class ActivationPresenter extends BasePresenter<IActivationView> {

    public ActivationPresenter(MyActivity context) {
        super(context);
    }

    // 激活码兑换
    public void setActivation(String cardnum) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.jhmdhURL + "&uid=" + LoginUtils.getInstance().getUid() + "&cardnum=" + cardnum, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("activationResult", result);
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
                getView().getEditText().setText("");
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                getView().getEditText().setText("");
                e.printStackTrace();
                netWorkError();
            }
        });
    }
}
