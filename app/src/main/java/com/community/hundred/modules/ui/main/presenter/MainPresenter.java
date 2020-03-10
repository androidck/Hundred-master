package com.community.hundred.modules.ui.main.presenter;

import com.community.hundred.common.api.ApiRetrofit;
import com.community.hundred.common.base.BaseObserver;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.dialog.HomeDialog;
import com.community.hundred.modules.ui.main.entry.NoticeEntry;
import com.community.hundred.modules.ui.main.entry.UserCenterEntry;
import com.community.hundred.modules.ui.main.presenter.view.IMainView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;

public class MainPresenter extends BasePresenter<IMainView> {

    public MainPresenter(MyActivity context) {
        super(context);
    }

    // 公告
    public void notice() {
        prContext.showLoading();
        ApiRetrofit.getInstance().notice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<NoticeEntry>(prContext) {
                    @Override
                    protected void onSuccess(BaseResponse<NoticeEntry> t) throws Exception {
                        prContext.showComplete();
                        if (t.getDate() != null) {
                            NoticeEntry entry = t.getDate();
                            if (entry.getStatus() == 1) {
                                new HomeDialog(prContext, entry).show();
                            }
                        }
                    }

                    @Override
                    protected void onError(BaseResponse<NoticeEntry> t) throws Exception {
                        toast(t.getMessage());
                        prContext.showComplete();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        e.printStackTrace();
                        prContext.showComplete();
                    }
                });
    }


}
