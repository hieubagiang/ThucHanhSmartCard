package Buoi2;

import javacard.framework.*;

public class NameAndDate extends Applet
{

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new NameAndDate().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}
	byte[] hoTen = {'P', 'h', 'a', 'm', 'D','o','a','n','H', 'i', 'e', 'u'};
	byte[] ngaySinh = {'0','5','/','0','4','/','2','0','0','0'};

	short len1;
	short len2;
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
					len1 = (short)hoTen.length;
					Util.arrayCopy(hoTen, (short)0, buf,(short)0, (short)len1);
					apdu.setOutgoingAndSend((short)0, (short)(len1) );
					break;
				}
				case (byte)0x11:
				{
					len1 = (short)ngaySinh.length;
					Util.arrayCopy(ngaySinh, (short)0, buf,(short)0, (short)len1);
					apdu.setOutgoingAndSend((short)0, len1);
					break;
				}
				
			case (byte)0x22:
				{
					
					len1 = (short)hoTen.length;
					len2 = (short)ngaySinh.length;
					Util.arrayCopy(hoTen, (short)0, buf,(short)0, (short)len1);
					Util.arrayCopy(ngaySinh, (short)0, buf,(short)len1, (short)len2);
					apdu.setOutgoingAndSend((short)0, (short)(len1 + len2));
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
