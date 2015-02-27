package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.lee.android.doodles.ApiClient;
import org.lee.android.doodles.MessageView;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.WebViewActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.util.Log;

/**
 * 浏览涂鸦列表
 *
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(ARG_SECTION_NUMBER));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doodles_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        Log.anchor("year:" + year + ", month:" + month);
        mMessageView = (MessageView) view.findViewById(R.id.MessageView);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);

        mApiClient.requestDoodles(year, month, callbacks);
    }

    /**
     * Doodles列表每一项点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DoodleAdapter doodleAdapter = (DoodleAdapter) parent.getAdapter();
        Doodle doodle = (Doodle) doodleAdapter.getItem(position);
        WebViewActivity.launch(getActivity(), ApiClient.GOOGLE_DOODLES_ROOT + doodle.name, null);
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

//            try {
//                InputStream inputStream = getActivity().getAssets().open("data/doodles.json");
//                String sources = FileUtils.readInStream(inputStream);
//                attachData(sources);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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
//            try {
//                InputStream inputStream = getActivity().getAssets().open("data/doodles.json");
//                sources = FileUtils.readInStream(inputStream);
//                attachData(sources);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onSuccess(int i, Header[] headers, String sources) {
            mMessageView.setVisibility(View.GONE);
            attachData(sources);
        }
    };

    /**
     * 将Doodles列表的Json数据解析并显示到ListView
     * @param doodlesJson
     */
    private void attachData(String doodlesJson) {
        Gson gson = new Gson();
        Doodle[] doodles = gson.fromJson(doodlesJson, Doodle[].class);
        DoodleAdapter adapter = new DoodleAdapter(getActivity(), 0, doodles);
        mListView.setAdapter(adapter);
    }

}