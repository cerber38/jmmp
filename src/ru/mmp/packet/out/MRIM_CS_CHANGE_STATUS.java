package ru.mmp.packet.out;

import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_CHANGE_STATUS {

	public static final int MRIM_CS_CHANGE_STATUS = 0x1022;

	public Packet push() {
		PacketData data = new PacketData();
		data.putDWord(4);
		data.putString("STATUS_29");
		data.putUcs2String("заголовок");
		data.putUcs2String("описание");
		data.putDWord(0x000003FF);
		return new Packet(MRIM_CS_CHANGE_STATUS, data);
	}

}
