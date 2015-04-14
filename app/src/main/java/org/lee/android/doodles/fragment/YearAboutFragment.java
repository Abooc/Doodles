package org.lee.android.doodles.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.test.FragmentLog;
import org.lee.android.util.Log;

/**
 * 指定年份的关于页面
 */
public class YearAboutFragment extends FragmentLog {

    public static YearAboutFragment newInstance(int year) {
        YearAboutFragment fragment = new YearAboutFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_year_about, null, false);
    }

    private int mYear;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mYear = getArguments().getInt("year");

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String json = savedInstanceState.getString("data");
            Log.anchor(savedInstanceState.toString());
        }
    }

}