package org.lee.android.doodles.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.util.Log;

/**
 */
public class TodayFragment extends Fragment {

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onShowFragment(this);
        ActionBar actionBar = activity.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        return rootView;
    }

    private final String AboutGoogleDoodlesUrl = "file:///android_asset/html/AboutGoogleDoodles.html";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mWebView = (WebView) view.findViewById(R.id.WebView);
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = mWebView.getSettings();
        /**(见@1)WebView加载某些js代码时，WebView会找不到js中的localStorage属性，例如localStorage.index */
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        mWebView.setWebChromeClient(iWebChromeClient);
        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.loadUrl("https://www.google.com/doodles");
        mWebView.loadUrl(AboutGoogleDoodlesUrl);


        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.ViewPager);

        PagerFragment.IFragmentPagerAdapter adapter = new PagerFragment.IFragmentPagerAdapter(
                getActivity().getSupportFragmentManager());
        adapter.setYear("2016");
        adapter.setCount(0);
        mViewPager.setAdapter(adapter);
        adapter.setCount(5);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onShowFragment(this);
        Log.anchor();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.anchor();
    }


    private WebChromeClient iWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        Log.anchor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.anchor();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.anchor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.anchor();
    }
}