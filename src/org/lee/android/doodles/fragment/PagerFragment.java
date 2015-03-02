package org.lee.android.doodles.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.fragment.CategorysFragment.Year;
import org.lee.android.util.Log;

import java.util.Calendar;

/**
 * 浏览涂鸦列表
 */
public class PagerFragment extends Fragment {

    private ViewPager mViewPager;
    private Year mYear = new Year("2015", "i_2002");

    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
//        ((MainActivity) activity).onShowFragment(this);
//        ActionBar actionBar = activity.getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//
//        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(activity,
//                R.array.menu, android.R.layout.simple_spinner_dropdown_item);
//        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }

    private ActionBar.OnNavigationListener mOnNavigationListener =
            new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int position, long itemId) {
//                    mApiClient.requestDoodles(2014 - position, 2, callbacks);
                    return true;
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.ViewPager);
        if (savedInstanceState != null) {
            mYear = (Year) savedInstanceState.getSerializable("year");
            Log.anchor("mYear = " + mYear);
        }
        attachData(mYear);
        Log.anchor(mYear);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.anchor(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onShowFragment(this);
        Log.anchor();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.anchor();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.anchor();
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
                getActivity().getSupportFragmentManager());
        adapter.setYear(year.name);
        adapter.setCount(0);
        mViewPager.setAdapter(adapter);
        adapter.setCount(count);
        adapter.notifyDataSetChanged();
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
            Fragment fragment = new DoodlesListFragment();
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
            return year + "-" + (month < 10 ? "0" + month : month);
        }
    }

}