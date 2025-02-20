package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@TableName("user_groups")
public enum UserGroups implements Columns {
	GROUP_ID("group_id", Integer.class), GROUP_NAME("group_name", String.class), USER_ID("user_id", Integer.class),
	CREATED_AT("created_at", Long.class), MODIFIED_AT("modified_at", Long.class), ALL_COLS("*", null);

	private static final Map<String, Columns> LOOKUP_MAP = new HashMap<>();

	static {
		// Populate the lookup map
		for (UserGroups userGroup : UserGroups.values()) {
			LOOKUP_MAP.put(userGroup.columnName, userGroup);
		}
	}

	private final String columnName;
	private final Class<?> dataType;

	UserGroups(String columnName, Class<?> dataType) {
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
		return GROUP_ID.value();
	}

	public static Columns getCol(String colName) {
		Columns userGroup = LOOKUP_MAP.get(colName);
		if (userGroup == null) {
			throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return userGroup;
	}

	public static List<Columns> getAllCols() {
		return Arrays.asList(UserGroups.values());
	}
}
