package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.chat.entry.MsgEntry;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 消息适配器
public class PrivateLetterAdapter extends BaseRecyclerViewAdapter<PrivateLetterAdapter.ViewHolder> {

    private Context mContext;
    private List<MsgEntry> list;

    public PrivateLetterAdapter(Context context, List<MsgEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_private_letter);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MsgEntry msgEntry = list.get(position);
        if (msgEntry.getUid().equals(LoginUtils.getInstance().getUid())) {// 收到消息布局
            holder.ly_received_msg.setVisibility(View.GONE);
            holder.ly_send_msg.setVisibility(View.VISIBLE);
            holder.tv_send_msg.setText(msgEntry.getContent());
            Glide.with(mContext).load(msgEntry.getImage()).placeholder(R.mipmap.icon_live).into(holder.img_send_user_head);
        } else {// 发送消息布局
            holder.ly_received_msg.setVisibility(View.VISIBLE);
            holder.ly_send_msg.setVisibility(View.GONE);
            holder.tv_received_msg.setText(msgEntry.getContent());
            Glide.with(mContext).load(msgEntry.getImage()).placeholder(R.mipmap.item_live).into(holder.img_received_user_head);
        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        AutoRelativeLayout ly_received_msg, ly_send_msg;
        CircleImageView img_received_user_head, img_send_user_head;
        TextView tv_received_msg, tv_send_msg;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            ly_received_msg = (AutoRelativeLayout) findViewById(R.id.ly_received_msg);
            ly_send_msg = (AutoRelativeLayout) findViewById(R.id.ly_send_msg);
            img_received_user_head = (CircleImageView) findViewById(R.id.img_received_user_head);
            img_send_user_head = (CircleImageView) findViewById(R.id.img_send_user_head);
            tv_received_msg = (TextView) findViewById(R.id.tv_received_msg);
            tv_send_msg = (TextView) findViewById(R.id.tv_send_msg);
        }
    }
}
