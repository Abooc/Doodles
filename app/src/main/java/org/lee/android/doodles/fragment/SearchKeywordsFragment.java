package org.lee.android.doodles.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.DoodlePackage;
import org.lee.android.test.FragmentLog;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;

/**
 * 搜索Doodles历史关键词页面
 */
public class SearchKeywordsFragment extends FragmentLog implements
        RecyclerItemViewHolder.OnRecyclerItemChildClickListener {

    public static SearchKeywordsFragment newInstance(String q) {
        SearchKeywordsFragment fragment = new SearchKeywordsFragment();
        Bundle args = new Bundle();
        args.putString("q", q);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_keywords, null, false);
    }

    private String mQ;
    private View listContainer;
    private TextView internalEmpty;
    private RecyclerView recyclerView;
    private DoodlePackage mDoodlePkg;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listContainer = view.findViewById(R.id.listContainer);
        internalEmpty = (TextView) view.findViewById(R.id.internalEmpty);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        mQ = getArguments().getString("q");

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String json = savedInstanceState.getString("data");
            mDoodlePkg = new Gson().fromJson(json, DoodlePackage.class);
            initRecyclerView(recyclerView, mDoodlePkg.doodles);
            Log.anchor(savedInstanceState.toString());
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, Doodle[] doodles) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        DoodleRecyclerAdapter recyclerAdapter = new DoodleRecyclerAdapter(
//                getActivity(), doodles, this, null);
//        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(View parent, int position) {
        Toast.show("onItemClick");
    }

    @Override
    public void onItemChildClick(View itemChildView, int position) {
        Toast.show("onItemChildClick");

    }

}