package TH4;

import javacard.framework.*;

public class BTVN_TH4 extends Applet
{
	final static byte SV_ID_LENGTH = (byte)0x04;

	private static byte [] result, sinhVien;
	private static byte totalSubject;
	private static short index = 0; //array Index
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new BTVN_TH4(bArray, bOffset, bLength);
		result = new byte[20]; 
	}
	
	private BTVN(byte[] bArray, short bOffset, byte bLength) {
		
		//check input
		byte iLen = bArray[bOffset]; // AID length
		if(iLen == 0) 
		{
			//default AID
			register();
		}
		else 
		{
			//AID in run config
			register(bArray, (short)(bOffset +1), iLen);
		}
		bOffset = (short) (bOffset+iLen+1);
		byte cLen = bArray[bOffset];
		bOffset = (short) (bOffset+cLen+1);
		byte aLen = bArray[bOffset]; // applet data length
		bOffset = (short) (bOffset + 1);
		// read
		if (aLen != 0)
		{
			//set student id
			sinhVien = new byte[SV_ID_LENGTH];
			Util.arrayCopy(bArray, bOffset, sinhVien, (byte)0, SV_ID_LENGTH);
			bOffset += SV_ID_LENGTH;
			//set number arrray
			totalSubject = bArray[bOffset];
		}
		else
		{
			//hard code Student ID and Total subject
			sinhVien = new byte[] {'S', 'V', '0','1'};
			totalSubject = (byte)0x08;
		}
		
			
	}
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
			//print student id and total subject
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)5);
			apdu.sendBytesLong(sinhVien, (short)0, SV_ID_LENGTH);
			buf[0] = totalSubject;
			apdu.sendBytes((short)0, (short)1);
			break;
		case (byte)0x01:
			//input student id and total subject
			input(apdu,buf);
			break;	
		case (byte)0x02:
			//show subject and mark
			showSubjectAndMark(apdu,buf);
			break;	
		case (byte)0x03:
			//update mark 
			updateMark(apdu,buf);
			break;
		case (byte)0x04:
			//delete mark
			deleteMark(apdu,buf);
			break;					
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	
	void input(APDU apdu, byte[] buf) {
		short idSubject = buf[ISO7816.OFFSET_P1];
		short idPoint = buf[ISO7816.OFFSET_P2];
		boolean isExist = false;
		
		if(index == 0) {
			result[index] = (byte)idSubject;
			index = (short)(index + 1);
			result[index] = (byte) idPoint;
			index = (short)(index + 1);
		}
		else {
			for(short i = 0; i< index ; i = (short)(i + 2)) {
				if(result[i] == idSubject) {
					isExist = true;
					return;
				}
			}
			if(!isExist) {
				result[index] = (byte)idSubject;
				index = (short)(index + 1);
				result[index] = (byte) idPoint;
				index = (short)(index + 1);
			}
			
		}
		showSubjectAndMark(apdu, buf);
	}
	void showSubjectAndMark(APDU apdu, byte[] buf) {
		// copy mark to bufffer
		Util.arrayCopy(result, (short) 0, buf, (short) 0, index);
		apdu.setOutgoingAndSend((short) 0, index);
		
		
	}
	
	void updateMark(APDU apdu, byte[] buf) {
		short idSubject = buf[ISO7816.OFFSET_P1];
		short idPoint = buf[ISO7816.OFFSET_P2];
		boolean isExist = false;
		for(short i = 0; i < index ; i = (short) (i + 2)) {
			if(result[i] == idSubject) {
				result[(short)(i + 1)] = (byte)idPoint;
			}
		}
		showSubjectAndMark(apdu, buf);
	}
	
	void deleteMark(APDU apdu, byte[] buf) {
		short idSubject = buf[ISO7816.OFFSET_P1];
		for(short i = 0; i < index ; i = (short) (i + 2)) {
			if(result[i] == idSubject) {
				for(short j = (short) (i+2); j < index ; j ++){
					result[i++] = result[j];
				}
				index = (short) (index - 2);
			}
		}
		showSubjectAndMark(apdu, buf);
	}
	

}
