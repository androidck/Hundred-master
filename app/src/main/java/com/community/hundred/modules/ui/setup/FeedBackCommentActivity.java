package com.community.hundred.modules.ui.setup;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.modules.ui.setup.presenter.FeedBackPresenter;
import com.community.hundred.modules.ui.setup.presenter.view.IFeedBackView;
import com.hjq.widget.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//意见返回
@Route(path = ActivityConstant.FEED_BACK)
public class FeedBackCommentActivity extends MyActivity<IFeedBackView, FeedBackPresenter> {
    @BindView(R.id.ed_feedback)
    ClearEditText edFeedback;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    @Override
    protected FeedBackPresenter createPresenter() {
        return new FeedBackPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
    }

    @Override
    protected void initData() {

    }


    @Override
    public void isVerification() {
        super.isVerification();
        String feedBackStr = edFeedback.getText().toString().trim();
        if (TextUtils.isEmpty(feedBackStr)) {
            toast("意见内容不能为空");
        } else if (feedBackStr.length() > 100) {
            toast("意见内容不能大于100字");
        } else {
            mPresenter.feedBack(feedBackStr);
        }
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        isVerification();
    }
}
