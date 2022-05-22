package TH4;

import javacard.framework.*;

public class TH4 extends Applet
{
	final static byte SO_LUONG_MON_HOC = (byte)10;
	private byte [] monhoc, diem;
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new TH4();
		
	}
	private TH4()
	{
		//tao mang byte luu tru ID cua mon hoc
		monhoc = new byte[SO_LUONG_MON_HOC];
		//tao mang byte luu tru diem thi
		diem = new byte[SO_LUONG_MON_HOC];
		//dang ky Applet
		register();
	}
	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		switch (buf[ISO7816.OFFSET_INS])
		{
		case (byte)0x00:
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
