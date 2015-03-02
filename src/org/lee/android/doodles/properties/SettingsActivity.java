package org.lee.android.doodles.properties;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import org.lee.android.doodles.R;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;


/**
 * 设置页面
 *
 * @author ruiyuLee
 */
public class SettingsActivity extends PreferenceActivity implements PreferencesKeys {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.settings);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.anchor();
        mPreferences.registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreferences.unregisterOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.anchor();
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private OnSharedPreferenceChangeListener mListener = new OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
            Log.anchor(key);
            if (key.equals(pref_key_clear_cache)) {
                Toast.show("清除缓存");
                return;
            }
            if (key.equals(pref_key_about_us)) {
                AboutUsActivity.launch(getBaseContext());
                return;
            }
        }
    };
}
