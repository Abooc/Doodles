package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.lee.android.doodles.AppApplication;
import org.lee.android.doodles.LifecycleFragment;
import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.test.data.DataGeter;
import org.lee.android.util.Toast;

/**
 * 最新Doodles页面
 */
public class TodayFragment extends LifecycleFragment implements
        RecyclerItemViewHolder.OnRecyclerItemChildClickListener {

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView.OnScrollListener mOnScrollListener;
    private Doodle[] mDoodles;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
        mOnScrollListener = mainActivity.getHidingScrollListener();
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
        DoodleRecyclerAdapter recyclerAdapter = new DoodleRecyclerAdapter(
                getActivity(), mDoodles, this, mMenuItemClickListener);
        recyclerAdapter.setHasHeader(true);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(mOnScrollListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setTitle(getTag());
    }

    /**
     * RecyclerView每一项View的点击事件
     *
     * @param itemView 列表项
     * @param position 点击的position
     */
    @Override
    public void onItemClick(View itemView, int position) {
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
     *
     * @param clickView 响应点击事件的View
     * @param position
     */
    @Override
    public void onItemChildClick(View clickView, int position) {
        switch (clickView.getId()) {
            case R.id.ArchiveDoodles:
                mainActivity.onNavigationDrawerItemSelected(1);
                return;
            case R.id.AdView:
                Toast.show("查看广告");
                return;
        }
    }

    /**
     * Doodles列表卡片上的Toolbar菜单
     */
    private Toolbar.OnMenuItemClickListener mMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Toast.show("" + menuItem.getTitle());
            switch (menuItem.getItemId()) {
                case R.id.Share:
                    AppApplication.share(getActivity());
                    return true;
            }
            return false;
        }
    };

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}