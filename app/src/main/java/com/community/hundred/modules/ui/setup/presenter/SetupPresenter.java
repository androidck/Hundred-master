package com.community.hundred.modules.ui.setup.presenter;

import android.os.Build;

import com.community.hundred.BuildConfig;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.eventbus.UpdateWrap;
import com.community.hundred.modules.ui.setup.entry.UpdateEntry;
import com.community.hundred.modules.ui.setup.presenter.view.ISetupView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Request;

// 业务逻辑处理
public class SetupPresenter extends BasePresenter<ISetupView> {

    public SetupPresenter(MyActivity context) {
        super(context);
    }

    // 检查更
    public void checkUpdate() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.checkUpdateURL, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                UpdateEntry entry = new Gson().fromJson(object.toString(), UpdateEntry.class);
                if (!entry.getBanbenhao().equals(BuildConfig.VERSION_NAME)) {
                    EventBus.getDefault().post(UpdateWrap.getInstance(entry));
                } else {
                    toast("这已经是最新版本了");
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                netWorkError();
                e.printStackTrace();
            }
        });
    }
}
