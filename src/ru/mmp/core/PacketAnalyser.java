package ru.mmp.core;

import ru.mmp.packet.Packet;
import ru.mmp.packet.inc.*;

/**
 * 
 * @author Raziel
 */
public class PacketAnalyser {

	public static final int MRIM_CS_HELLO_ACK = 0x1002;

	private MMPClient client;
	private MRIM_SC_HELLO_ASC hello_asc;

	public PacketAnalyser(MMPClient client) {
		this.client = client;
		hello_asc = new MRIM_SC_HELLO_ASC();
	}

	public void parser(Packet packet) {
		System.out.println("Parse packet");
		int cmd = packet.getCommand();
		switch (cmd) {
		case MRIM_CS_HELLO_ACK:
			hello_asc.parser(packet);
			hello_asc.execute(client);
			break;
		}

	}

}
