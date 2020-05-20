package com.community.hundred.modules.ui.pay.fragment;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.modules.adapter.MemberRechargeAdapter;
import com.community.hundred.modules.adapter.PayTypeAdapter;
import com.community.hundred.modules.ui.pay.entry.MemberEntry;
import com.community.hundred.modules.ui.pay.entry.PayTypeEntry;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

// 会员充值
public class MemberRechargeFragment extends MyLazyFragment {
    @BindView(R.id.img_control_logo)
    ImageView imgControlLogo;
    @BindView(R.id.recyclerView_one)
    RecyclerView recyclerViewOne;
    @BindView(R.id.recyclerView_two)
    SwipeRecyclerView recyclerViewTwo;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;


    private String money;

    private int moneySelectPosition = 0;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private List<MemberEntry> list = new ArrayList<>();

    private MemberRechargeAdapter adapter;

    private PayTypeAdapter payTypeAdapter;

    private List<PayTypeEntry> payTypeEntries = new ArrayList<>();

    // 支付方式
    private int checkPayTypePosition = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member_recharge;
    }

    @Override
    protected void initView() {
        recyclerViewOne.setLayoutManager(new GridLayoutManager(mActivity, 2));
        adapter = new MemberRechargeAdapter(mActivity, list);
        recyclerViewOne.setAdapter(adapter);
        adapter.setOnItemOnClickListener(position -> {
            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
                    moneySelectPosition = position;
                    list.get(i).setSelect(true);
                } else {
                    list.get(i).setSelect(false);
                }
            }
            adapter.notifyDataSetChanged();
        });
        for (int i = 0; i < getPayType().size(); i++) {
            if (i == 0) {
                getPayType().get(i).setSelect(true);
            } else
                getPayType().get(i).setSelect(false);
            payTypeEntries.add(getPayType().get(i));
        }
        recyclerViewTwo.setLayoutManager(new LinearLayoutManager(getAttachActivity()));
        payTypeAdapter = new PayTypeAdapter(getAttachActivity(), payTypeEntries);
        recyclerViewTwo.setAdapter(payTypeAdapter);
        payTypeAdapter.setOnItemClickListener(position -> {
            for (int i = 0; i < payTypeEntries.size(); i++) {
                if (i == position) {
                    checkPayTypePosition = position;
                    payTypeEntries.get(i).setSelect(true);
                } else {
                    payTypeEntries.get(i).setSelect(false);
                }
            }
            payTypeAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void initData() {
        getData();
    }

    public static MemberRechargeFragment getInstance() {
        MemberRechargeFragment fragment = new MemberRechargeFragment();
        return fragment;
    }

    public void getData() {
        list.add(new MemberEntry("1", "30", "50", true));
        list.add(new MemberEntry("2", "90", "130", false));
        list.add(new MemberEntry("3", "180", "200", false));
        list.add(new MemberEntry("4", "365", "360", false));
        adapter.notifyDataSetChanged();
    }

    public List<PayTypeEntry> getPayType() {
        List<PayTypeEntry> list = new ArrayList<>();
        list.add(new PayTypeEntry("支付宝", R.mipmap.icon_alipay, "1", true));
        list.add(new PayTypeEntry("微信支付", R.mipmap.icon_we_chat, "2", false));
        return list;
    }

    @OnClick(R.id.tv_recharge)
    public void onViewClicked() {
        toast("金额：" + list.get(moneySelectPosition).getMoney() + "\t支付方式：" + getPayType().get(checkPayTypePosition).getName());
    }
}
