package com.community.hundred.modules.ui.main.fragment.specialchild;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.modules.adapter.CircleAdapter;
import com.community.hundred.modules.dialog.GiftDialog;
import com.community.hundred.modules.dialog.ReportDialog;
import com.community.hundred.modules.eventbus.SpecialWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.SpecialChildPresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.ISpecialChildView;
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

public class SpecialChildFragment extends MyLazyFragment<MainActivity, ISpecialChildView, SpecialChildPresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private int x = 1;


    private CircleAdapter adapter;
    private String url;

    private List<CircleChildEntry> list = new ArrayList<>();

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
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new CircleAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    x++;
                    getData();
                    refreshLayout.finishLoadMore();
                }, 200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    list.clear();
                    x = 1;
                    getData();
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
            ARouter.getInstance().build(ActivityConstant.POST_DETAILS)
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

        getData();
    }

    @Override
    protected void initLazyLoad() {
        super.initLazyLoad();
        if (list.size() != 0) {
            list.clear();
        }

    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static SpecialChildFragment getInstance(String url) {
        SpecialChildFragment fragment = new SpecialChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void getData() {
        url = getArguments().getString("url");
        mPresenter.getAttentionList(url, x, pageSize);
        mPresenter.setOnSpecialChildListener(list1 -> {
            list.addAll(list1);
            adapter.notifyDataSetChanged();
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
        if (code == EventBusConstant.FOLLOW_REFRESH || code == EventBusConstant.SEND_POST_CIRCLE) {

        }
    }
}
