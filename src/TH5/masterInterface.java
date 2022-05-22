package TH5;

import javacard.framework.Shareable;

public interface masterInterface extends Shareable{
	public short getArray(byte[] array);
	public byte tinhdiem(byte toan, byte van);
	public byte xemdiem(byte id);
}
