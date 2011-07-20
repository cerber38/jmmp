package ru.mmp.core;

import java.io.IOException;

import ru.mmp.packet.Packet;
import ru.mmp.packet.out.MRIM_CS_HELLO;
import ru.mmp.packet.out.MRIM_CS_PING;

/**
 * 
 * @author Raziel
 */
public class MMPClient {

	public MMPConnect con;
	private String email;
	private String pass;
	private boolean connect = false;
	private PacketAnalyser analyzer;
	private MRIM_CS_PING ping;

	public MMPClient() {
		this.email = email;
		this.pass = pass;
		analyzer = new PacketAnalyser(this);
		con = new MMPConnect(analyzer);
		ping = new MRIM_CS_PING(this);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void connect() throws IOException {
		con.connect();
		connect = true;
		// Отослать пакет MRIM_CS_HELLO
		sendPacket(new MRIM_CS_HELLO());
	}

	public void sendPacket(Packet p) {
		if (connect)
			con.sendPacket(p);
	}

	public MRIM_CS_PING getPing() {
		return ping;
	}

}
