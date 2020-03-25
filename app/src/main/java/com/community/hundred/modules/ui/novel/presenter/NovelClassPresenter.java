package com.community.hundred.modules.ui.novel.presenter;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.main.fragment.entry.NovEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.HomePresenter;
import com.community.hundred.modules.ui.novel.presenter.view.INovelView;
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

// 小说列表
public class NovelClassPresenter extends BasePresenter<INovelView> {

    private OnNovListener onNovListener;

    public NovelClassPresenter(MyActivity context) {
        super(context);
    }

    // 获取列表
    public void getNovelClass(String flId) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("fl_id", flId);
        OkHttp.postAsync(HttpConstant.novClassListURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray info = jsonObject.getAsJsonArray("list");
                List<NovEntry> novEntries = new Gson().fromJson(info.toString(), new TypeToken<List<NovEntry>>() {
                }.getType());
                onNovListener.onNov(novEntries);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                netWorkError();
                prContext.showError();
            }
        });
    }

    public interface OnNovListener {
        void onNov(List<NovEntry> list);
    }

    public void setOnNovListener(OnNovListener onNovListener) {
        this.onNovListener = onNovListener;
    }
}
