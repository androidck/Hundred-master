package com.community.hundred.modules.ui.main.fragment.forumchild;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.KeyConstant;
import com.community.hundred.common.ext.OnPageScrollListener;
import com.community.hundred.common.ext.ViewPagerUtil;
import com.community.hundred.common.manager.MyLinearLayoutManager;
import com.community.hundred.common.util.BannerImageLoader;
import com.community.hundred.common.util.DensityUtil;
import com.community.hundred.common.view.CustomNestedScrollView;
import com.community.hundred.modules.adapter.ForumChildAdapter;
import com.community.hundred.modules.entry.ColorInfo;
import com.community.hundred.modules.eventbus.GradualWrap;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.fragment.entry.BannerEntry;
import com.community.hundred.modules.ui.main.fragment.entry.ForumChildNewEntry;
import com.community.hundred.modules.ui.main.fragment.homechild.HomeChildNewFragment;
import com.community.hundred.modules.ui.main.fragment.presenter.HomePresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IHomeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.view.BannerViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

// 子页面
public class ForumChildNewFragment extends MyLazyFragment<MainActivity, IHomeView, HomePresenter> {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.scrollView)
    CustomNestedScrollView scrollView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private List<ColorInfo> colorList = new ArrayList<>();
    private BannerImageLoader imageLoader;
    private BannerViewPager vBannerViewPager;
    private int imageHeight;

    public static OnScrollChangeListener listener;


    private String id;

    private ForumChildAdapter adapter;

    private List<ForumChildNewEntry> lists = new ArrayList<>();
    private int count;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(mActivity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_child_new;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initView() {
        vBannerViewPager = banner.findViewById(R.id.bannerViewPager);
        getBanner();
        MyLinearLayoutManager manager = new MyLinearLayoutManager(getContext());
        manager.setOrientation(OrientationHelper.VERTICAL);  //垂直
        manager.setScrollEnabled(false);// 禁止滑动
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new ForumChildAdapter(mActivity, lists);
        recyclerView.setAdapter(adapter);
        // 点击事件查看详情
        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance()
                    .build(ActivityConstant.FEMALE_STAR)
                    .withString("pid", lists.get(position).getId())
                    .navigation();
        });
        ViewTreeObserver vto = banner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            //设置banner图距离顶部的距离 56dp + 20dp
            imageHeight = DensityUtil.dp2px(getContext(), 76);
            /**
             * 获取顶部图片高度后，设置滚动监听
             */
            scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mScrollY = scrollY;
                    listener.onScrollChange(imageHeight, scrollY);
                }
            });
        });

        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    page++;
                    getForumChild();
                    refreshLayout.finishLoadMore();
                }, 200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    page = 0;
                    lists.clear();
                    getForumChild();
                    refreshLayout.finishRefresh();
                }, 200);
            }
        });

    }

    @Override
    protected void initData() {
        // 获取上页面传递过来的ID
        id = this.getArguments().getString("type");
        getForumChild();
    }

    public static ForumChildNewFragment getInstance(String id) {
        ForumChildNewFragment fragment = new ForumChildNewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnScrollChangeListener {
        void onScrollChange(int imageHeight, int scrollY);
    }

    public static void setListener(OnScrollChangeListener onScrollChangeListener) {
        listener = onScrollChangeListener;
    }

    private int mScrollY = 0;

    public void getTitleInit() {
        listener.onScrollChange(imageHeight, mScrollY);
    }

    public void getBanner() {
        mPresenter.getBanner("2");
        mPresenter.setOnBannerListener(list -> {
            count = list.size();
            colorList.clear();
            for (int i = 0; i <= count + 1; i++) {
                ColorInfo info = new ColorInfo();
                if (i == 0) {
                    info.setImgUrl(list.get(count - 1));
                } else if (i == count + 1) {
                    info.setImgUrl(list.get(0));
                } else {
                    info.setImgUrl(list.get(i - 1));
                }
                colorList.add(info);
            }
            imageLoader = new BannerImageLoader(colorList);
            banner.setImageLoader(imageLoader);
            //设置图片集合
            banner.setImages(list);
            //设置轮播时间
            banner.setDelayTime(3000);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
            ViewPagerUtil.bind(vBannerViewPager, new OnPageScrollListener() {
                @Override
                public void onPageScroll(int enterPosition, int leavePosition, float percent) {
                    if (banner != null) {
                        int enter = banner.toRealPosition(enterPosition);
                        int leave = banner.toRealPosition(leavePosition);
                        int vibrantColor = ColorUtils.blendARGB(imageLoader.getMutedColor(enter), imageLoader.getMutedColor(enter + 1), percent);
                        EventBus.getDefault().post(GradualWrap.getInstance(KeyConstant.FORUM_TITLE, vibrantColor));
                    }
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        });
    }

    // 获取专栏列表
    public void getForumChild() {
        mPresenter.getForumChild(id, page, pageSize);
        mPresenter.setOnForumChildListener(list -> {
            lists.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }


}
