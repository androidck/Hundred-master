package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;

// 系统通知适配器
public class SystemNotifyAdapter extends BaseRecyclerViewAdapter<SystemNotifyAdapter.ViewHolder> {
    public SystemNotifyAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_system_notify);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        TextView tvContent, tvTime;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            tvContent = (TextView) findViewById(R.id.tv_content);
            tvContent = (TextView) findViewById(R.id.tv_time);
        }
    }
}
