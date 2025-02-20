package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbObjects.PasswordsObj;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
@TableName("passwords")
public enum Passwords implements Columns {
	USER_ID("user_id", Integer.class), PASSWORD("password", String.class), PASSWORD_VERSION("password_version", Integer.class),
	CREATED_AT("created_at", Long.class), MODIFIED_AT("modified_at", Integer.class);

	private static final Map<String, Passwords> LOOKUP_MAP = new HashMap<>();

	static {
		// Populate the lookup map
		for (Passwords password : Passwords.values()) {
			LOOKUP_MAP.put(password.columnName, password);
		}
	}

	private final String columnName;
	private final Class<?> dataType;

	Passwords(String columnName, Class<?> dataType) {
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
		return USER_ID.value();
	}

	public static Passwords getCol(String colName) {
		Passwords password = LOOKUP_MAP.get(colName);
		if (password == null) {
			throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return password;
	}

	public static List<Columns> getAllCols() {
		return Arrays.asList(Passwords.values());
	}

	public static void main(String[] args) throws QueryException {
		Select s = new Select();
		s.table(Table.Passwords);
		System.out.println(s.executeQuery(PasswordsObj.class));

	}
}
