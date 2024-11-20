package com.tables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Passwords implements Columns {
    USER_ID("user_id", Integer.class),
    PASSWORD("password", String.class),
    PW_VERSION("pw_version", Integer.class),
    CREATED_AT("created", Long.class),
    MODIFIED_AT("modified_at", Integer.class);

    private static final Map<String, Passwords> LOOKUP_MAP = new HashMap<>();

    static {
        // Populate the lookup map
        for (Passwords password : Passwords.values()) {
            LOOKUP_MAP.put(password.columnName, password);
        }
    }

    private final String columnName;
    private final Class<?> dataType;

    Passwords(String columnName, Class<?> dataType) {
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

    public static Passwords getCol(String colName) {
        Passwords password = LOOKUP_MAP.get(colName);
        if (password == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
        }
        return password;
    }
    
    public static List<Columns> getAllCols(){
    	return Arrays.asList(Passwords.values());
    }
}
