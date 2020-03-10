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
import com.community.hundred.modules.ui.main.fragment.entry.LiveBroadCastPlatformEntry;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveBroadcastAdapter extends BaseRecyclerViewAdapter<LiveBroadcastAdapter.ViewHolder> {

    private Context mContext;
    private List<LiveBroadCastPlatformEntry.PingtaiBean> list;

    private OnItemClickListener onItemClickListener;

    public LiveBroadcastAdapter(Context context, List<LiveBroadCastPlatformEntry.PingtaiBean> list) {
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
        return new ViewHolder(parent, R.layout.item_live_broad_cast);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LiveBroadCastPlatformEntry.PingtaiBean entry = list.get(position);
        holder.item_tv_title.setText(entry.getTitle());
        Glide.with(mContext).load(entry.getXinimg())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(holder.item_logo);
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        CircleImageView item_logo;
        TextView item_tv_title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            item_logo = (CircleImageView) findViewById(R.id.item_logo);
            item_tv_title = (TextView) findViewById(R.id.item_tv_title);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
