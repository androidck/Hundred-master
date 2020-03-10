package com.community.hundred.modules.ui.user.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.eventbus.ModifyUserWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.user.entry.UpdateUserEntry;
import com.community.hundred.modules.ui.user.entry.UserInfoEntry;
import com.community.hundred.modules.ui.user.presenter.view.IModifyUserView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

// 用户资料业务逻辑
public class ModifyUserDataPresenter extends BasePresenter<IModifyUserView> {

    private OnUserDataListener onUserDataListener;

    public ModifyUserDataPresenter(MyActivity context) {
        super(context);
    }

    // 获取用户资料
    public void getUserData() {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.getUserAllURL + "&uid=" + LoginUtils.getInstance().getUid(), new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("userDataInfo", result);
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonObject jsonObject = object.getAsJsonObject("data");
                UserInfoEntry entry = new Gson().fromJson(jsonObject.toString(), UserInfoEntry.class);
                onUserDataListener.onUserData(entry);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 修改用户资料
    public void setUserData(String nickName, String image, String qianming, String age, String sex) {
        prContext.showLoading();
        String str = HttpConstant.xgdata + "&uid=" + LoginUtils.getInstance().getUid() + "+&nickname=" + nickName + "&image="
                + image + "&qianming=" + qianming + "&age=" + age + "&sex=" + sex;
        OkHttp.getAsync(str, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("dasd", result);
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonObject jsonObject = object.getAsJsonObject("data");
                UpdateUserEntry entry = new Gson().fromJson(jsonObject.toString(), UpdateUserEntry.class);
                toast(entry.getMsg());
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    public void delUserImg(String imageId) {
        prContext.showLoading();
        OkHttp.getAsync(HttpConstant.delUserImgURL + "&imgid=" + imageId, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if (response.getRet() == 200) {
                    EventBus.getDefault().post(ModifyUserWrap.getInstance(response.getRet()));
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
            }
        });
    }

    public interface OnUserDataListener {
        void onUserData(UserInfoEntry entry);
    }

    public void setOnUserDataListener(OnUserDataListener onUserDataListener) {
        this.onUserDataListener = onUserDataListener;
    }
}
