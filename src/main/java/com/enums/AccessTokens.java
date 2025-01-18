package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum AccessTokens implements Columns{
	ACCESSTOKEN_ID("accesstoken_id",Integer.class),
	ACCESS_TOKEN("access_token", String.class),
	SCOPES("scopes", String.class),
	CREATED_AT("created_at", Long.class),
	REFTOKEN_ID("reftoken_id",Integer.class),
	USER_ID("user_id", Integer.class),
	CLIENT_ID("client_id", String.class),
	ALL_COLS("*",null);
	
	private static final Map<String, AccessTokens> LOOKUP_MAP = new HashMap<>();
	
	static {
		for(AccessTokens col : AccessTokens.values()) {
			LOOKUP_MAP.put(col.value(), col);
		}
	}
	
	private final String columnName;
	private final Class<?> dataType;
	
	AccessTokens(String colName, Class<?> dataType){
		this.columnName = colName;
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
	
	public static AccessTokens getCol(String columnName) {
		AccessTokens colName  = LOOKUP_MAP.get(columnName);
		if(colName == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return colName;
	}
	
	public static List<Columns> getAllCols(){
		 return Arrays.asList(AccessTokens.values());
	}
}
