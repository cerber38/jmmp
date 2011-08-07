package ru.mmp.packet;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Raziel
 * @author Vladimir Krukov
 */
public class PacketData {

	private ByteArrayOutputStream out;
	private ByteArrayOutputStream base;
	byte[] data;
	int position = 0;

	public PacketData() {
		out = new ByteArrayOutputStream();
		base = new ByteArrayOutputStream();
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

	public void putDWord(int value) {
		try {
			out.write(value & 0x000000FF);
			out.write(((value & 0x0000FF00) >> 8) & 0xFF);
			out.write(((value & 0x00FF0000) >> 16) & 0xFF);
			out.write(((value & 0xFF000000) >> 24) & 0xFF);
		} catch (Exception ex) {
		}
	}

	public void putString(String str) {
		byte[] strBytes = null;
		try {
			strBytes = str.getBytes("CP1251");
		} catch (UnsupportedEncodingException e) {
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
		// System.out.println("Read DWord");
		// System.out.println("read byte =" + position + " of "+data.length);
		// System.out.println("int = "+res);
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
		String str = new String(data, position, msgLen);
		// System.out.print("String bytes ");
		 byte[] ms = new byte[msgLen];
		 System.arraycopy(data, position, ms, 0, msgLen);
		 //try {
		 //System.out.println("String " + Packet.getHexString(ms));
		 //} catch (UnsupportedEncodingException e) {
		 // TODO Auto-generated catch block
		 //e.printStackTrace();
		 //}
		position += msgLen;
		// System.out.println("read byte =" + position + " of "+data.length);
		return removeCr(str);
	}

	public String getUCS2byteToString() {
		int msgLen = (int) getDWord();
		byte[] ms = new byte[msgLen];
		System.arraycopy(data, position, ms, 0, msgLen);
		String m = "";
		try {
			String buffer = new String(ms, "UTF-16LE");
			byte[] b = buffer.getBytes("Cp1251");
			m = new String(b, "Cp1251");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			m = new String(ms);
			e.printStackTrace();
		}
		// byte[] mss = new byte[msgLen];
		// System.arraycopy(data, position, mss, 0, msgLen);
		// try {
		// System.out.println("String " + Packet.getHexString(mss));
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		position += msgLen;
		return m;
	}

	public String getWinByteToString() {
		int msgLen = (int) getDWord();
		byte[] ms = new byte[msgLen];
		System.arraycopy(data, position, ms, 0, msgLen);
		String m = "";
		try {
			String buffer = new String(ms, "Cp1251");
			byte[] b = buffer.getBytes("Cp1251");
			m = new String(b, "Cp1251");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			m = new String(ms);
			e.printStackTrace();
		}
		// byte[] mss = new byte[msgLen];
		// System.arraycopy(data, position, mss, 0, msgLen);
		// try {
		// System.out.println("String " + Packet.getHexString(mss));
		// } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		// /e.printStackTrace();
		// }
		position += msgLen;
		return m;
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

	public String getUcs2String() {
		final int msgLen = (int) getDWord();
		return getUcs2StringZ(msgLen);
	}

	public String getUcs2StringZ(int dataLength) {
		StringBuffer sb = new StringBuffer(dataLength / 2);
		for (int i = 0; i < dataLength; i += 2) {
			int halfCh = data[position++];
			sb.append((char) (halfCh | (data[position++] << 8)));
		}
		return sb.toString();
	}

	public void putUcs2String(String str) {
		putDWord(str.length() * 2);
		try {
			for (int i = 0; i < str.length(); ++i) {
				char ch = str.charAt(i);
				out.write(ch & 0xFF);
				out.write((ch >> 8) & 0xFF);
			}
		} catch (Exception ex) {
		}
	}
	
	public static final byte[] base64decode(String str) {
        if (null == str) str = "";
        PacketData out = new PacketData();
        for (int strIndex = 0; strIndex < str.length(); ++strIndex) {
    	    strIndex = base64GetNextIndex(str, strIndex);
            if (-1 == strIndex) break;
            int ch1 = base64GetNextChar(str, strIndex);
            if (-1 == ch1) break;

            strIndex = base64GetNextIndex(str, strIndex + 1);
            if (-1 == strIndex) break;
            int ch2 = base64GetNextChar(str, strIndex);
            if (-1 == ch2) break;
            out.writeByte((byte)(0xFF & ((ch1 << 2) | (ch2 >>> 4))));

            strIndex = base64GetNextIndex(str, strIndex + 1);
            if (-1 == strIndex) break;
            int ch3 = base64GetNextChar(str, strIndex);
            if (-1 == ch3) break;
            out.writeByte((byte)(0xFF & ((ch2 << 4) | (ch3 >>> 2))));

            strIndex = base64GetNextIndex(str, strIndex + 1);
            if (-1 == strIndex) break;
            int ch4 = base64GetNextChar(str, strIndex);
            if (-1 == ch4) break;
            out.writeByte((byte)(0xFF & ((ch3 << 6) | (ch4 >>> 0))));
        }
        return out.toByteArray();
    }
	
	private static final int base64GetNextIndex(String str, int index) {
        for (; index < str.length(); ++index) {
            char ch = str.charAt(index);
            if ('=' == ch) {
                return index;
            }
            int code = base64.indexOf(ch);
            if (-1 != code) {
                return index;
            }
        }
        return -1;
    }
	
	private static final int base64GetNextChar(String str, int index) {
        if (-1 == index) return -2;
        char ch = str.charAt(index);
        if ('=' == ch) {
            return -1;
        }
        return base64.indexOf(ch);
    }
	
	public void writeByte(int value) {
        try {
            base.write(value);
        } catch (Exception e) {
        }
    }
	
	private static final String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

}
