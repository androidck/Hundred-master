package com.community.hundred.modules.ui.user.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.user.entry.FansEntry;
import com.community.hundred.modules.ui.user.presenter.view.IFansView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

// 粉丝业务逻辑处理
public class FansPresenter extends BasePresenter<IFansView> {

    private OnDataListener onDataListener;

    public FansPresenter(MyActivity context) {
        super(context);
    }

    // 获取粉丝列表
    public void getMyFansList() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.guanzhumyURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonObject = object.getAsJsonArray("data");
                List<FansEntry> list = new Gson().fromJson(jsonObject.toString(), new TypeToken<List<FansEntry>>() {
                }.getType());
                if (list.size() != 0) {
                    prContext.showComplete();
                    onDataListener.onData(list);
                } else {
                    prContext.showEmpty();
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showError();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    public interface OnDataListener {
        void onData(List<FansEntry> list);
    }

    public void setOnDataListener(OnDataListener onDataListener) {
        this.onDataListener = onDataListener;
    }
}
