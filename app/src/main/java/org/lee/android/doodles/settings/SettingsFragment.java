package org.lee.android.doodles.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import org.lee.android.doodles.AboutUsActivity;
import org.lee.android.doodles.AppSettings;
import org.lee.android.doodles.R;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;


/**
 * 设置页面
 *
 * @author ruiyuLee
 */
@TargetApi(11)
public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, PreferencesKeys {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingsFragment.class);
        context.startActivity(intent);
    }

    private SharedPreferences mPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }


    @Override
    public void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.anchor(key);
        if (key.equals(pref_key_clear_cache)) {
            Toast.show("清除缓存");
            return;
        }
        if (key.equals(pref_key_open_devmode)) {
            boolean open = sharedPreferences.getBoolean(key, false);
            AppSettings.getInstance().setOpenDev(open);
            return;
        }
        if (key.equals(pref_key_about_us)) {
            AboutUsActivity.launch(getActivity());
            return;
        }
    }

}
