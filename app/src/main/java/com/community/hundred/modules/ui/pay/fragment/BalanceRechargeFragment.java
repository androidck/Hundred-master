package com.community.hundred.modules.ui.pay.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.modules.ui.BalanceRechargeAdapter;
import com.community.hundred.modules.ui.pay.entry.ItemModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

// 余额充值
public class BalanceRechargeFragment extends MyLazyFragment {
    @BindView(R.id.img_control_logo)
    ImageView imgControlLogo;
    @BindView(R.id.recyclerView_one)
    RecyclerView recyclerViewOne;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;

    private BalanceRechargeAdapter adapter;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recharge;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        recyclerViewOne.setLayoutManager(new GridLayoutManager(getAttachActivity(), 2));
        recyclerViewOne.setAdapter(adapter = new BalanceRechargeAdapter());
        adapter.replaceAll(getData(), getAttachActivity());

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
        String money = model.data.toString();

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
}
