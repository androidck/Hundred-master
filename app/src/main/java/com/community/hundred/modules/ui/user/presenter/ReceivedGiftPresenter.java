package com.community.hundred.modules.ui.user.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.user.entry.GiftEntry;
import com.community.hundred.modules.ui.user.presenter.view.IReceivedGiftView;
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

// 礼物
public class ReceivedGiftPresenter extends BasePresenter<IReceivedGiftView> {

    private OnDataListener onDataListener;

    public ReceivedGiftPresenter(MyActivity context) {
        super(context);
    }

    // 获取礼物列表
    public void getMyGiftList() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.mylwlistURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("mygiftResult", result);
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

    public interface OnDataListener {
        void onData(List<GiftEntry> list);
    }

    public void setOnDataListener(OnDataListener onDataListener) {
        this.onDataListener = onDataListener;
    }
}
