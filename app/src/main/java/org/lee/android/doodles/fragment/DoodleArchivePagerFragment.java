package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.common.view.SlidingTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.fragment.YearsFragment.Year;
import org.lee.android.util.Log;

import java.util.Calendar;

/**
 * 浏览存档涂鸦
 */
public class DoodleArchivePagerFragment extends Fragment {

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private Year mYear = new Year("2015", "i_2002");

    public static DoodleArchivePagerFragment newInstance() {
        DoodleArchivePagerFragment fragment = new DoodleArchivePagerFragment();
        Bundle args = new Bundle();
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
        int paddingTop = Utils.getToolbarHeight(getActivity());
        container.setPadding(container.getPaddingLeft(), paddingTop,
                container.getPaddingRight(), container.getPaddingBottom());

        mViewPager = (ViewPager) container.findViewById(R.id.ViewPager);
        mSlidingTabLayout = (SlidingTabLayout) container.findViewById(R.id.SlidingTabs);
        if (savedInstanceState != null) {
            mYear = (Year) savedInstanceState.getSerializable("year");
            Log.anchor("mYear = " + mYear);
        }
        attachData(mYear);

//        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
//            @Override
//            public int getIndicatorColor(int position) {
//                return Color.WHITE;
//            }
//
//            @Override
//            public int getDividerColor(int position) {
//                return Color.WHITE;
//            }
//        });

        Log.anchor(mYear);
        return container;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.RemoveAd:
                attachData(new Year("2015", "i_2000"));
                return true;
            case R.id.AboutDoodles:
                attachData(new Year("2014", "i_2000"));

                return true;
            case R.id.Share:
                attachData(new Year("2013", "i_2000"));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void attachData(Year year) {
        int count;
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        if (currYear > Integer.valueOf(year.name)) {
            count = 12;
        } else {
            count = Calendar.getInstance().get(Calendar.MONTH);
        }

        IFragmentPagerAdapter adapter = new IFragmentPagerAdapter(
                getChildFragmentManager());
        adapter.setYear(year.name);
        adapter.setCount(count);
        mViewPager.setAdapter(adapter);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    public void setYear(Year year) {
        mYear = year;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.anchor();
        outState.putSerializable("year", mYear);
    }

    public static class IFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private int count = 12;
        private String year;

        public IFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setYear(String year) {
            this.year = year;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DoodlesArchiveListFragment();
            Bundle args = new Bundle();
            args.putInt("year", 2014);
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
            int month = count - position;
            return (month < 10 ? "0" + month : month) + "月份";
        }
    }

}