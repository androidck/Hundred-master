package com.community.hundred.modules.dialog;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.community.hundred.R;
import com.community.hundred.common.dialog.BaseCustomDialog;
import com.community.hundred.modules.ui.main.entry.NoticeEntry;


// 首页弹窗
public class HomeDialog extends BaseCustomDialog {


    private TextView tv_know, tv_tip;
    private NoticeEntry noticeEntry;
    private Context context;

    public HomeDialog(@NonNull Context context, NoticeEntry noticeEntry) {
        super(context);
        this.context = context;
        this.noticeEntry = noticeEntry;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_home;
    }

    @Override
    public void initView() {
        tv_know = findViewById(R.id.tv_know);
        tv_tip = findViewById(R.id.tv_tip);
        tv_know.setOnClickListener(v -> {
            dismiss();
        });
        tv_tip.setText(noticeEntry.getZxdd() + "\t" + noticeEntry.getBydz());
        tv_tip.setText(noticeEntry.getContent());
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
        return Gravity.CENTER;
    }
}
