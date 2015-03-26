package org.lee.android.doodles.activity;

import android.support.common.activities.SampleActivityBase;
import android.support.common.logger.Log;
import android.support.common.logger.LogFragment;
import android.support.common.logger.LogWrapper;
import android.support.common.logger.MessageOnlyLogFilter;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.findItem(R.id.menu_toggle_log) != null) {
            MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
            logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
            logToggle.setTitle(mLogShown ? "hide log" : "show log");
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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