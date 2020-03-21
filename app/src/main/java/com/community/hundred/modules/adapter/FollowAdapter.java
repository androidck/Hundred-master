package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.user.entry.FollowEntry;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 我的粉丝适配器
public class FollowAdapter extends BaseRecyclerViewAdapter<FollowAdapter.ViewHolder> {

    private List<FollowEntry> list;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public FollowAdapter(Context context, List<FollowEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_fans);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowEntry entry = list.get(position);
        Glide.with(mContext).load(entry.getImage()).into(holder.user_head);
        holder.tv_nick_name.setText(entry.getName());
        holder.tv_desc.setText(entry.getQianming());
        holder.btn_follow.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        CircleImageView user_head;
        TextView tv_nick_name, tv_desc;
        TextView btn_follow;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            user_head = (CircleImageView) findViewById(R.id.user_head);
            tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
            tv_desc = (TextView) findViewById(R.id.tv_desc);
            btn_follow = (TextView) findViewById(R.id.btn_follow);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
