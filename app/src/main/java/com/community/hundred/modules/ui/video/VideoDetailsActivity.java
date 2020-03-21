package com.community.hundred.modules.ui.video;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.ext.OnPageScrollListener;
import com.community.hundred.common.ext.ViewPagerUtil;
import com.community.hundred.common.util.BannerImageLoader;
import com.community.hundred.common.util.TimeUtils;
import com.community.hundred.modules.adapter.FemaleStarVideoAdapter;
import com.community.hundred.modules.entry.ColorInfo;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.femalestar.entry.FemaleInfoEntry;
import com.community.hundred.modules.ui.video.presenter.VideoDetailsPresenter;
import com.community.hundred.modules.ui.video.presenter.view.IVideoDetailsView;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.view.BannerViewPager;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.ui.ControlPanel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 视频详情
@Route(path = ActivityConstant.VIDEO_DETAILS)
public class VideoDetailsActivity extends MyActivity<IVideoDetailsView, VideoDetailsPresenter> implements IVideoDetailsView {


    @BindView(R.id.video_view)
    VideoView videoView;

    @Autowired
    String videoId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_play_count)
    TextView tvPlayCount;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_not_like_count)
    TextView tvNotLikeCount;
    @BindView(R.id.tv_like_count)
    TextView tvLikeCount;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_huan)
    TextView tvHuan;
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.ly_jieben)
    AutoLinearLayout lyJieben;
    @BindView(R.id.ly_collection)
    AutoRelativeLayout lyCollection;

    private int p = 1;

    private FemaleStarVideoAdapter adapter;

    private BannerImageLoader imageLoader;
    private BannerViewPager vBannerViewPager;
    private List<ColorInfo> colorList = new ArrayList<>();


    private List<FemaleInfoEntry> infoEntries = new ArrayList<>();

    @Override
    protected VideoDetailsPresenter createPresenter() {
        return new VideoDetailsPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_details;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        videoView.setControlPanel(new ControlPanel(this));
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        adapter = new FemaleStarVideoAdapter(this, infoEntries);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.VIDEO_DETAILS)
                    .withString("videoId", infoEntries.get(position).getId())
                    .navigation();
        });

        vBannerViewPager = banner.findViewById(R.id.bannerViewPager);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initData() {
        mPresenter.getVideoDetails(videoId);
        mPresenter.setOnVideoDetailsListener(entry -> {
            videoView.setUp(entry.getLink());
            videoView.start();
            tvTitle.setText(entry.getName());
            tvDesc.setText(entry.getDefinition());
            tvLikeCount.setText(entry.getLove());
            tvPlayCount.setText(entry.getPlay_num() + "\t次播放");
            tvNotLikeCount.setText(entry.getCai());
            tvTime.setText(TimeUtils.timeStamp2Date(entry.getRecord_time(), "yyyy.MM.dd"));
            getFemaleStar(entry.getPerformer_id());
            if ("1".equals(entry.getIslike())) {
                Drawable drawableLeft = getDrawable(R.mipmap.icon_yizan);
                drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                tvLikeCount.setCompoundDrawables(drawableLeft, null, null, null);
            } else {
                Drawable drawableLeft = getDrawable(R.mipmap.icon_zan);
                drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                tvLikeCount.setCompoundDrawables(drawableLeft, null, null, null);
            }

            if ("1".equals(entry.getIsstep())) {
                Drawable drawableLeft = getDrawable(R.mipmap.icon_yicai);
                drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                tvNotLikeCount.setCompoundDrawables(drawableLeft, null, null, null);
            } else {
                Drawable drawableLeft = getDrawable(R.mipmap.icon_cai);
                drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                tvNotLikeCount.setCompoundDrawables(drawableLeft, null, null, null);
            }
        });
        mPresenter.addLook(videoId);
        getBanner();

    }

    // 获取banner 图
    public void getBanner() {
        mPresenter.getBanner("3");
        mPresenter.setOnBannerListener(list -> {
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

    public void getFemaleStar(String femaleId) {
        mPresenter.getFemaleStarDetails(femaleId, String.valueOf(p), "1");
        mPresenter.setOnFemaleStarDetailsListener((entrys, list) -> {
            tvDesc.setText(entrys.getDescription());
            infoEntries.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }


    @Override
    public void onBackPressed() {
        if (MediaPlayerManager.instance().backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.instance().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.instance().releasePlayerAndView(this);
    }


    @OnClick({R.id.tv_not_like_count, R.id.tv_like_count, R.id.tv_huan, R.id.ly_collection})
    public void onViewClicked(View view) {
        if (isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_not_like_count:
                if (LoginUtils.getInstance().isLogin()) {
                    mPresenter.addStep(videoId);
                } else {
                    notLogin();
                }
                break;
            case R.id.tv_like_count:
                if (LoginUtils.getInstance().isLogin()) {
                    mPresenter.addLike(videoId);
                } else {
                    notLogin();
                }
                break;
            case R.id.tv_huan:
                break;
            case R.id.ly_collection: // 收藏
                mPresenter.addCollect(videoId);
                break;
        }
    }

    @Override
    public TextView getLike() {
        return tvLikeCount;
    }

    @Override
    public TextView getSetup() {
        return tvNotLikeCount;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
