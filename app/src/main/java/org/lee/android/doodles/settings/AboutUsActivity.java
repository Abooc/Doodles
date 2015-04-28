package org.lee.android.doodles.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import org.lee.android.doodles.R;


/**
 * 设置/关于页面
 * 
 * @author ruiyuLee
 * 
 */
public class AboutUsActivity extends PreferenceActivity implements PreferencesKeys,
		OnSharedPreferenceChangeListener {

    public static void launch(Context context){
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(pref_key_feedback)) {
			return;
		}
		if (key.equals(pref_key_update)) {
			return;
		}
	}
}
