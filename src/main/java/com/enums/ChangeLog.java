package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TableName("change_log")
public enum ChangeLog implements Columns {

	LOG_ID("log_id", Long.class), TABLE_NAME("table_name", String.class), REQ_TYPE("req_type", String.class),
	OLD_VAL("old_val", String.class), NEW_VAL("new_val", String.class), SESSION_ID("session_id", String.class),
	END_POINT("end_point", String.class), MODIFIED_BY("modified_by", String.class), ALL_COLS("*", null);

	private static final Map<String, ChangeLog> LOOKUP_MAP = new HashMap<>();

	static {
		for (ChangeLog changeLog : ChangeLog.values()) {
			LOOKUP_MAP.put(changeLog.value(), changeLog);
		}
	}

	private final String colName;
	private final Class<?> dataType;

	ChangeLog(String colName, Class<?> dataType) {
		this.colName = colName;
		this.dataType = dataType;
	}

	@Override
	public String value() {
		return colName;
	}

	@Override
	public Class<?> getDataType() {
		return dataType;
	}

	public static ChangeLog getCol(String colName) {
		ChangeLog changeLog = LOOKUP_MAP.get(colName);
		if (changeLog == null) {
			throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return changeLog;
	}

	public static List<Columns> getAllCols() {
		return Arrays.asList(ChangeLog.values());
	}

}
