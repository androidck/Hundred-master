package com.community.hundred.modules.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.common.util.StatusBarUtil;
import com.community.hundred.modules.adapter.PrivateLetterAdapter;
import com.community.hundred.modules.eventbus.SendMsgWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.chat.entry.MsgEntry;
import com.community.hundred.modules.ui.chat.presenter.PrivateLetterPresenter;
import com.community.hundred.modules.ui.chat.presenter.view.IPrivateLetterView;
import com.community.hundred.modules.ui.main.entry.UserCenterEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hjq.widget.view.ClearEditText;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

// 私信界面
@Route(path = ActivityConstant.PRIVATE_LETTER)
public class PrivateLetterActivity extends MyActivity<IPrivateLetterView, PrivateLetterPresenter> {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ed_content)
    ClearEditText edContent;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ly_comment)
    AutoLinearLayout lyComment;

    @Autowired
    String nickName;

    @Autowired
    String bid;

    private int p = 1;

    private List<MsgEntry> list = new ArrayList<>();
    private PrivateLetterAdapter adapter;

    // 用户头像
    private String userHead;

    private String content;

    @Override
    protected PrivateLetterPresenter createPresenter() {
        return new PrivateLetterPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMode(this, false, R.color.colorAccent);
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_private_letter;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        setTitle(nickName);
        setWhiteLeftButtonIcon(getTitleBar());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new PrivateLetterAdapter(this, list);
        recyclerView.setAdapter(adapter);
        getUserCenter(LoginUtils.getInstance().getUid());

    }

    @Override
    protected void initData() {
        mPresenter.getChatDetails(bid, p);
        mPresenter.setOnDataListener(list1 -> {
            list.addAll(list1);
            adapter.notifyDataSetChanged();
        });
    }


    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        sendData();
    }

    public void sendData() {
        content = edContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toast("请输入发送的内容");
        } else {
            mPresenter.sendMgs(content, bid);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendMsgWrap(SendMsgWrap wrap) {
        int code = wrap.code;
        if (code == 200) {
            MsgEntry msgEntry = new MsgEntry();
            msgEntry.setContent(content);
            msgEntry.setImage(userHead);
            msgEntry.setUid(LoginUtils.getInstance().getUid());
            list.add(msgEntry);
            adapter.notifyItemInserted(list.size() - 1);
            recyclerView.scrollToPosition(list.size() - 1);
            edContent.setText("");
        }
    }

    public void getUserCenter(String uid) {
        Map<String, String> map = new HashMap();
        map.put("uid", uid);
        OkHttp.postAsync(HttpConstant.userURL, map, new OkHttp.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                BaseResponse response = new Gson().fromJson(result, BaseResponse.class);
                if ("1".equals(response.getSta())) {
                    JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                    JsonObject jsonObject = object.getAsJsonObject().getAsJsonObject("data");
                    UserCenterEntry entry = new Gson().fromJson(jsonObject.toString(), UserCenterEntry.class);
                    userHead = HttpConstant.BASE_HOST + entry.getImage();
                } else {
                    toast(response.getMsg());
                }


            }

            @Override
            public void requestFailure(Request request, IOException e) {
                toast("网络请求失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
