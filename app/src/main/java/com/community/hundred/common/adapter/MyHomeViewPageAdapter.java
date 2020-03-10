package com.community.hundred.common.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.community.hundred.common.base.MyLazyFragment;

import java.util.List;

/**
 * viewpage 适配器
 */
public class MyHomeViewPageAdapter extends FragmentPagerAdapter {

    private List<MyLazyFragment> mFragment;
    private List<String> mListTitle;
    private Context mContext;


    public MyHomeViewPageAdapter(FragmentManager fm, Context context, List<MyLazyFragment> mFragment, List<String> mListTitle) {
        super(fm);
        this.mContext = context;
        this.mFragment = mFragment;
        this.mListTitle = mListTitle;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragment.get(i);
    }

    @Override
    public int getCount() {
        return mListTitle == null ? 0 : mListTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mListTitle.get(position);
    }
}
