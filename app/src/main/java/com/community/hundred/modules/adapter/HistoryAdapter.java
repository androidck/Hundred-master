package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.view.RoundAngleImageView;
import com.community.hundred.modules.ui.setup.entry.HistoryEntry;

import java.util.List;

// 历史观看适配器
public class HistoryAdapter extends BaseRecyclerViewAdapter<HistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<HistoryEntry> list;

    public HistoryAdapter(Context context, List<HistoryEntry> list) {
        super(context);
        this.mContext = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_history);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryEntry entry = list.get(position);
        Glide.with(mContext).load(entry.getImage()).into(holder.item_img);
        holder.tv_title.setText(entry.getSpname());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        RoundAngleImageView item_img;
        TextView tv_title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            item_img = (RoundAngleImageView) findViewById(R.id.item_img);
            tv_title = (TextView) findViewById(R.id.tv_title);
        }
    }
}
