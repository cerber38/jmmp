package ru.mmp.packet.out;

import ru.mmp.packet.Packet;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_HELLO extends Packet {
	
	private static final int MRIM_CS_HELLO = 0x1001;

	public MRIM_CS_HELLO() {
		setCommand(MRIM_CS_HELLO);
	}

}
