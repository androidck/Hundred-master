package com.community.hundred.modules.ui.femalestar.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.femalestar.entry.FemaleInfoEntry;
import com.community.hundred.modules.ui.femalestar.entry.FemaleStarEntry;
import com.community.hundred.modules.ui.femalestar.presenter.view.IFemaleView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

// 女星详情
public class FemaleStarPresenter extends BasePresenter<IFemaleView> {

    private OnFemaleStarDetailsListener onFemaleStarDetailsListener;

    public FemaleStarPresenter(MyActivity context) {
        super(context);
    }

    // 获取女星详情 pid=9&p=1&order=1
    public void getFemaleStarDetails(String pid, String p, String order) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.nxDetailsURL + "&pid=" + pid + "&p=" + p + "&order=" + order, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("femalerequest", result);
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

    public interface OnFemaleStarDetailsListener {
        void onFemaleStar(FemaleStarEntry entry, List<FemaleInfoEntry> list);
    }

    public void setOnFemaleStarDetailsListener(OnFemaleStarDetailsListener onFemaleStarDetailsListener) {
        this.onFemaleStarDetailsListener = onFemaleStarDetailsListener;
    }
}
