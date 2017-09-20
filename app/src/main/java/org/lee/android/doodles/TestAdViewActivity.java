package org.lee.android.doodles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.lee.android.util.Log;

import java.text.DateFormat;
import java.util.Date;


/**
 *
 * @author ruiyuLee
 */
public class TestAdViewActivity extends ActionBarActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, TestAdViewActivity.class);
        context.startActivity(intent);
    }

    private AdView mAdView;
    private TextView mDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doodle_list_item_adview);

        mDateText = (TextView) findViewById(R.id.DateTime);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarMenu);
        toolbar.inflateMenu(R.menu.adview_menu);

        mAdView = (AdView)findViewById(R.id.adView);
//        mAdView.setAdSize(AdSize.BANNER);
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice(DefaultBuild.MEDIUM_RECTANGLE_AD_UNIT_ID)
                .build();
        mAdView.setAdListener(mAdListener);
        mAdView.loadAd(adRequest);
    }
    AdRequest adRequest;

    private AdListener mAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            DateFormat format = DateFormat.getDateTimeInstance();
            mDateText.setText(format.format(new Date()));


            resumeWebView(mAdView);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
            pauseWebView(mAdView);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mAdView != null) {
            Log.anchor();
            mAdView.resume();
            resumeWebView(mAdView);
        }
        super.onResume();
    }

    void resumeWebView(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);
            if (child instanceof WebView) {
                WebView webView = ((WebView) child);
                webView.resumeTimers();

                webView.setOnTouchListener(touch);
                webView.setWebViewClient(client);
                Log.anchor();
            }
            try {
                resumeWebView((ViewGroup) child);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP)
                if (v instanceof WebView) {

                    WebView webView = ((WebView) v);
                    Log.anchor(webView.getUrl());
                    Log.anchor(adRequest.getContentUrl());

                }
            return false;
        }
    };

    WebViewClient client = new WebViewClient() {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            Log.anchor();
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.anchor();
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    void pauseWebView(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);
            if (child instanceof WebView) {
                ((WebView) child).pauseTimers();
            }
            try {
                pauseWebView((ViewGroup) child);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
