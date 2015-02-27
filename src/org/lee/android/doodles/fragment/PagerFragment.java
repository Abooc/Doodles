package org.lee.android.doodles.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.doodles.TabsAdapter;

/**
 * 浏览涂鸦列表
 */
public class PagerFragment extends Fragment {

    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    private IFragmentPagerAdapter mIFragmentPagerAdapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ViewPager pager = (ViewPager) view.findViewById(R.id.ViewPager);

        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mIFragmentPagerAdapter = new IFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setOffscreenPageLimit(5);
        pager.setAdapter(mIFragmentPagerAdapter);
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class IFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public IFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DoodlesListFragment();
            Bundle args = new Bundle();
            args.putInt("year", 2014);
            args.putInt("month", 12-i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (12 - position) + "月份" ;
        }
    }

    private TabsAdapter addTab(TabsAdapter adapter){
        int count = 12;
        for (int i = count; i > 0; i--){
            Bundle iBundle = new Bundle();
            iBundle.putInt("year", 2014);
            iBundle.putInt("month", i);
            adapter.addTab(DoodlesListFragment.class, iBundle);
        }
        return adapter;
    }

}