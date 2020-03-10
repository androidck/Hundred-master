package com.community.hundred.modules.ui.user.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.common.api.ApiRetrofit;
import com.community.hundred.common.base.BaseObserver;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.util.CountDownTimerUtils;
import com.community.hundred.modules.ui.user.presenter.view.IRegisterView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// 发送验证码
public class RegisterPresenter extends BasePresenter<IRegisterView> {

    public RegisterPresenter(MyActivity context) {
        super(context);
    }

    // 发送验证码
    public void sendMsg(String phone){
        prContext.showLoading();
        ApiRetrofit.getInstance().sendMsg(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(prContext) {
                    @Override
                    protected void onSuccess(BaseResponse<String> t) throws Exception {
                        prContext.showComplete();
                        CountDownTimerUtils mCountDownTimerUtils =
                                new CountDownTimerUtils(getView().sendMsg(), 60000, 1000);
                        mCountDownTimerUtils.start();
                    }

                    @Override
                    protected void onError(BaseResponse<String> t) throws Exception {
                        prContext.showComplete();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        prContext.showComplete();
                    }
                });
    }

    // 注册
    public void register(String phone,String password,String code){
        prContext.showLoading();
        ApiRetrofit.getInstance().register(phone,password,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(prContext) {
                    @Override
                    protected void onSuccess(BaseResponse<String> t) throws Exception {
                        prContext.showComplete();
                        ARouter.getInstance().build(ActivityConstant.LOGIN).navigation();
                        prContext.finish();
                        toast(t.getMessage());
                    }

                    @Override
                    protected void onError(BaseResponse<String> t) throws Exception {
                        toast(t.getMsg());
                        prContext.showComplete();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        e.printStackTrace();
                        prContext.showComplete();
                    }
                });
    }

    // 忘记密码
    public void forgetPwd(String phone,String password,String code){
        prContext.showLoading();
        ApiRetrofit.getInstance().forgetPwd(phone,password,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(prContext) {
                    @Override
                    protected void onSuccess(BaseResponse<String> t) throws Exception {
                        prContext.showComplete();
                        ARouter.getInstance().build(ActivityConstant.LOGIN).navigation();
                        prContext.finish();
                        toast(t.getMessage());
                    }

                    @Override
                    protected void onError(BaseResponse<String> t) throws Exception {
                        toast(t.getMsg());
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
