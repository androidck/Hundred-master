package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.user.entry.FansEntry;
import com.community.hundred.modules.ui.user.entry.FollowEntry;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 我的粉丝适配器
public class FansAdapter extends BaseRecyclerViewAdapter<FansAdapter.ViewHolder> {

    private List<FansEntry> list;
    private Context mContext;

    public FansAdapter(Context context, List<FansEntry> list) {
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
        FansEntry entry = list.get(position);
        Glide.with(mContext).load(entry.getImage()).into(holder.user_head);
        holder.tv_nick_name.setText(entry.getName());
        holder.tv_desc.setText(entry.getQianming());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        CircleImageView user_head;
        TextView tv_nick_name, tv_desc;
        Button btn_follow;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            user_head = (CircleImageView) findViewById(R.id.user_head);
            tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
            tv_desc = (TextView) findViewById(R.id.tv_desc);
            btn_follow = (Button) findViewById(R.id.btn_follow);
        }
    }
}
