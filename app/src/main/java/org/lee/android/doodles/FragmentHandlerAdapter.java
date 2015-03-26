package org.lee.android.doodles;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import org.lee.android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liruiyu Email:allnet@live.cn
 */
public class FragmentHandlerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private final ArrayList<TabInfo> mTabs;
    private final FragmentManager mFragmentManager;

    /**
     * Tab Object
     */
    public static final class TabInfo {
        public final Class<?> clss;
        public final String title;
        public final Bundle args;

        public TabInfo(Class<?> _class, String _title, Bundle _args) {
            clss = _class;
            title = _title;
            args = _args;
        }
    }

    public FragmentHandlerAdapter(FragmentManager fm, Context context) {
        this(fm, context, new ArrayList<TabInfo>());
    }

    public FragmentHandlerAdapter(FragmentManager fm, Context context, ArrayList<TabInfo> tabInfos) {
        super(fm);
        mFragmentManager = fm;
        this.mContext = context;
        this.mTabs = tabInfos;
    }

    public FragmentHandlerAdapter addTab(TabInfo info) {
        mTabs.add(info);
        notifyDataSetChanged();
        return this;
    }

    public ArrayList<TabInfo> getTabs() {
        return mTabs;
    }

    public TabInfo getTabInfo(int position) {
        return mTabs.isEmpty() ? null : mTabs.get(position % mTabs.size());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTabInfo(position).title;
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = getTabInfo(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(info.title);
        if (fragment != null) {
            return fragment;
        }
        Fragment f = Fragment.instantiate(mContext, info.clss.getName(), info.args);
        return f;
    }

    public void clear() {
        if (mTabs != null) {
            mTabs.clear();
        }
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    private Fragment mCurrFragment;

    public Fragment currentFragment() {
        return mCurrFragment;
    }

    public void show(Fragment fragment, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCurrFragment != null) {
            transaction.hide(mCurrFragment);
        }

        Fragment fragment1 = mFragmentManager.findFragmentByTag(tag);
        if (fragment1 == null) {
            transaction.add(android.R.id.tabcontent, fragment, tag);
            transaction.addToBackStack(tag);
            transaction.setBreadCrumbTitle(tag);
        } else {
            transaction.show(fragment1);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commitAllowingStateLoss();
        mCurrFragment = fragment;
    }

    private void hideAll() {
        List<Fragment> list = mFragmentManager.getFragments();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : list) {
            transaction.hide(fragment);
        }
        transaction.commit();
    }

    /**
     * 打印追踪信息
     */
    public void track() {
        int BackStackEntryCount = mFragmentManager.getBackStackEntryCount();
        int size = mFragmentManager.getFragments().size();

        StringBuffer buffer = new StringBuffer();
        for (int i = BackStackEntryCount; i > 0; i--) {
            FragmentManager.BackStackEntry backStackEntryAt = mFragmentManager.getBackStackEntryAt(i - 1);
            buffer.append(backStackEntryAt.getName() + ", title:" + backStackEntryAt.getBreadCrumbTitle() + "\n");
        }

        Log.anchor("BackStackEntryCount:" + BackStackEntryCount + ", size:" + size
                + "\n" + buffer.toString());
    }
}