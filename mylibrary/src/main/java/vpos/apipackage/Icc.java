package vpos.apipackage;


/**
 * @ClassName:  Icc
 * @Description: Icc驱动接口
 * @author:
 * @date:
 *
 */
public class Icc {
//	static ComService ComService = null;
	//javah -classpath "F:\3385\3385api_demo\bin\classes" vpos.apipackage.Icc
	static {
		System.loadLibrary("PosApi");
//		ComService = new ComService();
	}


	/**
	 * @Title: Lib_IccReset
	 * @Description: 复位IC卡片,并返回卡片的复位应答内容
	 * @param: @param slot
	 * 0x00－IC卡通道;
	 * 0x01－PSAM1卡通道;
	 * 0x02－PSAM2卡通道;
	 * @param: @param VCC_Mode
	 * 1---5V;
	 * 2---3V;
	 * 3---1.8V;
	 * @param: @param ATR	卡片复位应答.（至少需要32+1bytes的空间）其内容为长度(1字节)+复位应答内容
	 * @param: @return
	 * 0	初始化成功.
	 * (-2403)	通道号错误
	 * (-2405)	卡拔出或无卡
	 * (-2404)	协议错误
	 * (-2500)	IC卡复位的电压模式错误
	 * (-2503)	通信失败.
	 * @return: int
	 * @throws
	 */
	public static native int Lib_IccOpen(byte slot, byte vccMode, byte[] atr);

	/**
	 * @Title: Lib_IccCommand
	 * @Description: IC卡读写
	 * @param: @param slot 卡通道号
	 * 0x00－IC卡通道
	 * 0x01－PSAM1卡通道
	 * 0x02－PSAM2卡通道
	 * @param: @param ApduSend 发送给卡片的apdu
	 * @param: @param ApduResp 接收到卡片返回的apdu
	 * @param: @return
	 * 0	执行成功
	 *(-2503)	通信超时
	 *(-2405)	交易中卡被拨出
	 *(-2401)	奇偶错误
	 *(-2403)	选择通道错误
	 *(-2400)	发送数据太长（LC）
	 *(-2404)	卡片协议错误（不为T＝0或T＝1）
	 *(-2406)	没有复位卡片
	 * @return: int
	 * @throws
	 */
	public static native int Lib_IccCommand(byte slot, byte[] apduSend, byte[]  apduResp);

	/**
	 * @Title: Lib_IccClose
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param: @param slot 卡通道号
	 * 0x00－IC卡通道
	 * 0x01－PSAM1卡通道
	 * 0x02－PSAM2卡通道
	 * @param: @return
	 * 0	成功
	 * 非0	失败
	 * @return: int
	 * @throws
	 */
	public static native int Lib_IccClose(byte slot);

	/**
	 * @Title: Lib_IccCheck
	 * @Description: 检查指定的卡座内是否有卡。
	 * @param: @param slot 卡通道号
	 * 0x00－IC卡通道
	 * @param: @return
	 * 0	有卡插入
	 * 非0	没有卡插入
	 * @return: int
	 * @throws
	 */

	public static native int Lib_IccCheck(byte slot);
//	public static int Lib_IccCheck(byte slot){
//		if(ComService.getIccCheck())
//			return 0;
//		else {
//			return -2405;
//		}
//	}

	public static native int Lib_IccSelectEtu(byte mode);


	//////////////////////////////////////////////////////////
	public static native int Lib_SleInit4442(byte slot,byte[] atr);

	public static native int Lib_SleOpen4442(byte slot);

	public static native int Lib_SleClose4442(byte slot);

	public static native int Lib_SleReset4442(byte slot, byte[] outData);

	public static native int Lib_SleReadMem4442(byte slot, byte startAddress, int length, byte []outData);

	public static native int Lib_SleWriteMem4442(byte slot, byte startAddress, int length, byte []inData);

	public static native int Lib_SleReadProMem4442(byte slot, byte []outData);

	public static native int Lib_SleWriteProMem4442(byte slot, byte startAddress, int length, byte []outData);

	public static native int Lib_SleVerSecCode4442(byte slot, byte []secCode);

	public static native int Lib_SleChangeSecCode4442(byte slot, byte []secCode);

	public static native int Lib_SleReadErrorCount4442(byte slot);

	public static native int Lib_IccDetectSYN(byte slot);


	//////////////////////////////////////////////////////////
	public static native int Lib_SleInit4428(byte slot,byte[] cardType);

	public static native int Lib_SleOpen4428(byte slot);

	public static native int Lib_SleClose4428(byte slot);

	public static native int Lib_SleReset4428(byte slot, byte[] outData);

	public static native int Lib_SleReadWithoutPB4428(byte slot, byte addr, int length, byte []dataBuf);

	public static native int Lib_SleReadWithPB4428(byte slot, byte addr, int length, byte []dataBuf);

	public static native int Lib_SleReadPinCounter4428(byte slot, byte []counter);

	public static native int Lib_SleWritePinCounter4428(byte slot, byte counter);

	public static native int Lib_SleVerifyPin4428(byte slot, byte []pinBuf);

	public static native int Lib_SleWritePin4428(byte slot, byte []pinBuf);

	public static native int Lib_SleWriteWithoutPB4428(byte slot, byte addr, int length, byte []dataBuf);

	public static native int Lib_SleWriteWithPB4428(byte slot, byte addr, int length, byte []dataBuf);

	public static native int Lib_SleWritePB4428(byte slot, byte addr, int length, byte []dataBuf);

	public static native int Lib_IccDetect_4428(byte slot);


}