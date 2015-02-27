package org.lee.android.doodles.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;

import org.lee.android.doodles.R;
import org.lee.android.doodles.TabsAdapter;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.util.Log;

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

    private LinearLayout mDateList;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mDateList = (LinearLayout) view.findViewById(R.id.DateList);

        ViewPager pager = (ViewPager) view.findViewById(R.id.ViewPager);
        TabsAdapter adapter = new TabsAdapter(getActivity(), pager);
//        adapter.addTab(TodayFragment.class, null);
        adapter = addTab(adapter);
        pager.setAdapter(adapter);

        attachListener();
    }

    private void attachListener() {
        int count = mDateList.getChildCount();
        for (int i = 0; i < count; i++) {
            mDateList.getChildAt(i).setOnClickListener(dateEvent);
        }
    }

    private View.OnClickListener dateEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int month = mDateList.indexOfChild(v);
//            mApiClient.requestDoodles(2014, 12 - month, callbacks);
        }
    };

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