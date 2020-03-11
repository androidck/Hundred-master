package com.community.hundred.modules.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.browseimg.JBrowseImgActivity;
import com.community.hundred.common.browseimg.util.JMatrixUtil;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.ninegridlayout.NineGridTestLayout;
import com.community.hundred.common.util.RelativeDateFormatUtils;
import com.community.hundred.common.view.RoundAngleImageView;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.post.entry.CircleEntry;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.salient.artplayer.Comparator;
import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.OnWindowDetachedListener;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.ui.ControlPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 圈子适配器
public class CircleAdapter extends BaseRecyclerViewAdapter<CircleAdapter.ViewHolder> {

    private List<CircleChildEntry> list;
    private Context mContext;
    private OnItemAttentionListener onItemAttentionListener;


    private OnJustLikeListener onJustLikeListener;

    private OnSendGiftListener onSendGiftListener;

    private OnClickListener onClickListener;

    private OnReportClickListener onReportClickListener;


    public CircleAdapter(Context context, List<CircleChildEntry> list) {
        super(context);
        this.mContext = context;
        this.list = list;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_follow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CircleChildEntry entry = list.get(position);
        if ("0".equals(entry.getVideo())) {
            holder.ly_nine.setVisibility(View.VISIBLE);
            holder.ly_video.setVisibility(View.GONE);
            holder.ly_nine.setUrlList(entry.getImages());
        } else {
            holder.ly_nine.setVisibility(View.GONE);
            holder.ly_video.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(HttpConstant.VIDEO_URL + entry.getVideo() + "?vframe/jpg/offset/1").into(holder.img_video_cover);
        }

        if ("1".equals(entry.getIsdz())) {
            Drawable drawableLeft = mContext.getDrawable(R.mipmap.icon_yizan);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            holder.tv_support.setCompoundDrawables(drawableLeft, null, null, null);
        } else {
            Drawable drawableLeft = mContext.getDrawable(R.mipmap.icon_zan);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            holder.tv_support.setCompoundDrawables(drawableLeft, null, null, null);
        }

        if ("1".equals(entry.getIsvip())) {
            holder.tv_is_vip.setVisibility(View.VISIBLE);
        } else {
            holder.tv_is_vip.setVisibility(View.GONE);
        }

        if ("1".equals(entry.getSex())) {
            holder.tv_nick_name.setCompoundDrawables(null, null, setDrawable(R.mipmap.icon_boy), null);
        }else {
            holder.tv_nick_name.setCompoundDrawables(null, null, setDrawable(R.mipmap.icon_girl), null);
        }
        // 头像
        Glide.with(mContext)
                .load(entry.getImage())
                .placeholder(R.mipmap.icon_not_login)
                .into(holder.user_head);
        holder.bindData(entry);


        // 关注按钮点击事件
        holder.tv_attention.setOnClickListener(v -> {
            onItemAttentionListener.onAttentionClick(position);
        });

        // 点赞
        holder.tv_support.setOnClickListener(v -> {
            onJustLikeListener.onJustLike(position);
        });

        // 送礼
        holder.img_gift.setOnClickListener(v -> {
            onSendGiftListener.onSendGift(position);
        });

        // item 点击事件
        holder.tv_comment.setOnClickListener(v -> {
            onClickListener.onClick(position);
        });

        // 举报
        holder.tv_report.setOnClickListener(v -> {
            onReportClickListener.onReport(position);
        });

        // 点击个人资料详情
        holder.user_head.setOnClickListener(v -> {
            ARouter.getInstance().build(ActivityConstant.PEOPLE_DETAILS)
                    .withString("uid", list.get(position).getUser_id())
                    .navigation();
        });

        // 播放视频
        holder.ly_video.setOnClickListener(v -> {
            ARouter.getInstance().build(ActivityConstant.LIVE_BROAD_CAST_DETAILS)
                    .withString("videoUrl", HttpConstant.VIDEO_URL + list.get(position).getVideo()).navigation();
        });


    }

    public Drawable setDrawable(int desId) {
        Drawable drawable = mContext.getDrawable(desId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        CircleImageView user_head;
        TextView tv_nick_name, img_gift, img_comment;
        ImageView tv_is_vip;
        TextView tv_location, tv_time, tv_attention, tv_report, tv_content, tv_label, tv_read_count, tv_support, tv_comment;
        NineGridTestLayout ly_nine;
        AutoLinearLayout ly_item;
        RoundAngleImageView img_video_cover;
        AutoRelativeLayout ly_video;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            ly_item = (AutoLinearLayout) findViewById(R.id.ly_item);
            user_head = (CircleImageView) findViewById(R.id.user_head);
            tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
            tv_is_vip = (ImageView) findViewById(R.id.tv_is_vip);
            img_gift = (TextView) findViewById(R.id.img_gift);
            img_comment = (TextView) findViewById(R.id.img_comment);

            tv_location = (TextView) findViewById(R.id.tv_location);
            tv_time = (TextView) findViewById(R.id.tv_time);
            tv_attention = (TextView) findViewById(R.id.tv_attention);
            tv_report = (TextView) findViewById(R.id.tv_report);
            tv_content = (TextView) findViewById(R.id.tv_content);
            tv_label = (TextView) findViewById(R.id.tv_label);
            tv_read_count = (TextView) findViewById(R.id.tv_read_count);
            tv_support = (TextView) findViewById(R.id.tv_support);
            tv_comment = (TextView) findViewById(R.id.tv_comment);

            ly_nine = (NineGridTestLayout) findViewById(R.id.ly_nine);
            img_video_cover = (RoundAngleImageView) findViewById(R.id.img_video_cover);
            ly_video = (AutoRelativeLayout) findViewById(R.id.ly_video);
        }

        public void bindData(CircleChildEntry entry) {
            // 头像
            tv_nick_name.setText(entry.getNickname());
            // 内容
            tv_content.setText(entry.getContent());
            // 城市
            tv_location.setText(entry.getCity());
            // 标签
            tv_label.setText(entry.getFlname());
            // 是否是vip
            if ("1".equals(entry.getIsvip())) {
                tv_is_vip.setVisibility(View.VISIBLE);
            } else {
                tv_is_vip.setVisibility(View.GONE);
            }
            // 礼物量
            img_gift.setText(entry.getLwsl());
            // 阅读量
            tv_read_count.setText("浏览量：" + entry.getLiulan());
            // 阅读量
            tv_comment.setText(entry.getComment_num());
            // 点赞
            tv_support.setText(entry.getLove());
            // 时间
            tv_time.setText(RelativeDateFormatUtils.format(new Date(Long.parseLong(entry.getRecord_time()) * 1000)));

            // 是否关注
            if ("1".equals(entry.getIsgz())) {
                tv_attention.setBackgroundResource(R.drawable.shape_setup_attent_button);
                tv_attention.setText("已关注");
            } else {
                tv_attention.setBackgroundResource(R.drawable.shape_setup_button);
                tv_attention.setText("关注");
            }
        }
    }


    // 关注/ 取消关注
    public interface OnItemAttentionListener {
        void onAttentionClick(int position);
    }

    public void setOnItemAttentionListener(OnItemAttentionListener onItemAttentionListener) {
        this.onItemAttentionListener = onItemAttentionListener;
    }

    public interface OnJustLikeListener {
        void onJustLike(int position);
    }

    public void setOnJustLikeListener(OnJustLikeListener onJustLikeListener) {
        this.onJustLikeListener = onJustLikeListener;
    }

    public interface OnSendGiftListener {
        void onSendGift(int position);
    }

    public void setOnSendGiftListener(OnSendGiftListener onSendGiftListener) {
        this.onSendGiftListener = onSendGiftListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnReportClickListener {
        void onReport(int position);
    }

    public void setOnReportClickListener(OnReportClickListener onReportClickListener) {
        this.onReportClickListener = onReportClickListener;
    }
}
