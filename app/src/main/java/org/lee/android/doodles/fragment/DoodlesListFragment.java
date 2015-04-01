package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.lee.android.doodles.ApiClient;
import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.MessageView;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.WebViewActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.FileUtils;
import org.lee.android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * 浏览涂鸦列表
 * <p/>
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-2-22.
 */
public class DoodlesListFragment extends Fragment implements AdapterView.OnItemClickListener {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DoodlesListFragment newInstance(Bundle args) {
        DoodlesListFragment fragment = new DoodlesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ListView mListView;
    private MessageView mMessageView;

    private ApiClient mApiClient = new ApiClient();

    public DoodlesListFragment() {
    }

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

    }

    private int year;
    private int month;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            doodlesJson = savedInstanceState.getString("data");
        }
        year = getArguments().getInt("year");
        month = getArguments().getInt("month");
        Log.anchor("year:" + year + ", month:" + month);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doodles_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mMessageView = (MessageView) view.findViewById(R.id.MessageView);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(mMessageView);
        mListView.setOnItemClickListener(this);

        if (TextUtils.isEmpty(doodlesJson)) {
//            mApiClient.requestDoodles(year, month, callbacks);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    useTest();
                }
            }, 1 * 1000);
        } else {
            Log.anchor("year:" + year + ", month:" + month + ", from savedInstanceState");
            attachData(doodlesJson);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.anchor("year:" + year + ", month:" + month);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.anchor("year:" + year + ", month:" + month);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.anchor("year:" + year + ", month:" + month);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.anchor("year:" + year + ", month:" + month);
    }


    /**
     * Doodles列表每一项点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DoodleAdapter doodleAdapter = (DoodleAdapter) parent.getAdapter();
        Doodle doodle = (Doodle) doodleAdapter.getItem(position);
//        WebViewActivity.launch(getActivity(), ApiClient.GOOGLE_DOODLES_ROOT + doodle.name, null);

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


    /**
     * 请求Doodles列表回调
     */
    private TextHttpResponseHandler callbacks = new TextHttpResponseHandler() {
        @Override
        public void onStart() {
            Log.anchor();
            mMessageView.setVisibility(View.VISIBLE);
            mListView.setAdapter(null);
            mMessageView.loading();

        }

        @Override
        public void onFinish() {
            Log.anchor();
            mMessageView.stop();
        }

        @Override
        public void onFailure(int i, Header[] headers, String sources, Throwable throwable) {
            mMessageView.setMessage(throwable.getMessage());
            mMessageView.setRetryEnable(true);
        }

        @Override
        public void onSuccess(int i, Header[] headers, String sources) {
            mMessageView.setVisibility(View.GONE);
            attachData(sources);
        }
    };

    /**
     * 将Doodles列表的Json数据解析并显示到ListView
     *
     * @param doodlesJson
     */
    private void attachData(String doodlesJson) {
        Gson gson = new Gson();
        Doodle[] doodles = gson.fromJson(doodlesJson, Doodle[].class);
        if (doodles == null || doodles.length == 0) return;
        DoodleAdapter adapter = new DoodleAdapter(mActivity, doodles);
        mListView.setAdapter(adapter);
        this.doodlesJson = doodlesJson;
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

    private String doodlesJson;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!TextUtils.isEmpty(doodlesJson)) {
            outState.putString("data", doodlesJson);
            Log.anchor("year:" + year + ", month:" + month);
        }
    }
}