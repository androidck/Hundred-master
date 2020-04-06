package com.community.hundred.modules.ui.livebroadcast;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.flowlayout.FlowLayout;
import com.community.hundred.common.flowlayout.TagAdapter;
import com.community.hundred.common.flowlayout.TagFlowLayout;
import com.community.hundred.modules.adapter.SearchAdapter;
import com.community.hundred.modules.ui.livebroadcast.entry.HotEntry;
import com.community.hundred.modules.ui.livebroadcast.entry.SearchEntry;
import com.community.hundred.modules.ui.livebroadcast.presenter.SearchPresenter;
import com.community.hundred.modules.ui.livebroadcast.presenter.view.ISearchView;
import com.hjq.widget.view.ClearEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 搜索
@Route(path = ActivityConstant.SEARCH)
public class SearchActivity extends MyActivity<ISearchView, SearchPresenter> {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.tv_esc)
    TextView tvEsc;
    @BindView(R.id.ed_search_count)
    ClearEditText edSearchCount;
    @BindView(R.id.ly_search_tip)
    AutoRelativeLayout lySearchTip;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    private SearchAdapter adapter;
    private View headerView;

    private TagFlowLayout fy_history, fy_hot;
    private TagAdapter<HotEntry> fyHistoryAdapter;
    private TagAdapter<String> fyHotAdapter;

    private List<SearchEntry> list = new ArrayList<>();

    @Autowired
    String name;
    private int p = 1;


    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }


    @Override
    protected void initView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        headerView = getLayoutInflater().inflate(R.layout.view_header_search, recyclerView, false);
        recyclerView.addHeaderView(headerView);
        adapter = new SearchAdapter(this, list);
        recyclerView.setAdapter(adapter);
        fy_history = headerView.findViewById(R.id.fy_history);
        fy_hot = headerView.findViewById(R.id.fy_hot);


        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    if (TextUtils.isEmpty(edSearchCount.getText().toString().trim())) {
                        toast("请输入搜索的内容");
                    } else {
                        p++;
                        getVideo(edSearchCount.getText().toString().trim());
                    }
                    refreshLayout.finishLoadMore();
                }, 200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    if (TextUtils.isEmpty(edSearchCount.getText().toString().trim())) {
                        toast("请输入搜索的内容");
                    } else {
                        list.clear();
                        p = 1;
                        getVideo(edSearchCount.getText().toString().trim());
                    }
                    refreshLayout.finishRefresh();
                }, 200);
            }
        });

        adapter.setOnItemClickListener(position -> {
            ARouter.getInstance().build(ActivityConstant.VIDEO_DETAILS)
                    .withString("videoId", list.get(position).getId())
                    .navigation();
        });

    }

    @Override
    protected void initData() {
        if ("推荐".equals(name)) {
            getHotVideo();
        } else if ("专栏".equals(name)) {
            getHotZhuanLan();
        }
        edSearchCount.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (headerView != null) {
                    recyclerView.removeHeaderView(headerView);
                }
                if ("推荐".equals(name)) {
                    getVideo(edSearchCount.getText().toString().trim());
                } else if ("专栏".equals(name)) {
                    getZhuanLan(edSearchCount.getText().toString().trim());
                }
                lySearchTip.setVisibility(View.VISIBLE);
                String str = "<font>搜索\t“</font><font color='#FE581E'>" + edSearchCount.getText().toString().trim() + "</font><font>”</font>";
                tvTip.setText(Html.fromHtml(str));
                return true;
            }
            return false;
        });
    }

    // 搜索视频
    public void getVideo(String str) {
        mPresenter.getHomeVideoSearch(str, p);
        mPresenter.setOnVideoSearchListener(list1 -> {
            list.addAll(list1);
            adapter.notifyDataSetChanged();
        });
    }

    // 搜索视频
    public void getZhuanLan(String str) {
        mPresenter.getZhuanLanSearch(str, p);
        mPresenter.setOnVideoSearchListener(list1 -> {
            list.addAll(list1);
            adapter.notifyDataSetChanged();
        });
    }

    // 视频热门搜索
    public void getHotVideo() {
        mPresenter.getHomeVideoMast();
        mPresenter.setOnHotVideoSearchListener((hot, arrayList) -> {
            if (arrayList.size() != 0) {
                setHistory(arrayList);
            }
            setHot(hot);
        });
    }

    // 专栏热门搜索
    public void getHotZhuanLan() {
        mPresenter.getZhuanLanHotSearch();
        mPresenter.setOnHotVideoSearchListener((hot, arrayList) -> {
            if (arrayList.size() != 0) {
                setHistory(arrayList);
            }
            setHot(hot);
        });
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }


    // 设置历史搜索
    public void setHistory(ArrayList<HotEntry> list) {
        fyHistoryAdapter = new TagAdapter<HotEntry>(list) {
            @Override
            public View getView(FlowLayout parent, int position, HotEntry hotEntry) {
                TextView mTag = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.remark_item, parent, false);
                mTag.setTextColor(Color.parseColor("#323232"));
                mTag.setBackgroundResource(R.drawable.shape_search_unselect_bg);
                mTag.setText(hotEntry.getKeyword());
                return mTag;
            }
        };
        fy_history.setAdapter(fyHistoryAdapter);
        fy_history.setmItemClickListener(((pos, data) -> {
            if (headerView != null) {
                recyclerView.removeHeaderView(headerView);
            }
            edSearchCount.setText(list.get(pos).getKeyword());
            lySearchTip.setVisibility(View.VISIBLE);
            String content = "<font>搜索\t“</font><font color='#FE581E'>" + list.get(pos).getKeyword() + "</font><font>”</font>";
            tvTip.setText(Html.fromHtml(content));
            getVideo(list.get(pos).getKeyword());
        }));
    }

    // 热门搜索
    public void setHot(String hot) {
        if (!TextUtils.isEmpty(hot)) {
            String[] str = hot.split(",");
            ArrayList<String> mList = new ArrayList<>();
            for (int i = 0; i < str.length; i++) {
                mList.add(str[i]);
            }
            fyHotAdapter = new TagAdapter<String>(mList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView mTag = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.remark_item, parent, false);
                    mTag.setTextColor(Color.parseColor("#323232"));
                    mTag.setBackgroundResource(R.drawable.shape_search_unselect_bg);
                    mTag.setText(s);
                    return mTag;
                }
            };
            fy_hot.setAdapter(fyHotAdapter);
            fy_hot.setmItemClickListener(((pos, data) -> {
                if (headerView != null) {
                    recyclerView.removeHeaderView(headerView);
                }
                edSearchCount.setText(mList.get(pos));
                lySearchTip.setVisibility(View.VISIBLE);
                String content = "<font>搜索\t“</font><font color='#FE581E'>" + mList.get(pos) + "</font><font>”</font>";
                tvTip.setText(Html.fromHtml(content));
                getVideo(mList.get(pos));
            }));
        }

    }

    @OnClick(R.id.tv_esc)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
