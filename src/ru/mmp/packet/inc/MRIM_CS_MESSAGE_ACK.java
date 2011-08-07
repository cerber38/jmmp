package ru.mmp.packet.inc;

import ru.mmp.core.MMPClient;
import ru.mmp.listener.MessageListener;
import ru.mmp.packet.Packet;
import ru.mmp.packet.PacketData;
import ru.mmp.packet.RreceivedPacket;
import ru.mmp.packet.out.MRIM_CS_MESSAGE_RECV;

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
	private final static int MESSAGE_QIP_AUTHORIZED = 0x0010000c;
	private final static int MESSAGE_MAGENT_NOTIFY = 0x00100404;
	private final static int MESSAGE_AGENT = 0x00100080;
	private final static int MESSAGE_AGENT_AUTHORIZED = 0x0018000c;

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
		else if (flag == MESSAGE_QIP_AUTHORIZED
				|| flag == MESSAGE_AGENT_AUTHORIZED)
			// TODO Разобраться с сообщением при авторизации
			msg = data.getString();
		else
			msg = data.getUCS2byteToString();
	}

	public void execute(MMPClient client) {
		if (flag == MESSAGE_QIP || flag == MESSAGE_JABBER
				|| flag == MESSAGE_AGENT) {
			MRIM_CS_MESSAGE_RECV m = new MRIM_CS_MESSAGE_RECV();
			m.setEmail(email);
			m.setSeq(seq);
			client.sendPacket(m.push());
		}
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
		} else if (flag == MESSAGE_QIP_AUTHORIZED
				|| flag == MESSAGE_AGENT_AUTHORIZED) {
			client.Authorize(email);
			System.out.println("Запрос авторизации от " + email + ", " + msg);
			for (int i = 0; i < client.getMessageListener().size(); i++) {
				MessageListener l = (MessageListener) client
						.getMessageListener().get(i);
				l.onAuthorization(email, msg);
			}
		}
	}

}
