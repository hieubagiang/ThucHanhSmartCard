package Buoi2;

import javacard.framework.*;

/*
	 ho + ten INS = 0x00 và P1 == 0x00
	lop INS = 0x00 và P1 == 0x11
	*/
public class Bai2 extends Applet
{

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new Bai2().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}
	byte[] ho = {'P', 'h', 'a', 'm', 'D','o','a','n'};
	byte[] ten = {'H', 'i', 'e', 'u'};
	byte[] lop = {'C', 'T', '3', 'D'};
	short len1;
	short len2;
	short len3;

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		apdu.setIncomingAndReceive();
		switch (buf[ISO7816.OFFSET_INS])
		{
		case (byte)0x00:
		{
			
			short p1 = buf[ISO7816.OFFSET_P1];
			 switch (p1)
			 {
				case (byte)0x00:
				{
					len1 = (short)ho.length;
					len2 = (short)ten.length;
					Util.arrayCopy(ho, (short)0, buf,(short)0, (short)len1);
					Util.arrayCopy(ten, (short)0, buf,(short)len1, (short)len2);
					apdu.setOutgoingAndSend((short)0, (short)(len1 + len2));
					break;
				}
				case (byte)0x11:
				{
					len1 = (short)lop.length;
					Util.arrayCopy(lop, (short)0, buf,(short)0, (short)len1);
					apdu.setOutgoingAndSend((short)0, len1);
					break;
				}
				case (byte)0x10:
				{
					len3 = (short)(len1+len2);
					Util.arrayCopy(ho, (short)0, buf,(short)0, (short)len1);
					Util.arrayCopy(ten, (short)0, buf,(short)len1, (short)len2);
					Util.arrayCopy(lop, (short)0, buf,(short)len3,(short)lop.length);
					apdu.setOutgoingAndSend((short)0, (short)(len3+lop.length));
					break;
				}
			
				default:
					ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
			 }
			break;
		}
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
