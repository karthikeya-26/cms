package com.tables;

public enum Sessions implements Columns{
	SESSION_ID("session_id",String.class),
	USER_ID("user_id",Integer.class),
	CREATED_AT("created_at",Long.class),
	LAST_ACCESSED_TIME("last_accessed_time",Long.class);
	
	private final String columnName;
	private Class<?> dataType;
	
	private Sessions(String columnNmae,Class<?> dataType) {
		this.columnName = columnNmae;
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
	
}
