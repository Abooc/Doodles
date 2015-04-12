package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.lee.android.doodles.R;
import org.lee.android.doodles.fragment.YearsFragment.Year;
import org.lee.android.util.Log;

import java.util.Calendar;

/**
 * 浏览存档涂鸦
 */
public class DoodleArchivePagerFragment extends Fragment {

    private ViewPager mViewPager;
    public static Year mYear = new Year("2015", "i_2002");
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
            Log.anchor("mYear = " + mYear);
            mHasYearPage = savedInstanceState.getBoolean("hasYearPage");
        } else {
            String json = getArguments().getString("year");
            if (!TextUtils.isEmpty(json))
                mYear = new Gson().fromJson(json, Year.class);
            mHasYearPage = getArguments().getBoolean("hasYearPage");
        }

        setUpPager(mYear, mHasYearPage);

        Log.anchor(mYear);
        return container;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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

    private void setUpPager(Year year, boolean hasYearPage) {
        IFragmentPagerAdapter adapter = new IFragmentPagerAdapter(
                getChildFragmentManager(), year, hasYearPage);
        mViewPager.setAdapter(adapter);
        if(hasYearPage)
            mViewPager.setCurrentItem(1);
    }

    public Year getYear() {
        return mYear;
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

    public static class IFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private int count = 1;
        private Year year;
        private Boolean hasYearPage;

        public IFragmentPagerAdapter(FragmentManager fm, Year year, boolean hasYearPage) {
            super(fm);
            this.year = year;
            this.hasYearPage = hasYearPage;

            caculateMonth(Integer.valueOf(year.name));
        }

        private void caculateMonth(int year){
            int currYear = Calendar.getInstance().get(Calendar.YEAR);
            if (currYear > year) {
                count = 12;
            } else {
                count = Calendar.getInstance().get(Calendar.MONTH);
            }
        }

        @Override
        public Fragment getItem(int i) {
            if (hasYearPage && i == 0) {
                return new YearsFragment();
            }
            Fragment fragment = new DoodleArchiveListFragment();
            Bundle args = new Bundle();
            args.putInt("year", Integer.valueOf(year.name));
            args.putInt("month", count - i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (hasYearPage && position == 0) return "选择年份";
            int month = count - position;
            return (month < 10 ? "0" + month : month) + "月份";
        }
    }

}