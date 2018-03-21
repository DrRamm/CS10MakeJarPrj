package vpos.apipackage;



public class IDCard {
	static {
		System.loadLibrary("PosApi");
	}
	
	public static native int Lib_IDCardOpen();
	public static native int Lib_IDCardClose();
	public static native int Lib_IDCardRead(String[] idCardInfo,byte[] img);
	public static native int Lib_IDCardRead2(String[] idCardInfo);
}