package org.lee.android.doodles.adview;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.DatePicker;

import org.lee.android.doodles.AboutUsActivity;
import org.lee.android.doodles.AppSettings;
import org.lee.android.doodles.R;
import org.lee.android.doodles.settings.PreferencesKeys;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;


/**
 * 设置页面
 *
 * @author ruiyuLee
 */
@TargetApi(11)
public class PropertiesFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, PreferencesKeys {

    public static void launch(Context context) {
        Intent intent = new Intent(context, PropertiesFragment.class);
        context.startActivity(intent);
    }

    private SharedPreferences mPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.adview_properties);


        boolean b = getPreferenceManager().getSharedPreferences().getBoolean("all_gender", true);
        getPreferenceManager().findPreference("gender").setEnabled(!b);
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
        if (key.equals("all_gender")) {
            boolean b = sharedPreferences.getBoolean(key, true);
            getPreferenceManager().findPreference("gender").setEnabled(!b);
            return;
        }
        if (key.equals("gender")) {
            String b = sharedPreferences.getString(key, null);
            Log.anchor(b);
            return;
        }
        if (key.equals("birthday")) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), onDateSetListener, 2015, 05, 20);
            datePickerDialog.show();
            return;
        }
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Preference preference = getPreferenceManager().findPreference("birthday");
            preference.setSummary("生日：" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };

}
