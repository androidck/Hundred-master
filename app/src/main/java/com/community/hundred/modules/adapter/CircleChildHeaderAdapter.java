package com.community.hundred.modules.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.view.RoundAngleImageView;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildHeaderEntry;

import java.util.ArrayList;
import java.util.List;

// 圈子头部适配器
public class CircleChildHeaderAdapter extends BaseRecyclerViewAdapter<CircleChildHeaderAdapter.ViewHolder> {

    private List<CircleChildHeaderEntry> list;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public CircleChildHeaderAdapter(Context context, List<CircleChildHeaderEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_circle_header);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CircleChildHeaderEntry entry = list.get(position);
        holder.tv_title.setText(entry.getName());
        Glide.with(mContext).load(entry.getImage()).into(holder.img_circle);
        if (entry.isSelect() == true) {
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }else {
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.textColor));
        }

        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        RoundAngleImageView img_circle;
        TextView tv_title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            img_circle = (RoundAngleImageView) findViewById(R.id.img_circle);
            tv_title = (TextView) findViewById(R.id.tv_title);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
