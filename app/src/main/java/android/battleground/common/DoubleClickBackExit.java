package android.battleground.common;

/**
 *
 * @author allnet@live.cn
 */
public class DoubleClickBackExit {

	private static long intervalTime = 0;

	public static boolean onBackPressed() {
		if ((System.currentTimeMillis() - intervalTime) > 1000) {
			intervalTime = System.currentTimeMillis();
			return false;
		} else {
			return true;
		}
	}
}