package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;

import org.lee.android.doodles.CustomFragmentPagerAdapter;
import org.lee.android.doodles.CustomFragmentPagerAdapter.TabInfo;
import org.lee.android.doodles.LifecycleFragment;
import org.lee.android.doodles.R;
import org.lee.android.doodles.fragment.YearsFragment.Year;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 浏览存档涂鸦-父Pager
 * <br/>
 * 承载"切换年份"页面和其他多个单月份页面
 */
public class DoodleArchivePagerFragment extends LifecycleFragment {

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
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

        Bundle args = new Bundle();
        args.putString("year", new Gson().toJson(mYear));
        TabInfo yearTabInfo = new TabInfo("关于" + mYear.year + "年", YearAboutFragment.class, args);
        tabInfos.add(0, yearTabInfo);

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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTag());
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

    public ViewPager getPager() {
        return mViewPager;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStackImmediate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter)
            return AnimationUtils.loadAnimation(getActivity(),
                    enter ? android.R.anim.fade_in : android.R.anim.fade_out);
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasYearPage", mHasYearPage);
        outState.putSerializable("year", mYear);
    }


}