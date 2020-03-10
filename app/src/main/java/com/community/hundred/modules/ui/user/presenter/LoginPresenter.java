package com.community.hundred.modules.ui.user.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.common.api.ApiRetrofit;
import com.community.hundred.common.base.BaseObserver;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.listener.OnSuccessListener;
import com.community.hundred.modules.entry.LoginEntry;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.manager.entry.UserEntry;
import com.community.hundred.modules.ui.user.presenter.view.ILoginView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<ILoginView> {

    private OnSuccessListener onSuccessListener;

    public LoginPresenter(MyActivity context) {
        super(context);
    }

    // 登录
    public void login(String phone, String passWord) {
        prContext.showLoading();
        ApiRetrofit.getInstance().login(phone, passWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<LoginEntry>(prContext) {
                    @Override
                    protected void onSuccess(BaseResponse<LoginEntry> t) throws Exception {
                        prContext.showComplete();
                        toast("登录成功");
                        UserEntry entry = new UserEntry();
                        entry.setUserId(t.getDate().getId());
                        entry.setPhone(t.getDate().getMobile());
                        LoginUtils.getInstance().saveUserInfo(entry);
                        onSuccessListener.onSuccess(1);
                    }

                    @Override
                    protected void onError(BaseResponse<LoginEntry> t) throws Exception {
                        toast(t.getMessage());
                        prContext.showComplete();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        prContext.showComplete();
                        e.printStackTrace();
                        toast("网络错误");
                    }
                });
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
