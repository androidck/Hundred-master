package com.community.hundred.modules.ui.classify.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.ui.classify.entry.ClassifyHeaderEntry;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


// 分类业务逻辑
public class ClassifyPresenter extends BasePresenter<IClassifyView> {

    private onDataSourceListener onDataSourceListener;

    public ClassifyPresenter(MyActivity context) {
        super(context);
    }

    // 获取分类
    public void getClassify() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.getHomeClassifyURL, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("dasdasd", result);
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonArray data = jsonObject.getAsJsonArray("data");
                ArrayList<ClassifyHeaderEntry> arrayList = new Gson().fromJson(data.toString(), new TypeToken<ArrayList<ClassifyHeaderEntry>>() {
                }.getType());
                onDataSourceListener.onDataSource(arrayList);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
            }
        });
    }

    public interface onDataSourceListener {
        void onDataSource(ArrayList<ClassifyHeaderEntry> list);
    }

    public void setOnDataSourceListener(ClassifyPresenter.onDataSourceListener onDataSourceListener) {
        this.onDataSourceListener = onDataSourceListener;
    }
}
