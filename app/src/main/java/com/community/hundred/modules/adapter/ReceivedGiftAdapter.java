package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.util.RelativeDateFormatUtils;
import com.community.hundred.modules.ui.user.entry.GiftEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 我的礼物列表适配器
public class ReceivedGiftAdapter extends BaseRecyclerViewAdapter<ReceivedGiftAdapter.ViewHolder> {

    private Context mContext;
    private List<GiftEntry> list;

    public ReceivedGiftAdapter(Context context, List<GiftEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_received_gift);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiftEntry entry = list.get(position);
        Glide.with(mContext).load(entry.getImage()).into(holder.user_head);
        holder.tv_gift_desc.setText("送给你" + entry.getCount() + "个" + entry.getName());
        holder.tv_time.setText(RelativeDateFormatUtils.format(new Date(Long.parseLong(entry.getTime()) * 1000)));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        CircleImageView user_head;
        TextView tv_nick_name;
        TextView tv_time;
        TextView tv_gift_desc;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            user_head = (CircleImageView) findViewById(R.id.user_head);
            tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
            tv_time = (TextView) findViewById(R.id.tv_time);
            tv_gift_desc = (TextView) findViewById(R.id.tv_gift_desc);
        }
    }
}


