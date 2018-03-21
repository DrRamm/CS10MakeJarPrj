package vpos.apipackage;


/**   
 * @ClassName:  Mcr   
 * @Description:  磁卡驱动接口
 * @author: 
 * @date:  
 *      
 */  
public class Mcr {
	static {
		System.loadLibrary("PosApi");
	}
	
	/**   
	 * @Title: Lib_McrOpen   
	 * @Description: 打开磁卡阅读器 
	 * @param: @return 
	 * 0	成功
	 * 非0	失败     
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_McrOpen();
	/**   
	 * @Title: Lib_McrClose   
	 * @Description: 关闭磁卡阅读器   
	 * @param: @return
	 * 0	成功
	 * 非0	失败     
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_McrClose();
	/**   
	 * @Title: Lib_McrReset   
	 * @Description: 复位磁头，并清除磁卡缓冲区数据  
	 * @param: @return      
	 * 0	成功
	 * 非0	失败     
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_McrReset();
	/**   
	 * @Title: Lib_McrCheck   
	 * @Description: 检查是否刷过卡   
	 * @param: @return      
	 * 0	成功
	 * 非0	失败     
	 * @return: int      
	 * @throws   
	 */  
	public static native int Lib_McrCheck();

	/**   
	 * @Title: Lib_McrRead   
	 * @Description: 读取磁卡缓冲区的1、2、3磁道的数据   
	 * @param: @param keyNo DES密钥索引号，取值范围0~4，必须与认证过程中所使用的AUTHDESK的密钥索引号相同
	 * @param: @param mode 模式
	 * 0：不加密
	 * 1：加密
	 * @param: @param track1 存放1磁道数据的指针 [应用层缓冲区要设为256]
	 * @param: @param track2 存放2磁道数据的指针 [应用层缓冲区要设为256]
	 * @param: @param track3 存放3磁道数据的指针 [应用层缓冲区要设为256]
	 * @param: @return  
	 * 0	刷卡错误
	 * 其它值
	 *（＞0）	bit0 = 1  正确读出 1磁道数据
	 * bit1 = 1  正确读出2磁道数据
	 * bit2 = 1  正确读出3磁道数据
	 * bit4 = 1  1磁道数据有校验错
	 * bit5 = 1  2磁道数据有校验错
	 * bit6 = 1  3磁道数据有校验错    
	 * @return: int      
	 * @throws   
	 */  

	public static native int Lib_McrRead(byte keyNo, byte mode, byte[] track1, byte[] track2, byte[] track3);
}