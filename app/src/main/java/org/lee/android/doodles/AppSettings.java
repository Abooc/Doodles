package org.lee.android.doodles;

/**
 * 应用设置管理器，缓存设置属性值
 */
public class AppSettings {
	private static AppSettings instance = null;
	private boolean mOpenDev = false;

	public static synchronized AppSettings getInstance() {
		if (instance == null) {
			instance = new AppSettings();
		}
		return instance;
	}

	/**
	 * 开启/关闭开发者模式
	 * @param open true开启开发者模式，false关闭
	 */
	public void setOpenDev(boolean open){
		mOpenDev = open;
	}

	/**
	 * 是否开启开发者模式
	 * @return true已开启，false已关闭
	 */
	public boolean isOpenDev(){
		return mOpenDev;
	}




}
