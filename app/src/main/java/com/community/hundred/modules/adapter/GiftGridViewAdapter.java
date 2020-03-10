package com.community.hundred.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.modules.dialog.entry.GiftEntry;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

// 礼物adapter
public class GiftGridViewAdapter extends BaseAdapter {

    private List<GiftEntry> mData;
    private LayoutInflater inflater;
    private Context mContext;

    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 8;

    public GiftGridViewAdapter(Context context, List<GiftEntry> mDatas, int curIndex) {
        inflater = LayoutInflater.from(context);
        this.mData = mDatas;
        this.curIndex = curIndex;
        this.mContext = context;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页？mDatas.size() > (curIndex+1)*pageSize,
     * 如果够，则直接返回每一页显示的最大条目个数pageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - curIndex * pageSize);(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mData.size() > (curIndex + 1) * pageSize ? pageSize : (mData.size() - curIndex * pageSize);
    }

    @Override
    public GiftEntry getItem(int position) {
        return mData.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_dialog_gift, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.item_gift_ly = convertView.findViewById(R.id.item_gift_ly);
            viewHolder.item_icon = convertView.findViewById(R.id.item_icon);
            viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
            viewHolder.tv_money = convertView.findViewById(R.id.tv_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 绑定数据
        GiftEntry entry = getItem(position);
        viewHolder.tv_title.setText(entry.getName());
        viewHolder.tv_money.setText("￥" + entry.getPrice());
        Glide.with(mContext).load(entry.getImage()).into(viewHolder.item_icon);
        if (entry.isSelected()) {
            viewHolder.item_gift_ly.setBackgroundDrawable(mContext.getDrawable(R.drawable.shape_gift_select));
        } else {
            viewHolder.item_gift_ly.setBackgroundDrawable(mContext.getDrawable(R.drawable.shape_gift_unselect));
        }
        return convertView;
    }

    class ViewHolder {
        AutoLinearLayout item_gift_ly;
        ImageView item_icon;
        TextView tv_title, tv_money;
    }
}
