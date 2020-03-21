package com.community.hundred.modules.ui.setup.presenter;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.listener.OnSuccessListener;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.setup.entry.HistoryEntry;
import com.community.hundred.modules.ui.setup.presenter.view.IMyCollectView;
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

// 收藏
public class MyCollectPresenter extends BasePresenter<IMyCollectView> {

    public MyCollectPresenter(MyActivity context) {
        super(context);
    }

    private OnListDataListener onListDataListener;

    private OnSuccessListener onSuccessListener;

    // 获取收藏列表
    public void getMyCollectList() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.myscjlURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                List<HistoryEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HistoryEntry>>() {
                }.getType());
                if (list.size() == 0) {
                    prContext.showEmpty();
                } else {
                    onListDataListener.onList(list);
                    prContext.showComplete();
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

    // 取消收藏
    public void escCollect(String id) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        OkHttp.postAsync(HttpConstant.delmyscURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
            }
        });
    }

    // 清空收藏
    public void clearCollect() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.delmyscqkURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
                if (response.getRet() == 1) {
                    onSuccessListener.onSuccess(1);
                }
                prContext.showEmpty();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                netWorkError();
            }
        });
    }

    public interface OnListDataListener {
        void onList(List<HistoryEntry> list);
    }

    public void setOnListDataListener(OnListDataListener onListDataListener) {
        this.onListDataListener = onListDataListener;
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
