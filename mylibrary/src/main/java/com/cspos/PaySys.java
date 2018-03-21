package com.cspos;

import android.content.Context;

public class PaySys {
	static {
		System.loadLibrary("PosApi");
	}
	public static native int EmvParaInit();
	public static native int EmvContextInit(int index, int i);
	public static native int EmvSetTransAmount(int amount);
	public static native int EmvSetCardType(int cardtype) ; 
	public static native int EmvSetTransType(int TransTypeNum );
	public static native int EmvProcess(int KernelType, int FlowType);
	public static native int EmvPrePare55Field(byte[]  OutPut, int OutputBufSize);
	public static native int EmvGetTagData(byte[]  OutPut, int OutputBufSize, short tagname); 
	public static native int EmvSetOnlineResult(byte[] result, byte[] IsSuerRespData, int IsSuerRespDataLength);
	public static native int EmvFinal();
	public static native int EmvGetVersion(byte[] Output);
	public static native int EmvClearAllAIDS();
	public static native int EmvClearOneAIDS(byte[] Input, int InLen);
	public static native int EmvAddOneAIDS(byte[] Input, int InLen);
	public static native int EmvClearAllCapks();
	public static native int EmvClearOneCapks(byte[] Input, int InLen);
	public static native int EmvAddOneCAPK(byte[] Input, int InLen);
	public static native int EmvReadTermPar(byte[] Input, int InLen, byte[] OutPut, int OutBufSize) ;
	public static native int EmvSaveTermParas(byte[] Input, int InLen); 
	
	public static native  void postest();
	public static native int poskeypad(Context ctx);
	public static native int possetkeypad(int slot, int minLen, int maxLen, int waittimeSec,byte[] pkeyData,byte[] pkeyDatalen, String string);
//	public static native int PciGetPin(int slot, int minLen, int maxLen, int waittimeSec,byte[] keyData,byte[] keyDataLen, String tittle);
    public static native int Getpinblock(int pinkey_n, byte[] card_no, byte[] mode,  byte[] pin_block);
    public static native int CallKeyPad(byte[] password);

}
