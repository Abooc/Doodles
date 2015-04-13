package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.lee.android.doodles.CustomFragmentPagerAdapter;
import org.lee.android.doodles.CustomFragmentPagerAdapter.TabInfo;
import org.lee.android.doodles.R;
import org.lee.android.doodles.fragment.YearsFragment.Year;
import org.lee.android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 浏览存档涂鸦-父Pager
 * </p>
 * 承载"切换年份"页面和其他多个单月份页面
 */
public class DoodleArchivePagerFragment extends Fragment {

    private ViewPager mViewPager;
    private Year mYear;
    public boolean mHasYearPage = true;

    public static DoodleArchivePagerFragment newInstance(Year year, boolean hasYearPage) {
        DoodleArchivePagerFragment fragment = new DoodleArchivePagerFragment();
        Bundle args = new Bundle();
        args.putBoolean("hasYearPage", hasYearPage);
        args.putString("year", new Gson().toJson(year));
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Fragment运行状态监听
     */
    private FragmentRunningListener mFrunningListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        mFrunningListener = (FragmentRunningListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_pager, container, false);
        mViewPager = (ViewPager) container.findViewById(R.id.ViewPager);

        if (savedInstanceState != null) {
            mYear = (Year) savedInstanceState.getSerializable("year");
            mHasYearPage = savedInstanceState.getBoolean("hasYearPage");
        } else {
            String json = getArguments().getString("year");
            if (!TextUtils.isEmpty(json))
                mYear = new Gson().fromJson(json, Year.class);
            mHasYearPage = getArguments().getBoolean("hasYearPage");
        }
        return container;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //优先计算当前年有几个月份，为了添加显示每个月份的Fragment
        int monthCount = calculateYear(mYear);
        ArrayList<TabInfo> tabInfos = getFragments(mYear, monthCount);

        if (mHasYearPage) {
            //添加需要带有"切换年份"的Fragment
            TabInfo tabInfo = new TabInfo("切换年份", YearsFragment.class);
            tabInfos.add(0, tabInfo);
        }

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(
                getChildFragmentManager(), getActivity(), tabInfos);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mHasYearPage ? 1 : 0);
    }

    private ArrayList<TabInfo> getFragments(Year year, int count) {
        ArrayList<TabInfo> tabInfos = new ArrayList<>();
        int month = count;
        TabInfo tabInfo;
        for (; month > 0; month--) {
            Bundle args = new Bundle();
            args.putInt("year", Integer.valueOf(year.year));
            args.putInt("month", month);

            tabInfo = new TabInfo((month < 10 ? "0" + month : month) + "月份",
                    DoodleArchiveListFragment.class, args);
            tabInfos.add(tabInfo);
        }
        return tabInfos;
    }

    /**
     * 根据Year年份计算该年1月份到当前时间，共有几个月份。
     * 主要用在计算当前时间是几月份，则表示有几个月的Doodles产生了
     *
     * @param year
     * @return
     */
    private int calculateYear(Year year) {
        int yearNum = Integer.valueOf(year.year);
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        if (currYear > yearNum) {
            return 12;
        } else {
            //由于get(Calendar.MONTH)返回的值从0开始，所以+1
            return Calendar.getInstance().get(Calendar.MONTH) + 1;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.anchor();
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStackImmediate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFrunningListener.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFrunningListener.onPause(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.anchor();
        outState.putBoolean("hasYearPage", mHasYearPage);
        outState.putSerializable("year", mYear);
    }

    public ViewPager getPager() {
        return mViewPager;
    }


}