package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.pay.entry.MemberEntry;

import java.util.ArrayList;
import java.util.List;

// 会员开通适配器
public class MemberRechargeAdapter extends BaseRecyclerViewAdapter<MemberRechargeAdapter.ViewHolder> {

    private Context mContext;

    private List<MemberEntry> list;

    private OnItemOnClickListener onItemOnClickListener;

    public MemberRechargeAdapter(Context context, List<MemberEntry> list) {
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
        return new ViewHolder(parent, R.layout.one);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberEntry entry = list.get(position);
        holder.tv.setText(entry.getTime() + "天/" + entry.getMoney() + "元");
        if (entry.isSelect() == true) {
            holder.tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            holder.tv.setBackground(mContext.getDrawable(R.drawable.shape_money_select_bg));
        } else {
            holder.tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            holder.tv.setBackground(mContext.getDrawable(R.drawable.shape_money_unselect_bg));
        }

        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            onItemOnClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        TextView tv;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            tv = (TextView) findViewById(R.id.tv);
        }
    }

    public interface OnItemOnClickListener {
        void onClick(int position);
    }

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }
}
