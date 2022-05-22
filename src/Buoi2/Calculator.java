package Buoi2;

import javacard.framework.*;

public class Calculator extends Applet
{

	// khai bao ma CLA cua applet
		final static byte Calculator_CLA = (byte)0xA0;
		// khai bao ma INS trong tieu de cua apdu

		final static byte INS_CONG = (byte)0x00;
		final static byte INS_TRU = (byte)0x01;
		final static byte INS_NHAN = (byte)0x02;
		final static byte INS_CHIA = (byte)0x03;
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new Calculator().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();

		// dat apdu o che do nhan du lieu
		apdu.setIncomingAndReceive();

		// kiem tra xem CLA nhap vao co hop le hay khong
		if (buf[ISO7816.OFFSET_CLA] != Calculator_CLA)
		ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		short res = 0;
		short tmp1 = buf[ISO7816.OFFSET_P1];
		short tmp2 = buf[ISO7816.OFFSET_P2];

		switch (buf[ISO7816.OFFSET_INS])
		{
			case INS_CONG:
				res = (short)(tmp1 + tmp2);
				break;
			case INS_TRU:
				res = (short)(tmp1 - tmp2);
				break;
			case INS_NHAN:
				res = (short)(tmp1 * tmp2);
				break;
			case INS_CHIA:
				res = (short)(tmp1 / tmp2);
				break;
			default:

			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}

		Util.setShort(buf, (short)0, res);

		// gui du lieu len may chu

		// dat apdu sang che do gui du lieu
		short le = apdu.setOutgoing();

		if (le < (short)2) ISOException.throwIt(
		ISO7816.SW_WRONG_LENGTH );

		// gui cho may chu so byte se gui
		apdu.setOutgoingLength( (short)2 );
		// gui du lieu cho may chu
		apdu.sendBytes((short)0, (short)2);

		/*Chu y: doi voi mot phan hoi ngan nhu trong truong hop nay,
		chung ta co the ket hop 3 phuong thuc
		apdu.setOutgoing(),
		apdu.setOutgoingLength()
		apdu.sendBytes()
		bang 1 phuong thuc duy nhat
		apdu.setOutgoingAndSend() */

		// apdu.setOutgoingAndSend((short)0, (short)2);
		
	}
}
