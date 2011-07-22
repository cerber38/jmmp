package ru.mmp.packet.inc;

import ru.mmp.core.MMPClient;
import ru.mmp.listener.MessageListener;
import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;
import ru.mmp.packet.RreceivedPacket;

/**
 * 
 * @author Raziel
 *
 */
public class MRIM_CS_MESSAGE_ACK implements RreceivedPacket {

	private final static int MESSAGE_JABBER = 0x00200080;
	private final static int MESSAGE_JABBER_NOTIFY = 0x00200404;
	private final static int MESSAGE_QIP = 0x00100000;
	private final static int MESSAGE_QIP_NOTIFY = 0x00300404;
	private final static int MESSAGE_MAGENT_NOTIFY = 0x00100404;
	private final static int MESSAGE_AGENT = 0x00100080;

	private int flag;
	private int seq;
	private String email;
	private String msg;

	public void parser(Packet packet) {
		PacketData data = packet.getData();
		seq = (int) data.getDWord();
		flag = (int) data.getDWord();
		email = data.getString();
		if (flag == MESSAGE_JABBER)
			msg = data.getWinByteToString();
		else
			msg = data.getUCS2byteToString();
	}

	@Override
	public void execute(MMPClient client) {
		// TODO Auto-generated method stub

	}

	public void notifyEvent(MMPClient client) {
		System.out.println("MRIM_CS_MESSAGE_ACK");
		if (flag == MESSAGE_QIP || flag == MESSAGE_JABBER
				|| flag == MESSAGE_AGENT) {
			for (int i = 0; i < client.getMessageListener().size(); i++) {
				MessageListener l = (MessageListener) client
						.getMessageListener().get(i);
				l.onIncomingMessage(email, msg);
			}
			System.out.println("Сообщение от " + email + ", " + msg);
		} else if (flag == MESSAGE_QIP_NOTIFY || flag == MESSAGE_MAGENT_NOTIFY
				|| flag == MESSAGE_JABBER_NOTIFY) {
			System.out.println(email + " пишет сообщение.");
		}
	}

}
