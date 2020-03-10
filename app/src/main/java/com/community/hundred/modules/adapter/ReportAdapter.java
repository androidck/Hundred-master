package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.entry.ReportEntry;

import java.util.ArrayList;
import java.util.List;

// 举报适配器
public class ReportAdapter extends BaseRecyclerViewAdapter<ReportAdapter.ViewHolder> {

    private Context mContext;

    private List<ReportEntry> list;
    private OnItemClickListener onItemClickListener;

    public ReportAdapter(Context context, List<ReportEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_report);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportEntry entry = list.get(position);
        holder.tv_content.setText(entry.getContent());
        if (entry.isSelect() == true) {
            holder.tv_content.setBackground(mContext.getDrawable(R.drawable.shape_report_select_bg));
        } else {
            holder.tv_content.setBackground(mContext.getDrawable(R.drawable.shape_report_unselect_bg));
        }
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        TextView tv_content;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            tv_content = (TextView) findViewById(R.id.tv_content);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
