package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.test.data.DataGeter;
import org.lee.android.util.Toast;

/**
 * 最新Doodles页面
 */
public class TodayFragment extends Fragment implements
        RecyclerItemViewHolder.OnRecyclerItemChildClickListener {

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Fragment运行状态监听
     */
    private FragmentRunningListener mFrunningListener;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private Doodle[] mDoodles;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        mOnScrollListener = mainActivity.getHidingScrollListener();
        mFrunningListener = mainActivity;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int paddingTop = Utils.getToolbarHeight(getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), paddingTop,
                recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getActivity(), mDoodles, this);
        recyclerAdapter.setHasHeader(true);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getActivity().getActionBar().setTitle(getTag());
        mFrunningListener.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFrunningListener.onPause(this);
    }

    /**
     * RecyclerView每一项View的点击事件
     * @param itemView 列表项
     * @param position 点击的position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        Toast.show("onItemClick");
        Doodle doodle = mDoodles[position];
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.tabcontent,
                        DoodleDetailsFragment.newInstance(doodle,
                                (int) itemView.getX(), (int) itemView.getY(),
                                itemView.getWidth(), itemView.getHeight()),
                        doodle.title
                )
                .addToBackStack("detail").commit();
    }

    /**
     * RecyclerView中Item项子View点击事件
     * @param itemChildView
     * @param position
     */
    @Override
    public void onItemChildClick(View itemChildView, int position) {
        Toast.show("onItemChildClick");

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }


}