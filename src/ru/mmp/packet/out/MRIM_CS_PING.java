package ru.mmp.packet.out;

import ru.mmp.core.MMPClient;
import ru.mmp.packet.Packet;

/**
 * 
 * @author Raziel
 */
public class MRIM_CS_PING extends Packet implements Runnable {

	private static final int MRIM_CS_PING = 0x1006;
	private static final String THREAD_NAME = "ThreadPing";

	private MMPClient client;
	private int time = 30;
	private Thread thread;
	private boolean run = false;

	public MRIM_CS_PING() {

	}

	public MRIM_CS_PING(MMPClient client) {
		this.client = client;
		thread = new Thread(this, THREAD_NAME);
		setCommand(MRIM_CS_PING);
	}

	@Override
	public void run() {
		while (run) {			
			client.sendPacket(this);
			try {
				Thread.sleep(1000 * time);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void start() {
		run = true;
		thread.start();
	}

	public void stop() {
		run = false;
		thread.stop();
	}

	public void setTime(int t) {
		this.time = t;
	}
	
	public Packet push(){
		return this;
	}

}
