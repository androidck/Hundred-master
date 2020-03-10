package com.community.hundred.modules.ui.post;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.adapter.MyViewPageAdapter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.widget.XCollapsingToolbarLayout;
import com.community.hundred.modules.ui.post.fragment.PeopleAlbumFragment;
import com.community.hundred.modules.ui.post.fragment.PeopleDynamicFragment;
import com.community.hundred.modules.ui.post.presenter.PeoplePostDetailsPresenter;
import com.community.hundred.modules.ui.post.presenter.view.IPeoplePostDetailsView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

// 个人帖子
@Route(path = ActivityConstant.PEOPLE_DETAILS)
public class PeoplePostDetailsActivity extends MyActivity<IPeoplePostDetailsView, PeoplePostDetailsPresenter> implements XCollapsingToolbarLayout.OnScrimsListener {


    @BindView(R.id.t_test_title)
    Toolbar tTestTitle;
    @BindView(R.id.ctl_test_bar)
    XCollapsingToolbarLayout ctlTestBar;
    @BindView(R.id.abl_test_bar)
    AppBarLayout ablTestBar;

    @Autowired
    String uid;
    @BindView(R.id.user_head)
    CircleImageView userHead;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_fans_count)
    TextView tvFansCount;
    @BindView(R.id.tv_follow_count)
    TextView tvFollowCount;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private String nickName;

    private List<String> titlist;
    private List<Fragment> fragments;
    private MyViewPageAdapter adapter;

    @Override
    protected PeoplePostDetailsPresenter createPresenter() {
        return new PeoplePostDetailsPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_people_post_details;
    }


    @Override
    protected void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.setTitleBar(this, tTestTitle);
        //设置渐变监听
        ctlTestBar.setOnScrimsListener(this);

        titlist = new ArrayList<>();
        fragments = new ArrayList<>();
        titlist.add("动态");
        titlist.add("相册");
        fragments.add(PeopleDynamicFragment.getInstance(uid));
        fragments.add(PeopleAlbumFragment.getInstance(uid));
        adapter = new MyViewPageAdapter(getSupportFragmentManager(), this, fragments, titlist);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initData() {
        mPresenter.getUserData(uid);
        mPresenter.setOnPeopleListener(entry -> {
            nickName = entry.getNickname();
            setTitle(nickName);
            tvNickName.setText(nickName);
            tvFansCount.setText(entry.getFenshi() + "\t粉丝");
            tvFollowCount.setText(entry.getGuanzhu() + "\t关注");
            Glide.with(this).load(entry.getImage()).into(userHead);
            if (TextUtils.isEmpty(entry.getQianming())) {
                tvSign.setText("这家伙很懒，什么都没留下~");
            } else {
                tvSign.setText(entry.getQianming());
            }
        });
    }

    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        if (shown) {
            getStatusBarConfig().statusBarDarkFont(true).init();
            getTitleBar().setLeftIcon(R.mipmap.bar_icon_back_black);
            getTitleBar().getRightView().setTextColor(Color.parseColor("#323232"));
            getTitleBar().getTitleView().setTextColor(Color.parseColor("#323232"));
            setTitle(nickName);
        } else {
            getStatusBarConfig().statusBarDarkFont(false).init();
            getTitleBar().setLeftIcon(R.mipmap.bar_icon_back_white);
            getTitleBar().getRightView().setTextColor(Color.parseColor("#ffffff"));
            getTitleBar().getTitleView().setTextColor(Color.parseColor("#ffffff"));
            setTitle(nickName);
        }
    }


}
