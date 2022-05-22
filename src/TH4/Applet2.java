package TH4;

import javacard.framework.*;

public class Applet2 extends Applet {
  final static byte SV_ID_LENGTH = (byte) 0x04;

  private static byte[] diemThi, sinhVien;
  private static byte soLuongMonThi;
  public static void install(byte[] bArray, short bOffset,
    byte bLength) {
    new Applet2(bArray, bOffset, bLength);
  }

  private Applet2(byte[] bArray, short bOffset, byte bLength) {

    // kiem tra du lieu den
    byte iLen = bArray[bOffset]; // do dai AID
    if (iLen == 0) {
      //dang ky Applet voi JCRE su dung AID mac dinh
      register();
    } else {

      //dang ky Applet voi JCRE su dung AID duoc chi dinh trong tham so cai dat
      register(bArray, (short)(bOffset + 1), iLen);
    }
    bOffset = (short)(bOffset + iLen + 1);
    byte cLen = bArray[bOffset]; // do dai thong tin dieukhien
    bOffset = (short)(bOffset + cLen + 1);
    byte aLen = bArray[bOffset]; // do dai du lieu Applet
    bOffset = (short)(bOffset + 1);
    // doc du lieu Applet
    if (aLen != 0) {
      //gan ID cua sinh vien
      sinhVien = new byte[SV_ID_LENGTH];
      Util.arrayCopy(bArray, bOffset, sinhVien, (byte) 0,
        SV_ID_LENGTH);
      bOffset += SV_ID_LENGTH;
      //gan so mon thi
      soLuongMonThi = bArray[bOffset];
    } else {
      //gan ID cua sinh vien va so luong mon thi
      sinhVien = new byte[] {
        'S',
        'V',
        '0',
        '1'
      };
      soLuongMonThi = (byte) 0x08;
    }

  }
  public void process(APDU apdu) {
    if (selectingApplet())

    {
      return;
    }
    byte[] buf = apdu.getBuffer();
    apdu.setIncomingAndReceive();
    switch (buf[ISO7816.OFFSET_INS]) {
    case (byte) 0x00:
      //in ra ID cua sinh vien va so luong mon thi
      apdu.setOutgoing();
      apdu.setOutgoingLength((short) 5);
      apdu.sendBytesLong(sinhVien, (short) 0,
        SV_ID_LENGTH);
      buf[0] = soLuongMonThi;
      apdu.sendBytes((short) 0, (short) 1);
      break;
    default:

      ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
    }
  }
}