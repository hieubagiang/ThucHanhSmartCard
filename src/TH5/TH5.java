package TH5;

import javacard.framework.*;
import TH5.masterInterface;

public class TH5 extends Applet
{
	/*
		DAY LA BAI 2
		MAY CHU MASTER LA : thuchanh_5_master
		lenh chon applet : /select 11223344557711
		lenh xem diem : 
			/send 00000100    xem diem sinh vien co id 0x01  
			*(mang diem sinh vien luu trong master thuchanh_5_master)
		
	*/
	
	// AID applet master ( aid cua applet thuchanh_5_master)
	final static byte[] serAID = new byte[]{0x11,0x22,0x33,0x44,0x55,0x55,0x11,0x00}; 

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new TH5().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}
	
	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		short byteRead = (short) apdu.setIncomingAndReceive();
		switch (buf[ISO7816.OFFSET_INS])
		{
		case (byte)0x00:
			AID masAID = (AID) (JCSystem.lookupAID(serAID, (short)0, (byte)serAID.length));
			masterInterface sio = (masterInterface) JCSystem.getAppletShareableInterfaceObject(masAID, (byte)0x01);
			buf[0] = (byte)sio.xemdiem(buf[ISO7816.OFFSET_P1]);
			apdu.setOutgoingAndSend((short)0, (short)1);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
}
