package com.community.hundred.modules.ui.main.fragment.presenter;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.main.fragment.entry.ForumEntry;
import com.community.hundred.modules.ui.main.fragment.entry.HomeEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IForumView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


public class ForumPresenter extends BasePresenter<IForumView> {

    private OnForumClassifyListener onForumClassifyListener;

    public ForumPresenter(MyActivity context) {
        super(context);
    }

    // 获取专栏tab
    public void getForumClassify() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.zlflURL, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = object.getAsJsonArray("date");
                List<ForumEntry> list = new ArrayList<>();
                for (JsonElement json : jsonArray) {
                    ForumEntry entry = new Gson().fromJson(json, ForumEntry.class);
                    list.add(entry);
                }
                onForumClassifyListener.onHomeClassify(list);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
            }
        });
    }

    public interface OnForumClassifyListener {
        void onHomeClassify(List<ForumEntry> list);
    }

    public void setOnForumClassifyListener(OnForumClassifyListener onForumClassifyListener) {
        this.onForumClassifyListener = onForumClassifyListener;
    }

}
