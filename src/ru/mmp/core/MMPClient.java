package ru.mmp.core;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import ru.mmp.listener.MessageListener;
import ru.mmp.listener.StatusListener;
import ru.mmp.packet.Packet;
import ru.mmp.packet.out.*;

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
	private List statusListener;
	private List messageListener;
	private Status status;

	public MMPClient() {
		this.email = email;
		this.pass = pass;
		analyzer = new PacketAnalyser(this);
		con = new MMPConnect(analyzer);
		ping = new MRIM_CS_PING(this);
		statusListener = new Vector();
		messageListener = new Vector();
		status = new Status();
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail(){
		return this.email;
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

	public void disconnect() throws IOException {
		ping.stop();
		con.disconnect();
		connect = false;
	}

	public void login() {
		MRIM_CS_LOGIN2 l = new MRIM_CS_LOGIN2();
		l.setEmail(email);
		l.setPass(pass);
		sendPacket(l.push());
	}

	public void sendMsg(String email, String msg) {
		MRIM_CS_MESSAGE m = new MRIM_CS_MESSAGE();
		m.setEmail(email);
		m.setMsg(msg);
		sendPacket(m.push());
	}

	public void sendPacket(Packet p) {
		if (connect)
			con.sendPacket(p);
	}

	public MRIM_CS_PING getPing() {
		return ping;
	}

	public void addStatusListener(StatusListener listener) {
		statusListener.add(listener);
	}

	public boolean removeStatusListener(StatusListener listener) {
		return statusListener.remove(listener);
	}

	public List getStatusListener() {
		return statusListener;
	}

	public void addMessageListener(MessageListener listener) {
		messageListener.add(listener);
	}

	public boolean removeMessageListener(MessageListener listener) {
		return messageListener.remove(listener);
	}

	public List getMessageListener() {
		return messageListener;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(int id, String title, String desc) {
		status.setStatus(id);
		status.setTitle(title);
		status.setDesc(desc);
		sendPacket(new MRIM_CS_CHANGE_STATUS().push(status));
	}

	public void Authorize(String email) {
		sendPacket(new MRIM_CS_AUTHORIZE().push(email));
	}

}
