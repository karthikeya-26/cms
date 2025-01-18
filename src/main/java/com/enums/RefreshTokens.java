package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RefreshTokens implements Columns{
	REFTOKEN_ID("reftoken_id", Integer.class),
	REFRESH_TOKEN("refresh_token", String.class),
	CLIENT_ID("client_id", String.class),
	USER_ID("user_id", Integer.class),
	SCOPES("scopes", String.class),
	CREATED_AT("created_at", Long.class),
	ALL_COLS("*", null);
	
	private final String columnName;
	private final Class<?> dataType;
	
	private static final Map<String, RefreshTokens> LOOKUP_MAP = new HashMap<>();
	
	static {
		for( RefreshTokens col : RefreshTokens.values()) {
			LOOKUP_MAP.put(col.value(), col);
		}
	}
	private RefreshTokens(String colName, Class<?> dataType){
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
	
	public static RefreshTokens getCol(String colName) {
		RefreshTokens col = LOOKUP_MAP.get(colName);
		if(col == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return col;
	}
	
	public static List<Columns> getAllCols(){
		return Arrays.asList(RefreshTokens.values());
	}
}
