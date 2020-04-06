package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.view.RoundAngleImageView;
import com.community.hundred.modules.ui.livebroadcast.entry.SearchEntry;

import java.util.ArrayList;
import java.util.List;

// 搜索适配器
public class SearchAdapter extends BaseRecyclerViewAdapter<SearchAdapter.ViewHolder> {

    private List<SearchEntry> list;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public SearchAdapter(Context context, List<SearchEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_search);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchEntry entry = list.get(position);
        holder.tv_title.setText(entry.getName());
        Glide.with(mContext).load(HttpConstant.VIDEO_URL + entry.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.tv_img);
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        RoundAngleImageView tv_img;
        TextView tv_title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            tv_img = (RoundAngleImageView) findViewById(R.id.tv_img);
            tv_title = (TextView) findViewById(R.id.tv_title);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
