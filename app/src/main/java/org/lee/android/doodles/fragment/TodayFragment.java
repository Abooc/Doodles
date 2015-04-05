package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.AppFunction;
import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.FileUtils;
import org.lee.android.test.DataGeter;
import org.lee.android.util.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * 最新Doodles页面
 */
public class TodayFragment extends Fragment implements AdapterView.OnItemClickListener,
        View.OnTouchListener, RecyclerItemViewHolder.ViewHolderClicks {

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ActionBar mActionBar;
    /**
     * Fragment运行状态监听
     */
    private FragmentRunningListener mFrunningListener;
    private RecyclerView.OnScrollListener mOnScrollListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        mOnScrollListener = mainActivity.getHidingScrollListener();
        mFrunningListener = mainActivity;
        mActionBar = mainActivity.getSupportActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_today, container, false);
        return container;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mDoodles = DataGeter.getDoodles();
        if (mDoodles == null || mDoodles.length == 0) return;

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        int paddingTop = Utils.getToolbarHeight(getActivity()) + Utils.getTabsHeight(getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), paddingTop, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getActivity(), mDoodles, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(mOnScrollListener);
        recyclerView.setOnTouchListener(this);
    }

    private Doodle[] mDoodles;

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


    private boolean blockTouch;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                blockTouch = false;
                v.requestFocus();
                blockTouch = AppFunction.hideInputMethod(getActivity(), v);
                if (blockTouch) {
                    return blockTouch;
                }
                return blockTouch;
            case MotionEvent.ACTION_MOVE:
                return blockTouch;
        }
        return blockTouch;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Doodle doodle = (Doodle) parent.getAdapter().getItem(position);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.tabcontent,
                        DoodleDetailsFragment.newInstance(doodle,
                                (int) view.getX(), (int) view.getY(),
                                view.getWidth(), view.getHeight())
                )
                .addToBackStack("detail")
                .commit();
    }

    @Override
    public void onItemClick(View parent, int position) {
        Toast.show("onItemClick");
        Doodle doodle = mDoodles[position];
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.tabcontent,
                        DoodleDetailsFragment.newInstance(doodle,
                                (int) parent.getX(), (int) parent.getY(),
                                parent.getWidth(), parent.getHeight())
                )
                .addToBackStack("detail").commit();
    }

    @Override
    public void onSearch(TextView searchView) {
        Toast.show("onSearch");

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }




}