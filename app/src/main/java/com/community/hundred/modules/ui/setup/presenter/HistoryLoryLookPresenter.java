package com.community.hundred.modules.ui.setup.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.listener.OnSuccessListener;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.setup.entry.HistoryEntry;
import com.community.hundred.modules.ui.setup.presenter.view.IHistoryLoryLookView;
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

// 历史观看
public class HistoryLoryLookPresenter extends BasePresenter<IHistoryLoryLookView> {

    private OnHistoryListener onHistoryListener;

    private OnSuccessListener onSuccessListener;

    public HistoryLoryLookPresenter(MyActivity context) {
        super(context);
    }

    // 获取观看列表
    public void getHistoryLookList() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.gkjlURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray array = jsonObject.getAsJsonArray("data");
                List<HistoryEntry> list = new Gson().fromJson(array.toString(), new TypeToken<List<HistoryEntry>>() {
                }.getType());
                if (list.size() == 0) {
                    prContext.showEmpty();
                } else {
                    onHistoryListener.onHistory(list);
                    prContext.showComplete();
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

    // 清空历史记录
    public void clearHistory() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.qingkongURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response=new Gson().fromJson(result,BaseResponse.class);
                toast(response.getMsg());
                if (response.getRet()==1){
                    onSuccessListener.onSuccess(1);
                }
                prContext.showEmpty();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 删除历史记录
    public void removeHistory(String id) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        OkHttp.postAsync(HttpConstant.delgkjlURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
            }
        });
    }

    public interface OnHistoryListener {
        void onHistory(List<HistoryEntry> list);
    }

    public void setOnHistoryListener(OnHistoryListener onHistoryListener) {
        this.onHistoryListener = onHistoryListener;
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
