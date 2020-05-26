package com.community.hundred.modules.adapter;

import android.content.Context;

import com.community.hundred.R;
import com.community.hundred.modules.ui.classify.entry.ClassifyChildEntry;
import com.community.hundred.modules.ui.classify.entry.ClassifyHeaderEntry;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;

import java.util.ArrayList;

// 分类适配器
public class GroupClassifyAdapter extends GroupedRecyclerViewAdapter {

    private Context mContext;

    private ArrayList<ClassifyHeaderEntry> list;


    public GroupClassifyAdapter(Context context, ArrayList<ClassifyHeaderEntry> list) {
        super(context);
        this.mContext = context;
        this.list = list;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public int getGroupCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ClassifyChildEntry> son = list.get(groupPosition).getSon();
        return son == null ? 0 : son.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.item_header_classify;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.item_child_classify;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        ClassifyHeaderEntry entry = list.get(groupPosition);
        holder.setText(R.id.tv_head_name, entry.getName());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        ClassifyChildEntry entry = list.get(groupPosition).getSon().get(childPosition);
        holder.setText(R.id.tv_title, entry.getName());
    }
}
