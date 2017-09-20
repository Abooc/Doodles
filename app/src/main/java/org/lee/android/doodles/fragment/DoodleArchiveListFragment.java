package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.battleground.widget.MessageView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.lee.android.doodles.ApiClient;
import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.Today;
import org.lee.android.doodles.volley.HttpHandler;
import org.lee.android.test.data.DataGeter;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;

/**
 * 浏览存档涂鸦列表
 * <p/>
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-2-22.
 */
public class DoodleArchiveListFragment extends Fragment
        implements DoodleRecyclerItemHolder.OnRecyclerItemChildClickListener {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DoodleArchiveListFragment newInstance(Bundle args) {
        DoodleArchiveListFragment fragment = new DoodleArchiveListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MessageView mMessageView;

    public DoodleArchiveListFragment() {
    }

    private Activity mActivity;
    private RecyclerView.OnScrollListener mOnScrollListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        MainActivity mainActivity = (MainActivity) activity;
        mOnScrollListener = mainActivity.getHidingScrollListener();

    }

    private Today mToday;
    private DoodleRecyclerAdapter.Card[] mCards;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Doodle[] mDoodles = DataGeter.getDoodles();
        if (mDoodles == null || mDoodles.length == 0) return;
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("monthOfYear");
        mToday = new Today(year, month, 0);

        if(savedInstanceState == null){
            new ApiClient(mActivity).requestDoodles(mToday.year, mToday.monthOfYear, iHttpHandler);
        }else {
            doodlesJson = savedInstanceState.getString("data");
            if(TextUtils.isEmpty(doodlesJson)) return;
            Gson gson = new Gson();
            Doodle[] doodles = gson.fromJson(doodlesJson, Doodle[].class);
            iHttpHandler.onSuccess(HttpStatus.SC_OK, null, doodlesJson, doodles);
        }
    }

    private HttpHandler iHttpHandler = new HttpHandler<Doodle[]>() {

        @Override
        public void onStart() {
            mMessageView.setVisibility(View.VISIBLE);
            mMessageView.loading();
        }

        @Override
        public void onFinish() {
            mMessageView.stop();
        }

        @Override
        public void onSuccess(int i, Header[] headers, String response, Doodle[] doodles) {
            Log.anchor();
            doodlesJson = response;
            mCards = DataGeter.toCards(doodles);
            mCards = DataGeter.getListCards(mCards, mToday);

            mMessageView.setVisibility(View.INVISIBLE);
            initRecyclerView(getView());
        }

        @Override
        public void onFailure(int statusCode, String error) {
            mMessageView.setMessage(error);
            Log.anchor();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.anchor(savedInstanceState);
        Log.anchor("year:" + mToday.year + ", monthOfYear:" + mToday.monthOfYear);
        View rootView = inflater.inflate(R.layout.fragment_doodles_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mMessageView = (MessageView) view.findViewById(R.id.MessageView);

        if(mCards != null){
            initRecyclerView(view);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.anchor("year:" + mToday.year + ", monthOfYear:" + mToday.monthOfYear);
    }

    private void initRecyclerView(View view) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        int toolbarHeight = Utils.getToolbarHeight(getActivity());
//        int slidingTabLayoutHeight = getActivity().findViewById(R.id.SlidingTabs).getHeight();
        recyclerView.setPadding(recyclerView.getPaddingLeft(), toolbarHeight + toolbarHeight,
                recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        DoodleRecyclerAdapter recyclerAdapter = new DoodleRecyclerAdapter(mActivity, mCards, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onItemClick(View parent, int position) {
        DoodleRecyclerAdapter.Card card = mCards[position];
        Doodle doodle = (Doodle) card.obj;
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.tabcontent,
                        DoodleDetailsFragment.newInstance(doodle,
                                (int) parent.getX(), (int) parent.getY(),
                                parent.getWidth(), parent.getHeight())
                        , doodle.title
                )
                .addToBackStack("detail").commit();
    }

    @Override
    public void onItemChildClick(View itemChildView, int position) {
        Toast.show("onItemChildClick");

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }

    private String doodlesJson;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!TextUtils.isEmpty(doodlesJson)) {
            outState.putString("data", doodlesJson);
            Log.anchor("year:" + mToday.year + ", monthOfYear:" + mToday.monthOfYear);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.anchor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doodlesJson = null;
        Log.anchor();
    }
}