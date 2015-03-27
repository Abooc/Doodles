package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.FileUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 */
public class TodayFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    private ViewPager mViewPager;
    private ListView mListView;
    private ActionBar mActionBar;
    private FragmentRunningListener mFrunningListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity)activity;
        mFrunningListener = mainActivity;
        mActionBar = mainActivity.getSupportActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_today, container, false);

        mListView = (ListView) container.findViewById(R.id.ListView);
        mListView.setOnItemClickListener(this);
        useTest();

//        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_today_header, null);
//        mViewPager = (ViewPager) headerView.findViewById(R.id.ViewPager);
//        mListView.addHeaderView(headerView);
//        DoodlePagerAdapter adapter = new DoodlePagerAdapter(getChildFragmentManager(), mDoodles);
//        mViewPager.setAdapter(adapter);
        return container;
    }


    private Doodle[] mDoodles;

    /**
     * 将Doodles列表的Json数据解析并显示到ListView
     *
     * @param doodlesJson
     */
    private void attachData(String doodlesJson) {
        Gson gson = new Gson();
        Doodle[] doodles = gson.fromJson(doodlesJson, Doodle[].class);
        if (doodles == null || doodles.length == 0) return;
        DoodleAdapter adapter = new DoodleAdapter(getActivity(), doodles);
        mListView.setAdapter(adapter);
        mDoodles = doodles;
    }

    private void useTest() {
        try {
            InputStream inputStream = AppContext.getContext().getAssets().open("data/doodles.json");
            String sources = FileUtils.readInStream(inputStream);
            attachData(sources);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Doodle doodle = (Doodle) parent.getAdapter().getItem(position);
//        WebViewActivity.launch(getActivity(), ApiClient.GOOGLE_DOODLES_ROOT + doodle.name, doodle.title);
//        WebViewActivity.launch(getActivity(), ApiClient.GOOGLE_DOODLES_SEARCH + doodle.query, doodle.title);

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
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }

    private class DoodlePagerAdapter extends FragmentStatePagerAdapter {

        private int count = 5;
        private Doodle[] mDoodles;
        private Gson mGson = new Gson();

        public DoodlePagerAdapter(FragmentManager fm, Doodle[] doodles) {
            super(fm);
            mDoodles = doodles;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new DoodlePagerFragment();
            Bundle args = new Bundle();
            String json = mGson.toJson(mDoodles[position]);
            args.putString("doodle", json);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDoodles[position].title;
        }
    }


}