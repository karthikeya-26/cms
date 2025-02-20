package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@TableName("user_mails")
public enum UserMails implements Columns {
	MAIL_ID("mail_id", Integer.class), MAIL("mail", String.class), USER_ID("user_id", Integer.class),
	IS_PRIMARY("is_primary", Integer.class), CREATED_AT("created_at", Long.class),
	MODIFIED_AT("modified_at", Long.class), ALL_COLS("*", null);

	private static final Map<String, UserMails> LOOKUP_MAP = new HashMap<>();

	static {
		// Populate the lookup map
		for (UserMails userMail : UserMails.values()) {
			LOOKUP_MAP.put(userMail.columnName, userMail);
		}
	}

	private final String columnName;
	private final Class<?> dataType;

	UserMails(String columnName, Class<?> dataType) {
		this.columnName = columnName;
		this.dataType = dataType;
	}

	@Override
	public String value() {
		return columnName;
	}

	@Override
	public Class<?> getDataType() {
		return dataType;
	}

	public String getPrimaryKey() {
		return MAIL_ID.value();
	}

	public static UserMails getCol(String colName) {
		UserMails userMail = LOOKUP_MAP.get(colName);
		if (userMail == null) {
			throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return userMail;
	}

	public static List<Columns> getAllCols() {
		return Arrays.asList(UserMails.values());
	}
}
