package org.lee.android.doodles.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.common.activities.SampleActivityBase;
import android.support.common.logger.Log;
import android.support.common.logger.LogFragment;
import android.support.common.logger.LogWrapper;
import android.support.common.logger.MessageOnlyLogFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewAnimator;

import org.lee.android.doodles.R;


/**
 * LoggerActivity
 * <p/>
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-2-22.
 */
public class LoggerActivity extends SampleActivityBase {

    public static final String TAG = "LoggerActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;


    private ViewAnimator output;

    @Override
    protected void onStart() {
        super.onStart();

        output = (ViewAnimator) findViewById(R.id.sample_output);
        output.setOnClickListener(ToggleLoggerView);
        output.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        output.findViewById(R.id.SampleMessage).setOnClickListener(ToggleLoggerView);
        output.findViewById(R.id.log_fragment).setOnClickListener(ToggleLoggerView);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.findItem(R.id.menu_toggle_log) != null) {
            MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
            logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
            logToggle.setTitle(findViewById(R.id.sample_output).getVisibility() ==
                    View.VISIBLE ? R.string.CloseDevMode : R.string.OpenDevMode);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toggle_log:
                if(output.getVisibility() == View.GONE){
                    output.setVisibility(View.VISIBLE);
                }else{
                    output.setVisibility(View.GONE);
                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener ToggleLoggerView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            org.lee.android.util.Log.anchor();
            ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
            mLogShown = !mLogShown;
            if (mLogShown) {
                output.setDisplayedChild(1);
            } else {
                output.setDisplayedChild(0);
            }
        }
    };

    /**
     * Create a chain of targets that will receive log data
     */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}