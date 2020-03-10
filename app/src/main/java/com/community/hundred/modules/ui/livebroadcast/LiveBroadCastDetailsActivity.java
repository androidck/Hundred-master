package com.community.hundred.modules.ui.livebroadcast;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;

import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.exo.ExoPlayer;
import org.salient.artplayer.ijk.IjkPlayer;
import org.salient.artplayer.ui.ControlPanel;

import butterknife.BindView;
import butterknife.ButterKnife;

// 直播详情
@Route(path = ActivityConstant.LIVE_BROAD_CAST_DETAILS)
public class LiveBroadCastDetailsActivity extends MyActivity {


    @BindView(R.id.video_view)
    VideoView videoView;

    @Autowired
    String videoUrl;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_broad_cast;
    }

    @Override
    protected void initView() {
        MediaPlayerManager.instance().setMediaPlayer(new ExoPlayer(this));
        videoView.setUp(videoUrl);
        videoView.setControlPanel(new ControlPanel(this));
        videoView.start();
    }

    @Override
    protected void initData() {

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



}
