package vpos.apipackage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public abstract class BarcodeCreater {
	
	  /**
	   * 图片锟斤拷锟斤拷锟斤拷锟斤拷锟侥空白的匡拷锟?
	   */
	   private static int marginW=20; 
	   /**
	    * 锟斤拷锟斤拷锟斤拷谋锟斤拷锟斤拷锟斤拷锟?
	    */
	   private static BarcodeFormat barcodeFormat= BarcodeFormat.CODE_128;

	   /**
	    * 锟斤拷锟斤拷锟斤拷锟斤拷锟?
	    * @param context
	    * @param contents  锟斤拷要锟斤拷傻锟斤拷锟斤拷锟?
	    * @param desiredWidth 锟斤拷锟斤拷锟斤拷锟斤拷锟侥匡拷锟?
	    * @param desiredHeight 锟斤拷锟斤拷锟斤拷锟斤拷锟侥高讹拷
	    * @param displayCode 锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟铰凤拷锟斤拷示锟斤拷锟斤拷
	    * @return
	    */
	   public static Bitmap creatBarcode(Context context,String contents ,
		        int desiredWidth,int desiredHeight,boolean displayCode){
		Bitmap ruseltBitmap=null;
		if (displayCode) {
			Bitmap barcodeBitmap=encodeAsBitmap(contents, barcodeFormat, desiredWidth, desiredHeight);
			Bitmap codeBitmap=creatCodeBitmap(contents, desiredWidth+2*marginW, desiredHeight, context);
			ruseltBitmap=mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(0, desiredHeight));
		} else {
			ruseltBitmap=encodeAsBitmap(contents,barcodeFormat, desiredWidth, desiredHeight);
		}
		
		return ruseltBitmap;
	}
	   
   public static Bitmap creatBarcode(String contents, int desiredWidth,int desiredHeight, BarcodeFormat barcodeFormat){
		Bitmap ruseltBitmap=null;
		
		ruseltBitmap=encodeAsBitmap(contents,barcodeFormat, desiredWidth, desiredHeight);
		
		return ruseltBitmap;
	}
	   
	 /**
	  * 锟斤拷锟斤拷锟绞撅拷锟斤拷锟斤拷Bitmap
	  * @param contents
	  * @param width
	  * @param height
	  * @param context
	  * @return
	  */
	protected static Bitmap creatCodeBitmap(String contents,int width,int height,Context context) {
		TextView tv=new TextView(context);
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setHeight(height);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);  
        tv.setTextColor(Color.BLACK);
        tv.measure(  
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),  
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
        tv.layout(0, 0, tv.getMeasuredWidth(),  
        		tv.getMeasuredHeight());
  
        tv.buildDrawingCache();  
        Bitmap bitmapCode=tv.getDrawingCache();
        return bitmapCode;
	}
	   
	   /**
	    * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷Bitmap
	    * @param contents  锟斤拷要锟斤拷傻锟斤拷锟斤拷锟?
	    * @param format    锟斤拷锟斤拷锟绞?
	    * @param desiredWidth 
	    * @param desiredHeight
	    * @return
	    * @throws WriterException
	    */
	  protected  static Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
		        int desiredWidth, int desiredHeight){
		        final int WHITE = 0xFFFFFFFF; 
		        final int BLACK = 0xFF000000;
		        
		        MultiFormatWriter writer = new MultiFormatWriter();
		        BitMatrix result=null;
				try {
					result = writer.encode(contents, format, desiredWidth,
					        desiredHeight, null);
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        int width = result.getWidth();
		        int height = result.getHeight();
		        int[] pixels = new int[width * height];
		        // All are 0, or black, by default
		        for (int y = 0; y < height; y++) {
		            int offset = y * width;
		            for (int x = 0; x < width; x++) {
		                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
		            }
		        }

		        Bitmap bitmap = Bitmap.createBitmap(width, height,
		                Config.ARGB_8888);
		        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		        writer = null;
		        result = null;
		        return bitmap;
		    }

		    
		 /**
		  * 锟斤拷锟斤拷锟斤拷Bitmap锟较诧拷锟斤拷一锟斤拷
		  * @param first
		  * @param second
		  * @param fromPoint 锟节讹拷锟斤拷Bitmap锟斤拷始锟斤拷锟狡碉拷锟斤拷始位锟矫ｏ拷锟斤拷锟斤拷诘锟揭伙拷锟绍itmap锟斤拷
		  * @return 
		  */
		    protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,PointF fromPoint) {  
		        if (first == null || second == null || fromPoint == null) {  
		            return null;  
		        }  
		        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth()+second.getWidth()+marginW, first.getHeight()+second.getHeight()
		        		, Config.ARGB_4444);  
		        Canvas cv = new Canvas(newBitmap);  
		        cv.drawBitmap(first,marginW,0,null);  
		        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);  
		        cv.save(Canvas.ALL_SAVE_FLAG);  
		        cv.restore();
		        
		        return newBitmap;  
		    }   
}
