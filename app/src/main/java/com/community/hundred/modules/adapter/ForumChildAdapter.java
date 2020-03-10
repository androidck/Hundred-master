package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseRecyclerViewAdapter;
import com.community.hundred.modules.ui.main.fragment.entry.ForumChildEntry;
import com.community.hundred.modules.ui.main.fragment.entry.ForumChildNewEntry;
import com.community.hundred.modules.ui.main.fragment.entry.ForumChildNewVideoEntry;
import com.community.hundred.modules.ui.main.fragment.entry.ForumChildVideoEntry;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 视频列表适配器
public class ForumChildAdapter extends BaseRecyclerViewAdapter<ForumChildAdapter.ViewHolder> {

    private Context mContext;
    private List<ForumChildNewEntry> list;
    private ForumChildVideoAdapter adapter;
    private List<ForumChildNewVideoEntry> entryList;
    private OnItemClickListener onItemClickListener;


    public ForumChildAdapter(Context context, List<ForumChildNewEntry> list) {
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
        return new ViewHolder(parent, R.layout.item_video);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForumChildNewEntry entry = list.get(position);
        holder.tv_nick_name.setText(entry.getName());
        holder.user_data.setText("身高：" + entry.getShengao() + "CM\t体重：" + entry.getShengao() + "KG\t罩杯：" + entry.getZhaobei() + "杯");
        holder.tv_desc.setText(entry.getDescription());
        Glide.with(mContext).load(entry.getImage()).placeholder(R.mipmap.icon_small_logo).into(holder.user_head);


        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        entryList = new ArrayList<>();
        entryList.addAll(entry.getVideo());
        adapter = new ForumChildVideoAdapter(mContext, entryList);
        holder.recyclerView.setAdapter(adapter);

        holder.ly_details.setOnClickListener(v -> {
            onItemClickListener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        CircleImageView user_head;
        TextView tv_nick_name, user_data, tv_desc;
        SwipeRecyclerView recyclerView;
        AutoLinearLayout ly_details;

        public ViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            user_head = (CircleImageView) findViewById(R.id.user_head);
            tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
            user_data = (TextView) findViewById(R.id.user_data);
            tv_desc = (TextView) findViewById(R.id.tv_desc);
            recyclerView = (SwipeRecyclerView) findViewById(R.id.recyclerView);
            ly_details= (AutoLinearLayout) findViewById(R.id.ly_details);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
