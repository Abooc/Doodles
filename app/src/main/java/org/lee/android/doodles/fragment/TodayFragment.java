package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.AppFunction;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.FileUtils;
import org.lee.android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * 最新Doodles页面
 */
public class TodayFragment extends Fragment implements AdapterView.OnItemClickListener,
        View.OnTouchListener {

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    //    private ViewPager mViewPager;
    private ListView mListView;
    private ActionBar mActionBar;
    /**
     * Fragment运行状态监听
     */
    private FragmentRunningListener mFrunningListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        mFrunningListener = mainActivity;
        mActionBar = mainActivity.getSupportActionBar();
        mActionBar.addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {
            @Override
            public void onMenuVisibilityChanged(boolean b) {
                Log.anchor(b);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_today, container, false);
        mListView = (ListView) container.findViewById(R.id.ListView);
        mListView.setOnItemClickListener(this);
        mListView.setOnTouchListener(this);
        useTest();

//        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_today_header, null);
//        mViewPager = (ViewPager) headerView.findViewById(R.id.ViewPager);
//        mListView.addHeaderView(headerView);
//        DoodlePagerAdapter adapter = new DoodlePagerAdapter(getChildFragmentManager(), mDoodles);
//        mViewPager.setAdapter(adapter);
        return container;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Retrieve the Toolbar from our content view, and set it as the action bar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        final ActionBar actionBar = getSupportActionBar();
//        View view = getLayoutInflater().inflate(R.layout.app_searcher, null);
//        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//        actionBar.setCustomView(view, params);
//        actionBar.setDisplayShowCustomEnabled(true);

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
        View app_searcher = getLayoutInflater(savedInstanceState).inflate(R.layout.app_searcher, null);
        toolbar.addView(app_searcher, params);

        final View menu = app_searcher.findViewById(R.id.Menu);
        EditText iEditText = (EditText) app_searcher.findViewById(R.id.EditText);
        iEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    menu.setVisibility(View.VISIBLE);
                    mActionBar.hide();
                } else {
                    menu.setVisibility(View.GONE);
                    mActionBar.show();
                }
            }
        });

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


    private float yy;
    private boolean hasHandle;
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

                hasHandle = false;
                yy = event.getY();
                return blockTouch;
            case MotionEvent.ACTION_MOVE:
                if (!hasHandle) {
                    float y = event.getY();
                    if ((y - yy) > 40) {
                        hasHandle = true;
                        mActionBar.show();
                    } else if ((y - yy) < -40) {
                        hasHandle = true;
                        mActionBar.hide();
                    }
                }
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