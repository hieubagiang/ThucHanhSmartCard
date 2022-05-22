package Buoi2;

import javacard.framework.*;

public class NameAndDateInput extends Applet
{

	byte[] hoten = new byte[256];		
	byte[] ngaysinh = new byte[256];
	short nameLength = 0x0000;
	short birthdayLen = 0x0000;
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new NameAndDateInput().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}
public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}
		// byte[] hoten = new byte[256];		
		// byte[] ngaysinh = new byte[256];
		byte[] buf = apdu.getBuffer();
		short byteRead = (short)(apdu.setIncomingAndReceive());
		short dataLen = (short)(buf[ISO7816.OFFSET_LC]&0xff);
		short buflen = (short)buf.length;
		short pointer = 0;
		switch (buf[ISO7816.OFFSET_INS])
		{
		case (byte)0x01:
			if(buf[ISO7816.OFFSET_P1] == (byte)0x01) {
				 nameLength = (short)dataLen;
				 Util.arrayCopy(buf, ISO7816.OFFSET_CDATA,hoten, pointer, byteRead);

					// while ( dataLen > 0){
						// // copy du lieu tu buf vao bien tam buf_temp
						// Util.arrayCopy(buf, ISO7816.OFFSET_CDATA,hoten, pointer, byteRead);
						// pointer += byteRead;
						// dataLen -= byteRead;
						// byteRead = apdu.receiveBytes(ISO7816.OFFSET_CDATA ); // doc them du lieu
					// }
			}
			if(buf[ISO7816.OFFSET_P1] == (byte)0x02) {
				 birthdayLen = (short)dataLen;
				 Util.arrayCopy(buf, ISO7816.OFFSET_CDATA,ngaysinh, pointer, byteRead);
					// while ( dataLen > 0){
						// // copy du lieu tu buf vao bien tam buf_temp
						// Util.arrayCopy(buf, ISO7816.OFFSET_CDATA,ngaysinh, pointer, byteRead);
						// pointer += byteRead;
						// dataLen -= byteRead;
						// byteRead = apdu.receiveBytes(ISO7816.OFFSET_CDATA ); // doc them du lieu
					// }
					
			}
			break;
		case (byte)0x02:
			if(buf[ISO7816.OFFSET_P1] == (byte)0x01) {
					// short nameLen = (short)hoten.length;
					Util.arrayCopy(hoten, (short)0, buf, (short)0,nameLength);
					apdu.setOutgoingAndSend((short)0, (short)nameLength);
					break;
			}
			if(buf[ISO7816.OFFSET_P1] == (byte)0x02) {
					// short ngaysinhLen = (short)ngaysinh.length;
					Util.arrayCopy(ngaysinh, (short)0, buf, (short)0,birthdayLen);
					apdu.setOutgoingAndSend((short)0, (short)birthdayLen);
					break;
			}		
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
}