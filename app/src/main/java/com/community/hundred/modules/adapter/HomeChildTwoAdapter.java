package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.main.fragment.entry.HomeChildMenuEntry;

import java.util.ArrayList;
import java.util.List;

public class HomeChildTwoAdapter extends BaseRecyclerViewAdapter<HomeChildTwoAdapter.ViewHolder> {

    private Context mContext;
    private List<HomeChildMenuEntry> list;
    private OnItemClickListener onItemClickListener;


    public HomeChildTwoAdapter(Context context, List<HomeChildMenuEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_child_two_menu);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeChildMenuEntry entry = list.get(position);
        holder.tv_item_menu.setText(entry.getName());
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        TextView tv_item_menu;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            tv_item_menu = (TextView) findViewById(R.id.tv_item_menu);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
