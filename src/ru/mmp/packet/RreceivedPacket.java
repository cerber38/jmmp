package ru.mmp.packet;

import ru.mmp.core.MMPClient;

/**
 * @author Raziel
 */
public interface RreceivedPacket {

	public void parser(Packet packet);

	public void execute(MMPClient client);

	public void notifyEvent(MMPClient client);

}
