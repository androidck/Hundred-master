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
import com.community.hundred.modules.ui.femalestar.entry.FemaleInfoEntry;
import com.community.hundred.modules.ui.livebroadcast.entry.SearchEntry;

import java.util.ArrayList;
import java.util.List;

// 女星适配器
public class FemaleStarVideoAdapter extends BaseRecyclerViewAdapter<FemaleStarVideoAdapter.ViewHolder> {

    private Context mContext;
    private List<FemaleInfoEntry> list;

    private OnItemClickListener onItemClickListener;

    public FemaleStarVideoAdapter(Context context, List<FemaleInfoEntry> list) {
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
        FemaleInfoEntry entry = list.get(position);
        Glide.with(mContext).load(HttpConstant.BASE_HOST + entry.getImage()).into(holder.tv_img);
        holder.tv_title.setText(entry.getName());
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
