package org.lee.android.doodles.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.util.Log;

/**
 * 搜索更多涂鸦
 */
public class SearchFragment extends Fragment {

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ActionBar actionBar = activity.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.anchor();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.anchor();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.anchor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.anchor();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.anchor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.anchor();
    }


}