package ru.mmp.core;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import ru.mmp.packet.Packet;
import ru.mmp.queue.MMPQueue;
import ru.mmp.queue.PacketHandler;
import ru.mmp.util.EncodeTools;

/**
 * 
 * @author Raziel
 */
public class MMPConnect implements Runnable {

	private static final String THREAD_NAME = "MMPConnect";

	private DataInputStream in;
	private DataOutputStream out;
	private boolean run;
	private Socket socketClient;
	private String host = "94.100.187.38";
	private int port = 2041;
	private Thread thread;
	private MMPQueue queue;
	private PacketAnalyser analyzer;
	private PacketHandler handler;

	public MMPConnect(PacketAnalyser analyzer) {
		this.analyzer = analyzer;
		handler = new PacketHandler(this);
		queue = new MMPQueue();
		thread = new Thread(this, THREAD_NAME);
	}

	public synchronized void connect() throws IOException {
		getServer();
		socketClient = new Socket(host, port);
		out = new DataOutputStream(socketClient.getOutputStream());
		in = new DataInputStream(socketClient.getInputStream());
		handler.start();
		thread.start();
		run = true;
	}

	public synchronized void disconnect() throws IOException {
		try {
			if (socketClient != null)
				socketClient.shutdownInput();
		} finally {
			try {
				if (socketClient != null)
					socketClient.shutdownOutput();
			} finally {
				try {
					socketClient.close();
				} finally {
					run = false;
					handler.stop();
					thread.stop();
				}
			}
		}
	}

	@Override
	public void run() {
		boolean isData = false;
		byte[] head = new byte[44];
		byte[] data = null;
		int dataLen = 0;
		try {
			while (run) {
				if (!isData) {
					if (in.available() >= 44) {
						in.read(head, 0, 44);
						dataLen = EncodeTools.byteArrayToInt(head, 16);
						data = new byte[dataLen];
						isData = true;
					}
				} else {
					if (in.available() >= dataLen) {
						in.read(data, 0, dataLen);
						// добавляем в очередь на обработку
						//System.out.println("Incoming packet len = "
						// + (head.length + data.length));
						//System.out.println("Head = "+getHexString(head));
						//System.out.println("Data = "+getHexString(data));
						queue.push(new Packet(head, data));
						isData = false;
					}
				}
				if (in.available() == 0)
					thread.sleep(200);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getServer() {
		try {
			Socket s = new Socket("mrim.mail.ru", 443);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String[] line = in.readLine().split(":");
			host = line[0];
			port = Integer.parseInt(line[1]);
			in.close();
			s.close();
		} catch (Exception ex) {
			System.out.println("Error get server");
			ex.printStackTrace();
		}
	}

	public void sendPacket(Packet p) {
		try {
			out.write(p.getBytePacket());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MMPQueue getQueue() {
		return queue;
	}

	public PacketAnalyser getAnalyser() {
		return analyzer;
	}

	public String getHexString(byte[] raw) throws UnsupportedEncodingException {
		byte[] hex = new byte[2 * raw.length];
		int index = 0;

		for (byte b : raw) {
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex, "ASCII");
	}

	static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1', (byte) '2',
			(byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
			(byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c',
			(byte) 'd', (byte) 'e', (byte) 'f' };
}