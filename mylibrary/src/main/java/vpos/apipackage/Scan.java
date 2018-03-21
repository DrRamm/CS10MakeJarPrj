package vpos.apipackage;


public class Scan {
	static {
		System.loadLibrary("PosApi");
	}
	public static native int Lib_ScanOpen();
	public static native int Lib_ScanClose();
	public static native int Lib_ScanRead(short waitTimeSec , String [] data);

}