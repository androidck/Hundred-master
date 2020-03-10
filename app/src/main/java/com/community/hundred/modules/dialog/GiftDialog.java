package com.community.hundred.modules.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.community.hundred.R;
import com.community.hundred.common.dialog.BaseCustomDialog;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.adapter.GiftGridViewAdapter;
import com.community.hundred.modules.adapter.GiftViewPagerAdapter;
import com.community.hundred.modules.dialog.entry.GiftEntry;
import com.community.hundred.modules.manager.LoginUtils;
import com.hjq.toast.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

// 礼物dialog
public class GiftDialog extends BaseCustomDialog implements View.OnClickListener {
    private Context mContext;

    private ViewPager viewPager;
    private TextView tv_title, tv_wallet, tv_unit_price, tv_send;

    private List<View> mPagerList;
    private List<GiftEntry> mDataList;
    private LinearLayout idotLayout;//知识圆点
    private LayoutInflater mInflater;
    private ImageView btn_close;

    private String name;

    private int currPage;
    /*总的页数*/
    private int pageCount;
    /*每一页显示的个数*/
    private int pageSize = 8;
    /*当前显示的是第几页*/
    private int curIndex = 0;

    private double yue;

    private String giftId;
    private String image;


    private OnItemSendGiftListener onItemSendGiftListener;

    private GiftGridViewAdapter[] arr = new GiftGridViewAdapter[2];

    public GiftDialog(@NonNull Context context, double yue, List<GiftEntry> mDataList, OnItemSendGiftListener onItemSendGiftListener) {
        super(context);
        this.mContext = context;
        this.yue = yue;
        this.mDataList = mDataList;
        this.onItemSendGiftListener = onItemSendGiftListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_gift;
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.view_pager);
        tv_title = findViewById(R.id.tv_title);
        tv_wallet = findViewById(R.id.tv_wallet);
        tv_unit_price = findViewById(R.id.tv_unit_price);
        tv_send = findViewById(R.id.tv_send);
        idotLayout = findViewById(R.id.ll_dot);
        mInflater = LayoutInflater.from(mContext);
        btn_close = findViewById(R.id.btn_close);

        tv_wallet.setText("钱包：" + yue);

        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDataList.size() * 1.0 / pageSize);
        //viewpager
        mPagerList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            // 循环添加view
            GridView gridView = (GridView) mInflater.inflate(R.layout.view_gridview_gift, viewPager, false);
            GiftGridViewAdapter gridAdapter = new GiftGridViewAdapter(mContext, mDataList, i);
            gridView.setAdapter(gridAdapter);
            arr[i] = gridAdapter;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 点击位置
                    for (int i = 0; i < mDataList.size(); i++) {
                        GiftEntry model = mDataList.get(i);
                        if (i == id) {
                            if (model.isSelected()) {
                                model.setSelected(false);
                            } else {
                                model.setSelected(true);
                            }
                            tv_unit_price.setText("￥" + model.getPrice());
                            giftId = model.getParent_id();
                            name = model.getName();
                            image = model.getImage();
                        } else {
                            model.setSelected(false);
                        }
                    }
                    gridAdapter.notifyDataSetChanged();
                }
            });
            mPagerList.add(gridView);
        }
        // 添加viewpager 中
        viewPager.setAdapter(new GiftViewPagerAdapter(mPagerList, mContext));
        setOvalLayout();

        btn_close.setOnClickListener(this);
        tv_send.setOnClickListener(this);
    }

    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            idotLayout.addView(mInflater.inflate(R.layout.dot, null));
        }

        // 默认显示第一页
        idotLayout.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                arr[0].notifyDataSetChanged();
                arr[1].notifyDataSetChanged();

                // 取消圆点选中
                idotLayout.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                idotLayout.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
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
        return Gravity.BOTTOM;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.tv_send:
                if (TextUtils.isEmpty(giftId)) {
                    ToastUtils.show("请选择要赠送的礼物");
                } else if (yue == 0 || yue == 0.0 || yue == 0.00) {
                    new ShortageDialog(mContext).show();
                } else {
                    onItemSendGiftListener.onSendGift(name, giftId, image);
                }
                break;
        }
    }

    public interface OnItemSendGiftListener {
        void onSendGift(String name, String giftId, String img);
    }
}
