package com.community.hundred.common.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.dialog.MessageDialog;
import com.community.hundred.common.other.StatusManager;
import com.community.hundred.common.web.BrowserActivity;
import com.community.hundred.common.web.SonicJavaScriptInterface;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.base.BaseDialog;
import com.hjq.base.BaseLazyFragment;
import com.hjq.base.BuildConfig;

import com.hjq.toast.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目中 Fragment 懒加载基类
 */
public abstract class MyLazyFragment<A extends MyActivity, V, T extends BasePresenter<V>>
        extends BaseLazyFragment<A> implements OnTitleBarListener {

    /**
     * 标题栏对象
     */
    private TitleBar mTitleBar;
    /**
     * 状态栏沉浸
     */
    private ImmersionBar mImmersionBar;
    /**
     * ButterKnife 注解
     */
    private Unbinder mButterKnife;

    protected T mPresenter;

    public int page = 0;
    public int pageSize = 10;

    /**
     * 获取标题栏 id
     */
    protected int getTitleId() {
        return 0;
    }

    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mButterKnife = ButterKnife.bind(this, view);
        }
        return view;
    }


    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    // 隐藏 左边按钮
    public void hideLeftButton(TitleBar titleBar) {
        titleBar.setVisibility(View.INVISIBLE);
    }

    // 设置 黑色 返回 按钮
    public void setBlackLeftButtonIcon(TitleBar titleBar) {
        titleBar.setLeftIcon(R.mipmap.bar_icon_back_black);
    }

    // 设置 白色 返回 按钮
    public void setWhiteLeftButtonIcon(TitleBar titleBar) {
        titleBar.setLeftIcon(R.mipmap.bar_icon_back_white);
    }

    @Override
    protected void initFragment() {
        if (getTitleId() > 0) {
            // 勤快模式
            View view = findViewById(getTitleId());
            if (view instanceof TitleBar) {
                mTitleBar = (TitleBar) view;
            }
        } else if (getTitleId() == 0 && getView() instanceof ViewGroup) {
            // 懒人模式
            mTitleBar = MyActivity.findTitleBar((ViewGroup) getView());
        }
        if (mTitleBar != null) {
            mTitleBar.setOnTitleBarListener(this);
        }

        initImmersion();
        super.initFragment();
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersion() {

        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            statusBarConfig().init();

            // 设置标题栏沉浸
            if (getTitleId() > 0) {
                ImmersionBar.setTitleBar(this, findViewById(getTitleId()));
            } else if (mTitleBar != null) {
                ImmersionBar.setTitleBar(this, mTitleBar);
            }
        }
    }

    /**
     * 是否在Fragment使用沉浸式
     */
    public boolean isStatusBarEnabled() {
        return false;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    protected ImmersionBar getStatusBarConfig() {
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式
     */
    private ImmersionBar statusBarConfig() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(statusBarDarkFont())
                // 解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardEnable(true);
        return mImmersionBar;
    }

    /**
     * 获取状态栏字体颜色
     */
    protected boolean statusBarDarkFont() {
        // 返回真表示黑色字体
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isStatusBarEnabled() && isLazyLoad()) {
            // 重新初始化状态栏
            statusBarConfig().init();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mButterKnife != null) {
            mButterKnife.unbind();
        }
    }

    public void getTitleInit() {

    }

    /**
     * 设置标题栏的标题
     */
    public void setTitle(@StringRes int id) {
        setTitle(getString(id));
    }

    /**
     * 设置标题栏的标题
     */
    public void setTitle(CharSequence title) {
        if (mTitleBar != null) {
            mTitleBar.setTitle(title);
        } else {
            // 如果没有标题栏对象就直接设置给绑定的 Activity
            getAttachActivity().setTitle(title);
        }
    }

    @Nullable
    public TitleBar getTitleBar() {
        if (getTitleId() > 0 && findViewById(getTitleId()) instanceof TitleBar) {
            return findViewById(getTitleId());
        }
        return mTitleBar;
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }

    /**
     * 打印日志
     */
    public void log(Object object) {
        if (BuildConfig.DEBUG) {
            Log.v(getClass().getSimpleName(), object != null ? object.toString() : "null");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * {@link OnTitleBarListener}
     */

    /**
     * TitleBar 左边的View被点击了
     */
    @Override
    public void onLeftClick(View v) {
    }

    /**
     * TitleBar 中间的View被点击了
     */
    @Override
    public void onTitleClick(View v) {
    }

    /**
     * TitleBar 右边的View被点击了
     */
    @Override
    public void onRightClick(View v) {
    }

    private final StatusManager mStatusManager = new StatusManager();

    /**
     * 显示加载中
     */
    public void showLoading() {
        mStatusManager.showLoading(getAttachActivity());
    }

    public void showLoading(@StringRes int id) {
        mStatusManager.showLoading(getAttachActivity(), getString(id));
    }

    public void showLoading(CharSequence text) {
        mStatusManager.showLoading(getAttachActivity(), text);
    }

    /**
     * 显示加载完成
     */
    public void showComplete() {
        mStatusManager.showComplete();
    }

    /**
     * 显示空提示
     */
    public void showEmpty() {
        mStatusManager.showEmpty(getView());
    }

    /**
     * 显示错误提示
     */
    public void showError() {
        mStatusManager.showError(getView());
    }

    /**
     * 显示自定义提示
     */
    public void showLayout(@DrawableRes int drawableId, @StringRes int stringId) {
        mStatusManager.showLayout(getView(), drawableId, stringId);
    }

    public void showLayout(Drawable drawable, CharSequence hint) {
        mStatusManager.showLayout(getView(), drawable, hint);
    }

    // 未登录操作
    public void notLogin() {
        new MessageDialog.Builder(mActivity)
                .setTitle("账号未登录")
                .setMessage("登录账号才能操作相关操作")
                .setConfirm(getString(R.string.common_confirm))
                .setCancel(getString(R.string.common_cancel))
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        ARouter.getInstance().build(ActivityConstant.LOGIN).navigation();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // 敬请期待提示
    public void expectTip(){
        toast(R.string.expect_title);
    }



    //跳转到浏览器
    public void startBrowserActivity(Context context, String title, int mode, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(BrowserActivity.PARAM_URL, url);
        intent.putExtra(BrowserActivity.PARAM_TITLE, title);
        intent.putExtra(BrowserActivity.PARAM_MODE, mode);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        context.startActivity(intent);
    }

}