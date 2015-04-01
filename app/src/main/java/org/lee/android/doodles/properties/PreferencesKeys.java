package org.lee.android.doodles.properties;

import android.content.res.Resources;

import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.R;


public interface PreferencesKeys {

	Resources res = AppContext.getContext().getResources();

    final String pref_key_open_devmode = res.getString(R.string.pre_key_open_devmode);

    final String pref_key_clear_cache = res.getString(R.string.pre_key_clear_cache);
    final String pref_key_about_us = res.getString(R.string.pre_key_about_us);

    final String pref_key_feedback = res.getString(R.string.pre_key_feedback);
	final String pref_key_update = res.getString(R.string.pre_key_update);

}
