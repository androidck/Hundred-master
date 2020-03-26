package com.community.hundred.modules.ui.chat.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.eventbus.SendMsgWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.chat.entry.MsgEntry;
import com.community.hundred.modules.ui.chat.presenter.view.IPrivateLetterView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

// 程序
public class PrivateLetterPresenter extends BasePresenter<IPrivateLetterView> {

    private OnDataListener onDataListener;

    public PrivateLetterPresenter(MyActivity context) {
        super(context);
    }

    // 获取聊天详情
    public void getChatDetails(String bid, int p) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("bid", bid);
        map.put("p", String.valueOf(p));
        OkHttp.postAsync(HttpConstant.chatDetails, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                List<MsgEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<MsgEntry>>() {
                }.getType());
                onDataListener.onData(list);

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();
                prContext.showComplete();
            }
        });
    }

    // 发送消息
    public void sendMgs(String content, String bid) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("content", content);
        map.put("bid", bid);
        OkHttp.postAsync(HttpConstant.siliaoURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if (response.getCode() == 200) {
                    EventBus.getDefault().post(SendMsgWrap.getInstance(response.getCode()));
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();

            }
        });
    }

    public interface OnDataListener {
        void onData(List<MsgEntry> list);
    }

    public void setOnDataListener(OnDataListener onDataListener) {
        this.onDataListener = onDataListener;
    }
}
