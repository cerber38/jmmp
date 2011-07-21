package ru.mmp.listener;

/**
 * 
 * @author Raziel
 */
public interface MessageListener {

	public void onIncomingMessage(String from, String msg);

	public void onMessageAck();

	public void onAuthorization(String from, String msg);

	public void onMessageError();
}
