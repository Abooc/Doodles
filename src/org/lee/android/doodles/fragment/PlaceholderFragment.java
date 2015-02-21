package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.FileUtils;
import org.lee.android.doodles.volley.HttpHandler;
import org.lee.android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mLabelText;
    private ListView mListView;

    public PlaceholderFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int SECTION = getArguments().getInt(ARG_SECTION_NUMBER);
        mLabelText = (TextView) view.findViewById(R.id.section_label);
        mListView = (ListView) view.findViewById(android.R.id.list);

        new ApiClient().request();

//        Tester.JsonObj();
    }

    class ApiClient {

                String url = "http://www.woolom.com";
//        String url = "https://www.google.com/doodles/json/2015/2?hl=zh_CN";

        void request() {
            ad();
//            getList();
        }


        void getList(){
            httpClient.get(url, null, new HttpHandler<JsonElement>() {
                @Override
                public void onStart() {
                    Log.anchor();
                    mLabelText.setText("start...");
                }

                @Override
                public void onFinish() {
                    Log.anchor();
                }

                @Override
                public void onFailure(int statusCode, String error) {

                    mLabelText.setText(error);
                }

                @Override
                public void onSuccess(int i, Header[] headers, String s, JsonElement jsonElement) {



                    mLabelText.setText(s.toString());
                }
            });
        }

        AsyncHttpClient httpClient = new AsyncHttpClient();

        void ad() {
            httpClient.get(url, null, new TextHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.anchor();
                }

                @Override
                public void onFinish() {
                    Log.anchor();
                    String s = "";
                    try {
                        InputStream inputStream = getActivity().getAssets().open("data/doodles.json");
                        s = FileUtils.readInStream(inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Gson gson = new Gson();
                    Doodle[] doodles = gson.fromJson(s, Doodle[].class);

                    Log.anchor(doodles == null ? "null" : doodles.length);
                    setAdapter(doodles);
                }

                @Override
                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                    mLabelText.setText(throwable.toString());

                }

                @Override
                public void onSuccess(int i, Header[] headers, String s) {

                }
            });
        }
    }

    private void setAdapter(Doodle[] doodles){
        DoodleAdapter adapter = new DoodleAdapter(getActivity(), 0, doodles);
        mListView.setAdapter(adapter);
    }

}