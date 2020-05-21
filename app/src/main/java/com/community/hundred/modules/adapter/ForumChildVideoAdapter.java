package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.view.RoundAngleImageView;
import com.community.hundred.modules.ui.main.fragment.entry.ForumChildNewVideoEntry;
import com.community.hundred.modules.ui.main.fragment.entry.ForumChildVideoEntry;

import java.util.ArrayList;
import java.util.List;

// 视频列表适配器
public class ForumChildVideoAdapter extends BaseRecyclerViewAdapter<ForumChildVideoAdapter.ViewHolder> {


    private Context mContext;
    private List<ForumChildNewVideoEntry> list;

    public ForumChildVideoAdapter(Context context, List<ForumChildNewVideoEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_forum_child_video);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForumChildNewVideoEntry entry = list.get(position);
        holder.tv_video_title.setText(entry.getName());
        Glide.with(mContext)
                .load(entry.getImage())
                .placeholder(R.drawable.banner_default)
                .into(holder.img_video);
        holder.itemView.setOnClickListener(v -> {
            ARouter.getInstance().build(ActivityConstant.VIDEO_DETAILS)
                    .withString("videoId", entry.getId())
                    .navigation();
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        RoundAngleImageView img_video;
        TextView tv_video_title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            img_video = (RoundAngleImageView) findViewById(R.id.img_video);
            tv_video_title = (TextView) findViewById(R.id.tv_video_title);
        }
    }


}
