package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum AuthorizationCodes implements Columns{
	AUTHCODE_ID("authcode_id",Integer.class),
	AUTHORIZATION_CODE("authorization_code", String.class),
	CLIENT_ID("client_id", String.class),
	USER_ID("user_id",Integer.class),
	CREATED_AT("created_at", Long.class),
	SCOPES("scopes", String.class),
	ACCESS_TYPE("access_type", String.class),
	ALL_COLS("*",null);
	
	private final static Map<String, AuthorizationCodes> LOOKUP_MAP = new  HashMap<>();
	
	static {
		for (AuthorizationCodes authorizationCodes : AuthorizationCodes.values()){
			LOOKUP_MAP.put(authorizationCodes.value(), authorizationCodes);
		}
	}
	private final String columnName;
	private final Class<?> dataType;
	
	AuthorizationCodes(String columnName, Class<?> dataType){
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
	
	public static AuthorizationCodes getCol(String colName) {
		AuthorizationCodes col = LOOKUP_MAP.get(colName);
		if(col == null ) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
		}
		return col;
	}
	
	public static List<Columns> getAllCols(){
		return Arrays.asList(AuthorizationCodes.values());
	}

}
