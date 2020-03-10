package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends BaseRecyclerViewAdapter<TestAdapter.ViewHolder> {

    private List<String> list;
    private Context mContext;

    public TestAdapter(Context context, List<String> list) {
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
        return new ViewHolder(parent, R.layout.item_test);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_content.setText(list.get(position));
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
}
