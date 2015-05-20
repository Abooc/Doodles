package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.lee.android.doodles.AppApplication;
import org.lee.android.doodles.DefaultBuild;
import org.lee.android.doodles.LifecycleFragment;
import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.Month;
import org.lee.android.test.data.DataGeter;
import org.lee.android.util.Log;

/**
 * 最新Doodles页面
 */
public class TodayFragment extends LifecycleFragment implements
        DoodleRecyclerItemHolder.OnRecyclerItemChildClickListener {

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView.OnScrollListener mOnScrollListener;
    private DoodleRecyclerAdapter.Card[] mCards;
    private MainActivity mMainActivity;

    private InterstitialAd interstitial;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.anchor();
        mMainActivity = (MainActivity) activity;
        mOnScrollListener = mMainActivity.getHidingScrollListener();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.anchor();
        Doodle[] mDoodles = DataGeter.getDoodles();
        mCards = DataGeter.toCards(mDoodles);
        Month monthBean = new Month(2015, 05);
        mCards = DataGeter.getTodayListCards(mCards, monthBean);


    }

    private AdListener mAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            Log.anchor();
            displayInterstitial();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_today, container, false);
        return container;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRecyclerView(view);

    }

    private void initRecyclerView(View view) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int paddingTop = Utils.getToolbarHeight(getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), paddingTop,
                recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        DoodleRecyclerAdapter recyclerAdapter = new DoodleRecyclerAdapter(
                getActivity(), mCards, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(mOnScrollListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.setTitle(getTag());
    }

    /**
     * RecyclerView每一项View的点击事件
     *
     * @param itemView 列表项
     * @param position 点击的position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        Doodle doodle = (Doodle) mCards[position].obj;
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
                mMainActivity.onNavigationDrawerItemSelected(1);
                return;
            case R.id.Search:
                mMainActivity.showToolbar();

                return;
            case R.id.Share:
                AppApplication.share(mMainActivity);
                return;
            case R.id.AdView:
                Log.anchor();
                if(interstitial == null){
                    loadInterstitial();
                }else
                    displayInterstitial();
                return;
        }
    }

    private void loadInterstitial(){
        // Create the interstitial.
        interstitial = new InterstitialAd(mMainActivity);
        interstitial.setAdUnitId(DefaultBuild.INTERSTITIAL_AD_UNIT_ID);

        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(mAdListener);
    }

    // Invoke displayInterstitial() when you are ready to display an interstitial.
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }

}