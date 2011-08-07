package ru.mmp.packet.inc;

import ru.mmp.core.MMPClient;
import ru.mmp.listener.StatusListener;
import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;
import ru.mmp.packet.RreceivedPacket;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_LOGIN_REJ implements RreceivedPacket {

	private String msg;

	public void parser(Packet packet) {
		PacketData data = packet.getData();
		msg = data.getString();
	}

	@Override
	public void execute(MMPClient client) {
		// TODO Auto-generated method stub

	}

	public void notifyEvent(MMPClient client) {
		for (int i = 0; i < client.getStatusListener().size(); i++) {
			StatusListener l = (StatusListener) client.getStatusListener().get(
					i);
			l.onAuthorizationError(msg);
			System.out.println(client.getEmail() + " On Authorization Error "+msg);
		}
	}

}
