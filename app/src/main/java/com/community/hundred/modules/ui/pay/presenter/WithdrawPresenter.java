package com.community.hundred.modules.ui.pay.presenter;

import android.util.Log;

import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.pay.entry.MoneyEntry;
import com.community.hundred.modules.ui.pay.presenter.view.IWithdrawView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

// 提现业务逻辑
public class WithdrawPresenter extends BasePresenter<IWithdrawView> {

    private OnMoneyDataListener onMoneyDataListener;

    public WithdrawPresenter(MyActivity context) {
        super(context);
    }

    // 获取用户余额
    public void getUserMoney() {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        OkHttp.postAsync(HttpConstant.useryueURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                prContext.showComplete();
                //{"sta":"1","data":{"money":"0.00","zong":0,"ktxian":0}}
                Log.d("moneyResult", result);
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                JsonObject object = jsonObject.getAsJsonObject("data");
                MoneyEntry entry = new Gson().fromJson(object.toString(), MoneyEntry.class);
                onMoneyDataListener.onMoney(entry);
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    // 提现
    public void withDraw(String money, String account, String name, String aqm) {
        prContext.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginUtils.getInstance().getUid());
        map.put("je", money);
        map.put("zhanghao", account);
        map.put("name", name);
        map.put("aqm", aqm);
        OkHttp.postAsync(HttpConstant.sqtxURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Log.d("withdrawResult", result);
                prContext.showComplete();
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                prContext.showComplete();
                e.printStackTrace();
                netWorkError();
            }
        });
    }

    public interface OnMoneyDataListener {
        void onMoney(MoneyEntry entry);
    }

    public void setOnMoneyDataListener(OnMoneyDataListener onMoneyDataListener) {
        this.onMoneyDataListener = onMoneyDataListener;
    }
}
