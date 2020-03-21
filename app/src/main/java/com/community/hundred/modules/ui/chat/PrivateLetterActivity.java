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
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.util.StatusBarUtil;
import com.community.hundred.modules.adapter.PrivateLetterAdapter;
import com.community.hundred.modules.ui.chat.entry.MsgEntry;
import com.hjq.widget.view.ClearEditText;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

// 私信界面
@Route(path = ActivityConstant.PRIVATE_LETTER)
public class PrivateLetterActivity extends MyActivity {
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

    private List<MsgEntry> list = new ArrayList<>();
    private PrivateLetterAdapter adapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
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
        setTitle(nickName);
        setWhiteLeftButtonIcon(getTitleBar());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        list.addAll(getData());
        adapter = new PrivateLetterAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        adapter.notifyItemInserted(getData().size() - 1);
        //定位将显示的数据定位到最后一行，保证可以看到最后一条消息
        recyclerView.scrollToPosition(getData().size() - 1);
    }


    public List<MsgEntry> getData() {
        List<MsgEntry> list = new ArrayList<>();
        list.add(new MsgEntry("Hello", "", 1));
        list.add(new MsgEntry("Hi!", "", 1));
        list.add(new MsgEntry("what's your name?", "", 0));
        list.add(new MsgEntry("can you speck chinese?", "", 0));
        list.add(new MsgEntry("是的，我会", "", 1));
        list.add(new MsgEntry("好的，我们用中文交流吧", "", 0));
        return list;
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        sendData();
    }

    public void sendData() {
        String content = edContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toast("请输入发送的内容");
        } else {
            MsgEntry entry = new MsgEntry(content, "", 1);
            list.add(entry);
            adapter.notifyItemInserted(list.size() - 1);//这两个我还不知道是啥- -,有大神可以评论一下
            recyclerView.scrollToPosition(list.size() - 1);
            edContent.setText("");//清空文本
        }
    }
}
