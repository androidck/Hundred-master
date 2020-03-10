package com.community.hundred.modules.ui.user.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.user.entry.FollowEntry;
import com.community.hundred.modules.ui.user.presenter.view.IFollowView;
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

// 我的关注
public class FollowPresenter extends BasePresenter<IFollowView> {

    private OnFollowListener onFollowListener;

    public FollowPresenter(MyActivity context) {
        super(context);
    }

    // 获取关注列表
    public void getMyFollowList() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.myguanzhuURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = object.getAsJsonArray("data").getAsJsonArray();
                List<FollowEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FollowEntry>>() {
                }.getType());
                if (list.size() == 0) {
                    prContext.showEmpty();
                } else {
                    onFollowListener.onFollowData(list);
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

    public interface OnFollowListener {
        void onFollowData(List<FollowEntry> followEntries);
    }

    public void setOnFollowListener(OnFollowListener onFollowListener) {
        this.onFollowListener = onFollowListener;
    }
}
