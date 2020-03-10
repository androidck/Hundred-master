package com.samluys.filtertab.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.R;
import com.samluys.filtertab.adapter.SeniorSingleAdapter;
import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.base.BasePopupWindow;
import com.samluys.filtertab.listener.OnFilterToViewListener;
import com.samluys.filtertab.util.GetJsonDataUtil;
import com.samluys.filtertab.util.Utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * 竖直单选样式
 */
public class SeniorSelectPopupWindow extends BasePopupWindow {

    private RecyclerView rv_content;
    private SeniorSingleAdapter mAdapter;

    private TextView tv_reset, tv_confirm;

    public SeniorSelectPopupWindow(Context context, List<BaseFilterBean> data, int filterType, int position, OnFilterToViewListener listener) {
        super(context, data, filterType, position, listener);
    }





    @Override
    public View initView() {
        // 初始化对象
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.popup_basice_select, null, false);
        rv_content = rootView.findViewById(R.id.rv_content);
        tv_reset = rootView.findViewById(R.id.tv_reset);
        tv_confirm = rootView.findViewById(R.id.tv_confirm);
        mAdapter = new SeniorSingleAdapter(getContext(), getData());
        final int maxHeight = Utils.dp2px(getContext(), 273);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
                super.setMeasuredDimension(childrenBounds, wSpec, View.MeasureSpec.makeMeasureSpec(maxHeight, AT_MOST));
            }
        };
        rv_content.setLayoutManager(linearLayoutManager);
        rv_content.setAdapter(mAdapter);

        View v_outside = rootView.findViewById(R.id.v_outside);
        v_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });

        // 重置
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < getData().size(); i++) {
                    getData().get(i).setCondition("不限");
                }
                mAdapter.notifyDataSetChanged();
            }
        });


        // 确定
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;

    }

    @Override
    public void initSelectData() {

    }

    @Override
    public void refreshData() {

    }


}
