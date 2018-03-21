package vpos.apipackage;

import android.R.integer;



public class Fingerprint {
	//javah -classpath "F:\Android\android_jni\bin\classes" vpos.apipackage.Fingerprint
	static {
		System.loadLibrary("PosApi");
	}
	public static native int Lib_FpOpen();
	public static native int Lib_FpClose();
	public static native int Lib_FpRegister();
	public static native int Lib_FpMatch();
	public static native int Lib_FpCode(byte[] code, int[] len);
	public static native int Lib_FpDeleteAll();
}