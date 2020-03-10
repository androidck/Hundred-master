package com.community.hundred.modules.ui;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.modules.ui.pay.entry.ItemModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

@Route(path = "/ui/TestActivity")
public class TestActivity extends MyActivity {


    @BindView(R.id.recylerview)
    RecyclerView recylerview;

    private BalanceRechargeAdapter adapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_activity;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        recylerview.setLayoutManager(new GridLayoutManager(this, 2));
        recylerview.setAdapter(adapter = new BalanceRechargeAdapter());
        adapter.replaceAll(getData(), this);
    }

    @Override
    protected void initData() {

    }

    public ArrayList<ItemModel> getData() {
        String data = "50,100,200,300,500";
        // isDiscount ：1、有角标 2、无角标
        String isDiscount = "2";
        String dataArr[] = data.split(",");
        ArrayList<ItemModel> list = new ArrayList<>();
        for (int i = 0; i < dataArr.length; i++) {
            String count = dataArr[i] + "元";
            list.add(new ItemModel(ItemModel.TWO, count, false));
        }
        list.add(new ItemModel(ItemModel.THREE, null, false));
        return list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAdapterClickInfo(ItemModel model) {
        String money = model.data.toString().replace("元", "");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
