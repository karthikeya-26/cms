package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ClientDetails  implements Columns{
	
	CLIENT_ID("client_id",String.class),
	USER_ID("user_id", Integer.class),
	CLIENT_NAME("client_name", String.class),	
	CLIENT_TYPE("client_type", String.class),
	CLIENT_SECRET("client_secret", String.class),
	CREATED_AT("created_at", Long.class),
	MODIFIED_AT("modified_at", Long.class),
	SCOPES("scopes",String.class),
	ALL_COLS("*",null);
	
	private static final Map<String, ClientDetails> LOOKUP_MAP = new HashMap<>();
	
	static {
		for(ClientDetails clientDetail :ClientDetails.values()) {
			LOOKUP_MAP.put(clientDetail.value(), clientDetail);
		}
	}
	
	private final String colName;
	private final Class<?> dataType;
	
	ClientDetails(String colName, Class<?> dataType ){
		this.colName = colName;
		this.dataType = dataType;
	}

	@Override
	public String value() {
		return colName;
	}

	@Override
	public Class<?> getDataType() {
		return  dataType;
	}
	
	public static ClientDetails getCol(String colName) {
		ClientDetails changeLog = LOOKUP_MAP.get(colName);
		if(changeLog == null) {
			throw new IllegalArgumentException("Column name "+ colName +" does not exist.");
		}
		return changeLog;
	}

	
	public static List<Columns> getAllCols() {
		return Arrays.asList(ClientDetails.values());
	}

}
