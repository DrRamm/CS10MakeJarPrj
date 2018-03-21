package vpos.apipackage;

/**
 * Created by Administrator on 2017/11/17.
 */

public class PrintInitException extends Exception {

    public static final int PRN_BUSY=-4001;
    public static final int PRN_NOPAPER=-4002;
    public static final int PRN_DATAERR=-4003;
    public static final int PRN_FAULT =-4004;
    public static final int PRN_TOOHEAT=-4005;
    public static final int PRN_UNFINISHED=-4006;
    public static final int PRN_NOFONTLIB =-4007;
    public static final int PRN_BUFFOVERFLOW  =-4008;
    public static final int PRN_SETFONTERR  =-4009;
    public static final int PRN_GETFONTERR   =-4010;

    public static int exceptionCode;

    public PrintInitException() {
        super();
    }
    public PrintInitException(int code) {
        super();
        if (code != 0) {
            this.exceptionCode = code;
        }
    }
    public int getExceptionCode() {
        return exceptionCode;
    }
    public void setExceptionCode(int exceptionCode) {
        this.exceptionCode = exceptionCode;
    }


}
