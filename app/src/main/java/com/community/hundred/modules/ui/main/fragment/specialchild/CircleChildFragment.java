package com.community.hundred.modules.ui.main.fragment.specialchild;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.modules.adapter.CircleAdapter;
import com.community.hundred.modules.adapter.CircleChildHeaderAdapter;
import com.community.hundred.modules.dialog.GiftDialog;
import com.community.hundred.modules.dialog.ReportDialog;
import com.community.hundred.modules.eventbus.SpecialWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildHeaderEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.CircleChildPresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.ICircleChildView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

// 圈子
public class CircleChildFragment extends MyLazyFragment<MainActivity, ICircleChildView, CircleChildPresenter> {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private CircleChildHeaderAdapter headerAdapter;
    private View headView;
    private SwipeRecyclerView headRecyclerView;
    private CircleAdapter adapter;
    private List<CircleChildHeaderEntry> headerEntryList = new ArrayList<>();
    private List<CircleChildEntry> list = new ArrayList<>();
    private List<CircleChildHeaderEntry> newList = new ArrayList<>();
    private String flid;

    @Override
    protected CircleChildPresenter createPresenter() {
        return new CircleChildPresenter(getAttachActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        headView = getLayoutInflater().inflate(R.layout.view_header_circle_child, recyclerView, false);
        recyclerView.addHeaderView(headView);
        adapter = new CircleAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        headRecyclerView = headView.findViewById(R.id.recyclerView);
        LinearLayoutManager hearManager = new LinearLayoutManager(getContext());
        hearManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        headRecyclerView.setLayoutManager(hearManager);
        headerAdapter = new CircleChildHeaderAdapter(getContext(), newList);
        headRecyclerView.setAdapter(headerAdapter);
        headerAdapter.setOnItemClickListener(position -> {
            for (int i = 0; i < newList.size(); i++) {
                if (i == position) {
                    list.clear();
                    flid = newList.get(i).getId();
                    newList.get(i).setSelect(true);
                } else {
                    newList.get(i).setSelect(false);
                }
            }
            headerAdapter.notifyDataSetChanged();
            list.clear();
            mPresenter.getCircleList(flid, page, pageSize);
        });
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    page++;
                    getCircleList();
                    refreshLayout.finishLoadMore();
                }, 200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    list.clear();
                    page = 0;
                    getCircleList();
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
                    }).show();
                });
            } else {
                notLogin();
            }
        });
        // 查看详情
        adapter.setOnClickListener(position -> {
            ARouter.getInstance()
                    .build(ActivityConstant.POST_DETAILS)
                    .withString("id", list.get(position).getId())
                    .navigation();
        });

        // 举报
        adapter.setOnReportClickListener(position -> {
            CircleChildEntry entry = list.get(position);
            if (LoginUtils.getInstance().getUid().equals(entry.getUser_id())) {
                toast("不能举报自己");
            } else {
                new ReportDialog(mActivity, content -> {
                    mPresenter.setReport(list.get(position).getId(), content);
                }).show();
            }
        });


    }


    @Override
    protected void initData() {
        getQzFl();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    // 圈子分类
    private void getQzFl() {
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
        if (TextUtils.isEmpty(flid)) {
            flid = newList.get(0).getId();
        }
        mPresenter.getCircleList(flid, page, pageSize);
        mPresenter.setOnCircleChildListener(childEntries -> {
            list.addAll(childEntries);
            adapter.notifyDataSetChanged();
        });
    }


    public static CircleChildFragment getInstance() {
        CircleChildFragment fragment = new CircleChildFragment();
        return fragment;
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
