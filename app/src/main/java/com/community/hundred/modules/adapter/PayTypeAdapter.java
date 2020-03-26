package com.community.hundred.modules.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.pay.entry.PayTypeEntry;

import java.util.ArrayList;
import java.util.List;

// 支付方式adapter
public class PayTypeAdapter extends BaseRecyclerViewAdapter<PayTypeAdapter.ViewHolder> {

    private Context mContext;
    private List<PayTypeEntry> list;
    private OnItemClickListener onItemClickListener;

    public PayTypeAdapter(Context context, List<PayTypeEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_pay_type);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PayTypeEntry entry = list.get(position);
        if (entry.isSelect() == true) {
            holder.img_select.setImageResource(R.mipmap.channel_active);
        } else {
            holder.img_select.setImageResource(R.mipmap.channel);
        }

        holder.tv_name.setText(entry.getName());
        holder.tv_name.setCompoundDrawables(setDrawable(entry.getIcon()), null, null, null);


        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        ImageView img_select;
        TextView tv_name;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            img_select = (ImageView) findViewById(R.id.img_select);
            tv_name = (TextView) findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Drawable setDrawable(int desId) {
        Drawable drawable = mContext.getDrawable(desId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

}
