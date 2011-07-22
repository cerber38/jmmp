package ru.mmp.packet;

import java.io.UnsupportedEncodingException;

import ru.mmp.util.EncodeTools;

/**
 * 
 * @author Raziel
 */
public class Packet {

	private int cmd;
	private int seq;
	private static int seqCount = 1;
	private PacketData data;
	private long ip = 0x7F000001;
	private long port = 666;

	public Packet() {
		data = new PacketData();
	}

	public Packet(int cmd, PacketData data) {
		this.cmd = cmd;
		this.data = data;
	}

	public Packet(byte[] head, byte[] data) {
		this.cmd = EncodeTools.getDWord(head, 12);
		this.seq = EncodeTools.getDWord(head, 8);
		this.data = new PacketData((null == data) ? new byte[0] : data);
	}

	public int nextSeq() {
		return ++Packet.seqCount;
	}

	public int getCommand() {
		return cmd;
	}

	public void setCommand(int cmd) {
		this.cmd = cmd;
	}

	public PacketData getData() {
		return data;
	}

	public byte[] getBytePacket() {
		byte[] body = data.toByteArray();
		byte[] packet = new byte[44 + body.length];
		if (body.length > 0)
			System.arraycopy(body, 0, packet, 44, body.length);
		setSeq(nextSeq());
		EncodeTools.putDWord(packet, 0, 0xDEADBEEF);
		EncodeTools.putDWord(packet, 4, 0x00010013);
		EncodeTools.putDWord(packet, 8, seq);
		EncodeTools.putDWord(packet, 12, cmd);
		EncodeTools.putDWord(packet, 16, body.length);
		EncodeTools.putDWord(packet, 20, ip);
		EncodeTools.putDWord(packet, 24, port);
		// try {
		// System.out.println("Send packet " + getHexString(packet));
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return packet;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getSeq() {
		return seq;
	}

	public static String getHexString(byte[] raw) throws UnsupportedEncodingException {
		byte[] hex = new byte[2 * raw.length];
		int index = 0;

		for (byte b : raw) {
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex, "ASCII");
	}

	static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1', (byte) '2',
			(byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
			(byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c',
			(byte) 'd', (byte) 'e', (byte) 'f' };

}
