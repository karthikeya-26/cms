package com.tables;

public enum Servers implements Columns{
	SERVER_NAME("server_name",String.class),
	PORT("port",String.class);
	
	private String columnName;
	private Class<?> dataType;
	
	private Servers(String columnName,Class<?> dataType) {
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

}
