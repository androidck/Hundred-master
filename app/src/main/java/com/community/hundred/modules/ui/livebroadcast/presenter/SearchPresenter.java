package com.community.hundred.modules.ui.livebroadcast.presenter;


import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.livebroadcast.entry.HotEntry;
import com.community.hundred.modules.ui.livebroadcast.entry.SearchEntry;
import com.community.hundred.modules.ui.livebroadcast.entry.SearchHotEntry;
import com.community.hundred.modules.ui.livebroadcast.presenter.view.ISearchView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

// 搜索
public class SearchPresenter extends BasePresenter<ISearchView> {

    private OnVideoSearchListener onVideoSearchListener;

    private OnHotVideoSearchListener onHotVideoSearchListener;

    public SearchPresenter(MyActivity context) {
        super(context);
    }

    // 首页视频搜索
    public void getHomeVideoSearch(String key, int p) {
        prContext.showLoading();
        String url = HttpConstant.homeVideoSearchURL + "&uid=" + LoginUtils.getInstance().getUid() + "&key=" + key + "&p=" + p;
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                List<SearchEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SearchEntry>>() {
                }.getType());
                if (list.size() == 0) {
                    prContext.showEmpty();
                } else {
                    prContext.showComplete();
                    onVideoSearchListener.onVideoData(list);
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

    // 视频 历史 热门搜索
    public void getHomeVideoMast() {
        prContext.showLoading();
        String url = HttpConstant.homeVideoMast + "&uid=" + LoginUtils.getInstance().getUid();
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                JsonArray jsonArray = object.getAsJsonArray("histry");
                ArrayList<HotEntry> arrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<HotEntry>>() {
                }.getType());
                SearchHotEntry entry = new Gson().fromJson(object.toString(), SearchHotEntry.class);
                onHotVideoSearchListener.onHotVideo(entry.getHot(), arrayList);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 小说搜索
    public void getNovSearch(String key, int p) {
        prContext.showLoading();
        String url = HttpConstant.homeNovSearchURL + "&uid=" + LoginUtils.getInstance().getUid() + "&key=" + key + "&p=" + p;
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("searchResult", result);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    //
    // 专栏搜索
    public void getZhuanLanSearch(String key, int p) {
        prContext.showLoading();
        String url = HttpConstant.zlSearchURL + "&uid=" + LoginUtils.getInstance().getUid() + "&key=" + key + "&p=" + p;
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                List<SearchEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SearchEntry>>() {
                }.getType());
                if (list.size() == 0) {
                    prContext.showEmpty();
                } else {
                    prContext.showComplete();
                    onVideoSearchListener.onVideoData(list);
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

    // 专栏热门搜索
    public void getZhuanLanHotSearch(){
        prContext.showLoading();
        String url = HttpConstant.zlMast + "&uid=" + LoginUtils.getInstance().getUid();
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                JsonArray jsonArray = object.getAsJsonArray("histry");
                ArrayList<HotEntry> arrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<HotEntry>>() {
                }.getType());
                SearchHotEntry entry = new Gson().fromJson(object.toString(), SearchHotEntry.class);
                onHotVideoSearchListener.onHotVideo(entry.getHot(), arrayList);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    public interface OnVideoSearchListener {
        void onVideoData(List<SearchEntry> list);
    }

    public void setOnVideoSearchListener(OnVideoSearchListener onVideoSearchListener) {
        this.onVideoSearchListener = onVideoSearchListener;
    }

    public interface OnHotVideoSearchListener {
        void onHotVideo(String hot, ArrayList<HotEntry> arrayList);
    }

    public void setOnHotVideoSearchListener(OnHotVideoSearchListener onHotVideoSearchListener) {
        this.onHotVideoSearchListener = onHotVideoSearchListener;
    }
}
