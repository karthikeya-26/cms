package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum UserDetails implements Columns {
    USER_ID("user_id", Integer.class),
    USER_NAME("user_name", String.class),
    FIRST_NAME("first_name", String.class),
    LAST_NAME("last_name", String.class),
    CONTACT_TYPE("contact_type", String.class),
    CREATED_AT("created_at", Long.class),
    MODIFIED_AT("modified_at", Long.class),
    ALL_COLS("*", null);

    private static final Map<String, UserDetails> LOOKUP_MAP = new HashMap<>();

    static {
        // Populate the lookup map
        for (UserDetails userDetail : UserDetails.values()) {
            LOOKUP_MAP.put(userDetail.columnName, userDetail);
        }
    }

    private final String columnName;
    private final Class<?> dataType;

    UserDetails(String columnName, Class<?> dataType) {
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
    
    public Columns getPrimaryKey() {
    	return USER_ID;
    }
    
    public static UserDetails getCol(String colName) {
        UserDetails userDetail = LOOKUP_MAP.get(colName);
        if (userDetail == null) {
            throw new IllegalArgumentException("Column name " + colName + " does not exist.");
        }
        return userDetail;
    }
    
    public static List<Columns> getAllCols(){
    	return Arrays.asList(UserDetails.values());
    }
}
