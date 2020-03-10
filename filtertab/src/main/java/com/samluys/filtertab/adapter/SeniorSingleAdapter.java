package com.samluys.filtertab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samluys.filtertab.R;
import com.samluys.filtertab.base.BaseFilterBean;

import java.util.List;


// 高级筛选适配器
public class SeniorSingleAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BaseFilterBean> mList;
    private OnItemClickListener onItemClickListener;

    public SeniorSingleAdapter(Context context, List<BaseFilterBean> list) {
        mContext = context;
        mList = list;
    }

    // 重置数据
    public void setReset(List<BaseFilterBean> list){
        mList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_basice_select, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder)holder;
        BaseFilterBean bean = mList.get(position);
        viewHolder.tv_tab_name.setText(bean.getItemName());
        viewHolder.tv_condition.setText(bean.getCondition());
        viewHolder.tv_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_tab_name,tv_condition;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_tab_name = itemView.findViewById(R.id.tv_tab_name);
            tv_condition = itemView.findViewById(R.id.tv_condition);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
