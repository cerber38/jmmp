package ru.jimbot.protocol.mrim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ru.jimbot.core.services.AbstractProperties;

/**
 * 
 * @author Raziel
 *
 */
public class MrimProtocolProperties implements AbstractProperties {

	public static final String FILE_NAME = "mrim-config";
	private String serviceName = "";
	private int status = 1;
	private String desc1 = "";
	private String desc = "";
	private int pauseOut = 500;
	private int msgOutLimit = 20;
	private long pauseRestart = 11 * 60 * 1000;
	private int maxOutMsgSize = 500;
	private int maxOutMsgCount = 5;

	/**
	 * 
	 */
	public MrimProtocolProperties() {

	}

	/**
	 * 
	 * @param serviceName
	 */
	public MrimProtocolProperties(String serviceName) {
		super();
		this.serviceName = serviceName;
	}

	@Override
	public String getTitle() {
		return "Настройки протокола MRIM";
	}

	@Override
	public String getExtendInfo() {
		return "Настройки протокола MRIM";
	}

	@Override
	public void save() {
		try {
			File t = new File("./services/" + serviceName);
			if (!t.exists())
				t.mkdir();

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("./services/" + serviceName + "/"
							+ FILE_NAME), "UTF8"));
			w.write(gson.toJson(this, new TypeToken<MrimProtocolProperties>() {
			}.getType()));
			w.close();
		} catch (Exception e) {
			System.err.println("Error saving configuration " + e.getMessage());
		}

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static synchronized MrimProtocolProperties load(String name) {
		Object o = new MrimProtocolProperties(name);
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream("./services/"
							+ name + "/" + FILE_NAME), "UTF8"));
			StringBuilder sb = new StringBuilder();
			while (br.ready()) {
				sb.append(br.readLine());
			}
			br.close();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			o = gson.fromJson(sb.toString(),
					new TypeToken<MrimProtocolProperties>() {
					}.getType());
		} catch (Exception e) {
			System.err.println("Error loading configuration " + e.getMessage());
		}
		return (MrimProtocolProperties) o;
	}

	/**
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 
	 * @return the statustext
	 */
	public String getDesc1() {
		return this.desc1;
	}

	/**
	 * 
	 * @param statustext
	 */
	public void setDesc1(String statustext) {
		this.desc1 = statustext;
	}

	/**
	 * 
	 * @return the desc
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * 
	 * @param desc
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the pauseOut
	 */
	public int getPauseOut() {
		return pauseOut;
	}

	/**
	 * @param pauseOut
	 *            the pauseOut to set
	 */
	public void setPauseOut(int pauseOut) {
		this.pauseOut = pauseOut;
	}

	/**
	 * @return the msgOutLimit
	 */
	public int getMsgOutLimit() {
		return msgOutLimit;
	}

	/**
	 * @param msgOutLimit
	 *            the msgOutLimit to set
	 */
	public void setMsgOutLimit(int msgOutLimit) {
		this.msgOutLimit = msgOutLimit;
	}

	/**
	 * @return the pauseRestart
	 */
	public long getPauseRestart() {
		return pauseRestart;
	}

	/**
	 * @param pauseRestart
	 *            the pauseRestart to set
	 */
	public void setPauseRestart(long pauseRestart) {
		this.pauseRestart = pauseRestart;
	}

	/**
	 * @return the maxOutMsgSize
	 */
	public int getMaxOutMsgSize() {
		return maxOutMsgSize;
	}

	/**
	 * @param maxOutMsgSize
	 *            the maxOutMsgSize to set
	 */
	public void setMaxOutMsgSize(int maxOutMsgSize) {
		this.maxOutMsgSize = maxOutMsgSize;
	}

	/**
	 * @return the maxOutMsgCount
	 */
	public int getMaxOutMsgCount() {
		return maxOutMsgCount;
	}

	/**
	 * @param maxOutMsgCount
	 *            the maxOutMsgCount to set
	 */
	public void setMaxOutMsgCount(int maxOutMsgCount) {
		this.maxOutMsgCount = maxOutMsgCount;
	}

}
