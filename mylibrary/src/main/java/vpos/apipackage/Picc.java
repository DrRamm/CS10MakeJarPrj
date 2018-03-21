package vpos.apipackage;


/**   
 * @ClassName:  Picc   
 * @Description:非接触式卡 
 * @author: 
 * @date:  
 *      
 */  
public class Picc {
	static {
		System.loadLibrary("PosApi");
	}
	

	/**   
	 * @Title: Lib_PiccOpen   
	 * @Description: 对非接触卡模块上电和复位，检查复位后模块初始状态是否正常  
	 * @param: @return  
	 * 0x00	成功
	 * 其他值	异常错误    
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccOpen();
	/**   
	 * @Title: Lib_PiccClose   
	 * @Description: 关闭非接触卡模块，使该模块处于关闭状态
	 * @param: @return 
	 * 0x00	成功
	 * 其他值	异常错误         
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccClose();
	/**   
	 * @Title: Lib_PiccCheck   
	 * @Description: 按指定的模式搜寻PICC卡片；搜到卡片后，将其选中和激活   
	 * @param: @param mode
	 * (一) ‘0’ ——先搜寻磁场中的所有A型卡：
	 * 1>搜到多张A型卡,返回多张卡错误；
	 * 2>没有搜到A型卡，再搜寻所有B型卡：
	 * 1．搜到多张B型卡，返回多张卡错误；
	 * 2．搜到1张B型卡，就激活此卡；
	 * 3．没有搜到B型卡，返回无卡；
	 * 3>搜到一张A型卡，再搜寻所以的B型卡:
	 * 1．搜到1张以上的B型卡，返回多卡错误；
	 * 2．没有搜到B型卡，就对此A型卡激活；
	 * (二) ‘a’或‘A’ 或0x0a---只搜寻A型卡一次；如果搜到多张A卡则只选中其中一张卡。（如果一张A型CPU卡和一张A型M1卡在感应区中，此时选中M1卡）
	 * (三) ‘b’或‘B’ 或0x0b --- 只搜寻B型卡一次；
	 * (四) ‘m’或‘M’ --- 只搜寻M1卡一次；如果搜到多张M1卡在感应区中则只选中其中一张卡。
	 * @param: @param cardType 卡片类型字节缓冲区；目前均返回二字节的类型值：
	 * CardType[0] ：
	 *		‘A’ —搜寻到A型卡
	 *		‘B’ —搜寻到B型卡
	 * CardType[1] ：
	 *		‘C’ —搜寻到CPU型卡
	 *		‘M’ —搜寻到M1型卡
	 * @param: @param serialNo 卡片序列号信息的缓冲区首址.
	 * 该信息依次包含了序列号长度和序列号内容等两项内容.
	 * B型卡和M1卡的序列号均为4字节;
	 * A型卡的序列号一般为4字节,也有7字节或10字节的.
	 * 采用字节SerialNo[0]指示序列号的长度,
	 * SerialNo[1~10]保存序列号(左对齐).
	 * 若需要读取序列号、且是A型卡,则要使用和判断长度字节.
	 * @param: @return  
	 * 0	成功
	 * (-3503)	参数错误(无效的Mode值)
	 * (-3502)	模块未开启
	 * (-3526)	冲突
	 * 其他值	操作失败    
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccCheck(byte mode, byte[] cardType, byte[] serialNo);
	/**   
	 * @Title: Lib_PiccCommand   
	 * @Description: 处理APDU卡片命令  
	 * @param: @param ApduSend 与接触式IC卡的应用数据读写操作参数框架基本相同
	 * @param: @param ApduResp 与接触式IC卡的应用数据读写操作参数框架基本相同
	 * @param: @return    
	 * 0	成功
	 * (-3503)	参数错误
	 * (-3502)	模块未开启
	 * (-3524)	数据交换错误  
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccCommand(byte[] apduSend, byte[] apduResp);

	/**
	 * @Description :Nfc
	 * @param NfcData_Len  NFC数据长度
	 * @param Technology  协议
	 * @param UID   返回的UID内容
	 * @param NDEF_message  返回内容
	 * @return
	 */
	public static native  int Lib_PiccNfc(byte[] NfcData_Len, byte[] Technology,  byte[]  UID,  byte[] NDEF_message);

	/**   
	 * @Title: Lib_PiccRemove   
	 * @Description: 判断卡片是否已经移开感应区  
	 * @param: @return      
	 * 0	成功,卡片已经移开感应区
	 * (-3502)	模块未开启
	 * (-3525)	卡片未移开感应区
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccRemove();


	/**   
	 * @Title: Lib_PiccHalt   
	 * @Description: 向卡片发送HALT指令 ,使PICC进入HALT状态   
	 * @param: @return   
	 * 0	成功
	 * 非0	失败   
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccHalt();
	/**   
	 * @Title: Lib_PiccReset   
	 * @Description: 向A类型而且为CPU卡片发送RATS指令 ,其他类型的卡不发送此命令
	 * @param: @return 
	 * 0	成功
	 * (-3527)	不是A类型卡片
	 * (-3517)	发送RATS命令错误     
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccReset();
	/**   
	 * @Title: Lib_PiccM1Authority   
	 * @Description: 验证M1卡访问时读写相应块需要提交的A密码或B密码
	 * @param: @param type 用于指定提交的密码类型：
	 * ‘A’或‘a’ 或0x0a ——提交的是A密码
	 * ‘B’或‘b’ 或0x0b ——提交的是B密码
	 * @param: @param blkNo 用于指定访问的块号，对于1K容量的M1卡，其有效范围为0~63
	 * @param: @param pwd 指向提交的密码缓冲区
	 * @param: @param SerialNo 指向存放卡片序列号的缓冲区首址；应指向Picc_Check( )调用返回的序列号信息SerialNo中的序列号部分，即SerialNo +1
	 * @param: @return    
	 * 0	成功
	 * (-3502)	模块未开启
	 * (-3503)	参数错误
	 * (-3523)	密码认证失败  
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccM1Authority(byte type, byte blkNo, byte[] pwd, byte[] serialNo);
	/**   
	 * @Title: Lib_PiccM1ReadBlock   
	 * @Description: 读取M1卡指定块的内容（共16字节）   
	 * @param: @param blkNo 读取M1卡指定块的内容（共16字节）
	 * @param: @param BlkValue 指向待存取块内容的缓冲区首址；该缓冲区至少应分配16字节
	 * @param: @return    
	 * 0	成功
	 * (-3502)	模块未开启
	 * (-3522)	读块数据失败  
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccM1ReadBlock(byte blkNo, byte[] blkValue);
	/**   
	 * @Title: Lib_PiccM1WriteBlock   
	 * @Description: 向M1卡指定块写入指定的内容（共16字节）   
	 * @param: @param blkNo 用于指定访问的块号，对于1K容量的M1卡，其有效范围为1~63
	 * @param: @param BlkValue 指向待写入块内容的缓冲区首址。
	 * @param: @return   
	 * 0	成功
	 * (-3502)	模块未开启
	 * (-3521)	写块数据失败   
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccM1WriteBlock(byte blkNo, byte[] blkValue);
	/**   
	 * @Title: Lib_PiccM1Operate   
	 * @Description: 对M1卡的钱包进行充值或减值操作。  
	 * @param: @param type 用于指定对钱包的操作类型：
	 * ‘+’ →充值，加号
	 * ‘-’ →减值，减号
	 * ‘=’ →钱包和钱包备份块之间的传输和恢复，等号
	 * @param: @param blkNo 用于指定钱包所在块号，对于1K容量的M1卡，其有效范围为1~63。
	 * @param: @param value 指向待充值或减值的金额数缓冲区首址；金额数共4字节，低字节在前。
	 * @param: @param updateBlkNo 指向待充值或减值的金额数缓冲区首址；金额数共4字节，低字节在前。
	 * @param: @return  
	 * 0	成功
	 * (-3503)	参数错误(无效的Type值)
	 * (-3502)	模块未开启
	 * (-3520)	冲值操作失败    
	 * @return: int      
	 * @throws   
	 */  
	public static native  int Lib_PiccM1Operate(byte type, byte blkNo, byte [] value, byte updateBlkNo);
}