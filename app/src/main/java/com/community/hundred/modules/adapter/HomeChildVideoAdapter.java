package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.view.RoundAngleImageView;
import com.community.hundred.modules.ui.main.fragment.entry.HomeVideoEntry;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

// 首页列表适配器
public class HomeChildVideoAdapter extends BaseRecyclerViewAdapter<HomeChildVideoAdapter.ViewHolder> {

    private Context mContext;
    private List<HomeVideoEntry> list;

    private OnItemClickListener onItemClickListener;

    public HomeChildVideoAdapter(Context context, List<HomeVideoEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_home_child_video);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeVideoEntry entry = list.get(position);
        holder.tv_video_title.setText(entry.getName());
        Glide.with(mContext).load(entry.getImage()).into(holder.img_video);
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        ImageView img_video;
        TextView tv_video_title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            img_video = (ImageView) findViewById(R.id.img_cover);
            tv_video_title = (TextView) findViewById(R.id.video_title);

        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
