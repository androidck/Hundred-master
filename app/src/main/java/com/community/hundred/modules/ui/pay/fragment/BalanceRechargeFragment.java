package com.community.hundred.modules.ui.pay.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.modules.adapter.PayTypeAdapter;
import com.community.hundred.modules.ui.BalanceRechargeAdapter;
import com.community.hundred.modules.ui.pay.entry.ItemModel;
import com.community.hundred.modules.ui.pay.entry.PayTypeEntry;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

// 余额充值
public class BalanceRechargeFragment extends MyLazyFragment {
    @BindView(R.id.img_control_logo)
    ImageView imgControlLogo;
    @BindView(R.id.recyclerView_one)
    RecyclerView recyclerViewOne;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.recyclerView_two)
    SwipeRecyclerView recyclerViewTwo;

    private BalanceRechargeAdapter adapter;
    private PayTypeAdapter payTypeAdapter;

    private List<PayTypeEntry> payTypeEntries = new ArrayList<>();

    private View footView;

    // 支付方式
    private int checkPayTypePosition = 0;

    private String money;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_balance_recharge;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        recyclerViewOne.setLayoutManager(new GridLayoutManager(getAttachActivity(), 2));
        recyclerViewOne.setAdapter(adapter = new BalanceRechargeAdapter());
        adapter.replaceAll(getData(), getAttachActivity());

        recyclerViewTwo.setLayoutManager(new LinearLayoutManager(getAttachActivity()));
        footView = getLayoutInflater().inflate(R.layout.view_footer_pay_type, recyclerViewTwo, false);
        recyclerViewTwo.addFooterView(footView);
        for (int i = 0; i < getPayType().size(); i++) {
            if (i == 0) {
                getPayType().get(i).setSelect(true);
            } else
                getPayType().get(i).setSelect(false);
            payTypeEntries.add(getPayType().get(i));
        }
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

    public ArrayList<ItemModel> getData() {
        String data = "50,100,200,300,500";
        String dataArr[] = data.split(",");
        ArrayList<ItemModel> list = new ArrayList<>();
        for (int i = 0; i < dataArr.length; i++) {
            String count = dataArr[i];
            list.add(new ItemModel(ItemModel.TWO, count, false));
        }
        list.add(new ItemModel(ItemModel.THREE, null, false));
        return list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAdapterClickInfo(ItemModel model) {
        money = model.data.toString();
    }

    @Override
    protected void initData() {

    }

    public static BalanceRechargeFragment getInstance() {
        BalanceRechargeFragment fragment = new BalanceRechargeFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public List<PayTypeEntry> getPayType() {
        List<PayTypeEntry> list = new ArrayList<>();
        list.add(new PayTypeEntry("支付宝", R.mipmap.icon_alipay, "1", true));
        list.add(new PayTypeEntry("微信支付", R.mipmap.icon_we_chat, "2", false));
        return list;
    }

    @OnClick(R.id.tv_recharge)
    public void onViewClicked() {
        if (TextUtils.isEmpty(money)) {
            toast("请选择或输入充值金额");
        } else {
            toast("金额：" + money + "\t支付方式：" + getPayType().get(checkPayTypePosition).getName());
        }
    }
}
