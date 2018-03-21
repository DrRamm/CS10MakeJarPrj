package vpos.apipackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.zxing.BarcodeFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/10/11.
 */

public class PosApiHelper {

    final static String SET_LEFT_VOLUME_KEY_SCAN = "android.intent.action.SET_LEFT_VOLUME_KEY_SCAN";

    final static String SET_RIGHT_VOLUME_KEY_SCAN = "android.intent.action.SET_RIGHT_VOLUME_KEY_SCAN";

    final static String NODE_BATT_VOL = "/sys/class/power_supply/battery/batt_vol";
    final static String NODE_BATT_STATUS = "/sys/class/power_supply/battery/status";
    final static String SDK_VERSION = "1.9";
    private int BatteryV;

    private static final Object mLock=new Object();

    private static Object mBCRService=getBCRService();

    private static PosApiHelper mInstance;


    /**
     * 记录打印的字串拼接
     */
    public static String TmpStr="";

    public static final int PRINT_MAX_LEN = 624;

    public int Des(byte[] input,byte[] output,byte[] deskey,int mode){
        return Sys.Lib_Des(input,output,deskey,mode);
    }


    public int getBatteryV() {
        return BatteryV;
    }

    public void setBatteryV(int batteryV) {
        BatteryV = batteryV;
    }

    public static PosApiHelper getInstance(){
        synchronized (mLock){
            if(mInstance==null){
                mInstance=new PosApiHelper();
            }
            return mInstance;
        }
    }
    /**
     * @Description: 检查指定的卡座内是否有卡

     * @param slot 卡座号：
     *             0x00－IC卡通道;
     *             0x01－PSAM1卡通道;
     *             0x02－PSAM2卡通道;
     * @return: return int
     * 0：检测到有卡插入
     * 非0	没有卡插入
     */
    public int IccCheck(byte slot) {
        return Icc.Lib_IccCheck(slot);
    }

    /**
     * @Description: 初始化IC卡片, 并返回卡片的复位应答内容

     * @param slot    卡座号：
     *                0x00－IC卡通道;
     *                0x01－PSAM1卡通道;
     *                0x02－PSAM2卡通道;
     * @param vccMode 读卡电压：
     *                1---5V;
     *                2---3V;
     *                3---1.8V;
     * @param atr     卡片复位应答.（至少需要32+1bytes的空间）其内容为长度(1字节)+复位应答内容
     * @return 0    初始化成功.
     * (-2403)	通道号错误
     * (-2405)	卡拔出或无卡
     * (-2404)	协议错误
     * (-2500)	IC卡复位的电压模式错误
     * (-2503)	通信失败.
     */
    public int IccOpen(byte slot, byte vccMode, byte[] atr) {
        return Icc.Lib_IccOpen(slot, vccMode, atr);
    }

    /**
     * @Description: IC卡读写

     * @param slot     卡座号：
     *                 0x00－IC卡通道;
     *                 0x01－PSAM1卡通道;
     *                 0x02－PSAM2卡通道;
     * @param apduSend 发送给卡片的apdu
     * @param apduResp 接收到卡片返回的apdu
     * @return int
     * 0	执行成功
     * (-2503)	通信超时
     * (-2405)	交易中卡被拨出
     * (-2401)	奇偶错误
     * (-2403)	选择通道错误
     * (-2400)	发送数据太长（LC）
     * (-2404)	卡片协议错误（不为T＝0或T＝1）
     * (-2406)	没有复位卡片
     */
    public int IccCommand(byte slot, byte[] apduSend, byte[] apduResp) {
        return Icc.Lib_IccCommand(slot, apduSend, apduResp);
    }

    /**
     * @param slot 卡通道号
     *             0x00－IC卡通道
     *             0x01－PSAM1卡通道
     *             0x02－PSAM2卡通道
     * @return int
     * 0	成功
     * 非0	失败
     */
    public int IccClose(byte slot) {
        return Icc.Lib_IccClose(slot);
    }


    /**
     * @Description: 对非接触卡模块上电和复位，检查复位后模块初始状态是否正常

     * @return 0    成功
     * 其他	异常错误
     */
    public int PiccOpen() {
        return Picc.Lib_PiccOpen();
    }

    /**
     * @Description: 按指定的模式搜寻PICC卡片；搜到卡片后，将其选中和激活

     * @param mode     指定寻卡协议--‘A’/‘B’/‘M’
     *                 (一) ‘0’ ——先搜寻磁场中的所有A型卡：
     *                 1>搜到多张A型卡,返回多张卡错误；
     *                 2>没有搜到A型卡，再搜寻所有B型卡：
     *                 1．搜到多张B型卡，返回多张卡错误；
     *                 2．搜到1张B型卡，就激活此卡；
     *                 3．没有搜到B型卡，返回无卡；
     *                 3>搜到一张A型卡，再搜寻所以的B型卡:
     *                 1．搜到1张以上的B型卡，返回多卡错误；
     *                 2．没有搜到B型卡，就对此A型卡激活；
     *                 (二) ‘a’或‘A’ 或0x0a---只搜寻A型卡一次；如果搜到多张A卡则只选中其中一张卡。（如果一张A型CPU卡和一张A型M1卡在感应区中，此时选中M1卡）
     *                 (三) ‘b’或‘B’ 或0x0b --- 只搜寻B型卡一次；
     *                 (四) ‘m’或‘M’ --- 只搜寻M1卡一次；如果搜到多张M1卡在感应区中则只选中其中一张卡。
     * @param cardType 卡片类型字节缓冲区；目前均返回二字节的类型值：
     *                 CardType[0] ：
     *                 ‘A’ —搜寻到A型卡
     *                 ‘B’ —搜寻到B型卡
     *                 CardType[1] ：
     *                 ‘C’ —搜寻到CPU型卡
     *                 ‘M’ —搜寻到M1型卡
     * @param serialNo 卡片序列号信息的缓冲区首址.
     *                  该信息依次包含了序列号长度和序列号内容等两项内容.
     *                  B型卡和M1卡的序列号均为4字节;
     *                  A型卡的序列号一般为4字节,也有7字节或10字节的.
     *                  采用字节SerialNo[0]指示序列号的长度,
     *                  SerialNo[1~10]保存序列号(左对齐).
     *                  若需要读取序列号、且是A型卡,则要使用和判断长度字节.
     * @return  int
     * 0	成功
     * (-3503)	参数错误(无效的Mode值)
     * (-3502)	模块未开启
     * (-3526)	冲突
     * 其他值	操作失败
     */
    public int PiccCheck(byte mode, byte[] cardType, byte[] serialNo) {
        return Picc.Lib_PiccCheck(mode, cardType, serialNo);
    }


    /**
     * @Description : 处理APDU卡片命令
     * @param apduSend  发送给卡片的apdu
     * @param apduResp  接收到卡片返回的apdu
     * @return int
     * 0	成功
     * (-3503)	参数错误
     * (-3502)	模块未开启
     * (-3524)	数据交换错误
     */
    public int PiccCommand(byte[] apduSend, byte[] apduResp){
        return Picc.Lib_PiccCommand(apduSend,apduResp);
    }


    /**
     * @Description : 关闭非接触卡模块，使该模块处于关闭状态
     * @return int
     * 0	    成功
     * 其他值	异常错误
     */
    public int PiccClose(){
        return Picc.Lib_PiccClose();
    }


    /**
     * @Description : 判断卡片是否已经移开感应区
     * @return int
     *  0	成功,卡片已经移开感应区
     * (-3502)	模块未开启
     * (-3525)	卡片未移开感应区
     */
    public int PiccRemove(){
        return Picc.Lib_PiccRemove();
    }


    /**
     * @Description : 向卡片发送HALT指令 ,使PICC进入HALT状态
     * @return int
     * 0	成功
     * 非0	失败
     */
    public int PiccHalt(){
        return  Picc.Lib_PiccHalt();
    }

    /**
     * @Description: 向A类型而且为CPU卡片发送RATS指令 ,其他类型的卡不发送此命令
     * @return : int
     * 0	成功
     * (-3527)	不是A类型卡片
     * (-3517)	发送RATS命令错误
     */
    public int PiccReset(){
        return  Picc.Lib_PiccReset();
    }

    /**
     * @Description :NFC
     * @param NfcData_Len NFC数据长度
     * @param Technology  协议
     * @param UID         UID
     * @param NDEF_message  返回数据
     * @return
     */
    public int PiccNfc(byte[] NfcData_Len, byte[] Technology,  byte[]  UID,  byte[] NDEF_message){
        return  Picc.Lib_PiccNfc(NfcData_Len,Technology,UID,NDEF_message);
    }


    /**
     * @Description: 验证M1卡访问时读写相应块需要提交的A密码或B密码
     * @param type
     * 用于指定提交的密码类型：
     * ‘A’或‘a’ 或0x0a ——提交的是A密码
     * ‘B’或‘b’ 或0x0b ——提交的是B密码
     * @param blkNo 用于指定访问的块号，对于1K容量的M1卡，其有效范围为0~63
     * @param pwd   指向提交的密码缓冲区
     * @param serialNo 指向存放卡片序列号的缓冲区首址；应指向Picc_Check( )调用返回的序列号信息SerialNo中的序列号部分，即SerialNo +1
     * @return int
     * 0	成功
     * (-3502)	模块未开启
     * (-3503)	参数错误
     * (-3523)	密码认证失败
     */
    public int PiccM1Authority(byte type, byte blkNo, byte[] pwd, byte[] serialNo){
        return Picc.Lib_PiccM1Authority(type,blkNo,pwd,serialNo);
    }


    /**
     * @Description: 读取M1卡指定块的内容（共16字节）
     * @param blkNo 读取M1卡指定块的内容（共16字节）
     * @param blkValue 指向待存取块内容的缓冲区首址；该缓冲区至少应分配16字节
     * @return int
     * 0	成功
     * (-3502)	模块未开启
     * (-3522)	读块数据失败
     */
    public int PiccM1ReadBlock(byte blkNo, byte[] blkValue){
        return Picc.Lib_PiccM1ReadBlock(blkNo,blkValue);
    }

    /**
     * @Description: 向M1卡指定块写入指定的内容（共16字节）
     * @param blkNo   用于指定访问的块号，对于1K容量的M1卡，其有效范围为1~63
     * @param blkValue 指向待写入块内容的缓冲区首址。
     * @return int
     * 0	成功
     * (-3502)	模块未开启
     * (-3521)	写块数据失败
     */
    public int PiccM1WriteBlock(byte blkNo, byte[] blkValue){
        return  Picc.Lib_PiccM1WriteBlock(blkNo,blkValue);
    }


    /**
     * @Description: 对M1卡的钱包进行充值或减值操作。

     * @param type
     * 用于指定对钱包的操作类型：
     * ‘+’ →充值，加号
     * ‘-’ →减值，减号
     * ‘=’ →钱包和钱包备份块之间的传输和恢复，等号
     * @param blkNo  用于指定钱包所在块号，对于1K容量的M1卡，其有效范围为1~63。
     * @param value  指向待充值或减值的金额数缓冲区首址；金额数共4字节，低字节在前。
     * @param updateBlkNo  指向待充值或减值的金额数缓冲区首址；金额数共4字节，低字节在前。
     * @return int
     * 0	成功
     * (-3503)	参数错误(无效的Type值)
     * (-3502)	模块未开启
     * (-3520)	冲值操作失败
     */
    public int PiccM1Operate(byte type, byte blkNo, byte [] value, byte updateBlkNo){
        return  Picc.Lib_PiccM1Operate(type,blkNo,value,updateBlkNo);
    }


    /**
     * @Description: 关闭磁卡阅读器
     * @return int
     * 0	成功
     * 非0	失败
     */
    public int McrClose(){
        return Mcr.Lib_McrClose();
    }

    /**
     * @Description: 打开磁卡阅读器
     * @return int
     * 0	成功
     * 非0	失败
     */
    public int McrOpen(){
        return Mcr.Lib_McrOpen();
    }

    /**
     * @Description: 复位磁头，并清除磁卡缓冲区数据
     * @return int
     * 0	成功
     * 非0	失败
     */
    public int McrReset(){
        return Mcr.Lib_McrReset();
    }

    /**
     * @Description: 检查是否刷过卡
     * @return int
     * 0	成功
     * 非0	失败
     */
    public int McrCheck(){
        return Mcr.Lib_McrCheck();
    }

    /**
     * @Description:    读取磁卡缓冲区的1、2、3磁道的数据
     * @param keyNo     DES密钥索引号，取值范围0~4，必须与认证过程中所使用的AUTHDESK的密钥索引号相同
     * @param mode      模式 0：不加密  1：加密
     * @param track1    存放1磁道数据的指针 [应用层缓冲区要设为256]
     * @param track2    存放2磁道数据的指针 [应用层缓冲区要设为256]
     * @param track3    存放3磁道数据的指针 [应用层缓冲区要设为256]
     * @return int
     * 0	刷卡错误
     * 其它值
     *（＞0）	bit0 = 1  正确读出 1磁道数据
     * bit1 = 1  正确读出2磁道数据
     * bit2 = 1  正确读出3磁道数据
     * bit4 = 1  1磁道数据有校验错
     * bit5 = 1  2磁道数据有校验错
     * bit6 = 1  3磁道数据有校验错
     */
    public int McrRead(byte keyNo, byte mode, byte[] track1, byte[] track2, byte[] track3){
        return Mcr.Lib_McrRead(keyNo,mode,track1,track2,track3);
    }


    /**
     * @Description : 初始化打印机功能参数和加载字库
     * 0	成功
     * 非0	失败
     * @return
     */
    public int PrintInit() throws PrintInitException {
//        int ret= Print.Lib_PrnInit();
//        if (ret<0){
//            throw new PrintInitException(ret);
//        }
        int ret = PrintInit(2, 24,24, 0);
        if(ret!=0){
            throw new PrintInitException(ret);
        }

        return ret;
    }

    /**
     * @Description : 设置电压
     * @param voltage 当前电池电压*10
     * @return int
     * 0     成功
        非0	 失败
     */
    public int PrintSetVoltage(int voltage){
        return Print.Lib_PrnSetVoltage(voltage);
    }


    /**
     * @Description : 检测打印机状态
     * @return int
     * 0 –成功
       -1 -缺纸
       -2 -温度过高
       -3 –电池电压过低
     */
    public int PrintCheckStatus(){

        //setVoltage
        int voltage=Integer.parseInt(readSysBattFile(NODE_BATT_VOL));

       int ret = Print.Lib_PrnSetVoltage(voltage*2/100);
        if(ret!=0){
            return ret;
        }

        return Print.Lib_PrnCheckStatus();
    }

    public int PrintCtnStart(){

        int ret =-1;
        if(!"Charging".equals(readSysBattFile(NODE_BATT_STATUS)))
        {
            Print.Lib_PrnIsCharge(0);
//            return  ret;
        }
        else{
            Print.Lib_PrnIsCharge(1);
        }
        ret = PrintCheckStatus();

        Log.e("liuhao","ret = " +ret);

        if(ret!=0){
//            if (ret==-3)
//            {
//                if(!"Charging".equals(readSysBattFile(NODE_BATT_STATUS)))
//                {
//                    Print.Lib_PrnIsCharge(0);
//                    return  ret;
//                }
//                else{
//                    Print.Lib_PrnIsCharge(1);
//                }
//            }
//            else
//            {
//                return ret;
//            }

            return  ret;
        }

        return Print.Lib_CTNPrnStart();
    }

    public int PrintClose(){
        return Print.Lib_PrnClose();
    }

    public int PrnConventional(int nlevel){
        return Print.Lib_PrnConventional(nlevel);
    }

    public int PrnContinuous(int nlevel){
        return Print.Lib_PrnContinuous(nlevel);
    }

    public int PrintOpen() throws PrintInitException {

        int ret =-1;

//        try {
//            ret= PrintInit();
//        } catch (PrintInitException e) {
//            e.printStackTrace();
//            int initRet = e.getExceptionCode();
//        }

        ret= PrintInit();

        if (ret!=0)
        {
            throw new PrintInitException(ret);
        }

        PrintStr("\n");
        PrintStr("\n");
        PrintStr("\n");

        return PrintStart();
    }



        /**
         * @Description : 设置打印灰度
         * @param nLevel
         * 灰度等级，值 1~3
           1:高浓度   2：中等  3：低浓度
         * @return int
         *              0 –成功
                        非0	-失败
         */
    public int PrintSetGray(int nLevel){
        return Print.Lib_PrnSetGray(nLevel);
    }

    /**
     * @Description : 设置打印字体大小
     * @param fontHeight 字体点阵高度
     * @param fontWidth 字体点阵宽度
     * @param zoom 字体加粗放大度
     * @return int
     *  0 –成功
        非0	-失败
     */
    public int PrintSetFont(byte fontHeight, byte fontWidth, byte zoom){
        return Print.Lib_PrnSetFont(fontHeight,fontWidth,zoom);
    }


    /**
     * @Description : 设置打印内容
     * @param str 打印的内容
     * @return int
     *  0 –成功
        -4002 –缺纸
        -4003 –数据错误
     */
    public int PrintStr(String str){
        //TmpStr+=str;
        return Print.Lib_PrnStr(str);
    }

    /**
     * @Description : 开始打印
     * @return int
     * 0 –成功
      非0	-失败
     */
    public int PrintStart(){
//        return PrintStartWork();
//        return Print.Lib_PrnStart();

        int ret =-1;

        if(!"Charging".equals(readSysBattFile(NODE_BATT_STATUS)))
        {
            Print.Lib_PrnIsCharge(0);
         //   return  ret;
        }
        else
        {
            Print.Lib_PrnIsCharge(1);
        }

        ret = PrintCheckStatus();

        Log.e("liuhao","ret = " +ret);

        if(ret!=0){
//            if (ret==-3)
//            {
//                if(!"Charging".equals(readSysBattFile(NODE_BATT_STATUS)))
//                {
//                    Print.Lib_PrnIsCharge(0);
//                    return  ret;
//                }
//                else{
//                    Print.Lib_PrnIsCharge(1);
//                }
//            }
//            else
//            {
//                return ret;
//            }

            return ret;
        }

        ret = Print.Lib_PrnStart();

        Log.e("liuhao","start ret = " +ret);
        if(ret!=0){
            return ret;
        }

        return ret;
    }



    //data -> 3639  data -> Charging
    /**
     * @Description : 第一种方式读取sys节点
     * @param sys_path
     * @return
     */
    //sys_path 为节点映射到的实际路径
    public static String readSysBattFile(String sys_path) {
        String data = "";// 默认值
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(sys_path));
            data = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e(TAG, " ***ERROR*** : " + e.getMessage());
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        Log.e(TAG, "data"+" -> "+data);
        return data;
    }


    //第二种方式读取系统节点的方法是通过java 的Runtime类来执行脚本命令(cat)
    //sys_path 为节点映射到的实际路径
    public static String readSysBattCat(String sys_path){

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("cat " + sys_path); // 此处进行读操作
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line="" ;
            while (null != (line = br.readLine())) {
//                line+=line;
//                Log.e(TAG, "read data ---> " + line);
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e(TAG, "*** ERROR *** : " + e.getMessage());
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isr!=null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    /**
     * @Description: 设置打印初始化基本参数
     *
     * @param gray  灰度等级，值 1 - 3  (1:高浓度   2：中等  3：低浓度)
     * @param fontHeight 字体点阵高度
     * @param fontWidth 字体点阵宽度
     * @param fontZoom  字体加粗放大度
     * @return 0 –成功  非0	-失败
     */
    public int PrintInit(int gray,int fontHeight, int fontWidth, int fontZoom) {
        int ret = -1;

        int voltage=0;

        //init
        ret= Print.Lib_PrnInit();

        if(ret!=0){
//            throw new PrintInitException(ret);
            return  ret;
        }

        //setVoltage
//        voltage=Integer.parseInt(readSysBattFile(NODE_BATT_VOL));
//
//        ret = Print.Lib_PrnSetVoltage(voltage*2/100);
//        if(ret!=0){
//           return ret;
//        }

        //setGray
        ret = Print.Lib_PrnSetGray(gray);
        if(ret!=0){
            return  ret;
        }

        //setFont
        ret = Print.Lib_PrnSetFont((byte)fontHeight,(byte)fontWidth,(byte)fontZoom);
        if(ret!=0){
            return  ret;
        }

        return  ret;
    }



    /**
     * @Description : 设置BMP图片打印内容
     * @param bitmap BMP图片数据
     * @return int
     * 0 –成功
        非0	-失败
     */
    public int PrintBmp(Bitmap bitmap){
        return new Print().Lib_PrnBmp(bitmap);
    }

    /**
     * @Description : 设置条码打印内容
     * @param contents 条码正文内容
     * @param desiredWidth 条码宽
     * @param desiredHeight 条码高
     * @param barcodeFormat 条码格式
     * BarcodeFormat.CODE_128 、BarcodeFormat.CODE_39、 BarcodeFormat.EAN_8、 BarcodeFormat.QR_CODE
        BarcodeFormat.PDF_417 、BarcodeFormat.ITF
     * @return int
     * 0 –成功
        非0	-失败
     */
    public int PrintBarcode(String contents, int desiredWidth,int desiredHeight, BarcodeFormat barcodeFormat){
        return new Print().Lib_PrnBarcode(contents,desiredWidth,desiredHeight,barcodeFormat);
    }

    /**
     * @Description : 写PIN主密钥
     * @param keyNo 密钥序号,取值范围0~9，其他值非法。
     * @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法
     * @param keyData 密钥数据
     * @param mode 加解密模式 0
     * @return int
     * (0)	写入成功
     * (-7000)	密码键盘已锁
     * (-7001)	密钥类型错误
     * (-7002)	密钥校验值错
     * (-7003)	密钥索引非法
     * (-7004)	密钥长度非法
     * (-7005)	模式错误
     * (-7011)	密钥不存在
     * (-7012)	写密钥失败
     */
    public int PciWritePIN_MKey(byte keyNo, byte keyLen, byte[] keyData, byte mode){
        return Pci.Lib_PciWritePIN_MKey(keyNo,keyLen,keyData,mode);
    }

    /**
     * @Description : 写MAC主密钥
     * @param keyNo 密钥序号,取值范围0~9，其他值非法。
     * @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法。
     * @param keyData 密钥数据
     * @param mode 加解密模式 0
     * @return int
     * (0)	写入成功
     * (-7000)	密码键盘已锁
     * (-7001)	密钥类型错误
     * (-7002)	密钥校验值错
     * (-7003)	密钥索引非法
     * (-7004)	密钥长度非法
     * (-7005)	模式错误
     * (-7011)	密钥不存在
     * (-7012)	写密钥失败
     */
    public int PciWriteMAC_MKey(byte keyNo, byte keyLen, byte[] keyData, byte mode){
        return Pci.Lib_PciWriteMAC_MKey(keyNo,keyLen,keyData,mode);
    }

    /**
     * @Description : 写DES主密钥
     * @param keyNo 密钥序号,取值范围0~9，其他值非法
     * @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法
     * @param keyData 密钥数据
     * @param mode 加解密模式 0
     * @return int
     * (0)	写入成功
     * (-7000)	密码键盘已锁
     * (-7001)	密钥类型错误
     * (-7002)	密钥校验值错
     * (-7003)	密钥索引非法
     * (-7004)	密钥长度非法
     * (-7005)	模式错误
     * (-7011)	密钥不存在
     * (-7012)	写密钥失败
     */
    public int PciWriteDES_MKey(byte keyNo, byte keyLen, byte[] keyData, byte mode){
        return Pci.Lib_PciWriteDES_MKey(keyNo,keyLen,keyData,mode);
    }

    /**
     * @Description : 写PIN专用密钥
     * @param keyNo PIN密钥序号，取值范围0~9，其他值非法
     * @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法
     * @param keyData 密钥数据
     * @param mode 加/解密模式
     * 0：使用PIN_MK解密PINK
     * 1：使用PIN_MK加密PINK
     * @param mkeyNo 主密钥序号，取值范围0~9，其他值非法
     * @return int
     *  (0)	写入成功
     * (-7000)	密码键盘已锁
     * (-7001)	密钥类型错误
     * (-7002)	密钥校验值错
     * (-7003)	密钥索引非法
     * (-7004)	密钥长度非法
     * (-7005)	模式错误
     * (-7011)	密钥不存在
     * (-7012)	写密钥失败
     */
    public int PciWritePinKey(byte keyNo, byte keyLen, byte[] keyData, byte mode, byte mkeyNo){
        return Pci.Lib_PciWritePinKey(keyNo, keyLen, keyData, mode, mkeyNo);
    }

    /**
     * @Description : 写MAC专用密钥
     * @param keyNo PIN密钥序号，取值范围0~9，其他值非法
     * @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法
     * @param keyData 密钥数据
     * @param mode 加/解密模式
     * 0：使用MAC_MK解密MACK
     * 1：使用MAC_MK加密MACK
     * @param mkeyNo 主密钥序号，取值范围0~9，其他值非法
     * @return int
     * (0)	写入成功
     * (-7000)	密码键盘已锁
     * (-7001)	密钥类型错误
     * (-7002)	密钥校验值错
     * (-7003)	密钥索引非法
     * (-7004)	密钥长度非法
     * (-7005)	模式错误
     * (-7011)	密钥不存在
     * (-7012)	写密钥失败
     */
    public int PciWriteMacKey(byte keyNo, byte keyLen, byte[] keyData, byte mode, byte mkeyNo){
        return Pci.Lib_PciWriteMacKey(keyNo, keyLen, keyData, mode, mkeyNo);
    }

    /**
     * @Description : 写DES专用密钥
     * @param keyNo PIN密钥序号，取值范围0~9，其他值非法
     * @param keyLen 密钥长度,只能取8、16、24这3个值，其他值非法
     * @param keyData 密钥数据
     * @param mode 加/解密模式
     * 0：使用DES_MK解密DESK
     * 1：使用DES_MK加密DESK
     * @param mkeyNo 主密钥序号，取值范围0~9，其他值非法
     * @return int
     * (0)	写入成功
     * (-7000)	密码键盘已锁
     * (-7001)	密钥类型错误
     * (-7002)	密钥校验值错
     * (-7003)	密钥索引非法
     * (-7004)	密钥长度非法
     * (-7005)	模式错误
     * (-7011)	密钥不存在
     * (-7012)	写密钥失败
     */
    public int PciWriteDesKey(byte keyNo, byte keyLen, byte[] keyData, byte mode, byte mkeyNo){
        return Pci.Lib_PciWriteDesKey(keyNo, keyLen, keyData, mode, mkeyNo);
    }

    /**
     * @Description : 获取加密的PIN－BLOCK
     * @param keyNo PIN密钥索引号，取值范围0~9，必须与认证过程中所使用的AUTHPINK的密钥索引号相同
     * @param minLen 指示输入数据的最小长度，取值范围，4~12
     * @param maxLen 指示输入数据的最大长度，取值范围，4~12
     * @param mode   加密模式：[该参数未使用，目前仅支持X9.8]
     * 0=X9.8
     * 1=X3.92
     * @param cardNo 指示相应的卡片或交易序号数
     * @param pinBlock 返回加密的PIN块
     * @param pin_len 返回加密的PIN长度
     * @param mark 金额信息显示标志（0x00：无；0x01：有）
     * @param iAmount 金额:
     * 举例：(123456.00) 如果金额长度小于14，则在后面补0.
     * @param waitTimeSec  超时等待时间，表示等待若干秒如果用户没有按按键，则超时退出。单位（秒），如果取值0则表示使用默认值120秒
     * @param ctx 上下文Context
     * @return int
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
     */
    public int PciGetPin(byte keyNo, byte minLen, byte maxLen, byte mode, byte[] cardNo, byte[] pinBlock, byte[] pinPasswd,byte pin_len, byte mark, byte[] iAmount, byte waitTimeSec, Context ctx)
    {
        return Pci.Lib_PciGetPin(keyNo, minLen, maxLen, mode, cardNo, pinBlock,pinPasswd,pin_len, mark, iAmount, waitTimeSec, ctx);
    }

    /**
     * @Description : 获取加密的MAC报文
     * @param keyNo 指定MAC密钥序号，取值范围0~9，其他值非法
     * @param inLen 计数MAC报文数据长度
     * @param inData 计数MAC报文数据内容
     * @param macOut 返回MAC报文加密结果
     * @param mode
     * 0＝算法1
     * 1＝算法2
     * 2＝算法3
     * @return : int
     *(0)	成功：
     * (-7000)	密码键盘已锁
     * (-7001)	密钥类型错误
     * (-7002)	密钥校验值错
     * (-7003)	密钥索引非法
     * (-7004)	密钥长度非法
     * (-7005)	输入模式非法
     * (-7011)	密钥不存在
     * (-7015)	输入数据长度错误
     */
    public int PciGetMac(byte keyNo, short inLen, byte[] inData, byte[] macOut, byte mode){
        return  Pci.Lib_PciGetMac(keyNo,inLen,inData,macOut,mode);
    }

    /**
     * @Description : 使用des key对数据进行加密/解密
     * @param keyNo DES密钥序号，取值范围0~9，其他值非法
     * @param inLen 要加密/解密的数据长度
     * @param inData 要加密/解密的数据
     * @param outData  加密/解密结果
     * @param mode  1 –加密  0 –解密
     * @return int
     * (0)	成功：
     * (-7000)	密码键盘已锁
     * (-7001)	密钥类型错误
     * (-7002)	密钥校验值错
     * (-7003)	密钥索引非法
     * (-7004)	密钥长度非法
     * (-7005)	输入模式非法
     * (-7011)	密钥不存在
     * (-7015)	输入数据长度错误
     */
    public int PciGetDes(byte keyNo, short inLen, byte[] inData, byte[] outData, byte mode){
        return  Pci.Lib_PciGetDes(keyNo,inLen,inData,outData,mode);
    }


    public int  PciGetRnd (byte []rnd){
        return Pci.Lib_PciGetRnd(rnd);
    }


    /**
     * @Description : 支付模组固件升级
     * @return ：int
     * 0	成功
     * 非0	失败
     */
    public int SysUpdate(){
        return Sys.Lib_Update();
    }


    public int SysSetLedMode(int ledIndex,int mode){
        return Sys.Lib_SetLed((byte)ledIndex,(byte)mode);
    }



    /**
     * @Description : 蜂鸣器响
     * @return
     * 0	成功
     * 非0	失败
     */
    public int SysBeep(){
        return Sys.Lib_Beep();
    }

    /**
     * @Description : 获取芯片ID号
     * @param buf 芯片ID号
     * @param len 长度
     * @return
     * 0	成功
     * 非0	失败
     */
    public int SysReadChipID (byte[] buf, int len){
        return Sys.Lib_ReadChipID(buf, len);
    }


    /**
     * @Description : 写序列号
     * @param SN 16字节序列号
     * @return
     * 0	成功
     * 非0	失败
     */
    public int SysWriteSN (byte[] SN){
        return Sys.Lib_WriteSN(SN);
    }

    /**
     * @Description : 读取序列号
     * @param SN 16字节序列号
     * @return
     * 0	成功
     * 非0	失败
     */
    public int SysReadSN (byte[] SN){
        return Sys.Lib_ReadSN(SN);
    }

    public int SysGetVersion (byte[] buf){
        return Sys.Lib_GetVersion(buf);
    }

    /**
     * @Description: 将 左 音量键 设置成为 扫描按键
     * @param mContext Context
     * @param mode  int , 0 is volume key or 1 is scan key
     */
    public void SetKeyScanByLetfVolume(Context mContext,int mode){
        Intent intent = new Intent (SET_LEFT_VOLUME_KEY_SCAN);
        intent.putExtra("value",mode);//0 is volume key or 1 is scan key
        mContext.sendBroadcast(intent);
    }

    /**
     * @Description: 将 右 音量键 设置成为 扫描按键
     * @param mContext Context
     * @param mode  int , 0 is volume key or 1 is scan key
     */
    public void SetKeyScanByRightVolume(Context mContext,int mode){
        Intent intent = new Intent (SET_RIGHT_VOLUME_KEY_SCAN);
        intent.putExtra("value",mode);//0 is volume key or 1 is scan key
        mContext.sendBroadcast(intent);
    }

    public String getSdkVersion(){
        return SDK_VERSION;
    }

    public String getOSVersion(Context context){
        return Settings.System.getString(context.getContentResolver(),"custom_build_version");
    }
    public String getMcuTargetVersion(Context context){
        return  Settings.System.getString(context.getContentResolver(),"mcu_target_version");

    }


    public static Object getBCRService(){
        Log.e("RomUtil", "getBCRService - s");
        try {
            //获取升级服务
            Class serviceManager = Class.forName("android.os.ServiceManager");
            Method method = serviceManager.getMethod("getService", String.class);
            IBinder b = (IBinder)method.invoke(serviceManager.newInstance(), "bcr_service");
            Class<?> stub=Class.forName("com.android.server.bcr.IBCRService$Stub");
            Method asInterfaceMethod=stub.getDeclaredMethod("asInterface", IBinder.class);

            Log.e("RomUtil", "getBCRService - ***********************");

            return asInterfaceMethod.invoke(stub, b);
        } catch (Exception e) {
            Log.e("RomUtil", "getBCRService - Exception");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean installRomPackage(Context context,String romFilePath){
        Log.e("RomUtil", "installRomPackage - s");
        try {
            if(mBCRService==null)
                mBCRService = getBCRService();
            Method installPackage=mBCRService.getClass().getDeclaredMethod("installPackage",String.class);

            Log.e("RomUtil", "installPackage - ***********************" + (Boolean)installPackage.invoke(mBCRService, romFilePath));

            return (Boolean)installPackage.invoke(mBCRService, romFilePath);
        } catch (Exception e) {
            Log.e("RomUtil", "installRomPackage :"  + e.getMessage());

            e.printStackTrace();
        }
        return false;
    }


}
