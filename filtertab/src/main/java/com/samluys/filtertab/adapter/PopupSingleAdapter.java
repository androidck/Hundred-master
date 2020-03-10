package com.samluys.filtertab.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.samluys.filtertab.R;
import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.util.SpUtils;

import java.util.List;


public class PopupSingleAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BaseFilterBean> mList;
    private OnItemClickListener onItemClickListener;

    public PopupSingleAdapter(Context context, List<BaseFilterBean> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_popup_single, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder viewHolder = (ViewHolder)holder;
        BaseFilterBean bean = mList.get(position);
        viewHolder.tv_content.setText(bean.getItemName());

        // 是否设置“不限”为选中
        boolean isSelectFirst = true;
        for (int i = 0; i < mList.size(); i++) {
            BaseFilterBean entity = mList.get(i);
            if (entity.getSelecteStatus() == 1) {
                isSelectFirst = false;
                break;
            }
        }

        if (isSelectFirst) {
            mList.get(0).setSelecteStatus(1);
        }

        if (bean.getSelecteStatus() == 0) {
            if (SpUtils.getInstance(mContext).getTextStyle() == 1) {
                TextPaint textPaint = viewHolder.tv_content.getPaint();
            }
            viewHolder.tv_content.setTextColor(Color.parseColor("#CACACA"));
            viewHolder.tv_content.setBackground(mContext.getDrawable(R.drawable.shape_item_pop_window_unselect));
        } else {
            if (SpUtils.getInstance(mContext).getTextStyle() == 1) {
                TextPaint textPaint = viewHolder.tv_content.getPaint();
            }
            viewHolder.tv_content.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.tv_content.setBackground(mContext.getDrawable(R.drawable.shape_item_pop_window_select));

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mList.size(); i++) {
                    if (i == position) {
                        mList.get(position).setSelecteStatus(1);
                    } else {
                        mList.get(i).setSelecteStatus(0);
                    }
                }
                notifyDataSetChanged();
                onItemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_tab_name,tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_tab_name = itemView.findViewById(R.id.tv_tab_name);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
