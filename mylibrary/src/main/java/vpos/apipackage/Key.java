package vpos.apipackage;

public class Key {
	static {
		System.loadLibrary("PosApi");
	}
	
	public static native int Lib_KbFlush();
	public static native int Lib_KbCheck();
	public static native int Lib_KbGetKey();

}