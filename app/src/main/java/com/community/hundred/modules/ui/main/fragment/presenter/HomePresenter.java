package com.community.hundred.modules.ui.main.fragment.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.main.fragment.entry.BannerEntry;
import com.community.hundred.modules.ui.main.fragment.entry.ForumChildNewEntry;
import com.community.hundred.modules.ui.main.fragment.entry.HomeEntry;
import com.community.hundred.modules.ui.main.fragment.entry.HomeVideoEntry;
import com.community.hundred.modules.ui.main.fragment.entry.NovEntry;
import com.community.hundred.modules.ui.main.fragment.forumchild.ForumChildNewFragment;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IHomeView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

// 首页业务逻辑处理
public class HomePresenter extends BasePresenter<IHomeView> {


    private OnBannerListener onBannerListener;

    private OnHomeClassifyListener onHomeClassifyListener;

    private OnForumChildListener onForumChildListener;

    private OnHomeVideoListener onHomeVideoListener;

    private OnNovListener onNovListener;

    public HomePresenter(MyActivity context) {
        super(context);
    }

    // 获取banner
    public void getBanner(String type) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        OkHttp.postAsync(HttpConstant.BASE_URL + HttpConstant.BANNER, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                ArrayList<BannerEntry> list = new ArrayList<>();
                JsonObject jsonParser = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonElements = jsonParser.getAsJsonArray("data");//获取JsonArray对象
                for (JsonElement json : jsonElements) {
                    BannerEntry entry = new Gson().fromJson(json, BannerEntry.class);
                    list.add(entry);
                }
                onBannerListener.onBannerData(list);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                prContext.showComplete();
                toast("网络链接失败，请稍后重试");
            }
        });
    }

    // 首页tab 标签
    public void getHomeClassify() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.BASE_URL + HttpConstant.HOME_CLASSIFY, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("homeNewChildResult", result);
                JsonObject jsonParser = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonElements = jsonParser.getAsJsonArray("data");//获取JsonArray对象
                ArrayList<HomeEntry> list = new Gson().fromJson(jsonElements.toString(), new TypeToken<ArrayList<HomeEntry>>() {
                }.getType());
                onHomeClassifyListener.onHomeClassify(list);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                prContext.showComplete();
                toast("网络链接失败，请稍后重试");
            }
        });
    }


    // 获取专栏列表
    public void getForumChild(String id, int x, int y) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        OkHttp.postAsync(HttpConstant.zhuanlanURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if (response.getCode() == 200) {
                    JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("date");
                    List<ForumChildNewEntry> list = new ArrayList<>();
                    for (JsonElement json : jsonArray) {
                        ForumChildNewEntry entry = new Gson().fromJson(json, ForumChildNewEntry.class);
                        list.add(entry);
                    }
                    onForumChildListener.onForumList(list);
                } else {
                    toast("暂无数据");
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                toast("网络错误");
            }
        });
    }

    // 获取视频列表
    public void getVideoList(String cate_id, int p) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("cate_id", cate_id);
        map.put("p", String.valueOf(p));
        OkHttp.postAsync(HttpConstant.listURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = object.getAsJsonArray("list");
                List<HomeVideoEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<HomeVideoEntry>>() {
                }.getType());
                prContext.showComplete();
                onHomeVideoListener.onVideoList(list);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 获取小数列表
    public void getNovList() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.novList, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("novListResult", result);
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                JsonArray info = data.getAsJsonArray("info");
                List<NovEntry> novEntries = new Gson().fromJson(info.toString(), new TypeToken<List<NovEntry>>() {
                }.getType());
                onNovListener.onNov(novEntries);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    public interface OnBannerListener {
        void onBannerData(ArrayList<BannerEntry> list);
    }


    public void setOnBannerListener(OnBannerListener onBannerListener) {
        this.onBannerListener = onBannerListener;
    }

    public interface OnHomeClassifyListener {
        void onHomeClassify(List<HomeEntry> list);
    }

    public void setOnHomeClassifyListener(OnHomeClassifyListener onHomeClassifyListener) {
        this.onHomeClassifyListener = onHomeClassifyListener;
    }

    public interface OnForumChildListener {
        void onForumList(List<ForumChildNewEntry> list);
    }

    public void setOnForumChildListener(OnForumChildListener onForumChildListener) {
        this.onForumChildListener = onForumChildListener;
    }

    public interface OnHomeVideoListener {
        void onVideoList(List<HomeVideoEntry> list);
    }

    public void setOnHomeVideoListener(OnHomeVideoListener onHomeVideoListener) {
        this.onHomeVideoListener = onHomeVideoListener;
    }

    public interface OnNovListener {
        void onNov(List<NovEntry> list);
    }

    public void setOnNovListener(OnNovListener onNovListener) {
        this.onNovListener = onNovListener;
    }
}
