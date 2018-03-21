package vpos.apipackage;

import android.content.Context;


/**
 * @ClassName:  Sys
 * @Description:TODO
 * @author:
 * @date:
 */
public class Sys {
	static {
		System.loadLibrary("PosApi");
	}

	public static native int Lib_PowerOn();
	public static native int Lib_PowerOff();

	/**
	 * @Title: Lib_Beep
	 * @Description: 蜂鸣器响
	 * @param: @param times [IN]次数
	 * @param: @return 
	 * 0	成功
	 * 非0	失败     
	 * @return: int
	 * @throws
	 */
	public static native int Lib_Beep();

	/**
	 * @Title: Lib_WriteSN
	 * @Description: 写序列号
	 * @param: @param SN [IN]16字节序列号
	 * @param: @return 
	 * 0	成功
	 * 非0	失败     
	 * @return: int
	 * @throws
	 */

	public static native int Print_Black();

	/**
	 * @Title: Lib_WriteSN
	 * @Description: 写序列号
	 * @param: @param SN [IN]16字节序列号
	 * @param: @return 
	 * 0	成功
	 * 非0	失败     
	 * @return: int
	 * @throws
	 */

	public static native int Print_Time();

	/**
	 * @Title: Lib_WriteSN
	 * @Description: 写序列号
	 * @param: @param SN [IN]16字节序列号
	 * @param: @return 
	 * 0	成功
	 * 非0	失败     
	 * @return: int
	 * @throws
	 */


	public static native int Lib_WriteSN (byte[] SN);

	public static native int Lib_SetLed (byte LEDn, byte mode);

	public static native int Lib_Des(byte[] input,byte[] output,byte[] deskey,int mode);

	/**
	 * @Title: Lib_ReadSN
	 * @Description: 读取序列号
	 * @param: @param SN [OUT]16字节序列号
	 * @param: @return 
	 * 0	成功
	 * 非0	失败     
	 * @return: int
	 * @throws
	 */
	public static native int Lib_ReadSN (byte[] SN);

	public static native int Lib_ReadChipID (byte[] buf, int len);

	/**
	 * @Title: Lib_GetTPMVersion
	 * @Description: 读取版本号，包括k21app 包括lib 库 包括 k21boot
	 * @param: @param v v[0~1]: 版本第一个byte是主版本号，第2个byte是次版本号，次版本号只有0~9（1位显示）如：1.0
	 *					v[2~3]:	lib 版本
	 *					v[4~5]: k21boot版本号 若为 0xff 0xff 表示无此版本号	
	 * @param: @return  
	 * 0	成功
	 * 非0	失败        
	 * @return: int
	 * @throws
	 */
	public static native int Lib_GetVersion(byte[] buf);

	public static native int Lib_GetTime(byte[] time);

	public static native int Lib_SetTime(byte[] time);

	public static native int Lib_LedCtrl(byte index, byte mode);

	public static native int Lib_KeyEvent();

	public static native int Lib_Test(byte subCmd);

	public static native void Lib_AppInit(Context cxt);


	public static native int Lib_SendBytes(byte[] buf, int len);

	public static native int Lib_RecvBytes(byte[] buf, int len, int timeout);

	public static native int Lib_SendPacket(byte[] buf, int len, byte mainCmd, byte subCmd);

	public static native int Lib_RecvPacket(byte[] buf, int len[], int timeout);

	public static native int Test_uarts();

	public static native int Lib_Update();
}