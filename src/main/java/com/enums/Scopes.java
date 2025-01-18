package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Scopes implements Columns{
	
	SCOPE_ID("scope_id",Integer.class),
	SCOPE("scope",String.class),
	CREATED_AT("created_at",Long.class),
	MODIFIED_AT("modified_at",Long.class),
	ALL_COLS("*",null);
	
	
	private static final Map<String, Scopes> LOOKUP_MAP = new HashMap<String,Scopes>();
		
		
	static {
		for(Scopes scope : Scopes.values()) {
			LOOKUP_MAP.put(scope.value(), scope);
		}
	}
	
	private final String colName;
	private final Class<?> dataType;
	
	Scopes(String colName, Class<?> dataType){
		this.colName = colName;
		this.dataType = dataType;
	}
	
	@Override
	public String value() {
		return colName;
	}
	
	@Override
	public Class<?> getDataType(){
		return dataType;
	}
	
	public static Scopes getCol(String colName) {
		Scopes scope = LOOKUP_MAP.get(colName);
		if(scope == null) {
			throw new IllegalArgumentException("Column name "+ colName +" does not exist.");
		}
		return scope;
	}
	
	public static List<Columns> getAllCols() {
		return Arrays.asList(Scopes.values());
	}
}
