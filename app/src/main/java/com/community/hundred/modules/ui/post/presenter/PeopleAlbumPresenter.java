package com.community.hundred.modules.ui.post.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.post.presenter.view.IPeopleAlbumView;
import com.community.hundred.modules.ui.user.entry.UserAlbumEntry;
import com.community.hundred.modules.ui.user.entry.UserInfoEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

// 个人中心 相册
public class PeopleAlbumPresenter extends BasePresenter<IPeopleAlbumView> {

    private OnAlbumListener onAlbumListener;

    public PeopleAlbumPresenter(MyActivity context) {
        super(context);
    }

    // 获取个人中心相册
    public void getAlum(String uid) {
        OkHttp.getAsync(HttpConstant.getUserAllURL + "&uid=" + uid, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                Log.d("userDataInfo", result);
                JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                JsonObject jsonObject = object.getAsJsonObject("data");
                UserInfoEntry entry = new Gson().fromJson(jsonObject.toString(), UserInfoEntry.class);
                List<UserAlbumEntry> album = entry.getAlbum();
                onAlbumListener.onAlbum(album);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    public interface OnAlbumListener {
        void onAlbum(List<UserAlbumEntry> album);
    }

    public void setOnAlbumListener(OnAlbumListener onAlbumListener) {
        this.onAlbumListener = onAlbumListener;
    }
}
