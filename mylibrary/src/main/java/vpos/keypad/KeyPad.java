package vpos.keypad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cspos.R;

import java.util.List;
import java.util.Random;

import vpos.util.Util;


public class KeyPad{

	Context mContext;
	AlertDialog dAlertDialog;
	private Keyboard mNumKeyboard; // 閺佹澘鐡ч柨顔炬磸鐎电钖�
	private EditText mEditText;
	private static final int MSG_WHAT_SHOW_DIALOG = 0;
	private static final int MSG_WHAT_HIDE_DIALOG = 1;
	private static final int MSG_WHAT_CLEAR_BUFFER = 2;
	private static final int TIMEOUT_MS = 30 * 1000;
	private boolean mIsInputFinish;

	final String tag = "KeyPad";
	int keyInputResult = -1;
	int keyInputMinLength = 0;
	int keyInputMaxLength = 0;
	String mTittleString = "";

	public KeyPad(Context context) {
//		super(context);
		this.mContext = context;
	}

	//	public KeyPad(Context ctx) {
//		this.mContext = ctx;
//	}

	public int ShowKeyPad(String tittle, final byte[] input, final byte[] input_len, int Minlength, int Maxlength) {
	
		long currentTime, startTime;
		mIsInputFinish = false;
		this.keyInputMinLength = Minlength;
		this.keyInputMaxLength = Maxlength;
		mTittleString = tittle;
		
		Log.e(tag, "ShowKeyPad ===");

		startTime = currentTime = System.currentTimeMillis();
		SendMsg(MSG_WHAT_SHOW_DIALOG,"");

		while(!mIsInputFinish){

			currentTime = System.currentTimeMillis();

			setIFinishInput(new IFinishInput() {
				public void isFinish(int finishResult) {

                    Log.e(tag, "ShowKeyPad ===   ....");

                    keyInputResult = finishResult;

					if(finishResult==0){
                        String inputString = mEditText.getText().toString();
                        Log.e(tag, "===ShowKeyPad  data= "+inputString);
                        Log.e(tag, "===ShowKeyPad  len= "+inputString.length());
                        input_len[0]=(byte) inputString.length();
                        System.arraycopy(inputString.getBytes(), 0, input, 0, inputString.length());
                    }

					HideKeyPad();
				}
			});

            Log.e(tag, "ShowKeyPad ===   ....currentTime :"+currentTime);


            if((currentTime - startTime) > TIMEOUT_MS){
//				keyInputResult = -1;
				//modify by liuhao 1218
				keyInputResult = 0;
				HideKeyPad();
				break;
			}
			Util.sleepMs(50);
		}

        Log.e(tag, "ShowKeyPad ===  isFinish ....3333");


//		setIFinishInput(new IFinishInput() {
//			@Override
//			public void isFinish(int inputResult) {
//
//				Log.e(tag,"ShowKeyPad ....keyInputResult ...");
//
//				if(inputResult == 0){	//input finish
//
//					keyInputResult = inputResult;
//
//					String inputString = mEditText.getText().toString();
//					Log.e(tag, "===ShowKeyPad  data= "+inputString);
//					Log.e(tag, "===ShowKeyPad  len= "+inputString.length());
//					input_len[0]=(byte) inputString.length();
//					System.arraycopy(inputString.getBytes(), 0, input, 0, inputString.length());
//				}
//			}
//		});

//		if(keyInputResult == 0){	//input finish
//			String inputString = mEditText.getText().toString();
//			Log.e(tag, "===ShowKeyPad  data= "+inputString);
//			Log.e(tag, "===ShowKeyPad  len= "+inputString.length());
//			input_len[0]=(byte) inputString.length();
//			System.arraycopy(inputString.getBytes(), 0, input, 0, inputString.length());
//		}
		
		ClearBuffer();	//timeout or cancel

		return keyInputResult;
	}

	public void HideKeyPad() {
		SendMsg(MSG_WHAT_HIDE_DIALOG,"");
		
		mIsInputFinish = true;
	}
	
	public void ClearBuffer() {
		SendMsg(MSG_WHAT_CLEAR_BUFFER,"");
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
	
	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WHAT_SHOW_DIALOG: {

				LayoutInflater inflater = LayoutInflater.from(mContext);
				View layout = inflater.inflate(R.layout.keypad_dialog_layout, null);
				StockKeyboardView mKeyboardView = (StockKeyboardView) layout.findViewById(R.id.keyboard_view);

				mEditText = (EditText) layout.findViewById(R.id.pwdEdtiInput);
				mEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
				mNumKeyboard = new Keyboard(mContext, R.xml.symbols);
				mKeyboardView.requestFocus();
				randomNumKey();
				mKeyboardView.setKeyboard(mNumKeyboard);
				mKeyboardView.setEnabled(true);
				mKeyboardView.setPreviewEnabled(false);
				mKeyboardView.setOnKeyboardActionListener(listener);

				//add by liuhao 1218
				Log.e("liuhao" ,"MSG_WHAT_SHOW_DIALOG");

				mKeyboardView.setMyViewFocusInterface(new StockKeyboardView.MyViewFocusInterface() {
					public void isNoFocus() {
//						mEditText.setText("000000");
						keyInputResult = -1;
						//add by liuhao 20180302
                        if(mIFinishInput!=null){
                            mIFinishInput.isFinish(keyInputResult);
                        }

						HideKeyPad();
					}
				});

				dAlertDialog = new AlertDialog.Builder(mContext).setTitle(mTittleString).setView(layout).show();
				dAlertDialog.setCanceledOnTouchOutside(false);

				//add by liuhao 20180302
                dAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                        Log.e("liuhao KeyEvent" ,".....start.....");

                        if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_UP) {

                            Log.e("liuhao KeyEvent" ,".....back key.....");

                            keyInputResult = -3;
                            //add by liuhao 20180302
                            if(mIFinishInput!=null){
                                mIFinishInput.isFinish(keyInputResult);
                            }

                        }
                        return false;
                    }
                });

				break;
			}
			case MSG_WHAT_HIDE_DIALOG: {
				if (dAlertDialog != null) {
					dAlertDialog.dismiss();
					dAlertDialog=null;
					mContext=null;
				}
				
				mIsInputFinish = true;
				break;
			}
			
			case MSG_WHAT_CLEAR_BUFFER: {
				mEditText.setText("");
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

	interface IFinishInput {
		void isFinish(int finishResult);
	}

	IFinishInput mIFinishInput;
	public void setIFinishInput(IFinishInput iFinishInput){
		this.mIFinishInput=iFinishInput;
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {

		@Override
		public void swipeUp() {
			// TODO Auto-generated method stub
		}

		@Override
		public void swipeRight() {
			// TODO Auto-generated method stub
		}

		@Override
		public void swipeLeft() {
			// TODO Auto-generated method stub
		}

		@Override
		public void swipeDown() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onText(CharSequence text) {
			// TODO Auto-generated method stub
		}

		public void onRelease(int primaryCode) {
			// TODO Auto-generated method stub
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {
			} else if (primaryCode == Keyboard.KEYCODE_DONE) {
				
			}

			Log.e(tag, "onRelease primaryCode = " + primaryCode);
		}

		public void onPress(int primaryCode) {
			// TODO Auto-generated method stub
			// sp.play(music, 1, 1, 0, 0, (float) 1); //Beep
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			// TODO Auto-generated method stub
			//Pci.Lib_PciBeep();
		//	Sys.Lib_Beep();
			Editable editable = mEditText.getText();
			int start = mEditText.getSelectionStart();
			switch (primaryCode) { // 閹稿鏁璫odes缂傛牜鐖�
			case Keyboard.KEYCODE_DELETE:// 閸掔娀娅�
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);// 瀵拷顫愰敍宀�波閺夌喍缍呯純锟�					
						}
				}
				break;
			case Keyboard.KEYCODE_CANCEL: //
				keyInputResult = -2;

				//add by liuhao 20180302
                if(mIFinishInput!=null){
                    mIFinishInput.isFinish(keyInputResult);
                }

				HideKeyPad();
				Log.e(tag, "Press Cancel");
				break;
			case Keyboard.KEYCODE_DONE:
		
				Log.e(tag, "Press Enter1");
				if ((mEditText.getText().length() > keyInputMinLength-1) && (mEditText.getText().length() < keyInputMaxLength+1)){
					keyInputResult = 0;
					//add by liuhao 20180302
                    if(mIFinishInput!=null){
                        mIFinishInput.isFinish(keyInputResult);
                    }

					HideKeyPad();
				}

				Log.e(tag, "Press Enter");
				break;
			default:
				Log.e(tag, "length="+mEditText.getText().length());
				Log.e(tag, "length="+(keyInputMaxLength));
				if (mEditText.getText().length() < keyInputMaxLength)
					editable.insert(start, Character.toString((char) primaryCode));
				break;
			}
		}
	};
	
	/**
	 * 闂呭繑婧�弫鏉跨摟闁款喚娲�闂呭繑婧�柨顔炬磸LABEL娑擃厺绗夐懗钘夌摠閸︺劌娴橀悧鍥风礉閸氾箑鍨崷銊╂閺堢儤宕叉担宥堢箖缁嬪鑵戞导姘Г闁匡拷
	 */
	private void randomNumKey(){
		List<Key> keyList = mNumKeyboard.getKeys();
		int size = keyList.size() - 3;
//		Log.e(tag, "size = " + size);
		int num[] = getSequence(size);

		for (int i = 0; i < size; i++) {
//			Log.e(tag, "num[i] = " + (num[i]));
			keyList.get(i).codes[0] = num[i]+0x30;
			keyList.get(i).label = "" + num[i];
		}
	}
	
	//鐏忓棔绔存稉顏堟毐鎼达缚璐焠o閻ㄥ嫭婀佹惔蹇旀殶缂佸嫯娴嗘稉娲閺堟椽銆庢惔蹇曟畱閺佹壆绮�
    public static int[] getSequence(int no) {
        int[] sequence = new int[no];
        for(int i = 0; i < no; i++){
            sequence[i] = i + 1;
        }

        Random random = new Random();
        for(int i = 0; i < no; i++){
            int p = random.nextInt(no);
            int tmp = sequence[i];
            sequence[i] = sequence[p];
            sequence[p] = tmp ;
        }
        random = null;
        return sequence;
    }
	
	public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
		public CharSequence getTransformation(CharSequence source, View view) {
		    return new PasswordCharSequence(source);
		}
		 
		private class PasswordCharSequence implements CharSequence {
		    private CharSequence mSource;
		    public PasswordCharSequence(CharSequence source) {
		        mSource = source; // Store char sequence
		    }
		    public char charAt(int index) {
//		    	Log.e("", "charAt index  ====== " + mSource.charAt(index)); 
		    	if(mSource.charAt(index)> '9' || mSource.charAt(index)< '0' )
		    		return 0;
		    	
		        return '*'; // This is the important part
		    }
		    public int length() {
		        return mSource.length(); // Return default
		    }
		    public CharSequence subSequence(int start, int end) {
		        return mSource.subSequence(start, end); // Return default
		    }
		}
	}

}
