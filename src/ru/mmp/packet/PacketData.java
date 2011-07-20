package ru.mmp.packet;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Vladimir Krukov
 */
public class PacketData {

	private ByteArrayOutputStream out;
	byte[] data;
	int position = 0;

	public PacketData() {
		out = new ByteArrayOutputStream();
	}

	public PacketData(byte[] data) {
		this.data = data;
	}

	public byte[] toByteArray() {
		return (null == out) ? data : out.toByteArray();
	}

	void putDWord(long value) {
		putDWord((int) value);
	}

	void putDWord(int value) {
		try {
			out.write(value & 0x000000FF);
			out.write(((value & 0x0000FF00) >> 8) & 0xFF);
			out.write(((value & 0x00FF0000) >> 16) & 0xFF);
			out.write(((value & 0xFF000000) >> 24) & 0xFF);
		} catch (Exception ex) {
		}
	}

	void putString(String str) {
		byte[] strBytes = null;
		try {
			strBytes = str.getBytes("CP1251");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			strBytes = str.getBytes();
			e.printStackTrace();
		}
		putDWord(strBytes.length);
		putBytes(strBytes);
	}

	void putBytes(byte[] data) {
		try {
			out.write(data);
		} catch (Exception ex) {
		}
	}

	public long getDWord() {
		long res = getDWord(data, position, false);
		position += 4;
		return res;
	}

	public static long getDWord(byte[] buf, int off, boolean bigEndian) {
		long val;
		if (bigEndian) {
			val = (((long) buf[off]) << 24) & 0xFF000000;
			val |= (((long) buf[++off]) << 16) & 0x00FF0000;
			val |= (((long) buf[++off]) << 8) & 0x0000FF00;
			val |= (((long) buf[++off])) & 0x000000FF;
		} else {
			// Little endian
			val = (((long) buf[off])) & 0x000000FF;
			val |= (((long) buf[++off]) << 8) & 0x0000FF00;
			val |= (((long) buf[++off]) << 16) & 0x00FF0000;
			val |= (((long) buf[++off]) << 24) & 0xFF000000;
		}
		return val;
	}

	public String getString() {
		int msgLen = (int) getDWord();
		System.out.println("msgLen =" + msgLen);
		// String str = byteArrayToString(data, position, msgLen);
		String str = new String(data, position, msgLen);
		position += msgLen;
		return removeCr(str);
	}

	public static String removeCr(String val) {
		if (val.indexOf('\r') < 0) {
			return val;
		}
		if (-1 == val.indexOf('\n')) {
			return val.replace('\r', '\n');
		}

		StringBuffer result = new StringBuffer();
		int size = val.length();
		for (int i = 0; i < size; i++) {
			char chr = val.charAt(i);
			if ((chr == 0) || (chr == '\r')) {
				continue;
			}
			result.append(chr);
		}
		return result.toString();
	}

}
