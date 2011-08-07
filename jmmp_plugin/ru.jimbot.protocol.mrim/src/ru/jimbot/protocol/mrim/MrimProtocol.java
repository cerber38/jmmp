package ru.jimbot.protocol.mrim;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.jimbot.core.Destroyable;
import ru.jimbot.core.Message;
import ru.jimbot.core.events.EventProxy;
import ru.jimbot.core.events.OutgoingMessageEventHandler;
import ru.jimbot.core.events.OutgoingMessageListener;
import ru.jimbot.core.events.ProtocolCommandEventHandler;
import ru.jimbot.core.events.ProtocolCommandListener;
import ru.jimbot.core.services.Log;
import ru.jimbot.core.services.Protocol;
import ru.jimbot.protocol.mrim.internal.ActivatorMrimProtocol;
//import ru.mmp.core.MMPClient;
import ru.mmp.core.MMPClient;
import ru.mmp.listener.MessageListener;
import ru.mmp.listener.StatusListener;

/**
 * 
 * @author Raziel
 *
 */
public class MrimProtocol extends Destroyable implements Protocol,
		OutgoingMessageListener, ProtocolCommandListener, StatusListener,
		MessageListener {

	private Log logger = ActivatorMrimProtocol.getExtendPointRegistry()
			.getLogger();
	private MMPClient con = null;
	private String screenName = "";
	private String pass = "";
	private int status = 1;
	private String desc1 = "";
	private String desc = "";
	private String serviceName = "";
	private boolean connected = false;
	private String lastError = "";
	private EventProxy eva;
	private OutgoingMessageEventHandler h1;
	private ProtocolCommandEventHandler h2;
	private long pauseOutMsg = 2000;
	private int maxOutQueue = 20;
	private ConcurrentLinkedQueue<Message> q = new ConcurrentLinkedQueue<Message>();
	private long timeLastOutMsg = 0; // Время последнего отправленного сообщения
	private Timer timer;
	private TimerTask qt;
	private MrimProtocolProperties p;

	public MrimProtocol(String serviceName, MrimProtocolProperties p) {
		super();
		this.serviceName = serviceName;
		this.p = p;
		eva = new EventProxy(ActivatorMrimProtocol.getEventAdmin(), serviceName);
	}

	@Override
	public void setConnectionData(String server, int port, String sn,
			String pass) {
		this.screenName = sn;
		this.pass = pass;
		h1 = new OutgoingMessageEventHandler(screenName, this);
		h2 = new ProtocolCommandEventHandler(screenName, this);
		ActivatorMrimProtocol.regEventHandler(h1,
				h1.getHandlerServiceProperties());
		ActivatorMrimProtocol.regEventHandler(h2,
				h2.getHandlerServiceProperties());
	}

	@Override
	public void setLogger(Log logger) {
		this.logger = logger;
	}

	@Override
	public void setStatusData(int status, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setXStatusData(int status, String text1, String text2) {
		this.status = status;
		this.desc1 = text1;
		this.desc = text2;

	}

	@Override
	public void connect() {
		con = new MMPClient();
		con.setEmail(screenName+"@mail.ru");
		con.setPass(pass);
		
		con.addStatusListener(this);
		con.addMessageListener(this);
		try {
			con.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		try {
			con.disconnect();
			con.removeMessageListener(this);
			con.removeStatusListener(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connected = false;
	}

	@Override
	public boolean isOnLine() {
		if (con == null)
			return false;
		return connected;
	}

	@Override
	public void sendMsg(String sn, String msg) {
		con.sendMsg(sn, msg);
	}

	@Override
	public void addContactList(String sn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void RemoveContactList(String sn) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getScreenName() {
		return screenName;
	}

	@Override
	public String getLastError() {
		return lastError;
	}

	@Override
	public void destroy() {
		ActivatorMrimProtocol.unregEventHandler(h1);
		ActivatorMrimProtocol.unregEventHandler(h2);
	}

	@Override
	public void logon(String sn) {
		connect();
		pauseOutMsg = p.getPauseOut();
		maxOutQueue = p.getMsgOutLimit();
		timer = new Timer("queue out " + screenName);
		qt = new TimerTask() {

			@Override
			public void run() {
				if (q.size() == 0)
					return;
				if ((System.currentTimeMillis() - timeLastOutMsg) < pauseOutMsg)
					return;
				Message m = q.poll();
				sendMsg(m.getSnOut(), m.getMsg());
				timeLastOutMsg = System.currentTimeMillis();
			}
		};
		timer.schedule(qt, pauseOutMsg / 2, pauseOutMsg / 2);
	}

	@Override
	public void logout(String sn) {
		timer.cancel();
		timer.purge();
		q.clear();
		disconnect();

	}

	@Override
	public void changeStatus(int id, String txt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeXStatus(int id, String txt1, String txt2) {
		this.status = id;
		this.desc1 = txt1;
		this.desc = txt2;
		con.setStatus(id, txt1, txt2);

	}

	@Override
	public void onMessage(Message m) {
		if (q.size() > 0
				|| (System.currentTimeMillis() - timeLastOutMsg) < pauseOutMsg) {
			if (q.size() <= maxOutQueue) {
				q.add(m);
			} else {
				q.poll();
				q.add(m);
			}
		} else {
			sendMsg(m.getSnOut(), m.getMsg());
			timeLastOutMsg = System.currentTimeMillis();
		}
	}

	@Override
	public void onLogin() {
		connected = true;
		eva.protocolChangeState(screenName, EventProxy.STATE_LOGON, null);
		con.setStatus(status, desc1, desc);

	}

	@Override
	public void onLogout() {
		System.err.println("Разрыв соединения: " + screenName);
		logger.error(screenName, "Разрыв соединения: " + screenName);
		lastError = "";
		disconnect();
		eva.protocolChangeState(screenName, EventProxy.STATE_LOGOFF, null);
		// TODO подумать где отключать таймер и нужно ли в случае ошибок очищать
		// очередь

	}

	@Override
	public void onAuthorizationError(String message) {
		logger.error(screenName, "Authorization for " + screenName
				+ " failed, reason " + message);
		lastError = message;
		disconnect();
		System.out.println("Authorization for " + screenName
				+ " failed, reason " + message);
	}

	@Override
	public void onIncomingMessage(String from, String msg) {
		eva.incomingMessage(new Message(from, screenName, msg,
				Message.TYPE_TEXT));
	}

	@Override
	public void onMessageAck() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthorization(String from, String msg) {
		con.Authorize(from);
	}

	@Override
	public void onMessageError() {
		logger.error(screenName, "Message error");

	}

}
