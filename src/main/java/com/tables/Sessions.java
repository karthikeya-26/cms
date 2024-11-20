package com.tables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Sessions implements Columns {
    SESSION_ID("session_id", String.class),
    USER_ID("user_id", Integer.class),
    CREATED_TIME("created_time", Long.class),
    LAST_ACCESSED_TIME("last_accessed_time", Long.class);

    private static final Map<String, Sessions> LOOKUP_MAP = new HashMap<>();

    static {
        // Populate the lookup map
        for (Sessions session : Sessions.values()) {
            LOOKUP_MAP.put(session.columnName, session);
        }
    }

    private final String columnName;
    private final Class<?> dataType;

    Sessions(String columnName, Class<?> dataType) {
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

    public static Sessions getCol(String colName) {
        Sessions session = LOOKUP_MAP.get(colName);
        if (session == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
        }
        return session;
    }
    
    public static List<Columns> getAllCols(){
    	return Arrays.asList(Sessions.values());
    }
}
