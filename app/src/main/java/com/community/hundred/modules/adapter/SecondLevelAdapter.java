package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.secondlevel.entry.SecondEntry;

import java.util.ArrayList;
import java.util.List;

// 视频列表适配器
public class SecondLevelAdapter extends BaseRecyclerViewAdapter<SecondLevelAdapter.ViewHolder> {

    private Context mContext;
    private List<SecondEntry> list;

    public SecondLevelAdapter(Context context, List<SecondEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_second_level_video);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SecondEntry entry = list.get(position);
        holder.video_title.setText(entry.getVideoTitle());
        holder.tv_time.setText(entry.getTime());
        holder.tv_play_count.setText(entry.getPlayCount() + "次播放");
        Glide.with(mContext).load(R.mipmap.item_live).into(holder.img_cover);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        ImageView img_cover, img_play;
        TextView video_title, tv_time, tv_play_count;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            img_cover = (ImageView) findViewById(R.id.img_cover);
            img_play = (ImageView) findViewById(R.id.img_play);
            video_title = (TextView) findViewById(R.id.video_title);
            tv_time = (TextView) findViewById(R.id.tv_time);
            tv_play_count = (TextView) findViewById(R.id.tv_play_count);
        }
    }
}
