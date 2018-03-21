package vpos.apipackage;


public class APDU_RESP {
	public short  LenOut = 0;
	public byte[] DataOut = new byte[512];
	public byte  SWA = 0;
	public byte  SWB = 0;

	public APDU_RESP(){

	}

	public APDU_RESP(short  LenOut, byte[] DataOut, byte  SWA, byte  SWB){
		this.LenOut = LenOut;
		this.DataOut = DataOut;
		this.SWA = SWA;
		this.SWB = SWB;
	}

	public APDU_RESP(byte[] resp){
//    	this.LenOut = (short)(resp[1] * 256 + resp[0]);
		this.LenOut = (short)((int)(resp[1] & 0xff) * 256 + (int)(resp[0] & 0xff));
//    	Log.e("", "LenOut 2 = " + LenOut);
		System.arraycopy(resp, 2, DataOut, 0, 512);
		this.SWA = resp[514];
		this.SWB = resp[515];
	}

	public short getLenOut(){
		return LenOut;
	}

	public byte[] getDataOut(){
		return DataOut;
	}

	public byte getSWA(){
		return SWA;
	}

	public byte getSWB(){
		return SWB;
	}
}
