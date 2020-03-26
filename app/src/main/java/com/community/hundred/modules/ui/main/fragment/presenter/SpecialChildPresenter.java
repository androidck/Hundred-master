package com.community.hundred.modules.ui.main.fragment.presenter;

import android.util.Log;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.listener.OnSuccessListener;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.dialog.entry.GiftEntry;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildHeaderEntry;
import com.community.hundred.modules.ui.main.fragment.entry.RankingEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.view.ISpecialChildView;
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

// 关注列表
public class SpecialChildPresenter extends BasePresenter<ISpecialChildView> {

    public OnSpecialChildListener onSpecialChildListener;

    private OnSuccessListener onSuccessListener;

    private OnGiftListener onGiftListener;

    private OnClassifyListener onClassifyListener;

    private OnCircleChildListener onCircleChildListener;

    private OnRankingListener onRankingListener;


    public SpecialChildPresenter(MyActivity context) {
        super(context);
    }

    // 获取圈子分类
    public void getQzFl() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.qzflURL, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                List<CircleChildHeaderEntry> headerEntryList = new ArrayList<>();
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = object.getAsJsonArray("data");
                for (JsonElement json : jsonArray) {
                    CircleChildHeaderEntry entry = new Gson().fromJson(json, CircleChildHeaderEntry.class);
                    headerEntryList.add(entry);
                }
                onClassifyListener.onClassify(headerEntryList);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                prContext.showComplete();
            }
        });
    }

    // 获取圈子列表
    public void getCircleList(String flid, int x, int y) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("flid", flid);
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        OkHttp.postAsync(HttpConstant.qztzURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if (response.getCode() == 200) {
                    JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                    List<CircleChildEntry> temp = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CircleChildEntry>>() {
                    }.getType());
                    if (temp.size() == 0) {
                        prContext.showEmpty();
                    } else {
                        prContext.showComplete();
                        onCircleChildListener.onCircle(temp);
                    }
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

    // 关注列表
    public void getAttentionList(String url, int x, int y) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        OkHttp.postAsync(url, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if (response.getCode() == 200) {
                    JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                    List<CircleChildEntry> temp = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CircleChildEntry>>() {
                    }.getType());
                    onSpecialChildListener.onSpecial(temp);
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

    // 关注
    public void setAttention(String gid) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("gid", gid);
        OkHttp.postAsync(HttpConstant.ljGuanzhuURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
                onSuccessListener.onSuccess(1);
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                prContext.showComplete();
                netWorkError();
            }
        });
    }

    // 取消关注
    public void setEscAttention(String gid) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("gid", gid);
        OkHttp.postAsync(HttpConstant.quxiaogzURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
                onSuccessListener.onSuccess(2);
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                prContext.showComplete();
                netWorkError();
            }
        });
    }

    // 点赞
    public void setJustLike(String isLove, String circle_id) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.loveCircleURL + "?uid=" + LoginUtils.getInstance().getUid() + "&circle_id=" + circle_id, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if ("0".equals(isLove)) {
                    onSuccessListener.onSuccess(3);
                } else {
                    onSuccessListener.onSuccess(4);
                }
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

    // 获取礼物列表
    public void getGiftList() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.liwuURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("giftlistresult", result);
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonObject jsonObject = object.getAsJsonObject("data");
                JsonArray jsonArray = jsonObject.getAsJsonArray("liwulist");
                List<GiftEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<GiftEntry>>() {
                }.getType());
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                onGiftListener.onGiftList(response.getYue(), list);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 赠送礼物
    public void senGift(String lid, String tid) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("lid", lid);
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("tid", tid);
        OkHttp.postAsync(HttpConstant.dashangURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                onSuccessListener.onSuccess(8);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 举报
    public void setReport(String tid, String content) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("jid", tid);
        map.put("content", content);
        OkHttp.postAsync(HttpConstant.reportURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();
                prContext.showComplete();
            }
        });
    }

    // 获取打赏榜
    public void getReward(String url) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(url, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                List<RankingEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<RankingEntry>>() {
                }.getType());
                onRankingListener.OnRanking(list);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 获取魅力榜
    public void getCharm(int type) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.mlBangURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                JsonArray benri = data.getAsJsonArray("benri");
                JsonArray benzhou = data.getAsJsonArray("benzhou");
                JsonArray benyue = data.getAsJsonArray("benyue");
                JsonArray zongbang = data.getAsJsonArray("zongbang");
                List<RankingEntry> list = null;
                switch (type) {
                    case 1:
                        list = new Gson().fromJson(benri.toString(), new TypeToken<List<RankingEntry>>() {
                        }.getType());
                        break;
                    case 2:
                        list = new Gson().fromJson(benzhou.toString(), new TypeToken<List<RankingEntry>>() {
                        }.getType());
                        break;
                    case 3:
                        list = new Gson().fromJson(benyue.toString(), new TypeToken<List<RankingEntry>>() {
                        }.getType());
                        break;
                    case 4:
                        list = new Gson().fromJson(zongbang.toString(), new TypeToken<List<RankingEntry>>() {
                        }.getType());
                        break;
                }
                onRankingListener.OnRanking(list);
                prContext.showComplete();


            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();
                prContext.showComplete();
            }
        });
    }


    public interface OnSpecialChildListener {
        void onSpecial(List<CircleChildEntry> list);
    }

    public void setOnSpecialChildListener(OnSpecialChildListener onSpecialChildListener) {
        this.onSpecialChildListener = onSpecialChildListener;
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public interface OnGiftListener {
        void onGiftList(double yue, List<GiftEntry> list);
    }

    public void setOnGiftListener(OnGiftListener onGiftListener) {
        this.onGiftListener = onGiftListener;
    }

    public interface OnClassifyListener {
        void onClassify(List<CircleChildHeaderEntry> list);
    }

    public void setOnClassifyListener(OnClassifyListener onClassifyListener) {
        this.onClassifyListener = onClassifyListener;
    }

    public interface OnCircleChildListener {
        void onCircle(List<CircleChildEntry> list);
    }

    public void setOnCircleChildListener(OnCircleChildListener onCircleChildListener) {
        this.onCircleChildListener = onCircleChildListener;
    }

    public interface OnRankingListener {
        void OnRanking(List<RankingEntry> list);
    }

    public void setOnRankingListener(OnRankingListener onRankingListener) {
        this.onRankingListener = onRankingListener;
    }
}
