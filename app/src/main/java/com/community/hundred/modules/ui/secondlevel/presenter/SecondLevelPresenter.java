package com.community.hundred.modules.ui.secondlevel.presenter;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.secondlevel.entry.SecondEntry;
import com.community.hundred.modules.ui.secondlevel.entry.SecondLevelEntry;
import com.community.hundred.modules.ui.secondlevel.presenter.view.ISecondLevelView;
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

// 获取列表
public class SecondLevelPresenter extends BasePresenter<ISecondLevelView> {

    private OnTabDataListener onTabDataListener;

    private OnVideoListener onVideoListener;

    public SecondLevelPresenter(MyActivity context) {
        super(context);
    }

    // 获取分类
    public void getSecondLevelClassify(String cate) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("cate", cate);
        OkHttp.postAsync(HttpConstant.secondLiveURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = object.getAsJsonArray("list");
                List<SecondLevelEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SecondLevelEntry>>() {
                }.getType());
                if (list.size() == 0) {
                    prContext.showEmpty();
                } else {
                    prContext.showComplete();
                    onTabDataListener.onTab(list);
                }

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                prContext.showComplete();
            }
        });
    }

    // 获取视频列表
    public void getSecondVideoList(String cate_id, String type) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("cate_id", cate_id);
        map.put("type", type);
        OkHttp.postAsync(HttpConstant.secondLiveChildURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = object.getAsJsonArray("list");
                List<SecondEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SecondEntry>>() {
                }.getType());
                if (list.size() == 0) {
                    prContext.showEmpty();
                } else {
                    prContext.showComplete();
                    onVideoListener.onVideo(list);
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

    public interface OnTabDataListener {
        void onTab(List<SecondLevelEntry> list);
    }

    public void setOnTabDataListener(OnTabDataListener onTabDataListener) {
        this.onTabDataListener = onTabDataListener;
    }

    public interface OnVideoListener {
        void onVideo(List<SecondEntry> list);
    }

    public void setOnVideoListener(OnVideoListener onVideoListener) {
        this.onVideoListener = onVideoListener;
    }
}