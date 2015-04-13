package org.lee.android.doodles;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public static class TabInfo {
        public final String name;
        public final Class<? extends Fragment> fragmentClass;
        public final Bundle args;

        public TabInfo(String name, @NonNull Class<? extends Fragment> fragmentClass) {
            this(name, fragmentClass, null);
        }

        public TabInfo(String name, @NonNull Class<? extends Fragment> fragmentClass, @Nullable Bundle args) {
            this.name = name;
            this.fragmentClass = fragmentClass;
            this.args = args;
        }
    }

    private Context mContext;
    private List<TabInfo> mTabInfoList;

    public CustomFragmentPagerAdapter(FragmentManager fm, Context context, @NonNull ArrayList<TabInfo> tabInfos) {
        super(fm);
        this.mContext = context;
        this.mTabInfoList = tabInfos;
    }

    public CustomFragmentPagerAdapter addTab(TabInfo info) {
        mTabInfoList.add(info);
        notifyDataSetChanged();
        return this;
    }

    public List<TabInfo> getTabs() {
        return mTabInfoList;
    }

    public TabInfo getTabInfo(int position) {
        return mTabInfoList.isEmpty() ? null : mTabInfoList.get(position % mTabInfoList.size());
    }


    @Override
    public Fragment getItem(int position) {
        TabInfo info = getTabInfo(position);
        Fragment fragment = Fragment.instantiate(mContext, info.fragmentClass.getName(), info.args);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTabInfoList.isEmpty() ? 0 : mTabInfoList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        TabInfo tabInfo = getTabInfo(position);
        return tabInfo.name;
    }
}