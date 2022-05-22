package TH3;

import javacard.framework.*;

public class VD2 extends Applet
{

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new VD2().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu) {
		if (selectingApplet()) {
			return;
		}

		byte[] buf = apdu.getBuffer();
		apdu.setIncomingAndReceive();
		switch (buf[ISO7816.OFFSET_INS]) {
			case (byte) 0x00:
				byte[] buffer1 = new byte[3];
				byte[] buffer2 =
					JCSystem.makeTransientByteArray((short) 3,
						JCSystem.CLEAR_ON_RESET);

				JCSystem.beginTransaction();
				for (byte i = 0; i < 3; i++) {
					buffer1[i] = (byte)(i + 1);
					buffer2[i] = (byte)(2 * (byte)(i + 1));
				}
				byte a_local = 1;
				JCSystem.abortTransaction();

				apdu.setOutgoing();
				apdu.setOutgoingLength((short) 7);
				apdu.sendBytesLong(buffer1, (short) 0, (short) 3);
				apdu.sendBytesLong(buffer2, (short) 0, (short) 3);
				Util.arrayFillNonAtomic(buf, (short) 0, (short) 1,
					a_local);
				apdu.sendBytes((short) 0, (short) 1);
				break;
			default:

				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
