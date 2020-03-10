package com.community.hundred.modules.ui.post.presenter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.adapter.CircleAdapter;
import com.community.hundred.modules.dialog.GiftDialog;
import com.community.hundred.modules.dialog.ReportDialog;
import com.community.hundred.modules.dialog.entry.GiftEntry;
import com.community.hundred.modules.eventbus.SpecialWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.post.presenter.view.IMySendPostView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

// 我的发布接口数据
public class MySendPostPresenter extends BasePresenter<IMySendPostView> {

    private List<CircleChildEntry> list = new ArrayList<>();
    private CircleAdapter adapter;

    public MySendPostPresenter(MyActivity context) {
        super(context);
    }

    public void init(String uid) {
        getView().getRecycleView().setLayoutManager(new LinearLayoutManager(prContext));
        adapter = new CircleAdapter(prContext, list);
        getView().getRecycleView().setAdapter(adapter);
        getView().getRefresh().setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    page++;
                    getMySendPost(uid, page, pageSize);
                    refreshLayout.finishLoadMore();
                }, 200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    list.clear();
                    page = 0;
                    getMySendPost(uid, page, pageSize);
                    refreshLayout.finishRefresh();
                }, 200);
            }
        });
        // 关注按钮点击事件
        adapter.setOnItemAttentionListener(position -> {
            if (LoginUtils.getInstance().isLogin()) {
                CircleChildEntry entry = list.get(position);
                if (LoginUtils.getInstance().getUid().equals(entry.getUser_id())) {
                    toast("不能关注自己");
                } else {
                    if ("0".equals(entry.getIsgz())) {
                        setAttention(position, entry.getUser_id());
                    } else {
                        setEscAttention(position, entry.getUser_id());
                    }
                }
            }
        });

        // 点赞
        adapter.setOnJustLikeListener(position -> {
            CircleChildEntry entry = list.get(position);
            // 将点赞数量转换为int
            setJustLike(position, entry.getId());
        });

        // 详情
        adapter.setOnClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.POST_DETAILS)
                    .withString("id", list.get(position).getId())
                    .navigation();
        });

        // 举报
        adapter.setOnReportClickListener(position -> {
            CircleChildEntry entry = list.get(position);
            if (LoginUtils.getInstance().getUid().equals(entry.getUser_id())) {
                toast("不能举报自己");
            } else {
                new ReportDialog(prContext, content -> {
                    setReport(list.get(position).getId(), content);
                }).show();
            }
        });

        // 送礼
        adapter.setOnSendGiftListener(position -> {
            if (LoginUtils.getInstance().isLogin()) {
                getGiftList(position);
            }
        });
    }

    // 我的发布
    public void getMySendPost(String uid, int x, int y) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("x", String.valueOf(x));
        map.put("y", String.valueOf(y));
        OkHttp.postAsync(HttpConstant.mySendPostURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("mySendPostResult", result);
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
                        list.addAll(temp);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();
                prContext.showError();
            }
        });
    }

    // 点赞
    public void setJustLike(int position, String circle_id) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.loveCircleURL + "?uid=" + LoginUtils.getInstance().getUid() + "&circle_id=" + circle_id, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                CircleChildEntry entry = list.get(position);
                int love = Integer.parseInt(entry.getLove());
                if ("0".equals(entry.getIsdz())) {
                    love++;
                    entry.setLove(String.valueOf(love));
                    entry.setIsdz("1");
                    adapter.notifyDataSetChanged();
                } else {
                    love--;
                    entry.setLove(String.valueOf(love));
                    entry.setIsdz("0");
                    adapter.notifyDataSetChanged();
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
    public void getGiftList(int position) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.liwuURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonObject jsonObject = object.getAsJsonObject("data");
                JsonArray jsonArray = jsonObject.getAsJsonArray("liwulist");
                List<GiftEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<GiftEntry>>() {
                }.getType());
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                new GiftDialog(prContext, response.getYue(), list, (name, giftId, img) -> {
                    // 调用打赏的接口
                    senGift(giftId, list.get(position).getId());
                }).show();
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
                Log.d("sendGiftResult", result);
                // {"sta":"0","message":"打赏失败！"}
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

    // 关注
    public void setAttention(int position, String gid) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("gid", gid);
        OkHttp.postAsync(HttpConstant.ljGuanzhuURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
                list.get(position).setIsgz("1");
                adapter.notifyDataSetChanged();
                prContext.showComplete();
                EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.FOLLOW_REFRESH));
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
    public void setEscAttention(int position, String gid) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("gid", gid);
        OkHttp.postAsync(HttpConstant.quxiaogzURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
                list.get(position).setIsgz("0");
                adapter.notifyDataSetChanged();
                EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.FOLLOW_REFRESH));
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

}
