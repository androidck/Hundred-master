package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.util.TimeUtils;
import com.community.hundred.modules.ui.livebroadcast.entry.SystemEntry;

import java.util.ArrayList;
import java.util.List;

// 系统通知适配器
public class SystemNotifyAdapter extends BaseRecyclerViewAdapter<SystemNotifyAdapter.ViewHolder> {

    private List<SystemEntry> list;
    private Context mContext;

    public SystemNotifyAdapter(Context context, List<SystemEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_system_notify);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SystemEntry entry = list.get(position);
        holder.tvContent.setText(entry.getDescription());
        holder.tvTime.setText(TimeUtils.timestampToStr(Long.valueOf(entry.getAdd_time())));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        TextView tvContent, tvTime;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            tvContent = (TextView) findViewById(R.id.tv_content);
            tvTime = (TextView) findViewById(R.id.tv_time);
        }
    }
}
