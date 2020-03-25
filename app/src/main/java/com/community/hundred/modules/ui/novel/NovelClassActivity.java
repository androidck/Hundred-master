package com.community.hundred.modules.ui.novel;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.util.SharedPreferencesUtils;
import com.community.hundred.modules.adapter.NovAdapter;
import com.community.hundred.modules.dialog.DownloadDialog;
import com.community.hundred.modules.manager.BookUtils;
import com.community.hundred.modules.manager.entry.BookEntry;
import com.community.hundred.modules.ui.main.fragment.entry.NovEntry;
import com.community.hundred.modules.ui.novel.presenter.NovelClassPresenter;
import com.community.hundred.modules.ui.novel.presenter.view.INovelView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.thl.reader.ReadActivity;
import com.thl.reader.db.BookList;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 小说 分类 Activity

@Route(path = ActivityConstant.NOV_CLASS_LIST)
public class NovelClassActivity extends MyActivity<INovelView, NovelClassPresenter> {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    @Autowired
    String classifyId; // 分类id
    private NovAdapter novAdapter;

    private List<NovEntry> novEntries = new ArrayList<>();

    @Override
    protected NovelClassPresenter createPresenter() {
        return new NovelClassPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_novel_class;
    }

    @Override
    protected void initView() {
        setWhiteLeftButtonIcon(getTitleBar());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        novAdapter = new NovAdapter(this, novEntries);
        recyclerView.setAdapter(novAdapter);
        // 小说点击事件
        novAdapter.setOnItemClickListener(position -> {
            BookEntry entry = BookUtils.getInstance().getBook(novEntries.get(position).getTitle());
            SharedPreferencesUtils.saveBoolean(getActivity(), "initFristData", false);
            if (entry != null) {
                BookList bookList = new BookList();
                bookList.setBookname(entry.getName());
                bookList.setBookpath(entry.getPath());
                bookList.setBegin(bookList.getBegin());
                ReadActivity.openBook(bookList, getActivity());
            } else {
                // 调用下载的方法
                NovEntry novEntry = novEntries.get(position);
                new DownloadDialog(this, this, novEntry).show();
            }
        });
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void initData() {
        getNovList();
    }

    // 获取列表
    public void getNovList() {
        mPresenter.getNovelClass(classifyId);
        mPresenter.setOnNovListener(list -> {
            if (list.size() == 0) {
                showEmpty();
            } else {
                showComplete();
                novEntries.addAll(list);
                novAdapter.notifyDataSetChanged();
            }
        });
    }


}
