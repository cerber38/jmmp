package ru.mmp.core;

/**
 * 
 * @author Raziel
 *
 */
public class Status {

	public static final int STATUS_INVISIBLE = 0x80000001;
	public static final int STATUS_AWAY = 0x00000002;
	public static final int STATUS_ONLINE = 0x00000001;
	public static final int STATUS_OTHER = 0x00000004;

	private int id = -1;
	private String title = "";
	private String desc = "";
	private static String status[][] = { { "STATUS_INVISIBLE", "Невидимый" },
			{ "STATUS_ONLINE", "Онлайн" }, { "STATUS_AWAY", "Отошоел" },
			{ "status_dnd", "Не беспокоить" }, { "status_46", "Занят" },
			{ "status_10", "Недоступен" }, { "status_6", "Кушаю" },
			{ "status_22", "На работе" }, { "status_5", "Дома" },
			{ "status_34", "Депрессия" }, { "status_38", "Злой" },
			{ "status_chat", "Готов поболтать" }, { "status_18", "Дела" },
			{ "status_37", "Сердитый" }, { "status_4", "Болею" },
			{ "status_12", "Вопрос" }, { "status_15", "Купаюсь" },
			{ "status_29", "Весело" }, { "status_23", "Сплю" },
			{ "status_40", "Сердце" }, { "status_19", "Друзья" },
			{ "status_24", "Болтаю" }, { "status_49", "Праздник" },
			{ "status_21", "Чай,кофе" }, { "status_16", "Играю" },
			{ "status_20", "Пиво" }, { "status_53", "Музыка" },
			{ "status_26", "Учусь" }, { "status_8", "Туалет" },
			{ "status_27", "Пишу" } };

	public String getStatusCode() {
		return (id < 0 || id > status.length) ? status[1][0] : status[id][0];
	}

	public void setStatus(int id) {
		this.id = (id < 0 || id > status.length) ? 1 : id;
	}

	public int getStatus() {
		switch (id) {
		case 0:
			return STATUS_INVISIBLE;
		case 1:
			return STATUS_ONLINE;
		case 2:
			return STATUS_AWAY;
		default:
			return STATUS_OTHER;
		}
	}

	public void setTitle(String t) {
		this.title = t;
	}

	public String getTitle() {
		return title;
	}

	public void setDesc(String s) {
		this.desc = s;
	}

	public String getDesc() {
		return desc;
	}

}
