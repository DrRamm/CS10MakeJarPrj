package vpos.apipackage;

import java.io.UnsupportedEncodingException;

import com.google.zxing.BarcodeFormat;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class Print{

	static {
		System.loadLibrary("PosApi");
	}

	private final String tag = "Print";
	
	public Print(){
		
	}

	public static native int Lib_PrnInit();

	public static native int Lib_PrnSetSpace(byte x, byte y);
	
	public static native int Lib_PrnSetFont(byte asciiFontHeight, byte extendFontHeight, byte zoom);

	public static native int Lib_PrnGetFont(byte asciiFontHeight[], byte extendFontHeight[], byte[] zoom);
	
	public static native int Lib_PrnStep(int pixel);
	
	public static native int Lib_PrnSetVoltage(int voltage);
	
//	public static native int Lib_PrnStr(String str);
	
	private static native int Lib_PrnStr(byte[] str);

//	public static native int Lib_PrnBlock(byte[] str);
	
	public static native int Lib_PrnLogo(byte logo[]);
	
	public static native int Lib_PrnStart();

	public static native int Lib_PrnSetAlign(int x);
	public static native int Lib_PrnSetCharSpace(int x);
	public static native int Lib_PrnSetLineSpace(int x);
	public static native int Lib_PrnSetLeftSpace(int x);
	public static native int Lib_PrnSetLeftIndent(int x);

	public static native int Lib_PrnSetGray(int nLevel);
	
	public static native int Lib_PrnSetSpeed(int iSpeed);
	
	public static native int Lib_PrnCheckStatus();

	public static native int Lib_PrnFeedPaper(int step);

	public static native int Lib_CTNPrnStart();

	public static native int Lib_PrnClose();

	public static native int Lib_PrnIsCharge( int ischarge);

	public static native int Lib_PrnConventional(int nlevel);

	public static native int Lib_PrnContinuous(int nlevel);
	/**
	 * 打印字符
	 * @param str 字符内容
	 * @return
	 */
	public static int Lib_PrnStr(String str){
		byte strbytes[] = null;
		try {

			System.out.println("original string---" + str);
			strbytes = str.getBytes("UnicodeBigUnmarked");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Lib_PrnStr(strbytes);
		return 0;
	}

	/**
	 *  打印条形码
	 * @param contents 条形码内容
	 * @param desiredWidth 宽
	 * @param desiredHeight 高
	 * @param barcodeFormat 条形码格式
	 * @return
	 */
	public int Lib_PrnBarcode(String contents, int desiredWidth,int desiredHeight, BarcodeFormat barcodeFormat){
		int iret;

        Bitmap bitmap= BarcodeCreater.creatBarcode(contents, desiredWidth, desiredHeight, barcodeFormat);
        
        iret = Lib_PrnBmp(bitmap);// 将BMP图片转成点阵
        if(iret != 0){
        	Log.e(tag, "Lib_PrnSendBmp fail, iret = " + iret);
        	return iret;
        }
		
		return iret;
	}

	/**
	 * 打印图片
	 * @param Bitmap
	 * @return
	 */
	public int Lib_PrnBmp(Bitmap bitmap) {
		int iRetCode = 0;

		PrinterBitmap pPrinterBmpLine = Bitmap2PrintDot(bitmap);

		int iBufferSize = pPrinterBmpLine.m_iRowBytes * pPrinterBmpLine.m_iHeight;
		byte[] byLogoBuffer = new byte[5 + iBufferSize];
		System.arraycopy(pPrinterBmpLine.m_pDotByteBuffer, 0, byLogoBuffer, 5, iBufferSize);
		byLogoBuffer[0] = (byte) (pPrinterBmpLine.m_iWidth / 256);
		byLogoBuffer[1] = (byte) (pPrinterBmpLine.m_iWidth % 256);
		byLogoBuffer[2] = (byte) (pPrinterBmpLine.m_iHeight / 256);
		byLogoBuffer[3] = (byte) (pPrinterBmpLine.m_iHeight % 256);
		iRetCode = Lib_PrnLogo(byLogoBuffer);
		if(iRetCode != 0){
			return iRetCode;
		}
//		Log.d("Prn_Bmp", "Lib_PrnLogo2 return = " + iRetCode);

		return iRetCode;
	}
	
	
	private PrinterBitmap Bitmap2PrintDot(Bitmap m_pBitmap){
		
		int iW = m_pBitmap.getWidth();
		int iH = m_pBitmap.getHeight();
		Log.i("iW = ", Integer.toString(iW));
		Log.i("iH = ", Integer.toString(iH));

		int iRowBytes = (iW + 7) / 8;//  
		Log.i("iRowBytes = ", Integer.toString(iRowBytes));
		int iBufferSize  = iRowBytes * iH;
		Log.i("iBufferSize = ", Integer.toString(iBufferSize));

		byte[] byBuffer = new byte[ iBufferSize ];
		for (int iBufferPos=0; iBufferPos<iBufferSize; ++iBufferPos){
			byBuffer[ iBufferPos ] = 0;
		}

		int x = 0;
		int y = 0;
		for (y = 0; y < iH; ++y){
			x = 0;
			for (int iRowByteIndex = 0; iRowByteIndex < iRowBytes; ++iRowByteIndex){
				for (int iBitIndex = 0; iBitIndex < 8; ++iBitIndex){
					x = iRowByteIndex * 8 + iBitIndex;
					if ( iW <= x ){
						break;
					}

					int iValue = m_pBitmap.getPixel(x, y);
					if (Color.BLACK == iValue){
						byBuffer[ y*iRowBytes + iRowByteIndex ] += Math.pow(2, 7-iBitIndex);//
					}
				}
			}		
		}

		PrinterBitmap bmp = new PrinterBitmap();
		bmp.m_pDotByteBuffer = byBuffer;
		bmp.m_iRowBytes = iRowBytes;
		bmp.m_iWidth = m_pBitmap.getWidth();
		bmp.m_iHeight = m_pBitmap.getHeight();

		return bmp;
	}

	/**
	 * 打印图类
	 */
	private class PrinterBitmap {
		public byte[] m_pDotByteBuffer = null;
		public int m_iRowBytes = 0;
		public int m_iWidth = 0;
		public int m_iHeight = 0;

		/**
		 * Construction default
		 */
		public PrinterBitmap(){
			m_pDotByteBuffer = null;
			m_iRowBytes = 0;
			m_iWidth = 0;
			m_iHeight = 0;
		}
	}


}
