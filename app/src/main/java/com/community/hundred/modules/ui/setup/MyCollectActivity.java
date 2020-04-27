package com.community.hundred.modules.ui.setup;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.dialog.MessageDialog;
import com.community.hundred.modules.adapter.HistoryAdapter;
import com.community.hundred.modules.ui.setup.entry.HistoryEntry;
import com.community.hundred.modules.ui.setup.presenter.MyCollectPresenter;
import com.community.hundred.modules.ui.setup.presenter.view.IMyCollectView;
import com.hjq.base.BaseDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.touch.OnItemStateChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 我的收藏
@Route(path = ActivityConstant.MY_COLLECT)
public class MyCollectActivity extends MyActivity<IMyCollectView, MyCollectPresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private List<HistoryEntry> lists = new ArrayList<>();
    private HistoryAdapter adapter;

    @Override
    protected MyCollectPresenter createPresenter() {
        return new MyCollectPresenter(this);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        if (lists.size() == 0) {
            toast("暂无数据");
        } else {
            new MessageDialog.Builder(this)
                    .setTitle("情况数据")
                    .setMessage("数据一旦清空不可恢复")
                    .setConfirm(getString(R.string.common_confirm))
                    .setCancel(getString(R.string.common_cancel))
                    .setListener(new MessageDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            mPresenter.clearCollect();
                            mPresenter.setOnSuccessListener(state -> {
                                lists.clear();
                                getMyCollectList();
                            });
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        // 点击事件
        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.VIDEO_DETAILS)
                    .withString("videoId", lists.get(position).getId())
                    .navigation();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        getTitleBar().setRightIcon(R.mipmap.icon_del);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this, lists);
        recyclerView.setOnItemMenuClickListener(mItemMenuClickListener); // Item的Menu点击。
        recyclerView.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。

        recyclerView.setAdapter(adapter);
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(refreshLayout -> {
            refresh.getLayout().postDelayed(() -> {
                lists.clear();
                getMyCollectList();
                refreshLayout.finishRefresh();
            }, 200);
        });

        recyclerView.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
        recyclerView.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。
        recyclerView.setOnItemMoveListener(getItemMoveListener());// 监听拖拽和侧滑删除，更新UI和数据源。
        recyclerView.setOnItemStateChangedListener(mOnItemStateChangedListener); // 监听Item的手指状态，拖拽、侧滑、松开。

        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.VIDEO_DETAILS)
                    .withString("videoId", lists.get(position).getId())
                    .navigation();
        });
    }

    protected OnItemMoveListener getItemMoveListener() {
        // 监听拖拽和侧滑删除，更新UI和数据源。
        return new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                // 不同的ViewType不能拖拽换位置。
                if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) return false;
                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                int fromPosition = srcHolder.getAdapterPosition() - recyclerView.getHeaderCount();
                int toPosition = targetHolder.getAdapterPosition() - recyclerView.getHeaderCount();
                Collections.swap(lists, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                int adapterPosition = srcHolder.getAdapterPosition();
                int position = adapterPosition - recyclerView.getHeaderCount();

                lists.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };
    }

    /**
     * Item的拖拽/侧滑删除时，手指状态发生变化监听。
     */
    private OnItemStateChangedListener mOnItemStateChangedListener = (viewHolder, actionState) -> {
        if (actionState == OnItemStateChangedListener.ACTION_STATE_DRAG) {
            // 拖拽的时候背景就透明了，这里我们可以添加一个特殊背景。
            viewHolder.itemView.setBackgroundColor(
                    ContextCompat.getColor(MyCollectActivity.this, R.color.white));
        } else if (actionState == OnItemStateChangedListener.ACTION_STATE_IDLE) {
            // 在手松开的时候还原背景。
            ViewCompat.setBackground(viewHolder.itemView,
                    ContextCompat.getDrawable(MyCollectActivity.this, R.color.white));
        }
    };

    /**
     * 菜单创建器。
     */
    private SwipeMenuCreator mSwipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.space_180);
        // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
        // 2. 指定具体的高，比如80;
        // 3. WRAP_CONTENT，自身高度，不推荐;
        int height = getResources().getDimensionPixelSize(R.dimen.space_200);

        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(MyCollectActivity.this).setBackground(
                    R.drawable.selector_red)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mItemMenuClickListener = (menuBridge, position) -> {
        menuBridge.closeMenu();
        int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
        int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
        if (menuPosition == 0) {
            mPresenter.escCollect(lists.get(position).getId());
            adapter.notifyItemRemoved(position);
            lists.remove(position);
            adapter.notifyDataSetChanged();
            if (lists.size() == 0) {
                refresh.autoRefresh();
            }
        }
    };

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initData() {
        getMyCollectList();
    }

    public void getMyCollectList() {
        mPresenter.getMyCollectList();
        mPresenter.setOnListDataListener(list -> {
            lists.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }


}
