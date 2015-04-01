package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.AppFunction;
import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.FileUtils;
import org.lee.android.doodles.volley.VolleyLoader;
import org.lee.android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
        useTest();
        return container;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        int paddingTop = Utils.getToolbarHeight(getActivity()) + Utils.getTabsHeight(getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), paddingTop, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(mDoodles);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(mOnScrollListener);
    }

    private Doodle[] mDoodles;

    /**
     * 将Doodles列表的Json数据解析
     *
     * @param doodlesJson
     */
    private void attachData(String doodlesJson) {
        Gson gson = new Gson();
        Doodle[] doodles = gson.fromJson(doodlesJson, Doodle[].class);
        if (doodles == null || doodles.length == 0) return;
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

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Doodle[] mDoodles;

        public RecyclerAdapter(Doodle[] doodles) {
            mDoodles = doodles;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_doodles_list_item, parent, false);
            return RecyclerItemViewHolder.newInstance(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
            Doodle doodle = mDoodles[position];
            holder.attachData(doodle);
        }

        @Override
        public int getItemCount() {
            return mDoodles == null ? 0 : mDoodles.length;
        }

    }




}