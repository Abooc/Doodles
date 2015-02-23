package org.lee.android.doodles.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.lee.android.doodles.ApiClient;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.activity.WebViewActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.FileUtils;
import org.lee.android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class DoodlesListFragment extends Fragment implements AdapterView.OnItemClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DoodlesListFragment newInstance(int sectionNumber) {
        DoodlesListFragment fragment = new DoodlesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private ProgressBar mProgressBar;
    private TextView mLabelText;
    private ListView mListView;
    private ApiClient mApiClient = new ApiClient();

    public DoodlesListFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

        ActionBar actionBar = activity.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(activity,
                R.array.menu, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doodles_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int SECTION = getArguments().getInt(ARG_SECTION_NUMBER);
        mLabelText = (TextView) view.findViewById(R.id.section_label);
        mProgressBar = (ProgressBar) view.findViewById(R.id.ProgressBar);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
    }

    private ActionBar.OnNavigationListener mOnNavigationListener =
            new ActionBar.OnNavigationListener() {
        @Override
        public boolean onNavigationItemSelected(int position, long itemId) {
            mApiClient.requestDoodles(2015 - position, 2, callbacks);
            return true;
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DoodleAdapter doodleAdapter = (DoodleAdapter) parent.getAdapter();
        Doodle doodle = (Doodle) doodleAdapter.getItem(position);
        WebViewActivity.launch(getActivity(), doodle.alternate_url, null);
    }

    private TextHttpResponseHandler callbacks = new TextHttpResponseHandler() {
        @Override
        public void onStart() {
            Log.anchor();
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                InputStream inputStream = getActivity().getAssets().open("data/doodles.json");
                String sources = FileUtils.readInStream(inputStream);
                attachData(sources);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            Log.anchor();
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure(int i, Header[] headers, String sources, Throwable throwable) {
            mLabelText.setText(throwable.toString());
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
            attachData(sources);
        }
    };

    private void attachData(String doodlesJson){
        Gson gson = new Gson();
        Doodle[] doodles = gson.fromJson(doodlesJson, Doodle[].class);
        Log.anchor(doodles == null ? "null" : doodles.length);
        DoodleAdapter adapter = new DoodleAdapter(getActivity(), 0, doodles);
        mListView.setAdapter(adapter);
    }

}