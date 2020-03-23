package com.community.hundred.modules.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.view.StrokeTextView;
import com.community.hundred.modules.ui.main.fragment.entry.RankingEntry;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 适配器
public class RankingAdapter extends BaseRecyclerViewAdapter<RankingAdapter.ViewHolder> {

    private Context mContext;

    private List<RankingEntry> list;

    private int type;

    private OnItemAttentionListener onItemAttentionListener;

    public RankingAdapter(Context context, List<RankingEntry> list) {
        super(context);
        this.mContext = context;
        this.list = list;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_ranking_user);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankingEntry entry = list.get(position);
        holder.gift_num.setText((position + 4) + "");
        holder.tv_nick_name.setText(entry.getName());
        Glide.with(mContext).load(entry.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.user_head);
        if (type == 1) {
            String str = "<font>打赏价值</font><font color='#FE581E'>" + entry.getZonjine() + "</font><font>元的礼物</font>";
            holder.tv_desc.setText(Html.fromHtml(str));
        }

        // 是否关注
        if ("1".equals(entry.getIsgz())) {
            holder.tv_attention.setBackgroundResource(R.drawable.shape_setup_attent_button);
            holder.tv_attention.setText("已关注");
            holder.tv_attention.setCompoundDrawables(null, null, null, null);
            holder.tv_attention.setPadding(0, 6, 0, 6);
        } else {
            holder.tv_attention.setBackgroundResource(R.drawable.shape_setup_button);
            holder.tv_attention.setText("关注");
            holder.tv_attention.setCompoundDrawables(setDrawable(R.mipmap.icon_add_follow), null, null, null);
            holder.tv_attention.setPadding(20, 6, 0, 6);
        }

        // 关注按钮点击事件
        holder.tv_attention.setOnClickListener(v -> {
            onItemAttentionListener.onAttentionClick(position);
        });
    }

    public Drawable setDrawable(int desId) {
        Drawable drawable = mContext.getDrawable(desId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        StrokeTextView gift_num;
        CircleImageView user_head;
        TextView tv_nick_name, tv_desc, tv_attention;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            gift_num = (StrokeTextView) findViewById(R.id.gift_num);
            user_head = (CircleImageView) findViewById(R.id.user_head);
            tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
            tv_desc = (TextView) findViewById(R.id.tv_desc);
            tv_attention = (TextView) findViewById(R.id.tv_attention);
        }
    }

    // 关注/ 取消关注
    public interface OnItemAttentionListener {
        void onAttentionClick(int position);
    }


    public void setOnItemAttentionListener(OnItemAttentionListener onItemAttentionListener) {
        this.onItemAttentionListener = onItemAttentionListener;
    }
}
