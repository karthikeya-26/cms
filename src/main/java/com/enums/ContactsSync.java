package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ContactsSync implements Columns {
	USER_ID("user_id",Integer.class),
	ACCOUNT_ID("account_id", String.class),
	REFRESH_TOKEN("refresh_token",String.class),
	PROVIDER("provider",String.class);
	
	private static final Map<String,ContactsSync> LOOKUP_MAP = new HashMap<>();
	
	static {
		for(ContactsSync c : ContactsSync.values()) {
			LOOKUP_MAP.put(c.columnName, c);
		}
	}
	
	private final String columnName;
	private final Class<?> dataType;
	
	ContactsSync(String colName, Class<?> dataType){
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

    public static ContactsSync getCol(String colName) {
        ContactsSync c = LOOKUP_MAP.get(colName);
        if (c == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
        }
        return c;
    }

	public static List<Columns> getAllCols() {
		return Arrays.asList(ChangeLog.values());
	}
	
}
