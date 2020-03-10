package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.classify.entry.ClassifyChildEntry;

import java.util.ArrayList;
import java.util.List;

// 分类适配器
public class ClassifyAdapter extends BaseRecyclerViewAdapter<ClassifyAdapter.ViewHolder> {

    private Context mContext;
    private List<ClassifyChildEntry> list;

    private OnItemClickListener onItemClickListener;
    private OnLongItemClickListener onLongItemClickListener;

    public ClassifyAdapter(Context context, List<ClassifyChildEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_classify);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassifyChildEntry entry = list.get(position);
        holder.tv_title.setText(entry.getName());
        holder.img_symbol.setImageResource(R.mipmap.item_add);
        if (entry.isEdit() == true) {
            holder.img_symbol.setVisibility(View.VISIBLE);
        } else if (entry.isEdit() == false) {
            holder.img_symbol.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongItemClickListener.onLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        TextView tv_title;
        ImageView img_symbol;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            tv_title = (TextView) findViewById(R.id.tv_title);
            img_symbol = (ImageView) findViewById(R.id.img_symbol);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnLongItemClickListener {
        void onLongClick(int position);
    }

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        this.onLongItemClickListener = onLongItemClickListener;
    }
}
