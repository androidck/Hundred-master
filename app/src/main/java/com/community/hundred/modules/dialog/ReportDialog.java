package com.community.hundred.modules.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.community.hundred.R;
import com.community.hundred.common.dialog.BaseCustomDialog;
import com.community.hundred.modules.adapter.ReportAdapter;
import com.community.hundred.modules.entry.ReportEntry;

import java.util.ArrayList;
import java.util.List;


// 举报dialog
public class ReportDialog extends BaseCustomDialog {

    private ReportAdapter adapter;

    private TextView tv_esc, tv_confirm;
    private RecyclerView recyclerView;
    private Context mContext;

    private OnReportListener onReportListener;

    private String contentStr;

    private List<ReportEntry> list = new ArrayList<>();

    public ReportDialog(@NonNull Context context, OnReportListener onReportListener) {
        super(context);
        this.mContext = context;
        this.onReportListener = onReportListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_report;
    }

    @Override
    public void initView() {
        tv_esc = findViewById(R.id.tv_esc);
        tv_confirm = findViewById(R.id.tv_confirm);
        recyclerView = findViewById(R.id.recyclerView);
        tv_esc.setOnClickListener(v -> {
            dismiss();
        });

        tv_confirm.setOnClickListener(v -> {

            if (TextUtils.isEmpty(contentStr)) {
                toast("请选择举报的类型");
            } else {
                onReportListener.onReport(contentStr);
                dismiss();
            }

        });

        for (int i = 0; i < getData().size(); i++) {
            if (i == 0) {
                getData().get(i).setSelect(true);
            } else
                getData().get(i).setSelect(false);
            list.add(getData().get(i));
        }

        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(manager);
        adapter = new ReportAdapter(mContext, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
                    list.get(i).setSelect(true);
                    contentStr = list.get(i).getContent();
                } else {
                    list.get(i).setSelect(false);
                }
            }
            adapter.notifyDataSetChanged();
        });
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

    // dialog 数据
    public List<ReportEntry> getData() {
        List<ReportEntry> list = new ArrayList<>();
        list.add(new ReportEntry("盗用原创", false));
        list.add(new ReportEntry("垃圾广告", false));
        list.add(new ReportEntry("虚假欺骗", false));
        list.add(new ReportEntry("辱骂他人", false));
        list.add(new ReportEntry("威逼利诱", false));
        list.add(new ReportEntry("其他行为", false));
        return list;
    }

    public interface OnReportListener {
        void onReport(String content);
    }
}
