package com.community.hundred.modules.ui.post.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.eventbus.SpecialWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildHeaderEntry;
import com.community.hundred.modules.ui.post.presenter.view.ISendPostView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

// 发布帖子
public class SendPostPresenter extends BasePresenter<ISendPostView> {

    private OnClassifyListener onClassifyListener;

    public SendPostPresenter(MyActivity context) {
        super(context);
    }

    // 获取圈子分类
    public void getQzFl() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.qzflURL, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonArray jsonArray = object.getAsJsonArray("data");
                List<CircleChildHeaderEntry> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CircleChildHeaderEntry>>() {
                }.getType());
                onClassifyListener.onClassify(list);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                e.printStackTrace();
                prContext.showComplete();
            }
        });
    }

    // 发布圈子
    public void sendQz(String content, String city, String fl_id, String images) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("content", content);
        map.put("city", city);
        map.put("fl_id", fl_id);
        map.put("images", images);
        OkHttp.postAsync(HttpConstant.saveCircleURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("sendQzResult", result);
                // {"code":200,"msg":"发布成功"}
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                toast(response.getMsg());
                EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.SEND_POST_CIRCLE));
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
            }
        });
    }

    public interface OnClassifyListener {
        void onClassify(List<CircleChildHeaderEntry> list);
    }

    public void setOnClassifyListener(OnClassifyListener onClassifyListener) {
        this.onClassifyListener = onClassifyListener;
    }


}
