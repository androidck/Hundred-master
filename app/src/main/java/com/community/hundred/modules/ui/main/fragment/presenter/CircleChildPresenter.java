package com.community.hundred.modules.ui.main.fragment.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.listener.OnSuccessListener;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.common.util.LocationUtils;
import com.community.hundred.modules.dialog.entry.GiftEntry;
import com.community.hundred.modules.eventbus.CommentWrap;
import com.community.hundred.modules.eventbus.PostDetailsWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildHeaderEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.view.ICircleChildView;
import com.community.hundred.modules.ui.post.entry.CommentEntry;
import com.community.hundred.modules.ui.post.entry.PostDetailsEntry;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

// 圈子列表
public class CircleChildPresenter extends BasePresenter<ICircleChildView> {

    private OnClassifyListener onClassifyListener;

    private OnCircleChildListener onCircleChildListener;

    private OnSuccessListener onSuccessListener;

    private OnGiftListener onGiftListener;

    private OnPostDetailsListener onPostDetailsListener;

    private OnCommentListener onCommentListener;

    public CircleChildPresenter(MyActivity context) {
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
                Log.d("sendGiftResult", result);
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

    // 获取帖子详情
    public void getPostDetails(String id) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.tiezifindURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("postDetailsResult", result);
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                PostDetailsEntry entry = new Gson().fromJson(object.toString(), PostDetailsEntry.class);
                onPostDetailsListener.onPost(entry);
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

    // 获取评论
    public void getCommentCircle(String tid) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.getCommentCircleURL + "&tid=" + tid + "&uid=" + LoginUtils.getInstance().getUid(), new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("getcommentList", result);
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray data = jsonObject.getAsJsonArray("data");
                List<CommentEntry> commentEntries = new Gson().fromJson(data.toString(), new TypeToken<List<CommentEntry>>() {
                }.getType());
                onCommentListener.onCommentList(commentEntries);
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
            }
        });
    }

    // 评论
    public void setComment(String circle_id, String content) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.commentCircleURL + "?uid=" + LoginUtils.getInstance().getUid() + "&circle_id=" + circle_id + "&content=" + content, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
                if (response.getCode() == 200) {
                    EventBus.getDefault().post(CommentWrap.getInstance(response.getCode()));
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
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
                Log.d("reportResult", result);
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();
                prContext.showComplete();
            }
        });
    }

    // 评论点赞
    public void commentLove(String circle_id) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.commentLoveURL + "?uid=" + LoginUtils.getInstance().getUid() + "&comment_id=" + circle_id, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("shanchuResult", result);
                prContext.showComplete();
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if (response.getCode() == 200) {
                    EventBus.getDefault().post(CommentWrap.getInstance(response.getCode()));
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

    // 删除评论
    public void delComment(String pid) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.commentDel + "?uid=" + LoginUtils.getInstance().getUid() + "&pid=" + pid, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if (response.getCode() == 200) {
                    EventBus.getDefault().post(CommentWrap.getInstance(response.getCode()));
                }
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


    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public interface OnGiftListener {
        void onGiftList(double yue, List<GiftEntry> list);
    }

    public void setOnGiftListener(OnGiftListener onGiftListener) {
        this.onGiftListener = onGiftListener;
    }

    public interface OnPostDetailsListener {
        void onPost(PostDetailsEntry entry);
    }

    public void setOnPostDetailsListener(OnPostDetailsListener onPostDetailsListener) {
        this.onPostDetailsListener = onPostDetailsListener;
    }

    public interface OnCommentListener {
        void onCommentList(List<CommentEntry> commentEntries);
    }

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }
}
