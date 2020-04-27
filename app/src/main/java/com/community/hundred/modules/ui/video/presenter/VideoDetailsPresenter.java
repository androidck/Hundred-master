package com.community.hundred.modules.ui.video.presenter;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.femalestar.entry.FemaleInfoEntry;
import com.community.hundred.modules.ui.femalestar.entry.FemaleStarEntry;
import com.community.hundred.modules.ui.femalestar.presenter.FemaleStarPresenter;
import com.community.hundred.modules.ui.main.fragment.entry.BannerEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.HomePresenter;
import com.community.hundred.modules.ui.video.entry.VideoDetailsEntry;
import com.community.hundred.modules.ui.video.entry.VideoZanAndLikeEntry;
import com.community.hundred.modules.ui.video.presenter.view.IVideoDetailsView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

// 视频详情处理
public class VideoDetailsPresenter extends BasePresenter<IVideoDetailsView> {

    private OnVideoDetailsListener onVideoDetailsListener;

    private OnFemaleStarDetailsListener onFemaleStarDetailsListener;

    private OnBannerListener onBannerListener;

    public VideoDetailsPresenter(MyActivity context) {
        super(context);
    }

    // 获取视频详情
    public void getVideoDetails(String videoid) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.videoDetails + "&uid=" + LoginUtils.getInstance().getUid() + "&videoid=" + videoid, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("videoDetails", result);
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonObject jsonObject = object.getAsJsonObject("data");
                VideoDetailsEntry entry = new Gson().fromJson(jsonObject.toString(), VideoDetailsEntry.class);
                if (entry.getIslike() == 1) {
                    Drawable drawableLeft = prContext.getDrawable(R.mipmap.icon_yizan);
                    getView().getLike().setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                } else if (entry.getIslike() == 0) {
                    Drawable drawableLeft = prContext.getDrawable(R.mipmap.icon_zan);
                    getView().getLike().setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                }
                onVideoDetailsListener.onVideoDetails(entry);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
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

    // 添加观看记录
    public void addLook(String videoId) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("shipingid", videoId);
        OkHttp.postAsync(HttpConstant.addGkjlURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("addlook", result);
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 视频点赞
    public void addLike(String videoId) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.addLikeURL + "&uid=" + LoginUtils.getInstance().getUid() + "&videoid=" + videoId, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                JsonArray jsonArray = object.getAsJsonArray("info");
                VideoZanAndLikeEntry entry = new Gson().fromJson(jsonArray.get(0), VideoZanAndLikeEntry.class);
                getView().getLike().setText(entry.getLikes());
                if (entry.getIslike().equals("1")) {
                    Drawable drawableLeft = prContext.getDrawable(R.mipmap.icon_yizan);
                    getView().getLike().setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                } else if (entry.getIslike().equals("0")) {
                    Drawable drawableLeft = prContext.getDrawable(R.mipmap.icon_zan);
                    getView().getLike().setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
            }
        });
    }

    // 视频踩
    public void addStep(String videoId) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.addStepURL + "&uid=" + LoginUtils.getInstance().getUid() + "&videoid=" + videoId, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("addStep", result);
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                JsonArray jsonArray = object.getAsJsonArray("info");
                VideoZanAndLikeEntry entry = new Gson().fromJson(jsonArray.get(0), VideoZanAndLikeEntry.class);
                getView().getSetup().setText(entry.getSteps());
                if (entry.getIsstep().equals("1")) {
                    Drawable drawableLeft = prContext.getDrawable(R.mipmap.icon_yicai);
                    getView().getSetup().setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                } else if (entry.getIsstep().equals("0")) {
                    Drawable drawableLeft = prContext.getDrawable(R.mipmap.icon_cai);
                    getView().getSetup().setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
            }
        });
    }

    // 获取女星详情 pid=9&p=1&order=1
    public void getFemaleStarDetails(String pid, String p, String order) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.nxDetailsURL + "&pid=" + pid + "&p=" + p + "&order=" + order, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                // 获取女星信息
                JsonObject thisInfo = data.getAsJsonObject("thisinfo");
                // 获取视频列表
                JsonArray jsonArray = data.getAsJsonArray("info");
                FemaleStarEntry entry = gson.fromJson(thisInfo.toString(), FemaleStarEntry.class);
                List<FemaleInfoEntry> list = gson.fromJson(jsonArray.toString(), new TypeToken<List<FemaleInfoEntry>>() {
                }.getType());
                onFemaleStarDetailsListener.onFemaleStar(entry, list);
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 添加收藏
    public void addCollect(String videoId) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("shipingid", videoId);
        OkHttp.postAsync(HttpConstant.addsclURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("addCollect", result);
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    public interface OnFemaleStarDetailsListener {
        void onFemaleStar(FemaleStarEntry entrys, List<FemaleInfoEntry> list);
    }

    public void setOnFemaleStarDetailsListener(OnFemaleStarDetailsListener onFemaleStarDetailsListener) {
        this.onFemaleStarDetailsListener = onFemaleStarDetailsListener;
    }

    public interface OnVideoDetailsListener {
        void onVideoDetails(VideoDetailsEntry entry);
    }

    public void setOnVideoDetailsListener(OnVideoDetailsListener onVideoDetailsListener) {
        this.onVideoDetailsListener = onVideoDetailsListener;
    }


    public interface OnBannerListener {
        void onBannerData(ArrayList<BannerEntry> list);
    }


    public void setOnBannerListener(OnBannerListener onBannerListener) {
        this.onBannerListener = onBannerListener;
    }

}
