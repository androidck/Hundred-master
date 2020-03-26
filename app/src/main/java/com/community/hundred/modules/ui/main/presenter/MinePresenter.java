package com.community.hundred.modules.ui.main.presenter;

import android.content.Intent;
import android.net.Uri;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.main.entry.UserCenterEntry;
import com.community.hundred.modules.ui.main.presenter.view.IMineView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class MinePresenter extends BasePresenter<IMineView> {

    private OnUserCenterListener onUserCenterListener;

    public MinePresenter(MyActivity context) {
        super(context);
    }

    // 获取个人中心资料
    public void getUserCenter(String uid) {
        Map<String, String> map = new HashMap();
        map.put("uid", uid);
        OkHttp.postAsync(HttpConstant.userURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if ("1".equals(response.getSta())) {
                    JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                    JsonObject jsonObject = object.getAsJsonObject().getAsJsonObject("data");
                    UserCenterEntry entry = new Gson().fromJson(jsonObject.toString(), UserCenterEntry.class);
                    onUserCenterListener.onUserCenter(entry);
                } else {
                    toast(response.getMsg());
                }


            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                toast("网络请求失败");
            }
        });
    }

    // 在线娱乐
    public void getOnlinePlay() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        OkHttp.postAsync(HttpConstant.zxylURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(response.getData().toString());
                intent.setData(content_url);
                prContext.startActivity(intent);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();
                prContext.showComplete();
            }
        });
    }

    // 交流群
    public void getJiaoLiuQun() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        OkHttp.postAsync(HttpConstant.jlqlURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(response.getData().toString());
                intent.setData(content_url);
                prContext.startActivity(intent);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();
                prContext.showComplete();
            }
        });
    }

    public interface OnUserCenterListener {
        void onUserCenter(UserCenterEntry entry);
    }

    public void setOnUserCenterListener(OnUserCenterListener onUserCenterListener) {
        this.onUserCenterListener = onUserCenterListener;
    }
}
