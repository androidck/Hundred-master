package com.community.hundred.modules.ui.setup.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.common.util.CountDownTimerUtils;
import com.community.hundred.modules.ui.setup.presenter.view.IUpdatePasswordView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class UpdatePasswordPresenter extends BasePresenter<IUpdatePasswordView> {
    public UpdatePasswordPresenter(MyActivity context) {
        super(context);
    }

    // 发送验证码
    public void sendCode(String phone) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttp.postAsync(HttpConstant.BASE_URL + HttpConstant.SEND_MSG, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("sendMsg", result);
                //{"code":200,"msg":554450,"time":"1581942880","data":"发送成功"}
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if (response.getCode() == 200) {
                    CountDownTimerUtils mCountDownTimerUtils =
                            new CountDownTimerUtils(getView().getSendCode(), 60000, 1000);
                    mCountDownTimerUtils.start();
                }
                toast(response.getData() + "");

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
            }
        });
    }

    // 修改密码
    public void updatePwd(String phone, String password, String code) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("code", code);
        String url = HttpConstant.BASE_URL + HttpConstant.FOR_GET_PWD + "?phone=" + map.get("phone") + "&password=" + map.get("password") + "&code=" + map.get("code");
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("updatepwd", result);
                //{"status":"error","code":400,"message":"无效验证吗，请重新发送"}
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if ("success".equals(response.getStatus())){
                }else {
                }
                toast(response.getMessage());

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
