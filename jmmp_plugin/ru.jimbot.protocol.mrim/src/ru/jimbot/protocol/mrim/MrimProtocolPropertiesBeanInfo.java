package ru.jimbot.protocol.mrim;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

/**
 * 
 * @author Raziel
 *
 */
public class MrimProtocolPropertiesBeanInfo extends SimpleBeanInfo {
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			ArrayList<PropertyDescriptor> pr = new ArrayList<PropertyDescriptor>();
			PropertyDescriptor p;

			p = new PropertyDescriptor("status", MrimProtocolProperties.class);
			p.setDisplayName("Email статус");
			pr.add(p);

			p = new PropertyDescriptor("desc1", MrimProtocolProperties.class);
			p.setDisplayName("Заголовок статуса");
			pr.add(p);

			p = new PropertyDescriptor("desc", MrimProtocolProperties.class);
			p.setDisplayName("Текст статуса");
			pr.add(p);

			p = new PropertyDescriptor("pauseOut", MrimProtocolProperties.class);
			p.setDisplayName("Пауза для исходящих сообщений");
			pr.add(p);

			p = new PropertyDescriptor("msgOutLimit",
					MrimProtocolProperties.class);
			p.setDisplayName("Ограничение очереди исходящих");
			pr.add(p);

			p = new PropertyDescriptor("pauseRestart",
					MrimProtocolProperties.class);
			p.setDisplayName("Пауза перед перезапуском коннекта");
			pr.add(p);

			p = new PropertyDescriptor("maxOutMsgSize",
					MrimProtocolProperties.class);
			p.setDisplayName("Максимальный размер одного исходящего сообщения");
			pr.add(p);

			p = new PropertyDescriptor("maxOutMsgCount",
					MrimProtocolProperties.class);
			p.setDisplayName("Максимальное число частей исходящего сообщения");
			pr.add(p);

			return pr.toArray(new PropertyDescriptor[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
