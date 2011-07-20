package ru.mmp.packet;

import ru.mmp.util.EncodeTools;

/**
 * 
 * @author Raziel
 */
public class Packet {

	public static final int MRIM_CS_HELLO = 0x1001;

	private int cmd;
	private int seq;
	private static int seqCount = 0;
	private PacketData data;
	private long ip = 0x7F000001;
	private long port = 666;

	public Packet() {
		data = new PacketData();
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
		EncodeTools.putDWord(packet, 4, 0x00010016);
		EncodeTools.putDWord(packet, 8, seq);
		EncodeTools.putDWord(packet, 12, cmd);
		EncodeTools.putDWord(packet, 16, body.length);
		EncodeTools.putDWord(packet, 20, ip);
		EncodeTools.putDWord(packet, 24, port);
		return packet;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getSeq() {
		return seq;
	}

}
