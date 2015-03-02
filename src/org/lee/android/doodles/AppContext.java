package org.lee.android.doodles;

import android.content.Context;

public class AppContext {
	private static AppContext instance = null;
	private Context context = null;
	private static boolean running = false;

	public static synchronized AppContext getInstance() {
		if (instance == null) {
			instance = new AppContext();
			running = true;
		}
		return instance;
	}

	public boolean isRunning() {
		return running;
	}

	public void destroy() {
		running = false;
	}

	public static Context getContext() {
		return getInstance().context;
	}

	public void setContext(Context context) {
		running = true;
		if (context != null && this.context != context) {
			this.context = context;
			initApp();
		}
	}

	private void initApp() {

	}

}
