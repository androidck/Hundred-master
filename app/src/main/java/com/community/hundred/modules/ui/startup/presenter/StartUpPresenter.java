package com.community.hundred.modules.ui.startup.presenter;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.api.ApiRetrofit;
import com.community.hundred.common.base.BaseObserver;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.modules.ui.startup.entry.StartUpEntry;
import com.community.hundred.modules.ui.startup.presenter.view.IStartUpView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StartUpPresenter extends BasePresenter<IStartUpView> {

    public StartUpPresenter(MyActivity context) {
        super(context);
    }

    // 启动
    public void startUp() {
        prContext.showLoading();
        ApiRetrofit.getInstance().startUp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<StartUpEntry>>(prContext) {
                    @Override
                    protected void onSuccess(BaseResponse<List<StartUpEntry>> t) throws Exception {
                        prContext.showComplete();
                        if (t.getDate() != null) {
                            for (StartUpEntry entry:t.getDate()){
                                Glide.with(mContext)
                                        .load(entry.getImage())
                                        .placeholder(R.mipmap.start_up)
                                        .error(R.mipmap.start_up)
                                        .into(getView().getImageView());
                            }

                        }
                    }

                    @Override
                    protected void onError(BaseResponse<List<StartUpEntry>> t) throws Exception {
                        toast(t.getMessage());
                        prContext.showComplete();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        prContext.showComplete();
                    }
                });
    }
}
