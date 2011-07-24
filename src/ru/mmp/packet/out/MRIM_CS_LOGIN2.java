package ru.mmp.packet.out;

import ru.mmp.core.Status;
import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_LOGIN2 {

	private static final int MRIM_CS_LOGIN2 = 0x00001038;

	private String email;
	private String pass;

	public MRIM_CS_LOGIN2() {
		// setCommand(MRIM_CS_LOGIN2);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Packet push() {
		PacketData data = new PacketData();
		data.putString(email);
		data.putString(pass);
		data.putDWord(Status.STATUS_ONLINE);
		data.putString("STATUS_ONLINE");
		data.putUcs2String("");
		data.putUcs2String("");
		data.putDWord(0x3FF);
		String version = "client=\"jImBot\" version=\"0.1\" build=\"1\"";
		data.putString(version);
		data.putString("ru");
		data.putString("jImBot");
		return new Packet(MRIM_CS_LOGIN2, data);
	}

}
