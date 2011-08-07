package ru.jimbot.protocol.mrim;

import java.util.HashMap;

import ru.jimbot.core.ExtendPoint;
import ru.jimbot.core.IProtocolBuilder;
import ru.jimbot.core.services.AbstractProperties;
import ru.jimbot.core.services.IProtocolManager;
import ru.jimbot.core.services.Log;
import ru.jimbot.core.services.Protocol;

/**
 * 
 * @author Raziel
 * 
 */
public class MrimProtocolManager implements IProtocolManager, ExtendPoint {

	private HashMap<String, MrimProtocol> protocols = new HashMap<String, MrimProtocol>();
	private HashMap<String, MrimProtocolProperties> props = new HashMap<String, MrimProtocolProperties>();

	@Override
	public String getType() {
		return "ru.jimbot.core.services.IProtocolManager";
	}

	@Override
	public String getPointName() {
		return "MrimProtocol";
	}

	@Override
	public void unreg() {
		// TODO Auto-generated method stub

	}

	@Override
	public Protocol getProtocol(String sn) {
		return protocols.get(sn);
	}

	@Override
	public Protocol addProtocol(Protocol p) {
		protocols.put(p.getScreenName(), (MrimProtocol) p);
		return p;
	}

	@Override
	public String getProptocolName() {
		return "mrim";
	}

	@Override
	public IProtocolBuilder getBuilder(String sn) {
		return new Builder(sn);
	}

	@Override
	public AbstractProperties getProtocolProperties(String serviceName) {
		if (props.containsKey(serviceName))
			return props.get(serviceName);
		else {
			MrimProtocolProperties p = MrimProtocolProperties.load(serviceName);
			props.put(serviceName, p);
			return p;
		}
	}

	class Builder implements IProtocolBuilder {
		private String screenName = "";
		private String pass = "";
		private Log logger = null;

		public Builder(String screenName) {
			this.screenName = screenName;
		}

		public Builder pass(String val) {
			pass = val;
			return this;
		}

		@Override
		public Protocol build(String serviceName) {
			MrimProtocolProperties pr = (MrimProtocolProperties) getProtocolProperties(serviceName);
			MrimProtocol p = new MrimProtocol(serviceName, pr);
			p.setConnectionData("", 0, screenName, pass);
			p.setXStatusData(pr.getStatus(), pr.getDesc1(), pr.getDesc());
			p.setLogger(logger);
			return p;
		}

		@Override
		public IProtocolBuilder logger(Log val) {
			logger = val;
			return this;
		}
	}

}
