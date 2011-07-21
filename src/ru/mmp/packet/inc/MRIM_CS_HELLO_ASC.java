package ru.mmp.packet.inc;

import ru.mmp.core.MMPClient;
import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;
import ru.mmp.packet.RreceivedPacket;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_HELLO_ASC implements RreceivedPacket {

	private int time = 1;

	public void parser(Packet packet) {
		// Получаем значение интервала пинга
		PacketData data = packet.getData();
		time = (int) data.getDWord();
		//System.out.println("Set ping time " + time);
	}

	public void execute(MMPClient client) {
		client.getPing().setTime(time);		
		client.getPing().start();
		// Логинимся
		client.login();
	}

	@Override
	public void notifyEvent(MMPClient client) {
		// TODO Auto-generated method stub

	}

}
