package vpos.util;

import android.util.Log;

public class Util {
	
	public static void sleepMs(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.e("SleepMs", "SleepMs fail : " + e.getMessage().toString());
		}
	}
}
