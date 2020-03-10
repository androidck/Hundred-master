package com.community.hundred.common.adapter;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.MyApplication;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.util.SizeUtil;
import com.community.hundred.modules.ui.user.entry.UserAlbumEntry;

import java.io.File;
import java.util.List;

// 九宫格图片
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>{
    private List<UserAlbumEntry> mData;
    private final int mCountLimit = 9;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onTakePhotoClick();
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public ImageAdapter(List<UserAlbumEntry> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtil.dip2px(parent.getContext(), 95), SizeUtil.dip2px(parent.getContext(), 95));
        params.setMargins(10, 10, 10, 10);
        params.gravity = Gravity.CENTER;
        imageView.setLayoutParams(params);
        return new MyViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (position == getItemCount() - 1 && mData.size() < mCountLimit) {
            holder.imageView.setImageResource(R.mipmap.icon_add_photo);
            holder.imageView.setOnClickListener(v -> mOnItemClickListener.onTakePhotoClick());
        } else {
            // 本地图片
            Glide.with(MyApplication.getInstances().getApplicationContext()).load(HttpConstant.BASE_HOST +mData.get(position).getImgdz()).into(holder.imageView);
            // 网络图片
            holder.imageView.setOnClickListener(v -> {
                // 点击操作，后续可添加点击后的响应
            });
            // 长按监听
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // 满 9张图就不让其添加新图
        if (mData != null && mData.size() >= mCountLimit) {
            return mCountLimit;
        } else {
            return mData == null ? 1 : mData.size() + 1;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        private MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }

}
