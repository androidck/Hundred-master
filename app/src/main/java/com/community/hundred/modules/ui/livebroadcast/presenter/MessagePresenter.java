package com.community.hundred.modules.ui.livebroadcast.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.livebroadcast.entry.SystemEntry;
import com.community.hundred.modules.ui.livebroadcast.presenter.view.IMessageView;
import com.community.hundred.modules.ui.user.entry.GiftEntry;
import com.community.hundred.modules.ui.user.presenter.ReceivedGiftPresenter;
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

public class MessagePresenter extends BasePresenter<IMessageView> {

    private OnSystemInfoListener onSystemInfoListener;

    private OnDataListener onDataListener;

    public MessagePresenter(MyActivity context) {
        super(context);
    }

    // 获取系统消息
    public void getSystemInfo() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.xtxxURL, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray array = jsonObject.getAsJsonArray("data").getAsJsonArray();
                List<SystemEntry> list = new Gson().fromJson(array.toString(), new TypeToken<List<SystemEntry>>() {
                }.getType());
                prContext.showComplete();
                onSystemInfoListener.onSystem(list);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 获取礼物列表
    public void getMyGiftList() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.mylwlistURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonObject = object.getAsJsonArray("data");
                List<GiftEntry> list = new Gson().fromJson(jsonObject.toString(), new TypeToken<List<GiftEntry>>() {
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
                e.printStackTrace();
                prContext.showComplete();
                netWorkError();
            }
        });
    }

    public interface OnSystemInfoListener {
        void onSystem(List<SystemEntry> list);
    }

    public void setOnSystemInfoListener(OnSystemInfoListener onSystemInfoListener) {
        this.onSystemInfoListener = onSystemInfoListener;
    }

    public interface OnDataListener {
        void onData(List<GiftEntry> list);
    }

    public void setOnDataListener(OnDataListener onDataListener) {
        this.onDataListener = onDataListener;
    }
}
