package com.community.hundred.modules.ui.post.fragment;

import android.os.Bundle;

import com.community.hundred.R;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.ninegridlayout.NineGridTestLayout;
import com.community.hundred.modules.ui.post.PeoplePostDetailsActivity;
import com.community.hundred.modules.ui.post.presenter.PeopleAlbumPresenter;
import com.community.hundred.modules.ui.post.presenter.view.IPeopleAlbumView;
import com.community.hundred.modules.ui.user.entry.UserAlbumEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

// 相册
public class PeopleAlbumFragment extends MyLazyFragment<PeoplePostDetailsActivity, IPeopleAlbumView, PeopleAlbumPresenter> {
    @BindView(R.id.ly_nine)
    NineGridTestLayout lyNine;

    private List<String> imageList;

    @Override
    protected PeopleAlbumPresenter createPresenter() {
        return new PeopleAlbumPresenter(getAttachActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_people_album;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        showLoading();
        imageList = new ArrayList<>();
        String uid = getArguments().getString("uid");
        mPresenter.getAlum(uid);
        mPresenter.setOnAlbumListener(album -> {
            if (album.size() == 0) {
                showEmpty();
            } else {
                showComplete();
                for (UserAlbumEntry entry : album) {
                    imageList.add(HttpConstant.BASE_HOST + entry.getImgdz());
                }
                lyNine.setUrlList(imageList);
            }

        });
    }

    public static PeopleAlbumFragment getInstance(String uid) {
        PeopleAlbumFragment fragment = new PeopleAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        fragment.setArguments(bundle);
        return fragment;
    }
}
