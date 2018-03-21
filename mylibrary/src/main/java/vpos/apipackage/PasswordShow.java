package vpos.apipackage;


import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;



/**   
 * @ClassName:  PasswordShow   
 * @Description:TODO  
 * @author: 
 * @date:   
 */  
public class PasswordShow{	
	
	private static native int Lib_GetPinEvent();
	
	private final String tag = "PasswordShow";
	public PasswordShow(Context ctx){
		mctx = ctx;
	}
	
	private static Dialog mdialog;
	private static BorderTextView textView;
	private static Context mctx;
	boolean isQuit = false;
	String amountString = null;
	byte mark;
    
	/*复写此函数，名字和参数必须一致，否则Jni库回调不到*/
    public int ShowDialog(byte mark, byte amount[]) {  
    	Log.d(tag, "ShowDialog iAmount = " + ByteUtil.bytearrayToHexString(amount, amount.length));
        this.mark = mark;
        amountString = new String(amount).trim();
        Log.e("amountString", "amountString = " + amountString);
    	SendMsg(3,"showMessage");
        
        isQuit = false;
		m_KBThread = new KB_Thread();
		m_KBThread.start();
        
        return 0;    
    }  
    
    public int DismissDialog() {  
    	Log.d(tag, "DismissDialog");
    	SendMsg(2,"disShowMessage");
    	isQuit = true;
        return 0;
    }  
    
	public void SendMsg(int iType, String strInfo) {
		if (null != handler) {
			Message msg = new Message();
			msg.what = iType;
			Bundle b = new Bundle();
			b.putString("MSG", strInfo);
			msg.setData(b);
			handler.sendMessage(msg);
		}
	}
	
	KB_Thread m_KBThread = null;
	public class KB_Thread extends Thread {
		private boolean m_bThreadFinished = false; 
		int ret, keyNum, keyNumOld = 0;
		int keycode;

		public boolean isThreadFinished() {
			return m_bThreadFinished;
		}

		public void run() {
			Log.e("KB_Thread[ run ]", "run() begin");
			synchronized (this) {
				while(isQuit == false){
					keyNum = Lib_GetPinEvent();
					if(keyNum < 0)
						continue;
					if(keyNum == 0x3b){//	Enter
						DismissDialog() ;
						return;
					}else if(keyNum == 0x1c){//	Cancel
						DismissDialog() ;
						return;
					}else {
						String strInfo = "";
						for (int i = 0; i < keyNum; ++i) {
							strInfo += "*";
						}
						
						SendMsg(4, strInfo);
					}				
				}
			}
		}
	};
	
	private Handler handler = new Handler(Looper.getMainLooper()) {
		@SuppressLint("InlinedApi")
		@Override
		public void handleMessage(Message msg) {
			Log.d(tag, "msg.what=" + msg.what);
			switch (msg.what) {
			case 1: {
				break;
			}
			case 2: {
				Bundle b = msg.getData();
				String strInfo = b.getString("MSG");
				Log.i(tag, strInfo);
				mdialog.dismiss();
				Log.d(tag, "mdialog.dismiss();");
				break;
			}
			case 3: {
				Bundle b = msg.getData();
				String strInfo = b.getString("MSG");
				Log.i("MSG_WHAT_ID_NATION", strInfo);
		        AlertDialog.Builder builder = new AlertDialog.Builder(mctx);   
		        String tittle = "";
		        if(mark != 0)
		        	tittle += "Amount: " + amountString + "\n";
		        tittle += "Input pin: ";
		        builder.setTitle(tittle); 
		        textView = new BorderTextView(mctx);
		        textView.setGravity(android.view.Gravity.CENTER);

		        builder.setCancelable(false);
		        builder.setView(textView);
		        mdialog = builder.show(); 

		        WindowManager.LayoutParams layoutParams = mdialog.getWindow().getAttributes();  
		        layoutParams.width = 500;  
		        layoutParams.height = LayoutParams.WRAP_CONTENT;  
		        mdialog.getWindow().setAttributes(layoutParams); 
				
				break;
			}

			case 4: {
				Bundle b = msg.getData();
				String strInfo = b.getString("MSG");
				Log.d(tag, strInfo);
				textView.setTextSize(40);
				textView.setText(strInfo);
				break;
			}
			
			default: {
				Bundle b = msg.getData();
				String strInfo = b.getString("MSG");
				Log.d(tag, strInfo);
				break;
			}
			}
		}
	};
	
	@SuppressLint("DrawAllocation")  
	public class BorderTextView extends TextView {
	  
	    public BorderTextView(Context context) {  
	        super(context);  
	    }  
	    public BorderTextView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    }  
	    private int sroke_width = 10;  
	    @Override  
	    protected void onDraw(Canvas canvas) {  
	        Paint paint = new Paint();  
	        //  将边框设为黑色  
	        paint.setColor(android.graphics.Color.BLUE);  
	        //  画TextView的4个边  
	        canvas.drawLine(0, 0, this.getWidth() - sroke_width, 0, paint);  
	        canvas.drawLine(0, 0, 0, this.getHeight() - sroke_width, paint);  
	        canvas.drawLine(this.getWidth() - sroke_width, 0, this.getWidth() - sroke_width, this.getHeight() - sroke_width, paint);  
	        canvas.drawLine(0, this.getHeight() - sroke_width, this.getWidth() - sroke_width, this.getHeight() - sroke_width, paint);  
	        super.onDraw(canvas);  
	    }  
	}  
}
