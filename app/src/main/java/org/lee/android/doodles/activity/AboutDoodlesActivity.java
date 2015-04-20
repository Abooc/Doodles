package org.lee.android.doodles.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.lee.android.doodles.R;

/**
 * 关于Google Doodle页面
 * Created by dayu on 14-11-17.
 */
public class AboutDoodlesActivity extends ActionBarActivity implements View.OnClickListener {

    private WebView iWebView;
    private final String AboutGoogleDoodlesUrl = "file:///android_asset/html/AboutGoogleDoodles.html";

    public static void launch(Context context, String url){
        Intent intent = new Intent(context, AboutDoodlesActivity.class);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        String url = AboutGoogleDoodlesUrl;
//        if (getIntent().getData() != null){
//            url = getIntent().getData().toString();
//        }

        iWebView = new WebView(this);
        setContentView(iWebView);

        iWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        iWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = iWebView.getSettings();
        /**(见@1)WebView加载某些js代码时，WebView会找不到js中的localStorage属性，例如localStorage.index */
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        iWebView.setWebChromeClient(iWebChromeClient);
        iWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadUrl(AboutGoogleDoodlesUrl);
            }
        });

        iWebView.loadUrl(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        iWebView.reload();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        iWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        iWebView.onPause();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private WebChromeClient iWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setProgress(newProgress * 100);

        }
    };

    @Override
    public void onDestroy() {
        destoryWebView();
        super.onDestroy();
    }

    private void destoryWebView() {
        iWebView.stopLoading();
        iWebView.clearHistory();
        iWebView.clearCache(true);
        iWebView.destroy();
    }

}
