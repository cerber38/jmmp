package ru.mmp.packet.out;

import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_MESSAGE extends Packet {
	
	public static final int MRIM_CS_MESSAGE = 0x1008;
	
	private String email;
	private String msg;

	public void setEmail(String email){
		this.email = email;
	}
	
	public void setMsg(String msg){
		this.msg = msg;
	}
	
	public Packet push(){
		PacketData data = new PacketData();
		data.putDWord(0x00100000);
		data.putString(email);
		data.putUcs2String(msg);
		data.putString("");
		return new Packet(MRIM_CS_MESSAGE,data);
	}

}
