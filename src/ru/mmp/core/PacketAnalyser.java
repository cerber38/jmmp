package ru.mmp.core;

import ru.mmp.listener.StatusListener;
import ru.mmp.packet.Packet;
import ru.mmp.packet.inc.*;

/**
 * 
 * @author Raziel
 */
public class PacketAnalyser {

	public static final int MRIM_CS_HELLO_ACK = 0x1002;
	public static final int MRIM_CS_LOGIN_ACK = 0x1004;
	public static final int MRIM_CS_LOGIN_REJ = 0x1005;
	public static final int MRIM_CS_MESSAGE_ACK = 0x00001009;
	
	private MMPClient client;
	private MRIM_CS_HELLO_ASC hello_asc;
	private MRIM_CS_LOGIN_REJ login_rej;
	private MRIM_CS_MESSAGE_ACK message_ack;

	public PacketAnalyser(MMPClient client) {
		this.client = client;
		hello_asc = new MRIM_CS_HELLO_ASC();
		login_rej = new MRIM_CS_LOGIN_REJ();
		message_ack = new MRIM_CS_MESSAGE_ACK();
	}

	public void parser(Packet packet) {
		int cmd = packet.getCommand();
		switch (cmd) {
		case MRIM_CS_HELLO_ACK:
			hello_asc.parser(packet);
			hello_asc.execute(client);
			break;
		case MRIM_CS_LOGIN_ACK:
			onLogin();
			break;
		case MRIM_CS_LOGIN_REJ:
			login_rej.parser(packet);
			login_rej.notifyEvent(client);
			break;
		case MRIM_CS_MESSAGE_ACK:
			message_ack.parser(packet);
			message_ack.notifyEvent(client);
			break;
		default:
			System.out.println("Ошибка обработки. Неизвестный пакет.");
			break;
		}

	}

	/**
	 * Уведомляем слушателей об успешной авторизации
	 */
	private void onLogin() {
		//client.chStatus();
		for (int i = 0; i < client.getStatusListener().size(); i++) {
			StatusListener l = (StatusListener) client.getStatusListener().get(
					i);
			l.onLogin();
		}
	}
}
