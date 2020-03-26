package com.community.hundred.modules.ui.main.fragment.specialchild;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.constant.KeyConstant;
import com.community.hundred.common.network.OkHttp;
import com.community.hundred.modules.adapter.CircleAdapter;
import com.community.hundred.modules.adapter.CircleChildHeaderAdapter;
import com.community.hundred.modules.dialog.GiftDialog;
import com.community.hundred.modules.dialog.ReportDialog;
import com.community.hundred.modules.eventbus.SendGiftWrap;
import com.community.hundred.modules.eventbus.SpecialWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.entry.UserCenterEntry;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildHeaderEntry;
import com.community.hundred.modules.ui.main.fragment.entry.SendGiftEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.SpecialChildPresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.ISpecialChildView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;

// 圈子子页面
public class CircleNewChildFragment extends MyLazyFragment<MainActivity, ISpecialChildView, SpecialChildPresenter> {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private int position;

    private int x = 1;

    private CircleChildHeaderAdapter headerAdapter;// 圈子类型
    private View headView;// 头布局
    private SwipeRecyclerView headRecyclerView;// 圈子类别布局
    private CircleAdapter adapter;// 圈子适配器
    private List<CircleChildHeaderEntry> headerEntryList = new ArrayList<>();// 圈子类型数据
    private List<CircleChildEntry> list = new ArrayList<>();// 圈子列表
    private List<CircleChildHeaderEntry> newList = new ArrayList<>();// 圈子列表新数据
    private String classifyId;// 分类id

    private int firstLoad = 0;

    private String nickName, userHead;

    @Override
    protected SpecialChildPresenter createPresenter() {
        return new SpecialChildPresenter(getAttachActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_child_special;
    }

    @Override
    protected void initView() {
        position = getArguments().getInt(KeyConstant.POSITION_KEY);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        headView = getLayoutInflater().inflate(R.layout.view_header_circle_child, recyclerView, false);
        headRecyclerView = headView.findViewById(R.id.recyclerView);
        // 圈子适配器
        LinearLayoutManager hearManager = new LinearLayoutManager(getContext());
        hearManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        headRecyclerView.setLayoutManager(hearManager);
        if (position == 1) {
            recyclerView.addHeaderView(headView);
        } else {
            recyclerView.removeHeaderView(headView);
        }
        headerAdapter = new CircleChildHeaderAdapter(getContext(), newList);
        headRecyclerView.setAdapter(headerAdapter);
        headerAdapter.setOnItemClickListener(position -> {
            for (int i = 0; i < newList.size(); i++) {
                if (i == position) {
                    list.clear();
                    classifyId = newList.get(i).getId();
                    newList.get(i).setSelect(true);
                } else {
                    newList.get(i).setSelect(false);
                }
            }
            headerAdapter.notifyDataSetChanged();
            list.clear();
            mPresenter.getCircleList(classifyId, page, pageSize);
        });
        // 圈子列表适配器
        adapter = new CircleAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);


        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    switch (position) {
                        case 0:
                            if (LoginUtils.getInstance().isLogin()) {
                                getFollow();
                            } else {
                                showEmpty();
                            }
                            break;
                        case 1:
                            getCircleList();
                            break;
                        case 2:
                            getNewList();
                            break;
                        case 3:
                            getRecommendList();
                            break;
                    }
                    refreshLayout.finishLoadMore();
                }, 200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    switch (position) {
                        case 0:
                            if (LoginUtils.getInstance().isLogin()) {
                                getFollow();
                            } else {
                                showEmpty();
                            }
                            break;
                        case 1:
                            getCircleList();
                            break;
                        case 2:
                            getNewList();
                            break;
                        case 3:
                            getRecommendList();
                            break;
                    }
                    refreshLayout.finishRefresh();
                }, 200);
            }
        });

        // 关注按钮点击事件
        adapter.setOnItemAttentionListener(position -> {
            if (LoginUtils.getInstance().isLogin()) {
                CircleChildEntry entry = list.get(position);
                if (LoginUtils.getInstance().getUid().equals(entry.getUser_id())) {
                    toast("不能关注自己");
                } else {
                    if ("1".equals(entry.getIsgz())) {
                        // 取消关注
                        mPresenter.setEscAttention(entry.getUser_id());
                    } else {
                        mPresenter.setAttention(entry.getUser_id());
                    }
                }
                mPresenter.setOnSuccessListener(state -> {
                    if (state == 1) {
                        entry.setIsgz("1");
                        adapter.notifyDataSetChanged();
                        EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.FOLLOW_REFRESH));
                    } else {
                        entry.setIsgz("0");
                        adapter.notifyDataSetChanged();
                        EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.FOLLOW_REFRESH));
                    }
                });
            } else {
                notLogin();
            }
        });
        // 点赞
        adapter.setOnJustLikeListener(position -> {
            if (LoginUtils.getInstance().isLogin()) {
                CircleChildEntry entry = list.get(position);
                // 将点赞数量转换为int
                mPresenter.setJustLike(entry.getIsdz(), entry.getId());
                mPresenter.setOnSuccessListener(state -> {
                    int love = Integer.parseInt(entry.getLove());
                    if (state == 3) {
                        love++;
                        entry.setIsdz("1");
                        entry.setLove(String.valueOf(love));
                        adapter.notifyDataSetChanged();
                    } else {
                        love--;
                        entry.setIsdz("0");
                        entry.setLove(String.valueOf(love));
                        adapter.notifyDataSetChanged();
                    }
                });
            } else {
                notLogin();
            }
        });
        // 送礼
        adapter.setOnSendGiftListener(position -> {
            if (LoginUtils.getInstance().isLogin()) {
                mPresenter.getGiftList();
                mPresenter.setOnGiftListener((yue, list1) -> {
                    new GiftDialog(mActivity, yue, list1, (name, giftId, img) -> {
                        // 调用打赏的接口
                        mPresenter.senGift(giftId, list.get(position).getId());
                        mPresenter.setOnSuccessListener(state -> {
                            if (state == 8) {
                                SendGiftEntry entry = new SendGiftEntry();
                                entry.name = nickName;// 名称
                                entry.giftName = "送你：" + name;//礼物名称
                                entry.img = userHead;// 头像
                                entry.giftImg = img;//礼物图片
                                entry.num = 0;// 数量
                                EventBus.getDefault().post(SendGiftWrap.getInstance(entry));
                            }
                        });
                    }).show();
                });
            } else {
                notLogin();
            }
        });
        // 查看详情
        adapter.setOnClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.POST_DETAILS)
                    .withString("id", list.get(position).getId())
                    .navigation();
        });
        // 举报
        adapter.setOnReportClickListener(position -> {
            if (LoginUtils.getInstance().isLogin()) {
                CircleChildEntry entry = list.get(position);
                if (LoginUtils.getInstance().getUid().equals(entry.getUser_id())) {
                    toast("不能举报自己");
                } else {
                    new ReportDialog(mActivity, content -> {
                        mPresenter.setReport(list.get(position).getId(), content);
                    }).show();
                }
            } else {
                notLogin();
            }
        });

        // 评论
        adapter.setOnCommentClickListener(position -> {
            if (LoginUtils.getInstance().isLogin()) {
                ARouter.getInstance().build(ActivityConstant.PRIVATE_LETTER)
                        .withString("nickName", list.get(position).getNickname())
                        .withString("bid", list.get(position).getUser_id())
                        .navigation();
            } else {
                notLogin();
            }
        });

    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        switch (position) {
            case 0:
                if (LoginUtils.getInstance().isLogin()) {
                    getFollow();
                } else {
                    showEmpty();
                }
                break;
            case 1:
                getQzFl();
                break;
            case 2:
                getNewList();
                break;
            case 3:
                getRecommendList();
                break;
        }
        getUserCenter(LoginUtils.getInstance().getUid());
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
                    nickName = entry.getNickname();
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

    public static CircleNewChildFragment getInstance(int position) {
        CircleNewChildFragment fragment = new CircleNewChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KeyConstant.POSITION_KEY, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    // 获取关注列表
    public void getFollow() {
        showLoading();
        mPresenter.getAttentionList(HttpConstant.guanzhulistURL, x, pageSize);
        mPresenter.setOnSpecialChildListener(list1 -> {
            if (list1.size() != 0) {
                list.addAll(list1);
                adapter.notifyDataSetChanged();
                showComplete();
            } else {
                showEmpty();
            }
        });
    }

    // 获取圈子分类
    public void getQzFl() {
        mPresenter.getQzFl();
        mPresenter.setOnClassifyListener(list -> {
            headerEntryList.addAll(list);
            for (int i = 0; i < headerEntryList.size(); i++) {
                if (i == 0) {
                    headerEntryList.get(i).setSelect(true);
                } else
                    headerEntryList.get(i).setSelect(false);
                newList.add(headerEntryList.get(i));
            }
            headerAdapter.notifyDataSetChanged();
            getCircleList();
        });
    }

    // 圈子列表
    public void getCircleList() {
        if (TextUtils.isEmpty(classifyId)) {
            classifyId = newList.get(0).getId();
        }
        mPresenter.getCircleList(classifyId, page, pageSize);
        mPresenter.setOnCircleChildListener(childEntries -> {
            list.addAll(childEntries);
            adapter.notifyDataSetChanged();
        });
    }

    // 获取最新圈子
    public void getNewList() {
        showLoading();
        mPresenter.getAttentionList(HttpConstant.zxqzURL, x, pageSize);
        mPresenter.setOnSpecialChildListener(list1 -> {
            if (list1.size() != 0) {
                list.addAll(list1);
                adapter.notifyDataSetChanged();
                showComplete();
            } else {
                showEmpty();
            }
        });
    }

    // 获取推荐
    public void getRecommendList() {
        showLoading();
        mPresenter.getAttentionList(HttpConstant.tjtzURL, x, pageSize);
        mPresenter.setOnSpecialChildListener(list1 -> {
            if (list1.size() != 0) {
                list.addAll(list1);
                adapter.notifyDataSetChanged();
                showComplete();
            } else {
                showEmpty();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpecialWrap(SpecialWrap wrap) {
        int code = wrap.code;
        if (code == EventBusConstant.FOLLOW_REFRESH) {
            // 刷新关注列表
            refresh.autoRefresh();
        }
    }
}
