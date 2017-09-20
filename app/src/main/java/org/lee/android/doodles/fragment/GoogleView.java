package org.lee.android.doodles.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GoogleView extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WebView webView = new WebView(getActivity());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.getSettings()
        .setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://cn.bing.com/search?q=" + getArguments().getString("q"));
        return webView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}