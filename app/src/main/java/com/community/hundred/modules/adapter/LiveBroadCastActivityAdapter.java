package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.livebroadcast.entry.LiveBroadCastEntry;
import com.community.hundred.modules.ui.main.fragment.entry.AnchorEntry;

import java.util.ArrayList;
import java.util.List;

// 直播adapter
public class LiveBroadCastActivityAdapter extends BaseRecyclerViewAdapter<LiveBroadCastActivityAdapter.ViewHolder> {

    private Context mContext;
    private List<AnchorEntry.ZhuboBean> list;

    public OnItemClickListener onItemClickListener;

    public LiveBroadCastActivityAdapter(Context context, List<AnchorEntry.ZhuboBean> list) {
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
        return new ViewHolder(parent, R.layout.item_live_broad_cast_activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnchorEntry.ZhuboBean entry = list.get(position);
        Glide.with(mContext).load(entry.getImg()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.item_img);
        holder.title.setText(entry.getTitle());
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        ImageView item_img;
        TextView title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            item_img = (ImageView) findViewById(R.id.item_img);
            title = (TextView) findViewById(R.id.title);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
