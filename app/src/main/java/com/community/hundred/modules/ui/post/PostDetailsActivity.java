package com.community.hundred.modules.ui.post;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.dialog.MessageDialog;
import com.community.hundred.common.helper.ActivityStackManager;
import com.community.hundred.common.ninegridlayout.NineGridTestLayout;
import com.community.hundred.common.util.RelativeDateFormatUtils;
import com.community.hundred.common.util.StatusBarUtil;
import com.community.hundred.modules.adapter.CommentAdapter;
import com.community.hundred.modules.dialog.GiftDialog;
import com.community.hundred.modules.dialog.ReportDialog;
import com.community.hundred.modules.eventbus.CommentWrap;
import com.community.hundred.modules.eventbus.SendGiftWrap;
import com.community.hundred.modules.eventbus.SpecialWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.entry.SendGiftEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.CircleChildPresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.ICircleChildView;
import com.community.hundred.modules.ui.post.entry.CommentEntry;
import com.hjq.base.BaseDialog;
import com.hjq.widget.view.ClearEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.exo.ExoPlayer;
import org.salient.artplayer.ui.ControlPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

// 帖子内容详情
@Route(path = ActivityConstant.POST_DETAILS)
public class PostDetailsActivity extends MyActivity<ICircleChildView, CircleChildPresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.ed_content)
    ClearEditText edContent;
    @BindView(R.id.img_emoji)
    ImageView imgEmoji;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.ly_comment)
    AutoLinearLayout lyComment;

    private View headView;

    @Autowired
    String id;

    private CircleImageView user_head;
    private TextView tv_nick_name, img_gift, img_comment;
    private ImageView tv_is_vip;
    private TextView tv_location, tv_time, tv_attention, tv_report, tv_content, tv_label, tv_read_count, tv_support, tv_comment;
    private NineGridTestLayout ly_nine;
    private VideoView videoView;

    private CommentAdapter adapter;

    private String circleId;

    private List<CommentEntry> list = new ArrayList<>();

    @Override
    protected CircleChildPresenter createPresenter() {
        return new CircleChildPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMode(this, false, R.color.colorAccent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_details;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        EventBus.getDefault().register(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headView = getLayoutInflater().inflate(R.layout.view_header_post_details, recyclerView, false);
        recyclerView.addHeaderView(headView);

        user_head = headView.findViewById(R.id.user_head);
        tv_nick_name = headView.findViewById(R.id.tv_nick_name);
        tv_is_vip = headView.findViewById(R.id.tv_is_vip);
        img_gift = headView.findViewById(R.id.img_gift);
        img_comment = headView.findViewById(R.id.img_comment);
        tv_location = headView.findViewById(R.id.tv_location);
        tv_time = headView.findViewById(R.id.tv_time);
        tv_attention = headView.findViewById(R.id.tv_attention);
        tv_report = headView.findViewById(R.id.tv_report);
        tv_content = headView.findViewById(R.id.tv_content);
        tv_label = headView.findViewById(R.id.tv_label);
        tv_read_count = headView.findViewById(R.id.tv_read_count);
        tv_support = headView.findViewById(R.id.tv_support);
        tv_comment = headView.findViewById(R.id.tv_comment);
        ly_nine = headView.findViewById(R.id.ly_nine);
        videoView = headView.findViewById(R.id.video_view);

        adapter = new CommentAdapter(this, list);
        recyclerView.setAdapter(adapter);
        MediaPlayerManager.instance().setMediaPlayer(new ExoPlayer(this));
        videoView.setControlPanel(new ControlPanel(this));
        // 删除评论
        adapter.setOnDelCommentListener(position -> {
            if (LoginUtils.getInstance().getUid().equals(list.get(position).getUserinfo().getId())) {
                new MessageDialog.Builder(this)
                        .setTitle("删除评论")
                        .setMessage("确定要删除评论吗？")
                        .setConfirm(getString(R.string.common_confirm))
                        .setCancel(getString(R.string.common_cancel))
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                mPresenter.delComment(list.get(position).getId());
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        // 评论点赞
        adapter.setOnLoveListener(position -> {
            mPresenter.commentLove(list.get(position).getId());
        });

        refresh.setEnableLoadMore(false);
        refresh.setEnableRefresh(false);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    protected void initData() {
        getDetails();
    }

    // 获取帖子详情
    public void getDetails() {
        mPresenter.getPostDetails(id);
        mPresenter.setOnPostDetailsListener(entry -> {
            setTitle(entry.getNickname());
            // 头像
            Glide.with(this).load(entry.getImage()).into(user_head);
            tv_nick_name.setText(entry.getNickname());
            tv_time.setText(RelativeDateFormatUtils.format(new Date(Long.parseLong(entry.getRecord_time()) * 1000)));
            tv_content.setText(entry.getContent());
            if (entry.getImages().size() != 0) {
                if ("0".equals(entry.getVideo())) {
                    ly_nine.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                    ly_nine.setUrlList(entry.getImages());
                } else {
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setUp(HttpConstant.VIDEO_URL + entry.getVideo());
                    ly_nine.setVisibility(View.GONE);
                }
            } else {
                ly_nine.setVisibility(View.GONE);
            }
            if ("1".equals(entry.getIsdz())) {
                Drawable drawableLeft = getDrawable(R.mipmap.icon_yizan);
                drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                tv_support.setCompoundDrawables(drawableLeft, null, null, null);
            } else {
                Drawable drawableLeft = getDrawable(R.mipmap.icon_zan);
                drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                tv_support.setCompoundDrawables(drawableLeft, null, null, null);
            }

            // 是否关注
            if ("1".equals(entry.getIsgz())) {
                tv_attention.setBackgroundResource(R.drawable.shape_setup_attent_button);
                tv_attention.setText("已关注");
            } else {
                tv_attention.setBackgroundResource(R.drawable.shape_setup_button);
                tv_attention.setText("关注");
            }

            tv_label.setText(entry.getFlname());
            tv_read_count.setText("浏览量：" + entry.getLiulan());
            tv_support.setText(entry.getLove());
            tv_comment.setText(entry.getComment_num());
            img_gift.setText(entry.getLwsl());
            tv_location.setText(entry.getCity());
            circleId = entry.getId();
            list.clear();
            getComment(entry.getId());

            // 送礼
            img_gift.setOnClickListener(v -> {
                if (LoginUtils.getInstance().isLogin()) {
                    gitGift();
                } else {
                    notLogin();
                }
            });

            // 点赞
            tv_support.setOnClickListener(v -> {
                mPresenter.setJustLike(entry.getIsdz(), id);
                mPresenter.setOnSuccessListener(state -> {
                    if (LoginUtils.getInstance().isLogin()) {
                        int love = Integer.parseInt(entry.getLove());
                        if (state == 3) {
                            love++;
                            entry.setIsdz("1");
                            tv_support.setText(String.valueOf(love));
                            Drawable drawableLeft = getDrawable(R.mipmap.icon_yizan);
                            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                            tv_support.setCompoundDrawables(drawableLeft, null, null, null);
                        } else {
                            love--;
                            entry.setIsdz("0");
                            tv_support.setText(String.valueOf(love));
                            Drawable drawableLeft = getDrawable(R.mipmap.icon_zan);
                            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                            tv_support.setCompoundDrawables(drawableLeft, null, null, null);
                        }
                    } else {
                        notLogin();
                    }
                });
            });

            // 关注 取消关注
            tv_attention.setOnClickListener(v -> {
                if (LoginUtils.getInstance().isLogin()) {
                    if (LoginUtils.getInstance().getUid().equals(entry.getUser_id())) {
                        toast("不能关注自己");
                    } else {
                        if ("1".equals(entry.getIsgz())) {
                            // 取消关注
                            mPresenter.setEscAttention(entry.getUser_id());
                        } else {
                            mPresenter.setAttention(entry.getUser_id());
                        }
                    }
                    mPresenter.setOnSuccessListener(state -> {
                        if (state == 1) {
                            entry.setIsgz("1");
                            tv_attention.setBackgroundResource(R.drawable.shape_setup_attent_button);
                            tv_attention.setText("已关注");
                            EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.FOLLOW_REFRESH));
                        } else {
                            entry.setIsgz("0");
                            tv_attention.setBackgroundResource(R.drawable.shape_setup_button);
                            tv_attention.setText("关注");
                            EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.FOLLOW_REFRESH));
                        }
                    });
                } else {
                    notLogin();
                }
            });

            // 举报
            tv_support.setOnClickListener(v -> {
                if (LoginUtils.getInstance().isLogin()) {
                    new ReportDialog(this, content -> {
                        mPresenter.setReport(id, content);
                    }).show();
                } else {
                    notLogin();
                }
            });
        });
    }

    @OnClick({R.id.img_emoji, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_emoji:
                break;
            case R.id.btn_send:
                if (LoginUtils.getInstance().isLogin()) {
                    isVerification();
                } else {
                    notLogin();
                }
                break;
        }
    }

    public void gitGift() {
        mPresenter.getGiftList();
        mPresenter.setOnGiftListener((yue, list1) -> {
            new GiftDialog(this, yue, list1, (name, giftId, img) -> {
                mPresenter.senGift(giftId, id);
                mPresenter.setOnSuccessListener(state -> {
                    if (state == 8) {
                        SendGiftEntry entry = new SendGiftEntry();
                        entry.name = "轮回的悲伤";// 名称
                        entry.giftName = "送你：" + name;//礼物名称
                        entry.img = img;// 头像
                        entry.giftImg = img;//礼物图片
                        entry.num = 0;// 数量
                        EventBus.getDefault().post(SendGiftWrap.getInstance(entry));
                    }
                });
            }).show();
        });
    }

    @Override
    public void isVerification() {
        super.isVerification();
        if (LoginUtils.getInstance().isLogin()) {
            if (TextUtils.isEmpty(edContent.getText().toString().trim())) {
                toast("请输入评论内容");
            } else {
                sendComment();
            }
        } else {
            notLogin();
        }

    }

    // 获取评论列表
    public void getComment(String tid) {
        mPresenter.getCommentCircle(tid);
        mPresenter.setOnCommentListener(commentEntries -> {
            list.addAll(commentEntries);
            adapter.notifyDataSetChanged();
        });
    }

    // 评论
    public void sendComment() {
        mPresenter.setComment(circleId, edContent.getText().toString().trim());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        MediaPlayerManager.instance().releasePlayerAndView(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentWrap(CommentWrap commentWrap) {
        if (commentWrap.code == 200) {
            // 刷新数据 清楚数据
            list.clear();
            getComment(circleId);
            getDetails();
            edContent.setText("");
        }
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
}
