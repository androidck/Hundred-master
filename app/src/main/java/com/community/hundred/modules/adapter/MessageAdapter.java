package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.util.RelativeDateFormatUtils;
import com.community.hundred.modules.ui.livebroadcast.entry.MyMessageEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 消息列表适配器
public class MessageAdapter extends BaseRecyclerViewAdapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<MyMessageEntry> list;

    private OnItemClickListener onItemClickListener;

    public MessageAdapter(Context context, List<MyMessageEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_my_message);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyMessageEntry entry = list.get(position);
        holder.tv_nick_name.setText(entry.getNickname());
        Glide.with(mContext).load(entry.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.user_head);
        // 时间
        holder.tv_time.setText(RelativeDateFormatUtils.format(new Date(Long.parseLong(entry.getCreatetime()) * 1000)));
        holder.tv_message.setText(entry.getContent());
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        CircleImageView user_head;
        TextView tv_message_unread_message;
        TextView tv_nick_name, tv_time, tv_message;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            user_head = (CircleImageView) findViewById(R.id.user_head);
            tv_message_unread_message = (TextView) findViewById(R.id.tv_message_unread_message);
            tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
            tv_time = (TextView) findViewById(R.id.tv_time);
            tv_message = (TextView) findViewById(R.id.tv_message);

        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
