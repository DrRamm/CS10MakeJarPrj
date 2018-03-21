package vpos.apipackage;

import android.content.Context;


/**   
 * @ClassName:  Pci   
 * @Description:密码键盘应用接口  
 * @author: 
 * @date:   
 */  
public class Pci {

	static {
		System.loadLibrary("PosApi");
	}
 
	public static native int  Lib_PciGetRnd (byte []rnd);
	/**   
	 * @Title: Lib_PciWritePinMKey   
	 * @Description: 写PIN主密钥   
	 * @param: @param keyNo 密钥序号,取值范围0~9，其他值非法。
	 * @param: @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法。
	 * @param: @param keyData 密钥数据
	 * @param: @return    
	 * (0)	写入成功
	 * (-7000)	密码键盘已锁
	 * (-7001)	密钥类型错误
	 * (-7002)	密钥校验值错
	 * (-7003)	密钥索引非法
	 * (-7004)	密钥长度非法
	 * (-7005)	模式错误
	 * (-7011)	密钥不存在 
	 * (-7012)	写密钥失败  
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciWritePIN_MKey(byte keyNo, byte keyLen, byte[] keyData, byte mode);
	/**   
	 * @Title: Lib_PciWriteMacMKey   
	 * @Description: 写MAC主密钥  
	 * @param: @param keyNo 密钥序号,取值范围0~9，其他值非法。
	 * @param: @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法。
	 * @param: @param keyData 密钥数据
	 * @param: @return  
	 * (0)	写入成功
	 * (-7000)	密码键盘已锁
	 * (-7001)	密钥类型错误
	 * (-7002)	密钥校验值错
	 * (-7003)	密钥索引非法
	 * (-7004)	密钥长度非法
	 * (-7005)	模式错误
	 * (-7011)	密钥不存在 
	 * (-7012)	写密钥失败    
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciWriteMAC_MKey(byte keyNo, byte keyLen, byte[] keyData, byte mode);
	/**   
	 * @Title: Lib_PciWriteDesMKey   
	 * @Description: 写DES主密钥   
	 * @param: @param keyNo 密钥序号,取值范围0~9，其他值非法。
	 * @param: @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法。
	 * @param: @param keyData 密钥数据
	 * @param: @return
	 * (0)	写入成功
	 * (-7000)	密码键盘已锁
	 * (-7001)	密钥类型错误
	 * (-7002)	密钥校验值错
	 * (-7003)	密钥索引非法
	 * (-7004)	密钥长度非法
	 * (-7005)	模式错误
	 * (-7011)	密钥不存在 
	 * (-7012)	写密钥失败      
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciWriteDES_MKey(byte keyNo, byte keyLen, byte[] keyData, byte mode);
	/**   
	 * @Title: Lib_PciWritePinKey   
	 * @Description: 写PIN专用密钥   
	 * @param: @param keyNo PIN密钥序号，取值范围0~9，其他值非法
	 * @param: @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法
	 * @param: @param keyData 密钥数据
	 * @param: @param mode 
	 * 0：使用PIN_MK解密PINK
	 * 1：使用PIN_MK加密PINK
	 * @param: @param mkeyNo 主密钥序号，取值范围0~9，其他值非法
	 * @param: @return   同上
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciWritePinKey(byte keyNo, byte keyLen, byte[] keyData, byte mode, byte mkeyNo);
	/**   
	 * @Title: Lib_PciWriteMacKey   
	 * @Description: 写MAC专用密钥   
	 * @param: @param keyNo MAC密钥序号，取值范围0~9，其他值非法
	 * @param: @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法
	 * @param: @param keyData 密钥数据
	 * @param: @param mode 
	 * 0：使用MAC_MK解密MACK
	 * 1：使用MAC_MK加密MACK
	 * @param: @param mkeyNo 主密钥序号，取值范围0~9，其他值非法
	 * @param: @return    同上  
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciWriteMacKey(byte keyNo, byte keyLen, byte[] keyData, byte mode, byte mkeyNo);
	/**   
	 * @Title: Lib_PciWriteDesKey   
	 * @Description: 写DES专用密钥   
	 * @param: @param keyNo DES密钥序号，取值范围0~9，其他值非法
	 * @param: @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法
	 * @param: @param keyData 密钥数据
	 * @param: @param mode 
	 * 0：使用DES_MK解密DESK
	 * 1：使用DES_MK加密DESK
	 * @param: @param mkeyNo 主密钥序号，取值范围0~9，其他值非法
	 * @param: @return    同上     
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciWriteDesKey(byte keyNo, byte keyLen, byte[] keyData, byte mode, byte mkeyNo);
	/**   
	 * @Title: Lib_PciGetPin   
	 * @Description: 获取加密的PIN－BLOCK  
	 * @param: @param keyNo PIN密钥索引号，取值范围0~9，必须与认证过程中所使用的AUTHPINK的密钥索引号相同
	 * @param: @param minLen 指示输入数据的最小长度，取值范围，4~12
	 * @param: @param maxLen 指示输入数据的最小长度，取值范围，4~12
	 * @param: @param mode 加密模式：[该参数未使用，目前仅支持X9.8]
	 * 0=X9.8
	 * 1=X3.92
	 * @param: @param waitTimeSec 超时等待时间，表示等待若干秒如果用户没有按按键，则超时退出。单位（秒），如果取值0则表示使用默认值120秒
	 * @param: @param cardNo 指示相应的卡片或交易序号数
	 * @param: @param mark 金额信息显示标志（0x00：无；0x01：有）
	 * @param: @param iAmount 金额:
	 * 举例：(123456.00) 如果金额长度小于14，则在后面补0.
	 * @param: @param pinBlock 返回加密的PIN块
	 * @param: @return  
	 * (0)	成功：
	 * (-7000)	密码键盘已锁
	 * (-7001)	密钥类型错误
	 * (-7002)	密钥校验值错
	 * (-7003)	密钥索引非法
	 * (-7004)	密钥长度非法
	 * (-7006)	输入可变长度非法
	 * (-7007)	取消输入
	 * (-7008)	密钥输入两次不匹配
	 * (-7009)	输入超时
	 * (-7010)	输入间隔小于30秒
	 * (-7011)	密钥不存在
	 * (-7015)	输入数据长度错误
	 * (-7016)	无输入   
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciGetPin(byte keyNo, byte minLen, byte maxLen, byte mode, byte[] cardNo, byte[] pinBlock, byte[] pinPasswd,byte pin_len, byte mark, byte[] iAmount, byte waitTimeSec, Context ctx);
	/**   
	 * @Title: Lib_PciGetMac   
	 * @Description: 获取加密的MAC报文
	 * @param: @param keyNo 指定MAC密钥序号，取值范围0~9，其他值非法
	 * @param: @param mode
	 * 0＝算法1
	 * 1＝算法2
	 * 2＝算法3
	 * @param: @param inData 计数MAC报文数据内容
	 * @param: @param macOut 返回MAC报文加密结果
	 * @param: @return  
	 * (0)	成功：
	 * (-7000)	密码键盘已锁
	 * (-7001)	密钥类型错误
	 * (-7002)	密钥校验值错	
	 * (-7003)	密钥索引非法
	 * (-7004)	密钥长度非法
	 * (-7005)	输入模式非法
	 * (-7011)	密钥不存在
	 * (-7015)	输入数据长度错误    
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciGetMac(byte keyNo, short inLen, byte[] inData, byte[] macOut, byte mode);
	/**   
	 * @Title: Lib_PciGetDes   
	 * @Description: 使用des key对数据进行加密/解密   
	 * @param: @param keyNo DES密钥序号，取值范围0~9，其他值非法
	 * @param: @param mode
	 * @param: @param inData 要加密/解密的数据
	 * @param: @param outData 加密/解密结果
	 * @param: @return  
	 * (0)	成功：
	 * (-7000)	密码键盘已锁
	 * (-7001)	密钥类型错误
	 * (-7002)	密钥校验值错	
	 * (-7003)	密钥索引非法
	 * (-7004)	密钥长度非法
	 * (-7005)	输入模式非法
	 * (-7011)	密钥不存在
	 * (-7015)	输入数据长度错误    
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_PciGetDes(byte keyNo, short inLen, byte[] inData, byte[] DesOut, byte mode);
	/**   
	 * @Title: Lib_PciOffLineEncPin   
	 * @Description: 脱机密文PIN验证   
	 * @param: @param slot Icc卡槽号
	 * @param: @param minlen 指示输入数据的最小长度，取值范围，4~12
	 * @param: @param maxlen 指示输入数据的最大长度，取值范围，4~12
	 * @param: @param waittime_sec 等待时间，单位（秒），取值范围为0-６0，
	 * waittime_sec为0 时表示默认值６0秒；
	 * 当取值大于６0时，为６0秒
	 * @param: @param pk 
	 * typedef struct{
	 * 	unsigned char ModulLen;          //Module length
	 * 	unsigned char Modul[248];        //Module
	 * 	unsignwed char ExponentLen;       //Exponent length
	 * 	unsigned char Exponent[3];       //Exponent
	 * }PUBLIC_KEY;
	 * 字节0是modul长度，中间248是module，字节249是Exponent length 最后3 字节是Exponent
	 * @param: @return   
	 * (0)	校验成功 
	 * (-7006)	输入min或max无效
	 * (-7007)	取消PIN输入
	 * (-7009)	输入超时
	 * (-7016)	无输入   
	 * @return: int      
	 * @throws   
	 */  
	public static native int  Lib_PciOffLineEncPin(byte slot, byte minLen, byte maxLen, short waittimeSec, byte []pk);
	/**   
	 * @Title: Lib_PciOffLinePlainPin   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param: @param slot Icc卡槽号
	 * @param: @param minlen 指示输入数据的最小长度，取值范围，4~12
	 * @param: @param maxlen 指示输入数据的最小长度，取值范围，4~12
	 * @param: @param waittime_sec 等待时间，单位（秒），取值范围为0-６0，
	 * waittime_sec为0 时表示默认值６0秒；
	 * 当取值大于６0时，为６0秒
	 * @param: @return  
	 * (0)	校验成功
	 * (-7006)	输入min或max无效
	 * (-7007)	取消PIN输入
	 * (-7009)	输入超时
	 * (-7016)	无输入    
	 * @return: int      
	 * @throws   
	 */  
	public static native int  Lib_PciOffLinePlainPin(byte slot, byte minLen, byte maxLen, short waittimeSec);
}