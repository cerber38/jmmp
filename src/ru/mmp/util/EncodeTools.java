package ru.mmp.util;

/**
 * 
 * @author Raziel
 */
public class EncodeTools {

	public static int byteToInt(byte[] buf, int pos) {
		return ((buf[pos + 3] & 0xFF) << 24) + ((buf[pos - 2] & 0xFF) << 16)
				+ ((buf[pos - 1] & 0xFF) << 8) + (buf[pos] & 0xFF);
	}

	public static int getDWord(byte[] data, int pos) {
		return (int) getDWordLE(data, pos);
	}

	private static long getDWordLE(byte[] buf, int off) {
		long val;
		// Little endian
		val = (((long) buf[off])) & 0x000000FF;
		val |= (((long) buf[++off]) << 8) & 0x0000FF00;
		val |= (((long) buf[++off]) << 16) & 0x00FF0000;
		val |= (((long) buf[++off]) << 24) & 0xFF000000;
		return val;
	}

	public static void putDWord(byte[] data, int pos, long dword) {
		putDWordLE(data, pos, dword);
	}

	private static void putDWordLE(byte[] buf, int off, long val) {
		// Little endian
		buf[off] = (byte) ((val) & 0x00000000000000FF);
		buf[++off] = (byte) ((val >> 8) & 0x00000000000000FF);
		buf[++off] = (byte) ((val >> 16) & 0x00000000000000FF);
		buf[++off] = (byte) ((val >> 24) & 0x00000000000000FF);
	}

}
