package com.community.hundred.modules.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.entry.MenuEntry;

import java.util.ArrayList;
import java.util.List;

// 筛选菜单适配器
public class ScreenMenuAdapter extends BaseRecyclerViewAdapter<ScreenMenuAdapter.ViewHolder> {

    private List<MenuEntry> list;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public ScreenMenuAdapter(Context context, List<MenuEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_screen_menu);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuEntry entry = list.get(position);
        holder.item_title.setText(entry.getTitle());
        if (entry.isSelect() == true) {
            // 选中
            holder.item_title.setBackground(mContext.getDrawable(R.drawable.shape_screen_menu_select));
            holder.item_title.setTextColor(Color.parseColor("#FE581E"));
        } else {
            holder.item_title.setBackground(mContext.getDrawable(R.drawable.shape_screen_menu_unselect));
            holder.item_title.setTextColor(Color.parseColor("#8d8d8d"));
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
        TextView item_title;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            item_title = (TextView) findViewById(R.id.item_title);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
