package ru.mmp.packet.out;

import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;

public class MRIM_CS_AUTHORIZE {

	private final static int MRIM_CS_AUTHORIZE = 0x1020;

	public Packet push(String email) {
		PacketData data = new PacketData();
		data.putString(email);
		return new Packet(MRIM_CS_AUTHORIZE, data);
	}

}
