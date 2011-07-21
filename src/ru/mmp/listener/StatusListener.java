package ru.mmp.listener;

/**
 * 
 * @author Raziel
 */
public interface StatusListener {
	
	public void onLogin();

	public void onLogout();

	public void onAuthorizationError(String message);
}
