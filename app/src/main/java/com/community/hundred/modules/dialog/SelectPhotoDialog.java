package com.community.hundred.modules.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.dialog.BaseCustomDialog;

// 头像选择dialog
public class SelectPhotoDialog extends BaseCustomDialog implements View.OnClickListener {

    private Context mContext;
    private TextView tv_photo, tv_album, tv_esc;
    private OnItemClickListener onItemClickListener;

    public SelectPhotoDialog(@NonNull Context context, OnItemClickListener onItemClickListener) {
        super(context);
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_photo_select;
    }

    @Override
    public void initView() {
        tv_photo = findViewById(R.id.tv_photo);
        tv_album = findViewById(R.id.tv_album);
        tv_esc = findViewById(R.id.tv_esc);

        tv_photo.setOnClickListener(this);
        tv_album.setOnClickListener(this);
        tv_esc.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public int showGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_photo:
                onItemClickListener.onClick(1);
                dismiss();
                break;
            case R.id.tv_album:
                onItemClickListener.onClick(2);
                dismiss();
                break;
            case R.id.tv_esc:
                dismiss();
                break;
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
