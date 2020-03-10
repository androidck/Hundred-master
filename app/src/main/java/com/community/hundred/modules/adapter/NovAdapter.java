package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.main.fragment.entry.NovEntry;

import java.util.ArrayList;
import java.util.List;

// 小说列表适配器
public class NovAdapter extends BaseRecyclerViewAdapter<NovAdapter.ViewHolder> {

    private Context mContext;
    private List<NovEntry> list;

    private OnItemClickListener onItemClickListener;

    public NovAdapter(Context context, List<NovEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_nov);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NovEntry entry = list.get(position);
        holder.tv_nov_title.setText(entry.getTitle());
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        TextView tv_nov_title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            tv_nov_title = (TextView) findViewById(R.id.tv_nov_title);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
