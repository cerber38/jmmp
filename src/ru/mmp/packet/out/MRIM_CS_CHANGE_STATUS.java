package ru.mmp.packet.out;

import ru.mmp.core.Status;
import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_CHANGE_STATUS {

	public static final int MRIM_CS_CHANGE_STATUS = 0x1022;

	public Packet push(Status st) {
		PacketData data = new PacketData();
		data.putDWord(st.getStatus());
		data.putString(st.getStatusCode());
		data.putUcs2String(st.getTitle());
		data.putUcs2String(st.getDesc());
		data.putDWord(0x000003FF);
		return new Packet(MRIM_CS_CHANGE_STATUS, data);
	}

}
