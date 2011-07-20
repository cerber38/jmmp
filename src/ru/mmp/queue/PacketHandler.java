package ru.mmp.queue;

import ru.mmp.core.MMPConnect;
import ru.mmp.packet.Packet;

/**
 * 
 * @author Raziel
 */
public class PacketHandler implements Runnable {
	private static final String THREAD_NAME = "PacketHandler";
	private Thread thread;
	private MMPConnect con;
	private boolean runnable;

	public PacketHandler(MMPConnect con) {
		this.con = con;
		thread = new Thread(this, THREAD_NAME);
	}

	public void run() {
		while (runnable) {
			if (!con.getQueue().isEmpty()) {
				con.getAnalyser().parser((Packet) con.getQueue().poll());
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}
			}
		}
	}

	public synchronized void start() {
		thread.start();
		runnable = true;
	}

	public synchronized void stop() {
		runnable = false;
		thread.stop();
	}
}
