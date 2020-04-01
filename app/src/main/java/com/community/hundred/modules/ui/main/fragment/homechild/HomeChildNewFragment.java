package com.community.hundred.modules.ui.main.fragment.homechild;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewTreeObserver;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.KeyConstant;
import com.community.hundred.common.download.DownloadUtil;
import com.community.hundred.common.ext.OnPageScrollListener;
import com.community.hundred.common.ext.ViewPagerUtil;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.common.util.BannerImageLoader;
import com.community.hundred.common.util.DensityUtil;
import com.community.hundred.common.util.FileUtils;
import com.community.hundred.common.util.SharedPreferencesUtils;
import com.community.hundred.common.view.CustomNestedScrollView;
import com.community.hundred.modules.adapter.HomeChildOneAdapter;
import com.community.hundred.modules.adapter.HomeChildTwoAdapter;
import com.community.hundred.modules.adapter.HomeChildVideoAdapter;
import com.community.hundred.modules.adapter.NovAdapter;
import com.community.hundred.modules.dialog.DownloadDialog;
import com.community.hundred.modules.entry.ColorInfo;
import com.community.hundred.modules.eventbus.GradualWrap;
import com.community.hundred.modules.manager.BookUtils;
import com.community.hundred.modules.manager.entry.BookEntry;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.fragment.entry.BannerEntry;
import com.community.hundred.modules.ui.main.fragment.entry.HomeChildMenuEntry;
import com.community.hundred.modules.ui.main.fragment.entry.HomeVideoEntry;
import com.community.hundred.modules.ui.main.fragment.entry.NovEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.HomePresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.IHomeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.thl.reader.ReadActivity;
import com.thl.reader.db.BookList;
import com.thl.reader.util.Fileutil;
import com.youth.banner.Banner;
import com.youth.banner.view.BannerViewPager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Request;

// 子页面
public class HomeChildNewFragment extends MyLazyFragment<MainActivity, IHomeView, HomePresenter> {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.scrollView)
    CustomNestedScrollView scrollView;
    @BindView(R.id.menu_recyclerView)
    RecyclerView menuRecyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private List<ColorInfo> colorList = new ArrayList<>();
    private BannerImageLoader imageLoader;
    private BannerViewPager vBannerViewPager;

    private HomeChildVideoAdapter adapter;
    private HomeChildOneAdapter oneAdapter;
    private HomeChildTwoAdapter twoAdapter;

    private final static String EXTRA_BOOK = "bookList";

    private List<HomeVideoEntry> list = new ArrayList<>();
    private List<HomeChildMenuEntry> entryList = new ArrayList<>();

    private List<NovEntry> novEntries = new ArrayList<>();

    private NovAdapter novAdapter;

    private int imageHeight;

    public static OnScrollChangeListener listener;
    private String menuStr;// 子菜单
    private String id;// id
    private String name; // 标题名称

    private int count;


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(mActivity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_child_new;
    }

    @Override
    protected void initView() {
        vBannerViewPager = banner.findViewById(R.id.bannerViewPager);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 2);
        recyclerView.setLayoutManager(manager);
        adapter = new HomeChildVideoAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        ViewTreeObserver vto = banner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            //设置banner图距离顶部的距离 56dp + 20dp
            imageHeight = DensityUtil.dp2px(getContext(), 76);
            /**
             * 获取顶部图片高度后，设置滚动监听
             */
            scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                mScrollY = scrollY;
                listener.onScrollChange(imageHeight, scrollY);
            });
        });

        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.VIDEO_DETAILS)
                    .withString("videoId", list.get(position).getId())
                    .navigation();
        });

        menuStr = getArguments().getString("childMenu");
        id = getArguments().getString("id");
        name = getArguments().getString("name");
        if (!TextUtils.isEmpty(menuStr)) {
            List<HomeChildMenuEntry> entries = new Gson().fromJson(menuStr, new TypeToken<List<HomeChildMenuEntry>>() {
            }.getType());
            entryList.addAll(entries);
            if ("推荐".equals(name)) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                menuRecyclerView.setLayoutManager(layoutManager);
                oneAdapter = new HomeChildOneAdapter(mActivity, entryList);
                menuRecyclerView.setAdapter(oneAdapter);
                oneAdapter.setOnItemClickListener(position -> {
                    ARouter.getInstance().build(ActivityConstant.SECOND_LEVEL)
                            .withString("title", entryList.get(position).getName())
                            .navigation();
                });
            } else if ("小说".equals(name)) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 4);
                menuRecyclerView.setLayoutManager(gridLayoutManager);
                twoAdapter = new HomeChildTwoAdapter(mActivity, entryList);
                menuRecyclerView.setAdapter(twoAdapter);
                // 小说点击事件
                twoAdapter.setOnItemClickListener(position -> {
                    ARouter.getInstance().build(ActivityConstant.NOV_CLASS_LIST)
                            .withString("classifyId", entryList.get(position).getId())
                            .navigation();
                });

                recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
                novAdapter = new NovAdapter(mActivity, novEntries);
                recyclerView.setAdapter(novAdapter);

                // 小说点击事件
                novAdapter.setOnItemClickListener(position -> {
                    BookEntry entry = BookUtils.getInstance().getBook(novEntries.get(position).getTitle());
                    SharedPreferencesUtils.saveBoolean(getActivity(), "initFristData", false);
                    if (entry != null) {
                        BookList bookList = new BookList();
                        bookList.setBookname(entry.getName());
                        bookList.setBookpath(entry.getPath());
                        bookList.setBegin(bookList.getBegin());
                        ReadActivity.openBook(bookList, getActivity());
                    } else {
                        // 调用下载的方法
                        NovEntry novEntry = novEntries.get(position);
                        new DownloadDialog(getContext(), mActivity, novEntry).show();
                    }
                });

            }

        }

    }

    @Override
    public boolean isStatusBarEnabled() {
        return super.isStatusBarEnabled();
    }

    @Override
    protected void initData() {
        getBanner();
        getVideoList();
        if ("小说".equals(name)) {
            getNovList();
        }
    }

    public void getVideoList() {
        mPresenter.getVideoList(id, 1);
        mPresenter.setOnHomeVideoListener(list1 -> {
            list.addAll(list1);
            adapter.notifyDataSetChanged();
        });
    }

    // 获取小说列表
    public void getNovList() {
        mPresenter.getNovList();
        mPresenter.setOnNovListener(list -> {
            novEntries.addAll(list);
            novAdapter.notifyDataSetChanged();
        });
    }

    public static HomeChildNewFragment getInstance(String id, String menu, String name) {
        HomeChildNewFragment fragment = new HomeChildNewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("childMenu", menu);
        bundle.putString("name", name);
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
        mPresenter.getBanner("1");
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
                        if (banner != null) {
                            int vibrantColor = ColorUtils.blendARGB(imageLoader.getMutedColor(enter), imageLoader.getMutedColor(enter + 1), percent);
                            EventBus.getDefault().post(GradualWrap.getInstance(KeyConstant.HOME_TITLE, vibrantColor));
                        }
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

}
