package com.community.hundred.modules.ui.post.presenter.view;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

public interface IMySendPostView {

    SwipeRecyclerView getRecycleView();

    SmartRefreshLayout getRefresh();
}
