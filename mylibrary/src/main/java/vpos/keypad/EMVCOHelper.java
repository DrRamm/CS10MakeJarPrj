package vpos.keypad;

import android.content.Context;

import com.cspos.PaySys;

/**
 * Created by Administrator on 2017/12/4.
 */

public class EMVCOHelper {

    private static final Object mLock=new Object();
    private static EMVCOHelper mInstance;

    public static EMVCOHelper getInstance(){
        synchronized (mLock){
            if(mInstance==null){
                mInstance=new EMVCOHelper();
            }
            return mInstance;
        }
    }


    /**
     * @Description: 初始化EMV运行环境参数
     * @return : int
     *          0：成功
     *           其他值：失败
     */
    public static int EmvEnvParaInit(){
        return PaySys.EmvParaInit();
    }

    /**
     * @Description : 初始化EMV内核参数
     * @param index AID和终端参数列表索引
     * @param flag  1 AID参数 2 终端参数
     * @return    0： 初始化成功
     *          否则： 失败
     */
    public static int EmvKernelInit(int index, int flag){
        return PaySys.EmvContextInit(index,flag);
    }

    /**
     * @Description : 设置交易金额
     * @param amount 金额，最小单位为分
     * @return 0：执行成功
     *       其他：失败
     */
    public static int EmvSetTransAmount(int amount){
        return PaySys.EmvSetTransAmount(amount);
    }

    /**
     * @Description : 设置本次EMV交易的卡类型
     * @param cardtype    卡的类型：
                           1－接触IC卡
                           2－非接触IC卡
     * @return   0：执行成功
     *       其他值：失败
     */
    public static int EmvSetCardType(int cardtype ){
        return PaySys.EmvSetCardType(cardtype);
    }

    /**
     * @Description: 设置本次EMV交易类型
     * @param TransType  交易类型：
     *                   1  － 商品和服务
     *                   2  － 查询余额
     *                   3  － 持卡人账户转账
     *                   5  － 读取交易日志（非EMV&PBOC协议定义）
     *                   6  － 电子现金消费
     *                   7  － 电子现金余额查询
     *                   8  － 查询余额-可用脱机消费金额
     *                   9  － 电子现金现金圈存
     * @return   0：成功
     * 其他值：失败
     */
    public static int EmvSetTransType(int TransType){
        return  PaySys.EmvSetTransType(TransType);
    }

    /**
     * @Description: 按指定的模式搜寻PICC卡片；搜到卡片后，将其选中和激活
     * @param KernelType
     * 1  －  接触式EMV_KERNEL
     * 2  －  非接触式EMV_KERNEL
     * 3  －  电子现金EMV_KERNEL
     * 4  －  中国银联闪付QUICS_KERNEL
     * @param FlowType
     * 0  －  完整流程
     * 1  －  简化流程
     * 2  －  应用初始化流程
     * @return
     * -1  －  EMV终止
     * 1   －  批准交易
     * 2   －  拒绝交易 DENIAL
     * 3   －  请求联机 EMV_GOONLINE
     * 12  －  脱机交易成功 EMV_OFFLINE_SUCCESS
     */
    public static int EmvProcess(int KernelType, int FlowType){
        return PaySys.EmvProcess(KernelType,FlowType);
    }


    /**
     * @Description : 获取TAG数据
     * @param OutPut 返回的TAG数据内容
     * @param OutputBufSize 设置TAG数据缓存区长度
     * @param tagname    设置要获取的TAG
     * @return
     */
    public static int EmvGetTagData(byte[]  OutPut, int OutputBufSize, short tagname){
        return PaySys.EmvGetTagData(OutPut,OutputBufSize,tagname);
    }

    /**
     * @Description : 获取55域数据
     * @param OutPut   获得的55域数据内容
     * @param OutputBufSize 设置数据缓存区长度
     * @return  0：成功
                其他值：失败
     */
    public static int EmvPrePare55Field(byte[]  OutPut, int OutputBufSize){
        return PaySys.EmvPrePare55Field(OutPut,OutputBufSize);
    }

    /**
     * @Description :处理联机流程返回的数据包
     * @param result   TAG-8A内容
     * @param IsSuerRespData  后台返回脚本数据和外部认证数据
     * @param IsSuerRespDataLength  长度
     * @return  0：成功
                其他值：失败
     */
    public static int EmvSetOnlineResult(byte[] result, byte[] IsSuerRespData, int IsSuerRespDataLength){
        return PaySys.EmvSetOnlineResult(result,IsSuerRespData,IsSuerRespDataLength);
    }

    public static int EmvFinal(){
        return PaySys.EmvFinal();
    }

    public static int EmvGetVersion(byte[] Output){
        return PaySys.EmvGetVersion(Output);
    }

    /**
     * @Description : 清除内核中全部AID
     * @return 0：成功
                其他值：失败
     */
    public static int EmvClearAllAIDS(){
        return PaySys.EmvClearAllAIDS();
    }

    /**
     * @Description : 清除内核中一个AID
     * @param Input  9F06 的Value 内容
     * @param InLen  Value的长度
     * @return  0：成功
                其他值：失败
     */
    public static int EmvClearOneAIDS(byte[] Input, int InLen){
        return PaySys.EmvClearOneAIDS(Input,InLen);
    }

    /**
     * @Description : 在内核中添加一个AID
     * @param Input    AID TLV格式数据
     * @param InLen    AID TLV格式数据长度
     * @return
     * 0：成功
     * 其他值：失败
     */
    public static int EmvAddOneAIDS(byte[] Input, int InLen){
        return PaySys.EmvAddOneAIDS(Input,InLen);
    }

    /**
     * @Description : 清除内核存储的全部capk 的数据
     * @return 0：成功
     * 其他值：失败
     */
    public static int EmvClearAllCapks(){
        return PaySys.EmvClearAllAIDS();
    }

    /**
     * @Description :  清除内核中的一个公钥
     * @param Input   输入数据TLV格式 tag Index rid 公钥算法标识
     * @param InLen   输入长度
     * @return
     * 0：成功
     * 其他值：失败
     */
    public static int EmvClearOneCapks(byte[] Input, int InLen){
        return PaySys.EmvClearOneCapks(Input,InLen);
    }

    /**
     *@Description :  在内核中添加一个capk
     * @param Input   输入数据TLV格式
     * @param InLen    输入长度
     * @return
     * 0：成功
     * 其他值：失败
     */
    public static int EmvAddOneCAPK(byte[] Input, int InLen){
        return PaySys.EmvAddOneCAPK(Input,InLen);
    }

    public static int EmvReadTermPar(byte[] Input, int InLen, byte[] OutPut, int OutBufSize){
        return PaySys.EmvReadTermPar(Input,InLen,OutPut,OutBufSize);
    }

    /**
     * @Description : 把终端参数处理后保存起来
     * @param Input  输入数据TLV格式
     * @param InLen  输入长度
     * @return
     * 0：成功
     * 其他值：失败
     */
    public static int EmvSaveTermParas(byte[] Input, int InLen){
        return PaySys.EmvSaveTermParas(Input,InLen);
    }

    /**
     * @Description： init key pad
     * @param ctx Context
     * @return 0 SUCCESS
     *          OTHER FAIL
     */
    public static int EmvKeyPadInit(Context ctx){
        return PaySys.poskeypad(ctx);
    }

    public static int EmvGetPinBlock(Context ctx,int pinkey_n, byte[] card_no, byte[] mode,  byte[] pin_block){
        int ret = EmvKeyPadInit(ctx);
        return PaySys.Getpinblock(pinkey_n,card_no,mode,pin_block);
    }

    public static int EmvShowKeyPad(Context ctx,byte[] password){
        int ret = EmvKeyPadInit(ctx);
        return PaySys.CallKeyPad(password);
    }
}
