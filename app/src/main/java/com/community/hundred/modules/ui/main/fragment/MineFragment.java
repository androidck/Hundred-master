package com.community.hundred.modules.ui.main.fragment;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.presenter.MinePresenter;
import com.community.hundred.modules.ui.main.presenter.view.IMineView;
import com.hjq.widget.layout.SettingBar;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

// 个人中心
public class MineFragment extends MyLazyFragment<MainActivity, IMineView, MinePresenter> {

    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_sex_age)
    TextView tvSexAge;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.ly_login)
    AutoLinearLayout lyLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_follow_count)
    TextView tvFollowCount;
    @BindView(R.id.ly_follow)
    AutoLinearLayout lyFollow;
    @BindView(R.id.tv_fans_count)
    TextView tvFansCount;
    @BindView(R.id.ly_my_fans)
    AutoLinearLayout lyMyFans;
    @BindView(R.id.tv_send_count)
    TextView tvSendCount;
    @BindView(R.id.ly_send_count)
    AutoLinearLayout lySendCount;
    @BindView(R.id.tv_my_wallet)
    TextView tvMyWallet;
    @BindView(R.id.tv_withdraw)
    TextView tvWithdraw;
    @BindView(R.id.tv_extension)
    TextView tvExtension;
    @BindView(R.id.tv_conduct)
    TextView tvConduct;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_open_member)
    TextView tvOpenMember;
    @BindView(R.id.tv_withdraw_count)
    TextView tvWithdrawCount;
    @BindView(R.id.tv_amusement)
    TextView tvAmusement;
    @BindView(R.id.tv_activation_code)
    TextView tvActivationCode;
    @BindView(R.id.tv_exchange_group)
    TextView tvExchangeGroup;
    @BindView(R.id.tv_share_vip)
    TextView tvShareVip;
    @BindView(R.id.item_gift)
    SettingBar itemGift;
    @BindView(R.id.item_look)
    SettingBar itemLook;
    @BindView(R.id.item_collection)
    SettingBar itemCollection;
    @BindView(R.id.tv_local_video)
    SettingBar tvLocalVideo;
    @BindView(R.id.tv_opinion)
    SettingBar tvOpinion;
    @BindView(R.id.item_about_us)
    SettingBar itemAboutUs;
    @BindView(R.id.item_cooperation)
    SettingBar itemCooperation;
    @BindView(R.id.ly_not_login)
    AutoLinearLayout lyNotLogin;
    @BindView(R.id.user_head)
    CircleImageView userHead;
    @BindView(R.id.img_is_vip)
    ImageView imgIsVip;
    @BindView(R.id.ly_my_wallet)
    AutoLinearLayout lyMyWallet;
    @BindView(R.id.ly_withdraw_count)
    AutoLinearLayout lyWithdrawCount;
    @BindView(R.id.ly_extension)
    AutoLinearLayout lyExtension;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(mActivity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }


    @Override
    protected void initView() {
        isLogin();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        ARouter.getInstance().build(ActivityConstant.SETUP).navigation();
    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    public static MineFragment getInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }


    @OnClick({R.id.ly_login, R.id.tv_login, R.id.ly_follow, R.id.ly_my_fans, R.id.ly_send_count,
            R.id.ly_my_wallet, R.id.ly_withdraw_count, R.id.ly_extension,
            R.id.tv_conduct, R.id.tv_pay, R.id.tv_open_member, R.id.tv_withdraw, R.id.tv_amusement, R.id.tv_activation_code, R.id.tv_exchange_group, R.id.tv_share_vip, R.id.item_gift, R.id.item_look, R.id.item_collection, R.id.tv_local_video, R.id.tv_opinion, R.id.item_about_us, R.id.item_cooperation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_login: // 登录后操作
                ARouter.getInstance().build(ActivityConstant.MODIFY_USER_DATA).navigation();
                break;
            case R.id.tv_login:// 登录
                ARouter.getInstance().build(ActivityConstant.LOGIN).navigation();
                break;
            case R.id.ly_follow:// 我的关注
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.FOLLOW).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.ly_my_fans:// 我的粉丝
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.FANS).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.ly_send_count:// 我的发布
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.MY_SEND_POST).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.ly_my_wallet:// 我的钱包
                expectTip();
                break;
            case R.id.ly_withdraw_count:// 累计提现
                expectTip();
                break;
            case R.id.ly_extension:// 我的钱包
                expectTip();
                break;
            case R.id.tv_conduct:// 全民理财
                expectTip();
                break;
            case R.id.tv_pay:// 钱包充值
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.RECHARGE).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.tv_open_member:// 开通会员
                expectTip();
                break;
            case R.id.tv_withdraw:
                ARouter.getInstance().build(ActivityConstant.WITHDRAW).navigation();
                break;
            case R.id.tv_amusement:// 在线娱乐
                expectTip();
                break;
            case R.id.tv_activation_code:
                ARouter.getInstance().build(ActivityConstant.ACTIVATION_CODE).navigation();
                break;
            case R.id.tv_exchange_group:// 交流群
                expectTip();
                break;
            case R.id.tv_share_vip:// 分享得vip
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.EXTENSION).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.item_gift:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.RECEIVED_GIFT).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.item_look:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.HISTORY_LOOK).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.item_collection: // 我的收藏
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.MY_COLLECT).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.tv_local_video:// 本地视频
                expectTip();
                break;
            case R.id.tv_opinion:
                if (LoginUtils.getInstance().isLogin()) {
                    ARouter.getInstance().build(ActivityConstant.FEED_BACK).navigation();
                } else {
                    notLogin();
                }
                break;
            case R.id.item_about_us:// 关于我们
                ARouter.getInstance().build(ActivityConstant.ABOUT_US).navigation();
                break;
            case R.id.item_cooperation:// 合作联系
                expectTip();
                break;
        }
    }

    // 获取个人中心资料
    public void getUserCenter() {
        mPresenter.getUserCenter(LoginUtils.getInstance().getUid());
        mPresenter.setOnUserCenterListener(entry -> {
            //昵称
            tvNickName.setText(entry.getNickname());
            // 头像
            Glide.with(mActivity)
                    .load(HttpConstant.BASE_HOST + entry.getImage())
                    .placeholder(R.mipmap.icon_not_login)
                    .into(userHead);
            if ("1".equals(entry.getVip())) {
                imgIsVip.setVisibility(View.VISIBLE);
            } else {
                imgIsVip.setVisibility(View.GONE);
            }
            // 性别 年龄
            String sex;
            if ("1".equals(entry.getSex())) {
                sex = "男";
                tvNickName.setCompoundDrawables(null, null, setDrawable(R.mipmap.icon_boy), null);
            } else {
                sex = "女";
                tvNickName.setCompoundDrawables(null, null, setDrawable(R.mipmap.icon_girl), null);
            }
            tvSexAge.setText(sex + "\t" + entry.getAge() + "岁");
            // 个性签名
            tvSign.setText("个性签名：" + entry.getQianming() == null ? "" : entry.getQianming());
            // 关注
            tvFollowCount.setText(entry.getMygz() == null ? "" : entry.getMygz());
            // 粉丝
            tvFansCount.setText(entry.getFenshi() == null ? "" : entry.getFenshi());
            // 发布
            tvSendCount.setText(entry.getTznum() == null ? "" : entry.getTznum());
            // 我的钱包
            tvMyWallet.setText(entry.getMoney() == null ? "" : entry.getMoney());
            // 累计提现
            tvWithdrawCount.setText(entry.getTixian() == null ? "" : entry.getTixian());
            // 累计推广
            tvExtension.setText(entry.getTznum() == null ? "" : entry.getTixian());


        });
    }


    @Override
    public void onResume() {
        super.onResume();
        isLogin();
    }

    public void isLogin() {
        if (LoginUtils.getInstance().isLogin() == true) {
            lyLogin.setVisibility(View.VISIBLE);
            lyNotLogin.setVisibility(View.GONE);
            getUserCenter();
        } else {
            lyNotLogin.setVisibility(View.VISIBLE);
            lyLogin.setVisibility(View.GONE);
        }
    }

    public Drawable setDrawable(int desId) {
        Drawable drawable = mActivity.getDrawable(desId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }
}
