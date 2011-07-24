package ru.mmp.packet.out;

import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;

/**
 * 
 * @author Raziel
 *
 */
public class MRIM_CS_MESSAGE_RECV {

	private final static int MRIM_CS_MESSAGE_RECV = 0x00001011;

	private int seq = 0;
	private String email = "";

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Packet push() {
		PacketData data = new PacketData();
		data.putString(email);
		data.putDWord(seq);
		return new Packet(MRIM_CS_MESSAGE_RECV, data);
	}

}
