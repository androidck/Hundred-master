package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.common.view.StrokeTextView;
import com.community.hundred.modules.ui.main.fragment.entry.RankingEntry;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 适配器
public class RankingAdapter extends BaseRecyclerViewAdapter<RankingAdapter.ViewHolder> {

    private Context mContext;

    private List<RankingEntry> list;

    public RankingAdapter(Context context, List<RankingEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_ranking_user);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankingEntry entry = list.get(position);
        holder.gift_num.setText(entry.getNumber());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        StrokeTextView gift_num;
        CircleImageView user_head;
        TextView tv_nick_name, tv_desc;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            gift_num = (StrokeTextView) findViewById(R.id.gift_num);
            user_head = (CircleImageView) findViewById(R.id.user_head);
            tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
            tv_desc = (TextView) findViewById(R.id.tv_desc);
        }
    }
}
