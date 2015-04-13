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
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.lee.android.doodles.ApiClient;
import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.DoodlePackage;
import org.lee.android.doodles.volley.HttpHandler;
import org.lee.android.test.data.DataGeter;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;

/**
 * 搜索Doodles页面
 */
public class SearchFragment extends Fragment implements
        RecyclerItemViewHolder.OnRecyclerItemChildClickListener {

    public static SearchFragment newInstance(String q) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("q", q);
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, null, false);
    }

    private String mQ;
    private View progressContainer;
    private View listContainer;
    private TextView internalEmpty;
    private RecyclerView recyclerView;
    private DoodlePackage mDoodlePkg;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressContainer = view.findViewById(R.id.progressContainer);
        listContainer = view.findViewById(R.id.listContainer);
        internalEmpty = (TextView) view.findViewById(R.id.internalEmpty);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        Bundle args = getArguments();
        if (args != null) {
            mQ = args.getString("q");
        }
        if (mDoodlePkg != null) {
            initRecyclerView(recyclerView, mDoodlePkg.doodles);
        }

    }

    private void initRecyclerView(RecyclerView recyclerView, Doodle[] doodles) {
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(
                mActivity, doodles, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle("搜索\"" + mQ + "\"");


        if (mDoodlePkg == null) {
            ApiClient apiClient = new ApiClient();
            mDoodlePkg = DataGeter.getSearchDoodles();
            initRecyclerView(recyclerView, mDoodlePkg.doodles);

            if (true)
                return;

            apiClient.searchDoodles(mQ, 1, new HttpHandler<DoodlePackage>() {
                @Override
                public void onStart() {
                    progressContainer.setVisibility(View.VISIBLE);
                    listContainer.setVisibility(View.GONE);
                }

                @Override
                public void onFinish() {
                    progressContainer.setVisibility(View.GONE);
                    listContainer.setVisibility(View.VISIBLE);

                }

                @Override
                public void onSuccess(int i, Header[] headers, String s, DoodlePackage doodlePkg) {
                    if (doodlePkg != null) {
                        Log.anchor(doodlePkg.results_number);
                        initRecyclerView(recyclerView, doodlePkg.doodles);
                    } else {
                        onFailure(0, "没有搜到匹配内容");
                    }
                }

                @Override
                public void onFailure(int statusCode, String error) {
                    Log.anchor("statusCode:" + statusCode + ", " + error);
                    internalEmpty.setText(error);

                }
            });

        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(View parent, int position) {
        Toast.show("onItemClick");
        Doodle doodle = mDoodlePkg.doodles[position];
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content,
                        DoodleDetailsFragment.newInstance(doodle,
                                (int) parent.getX(), (int) parent.getY(),
                                parent.getWidth(), parent.getHeight())
                )
                .addToBackStack("detail")
                .commit();
    }

    @Override
    public void onItemChildClick(View itemChildView, int position) {

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.anchor(outState.toString());
        String json = new Gson().toJson(mDoodlePkg);
        outState.putString("data", json);
    }
}