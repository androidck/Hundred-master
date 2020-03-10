package com.community.hundred.modules.ui.post.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.post.entry.PeopleEntry;
import com.community.hundred.modules.ui.post.presenter.view.IPeoplePostDetailsView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class PeoplePostDetailsPresenter extends BasePresenter<IPeoplePostDetailsView> {

    private OnPeopleListener onPeopleListener;

    public PeoplePostDetailsPresenter(MyActivity context) {
        super(context);
    }

    // 获取用户资料
    public void getUserData(String uid) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        OkHttp.postAsync(HttpConstant.getUserDataURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("getUserData", result);
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                PeopleEntry entry = new Gson().fromJson(object.toString(), PeopleEntry.class);
                onPeopleListener.onPeople(entry);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    public interface OnPeopleListener {
        void onPeople(PeopleEntry entry);
    }

    public void setOnPeopleListener(OnPeopleListener onPeopleListener) {
        this.onPeopleListener = onPeopleListener;
    }
}
