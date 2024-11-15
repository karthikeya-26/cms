package com.tables;

public enum UserDetails implements Columns {
    USER_ID("user_id", Integer.class),
    USER_NAME("user_name", String.class),
    PASSWORD("password", String.class),
    FIRST_NAME("first_name", String.class),
    LAST_NAME("last_name", String.class),
    CONTACT_TYPE("contact_type", String.class),
    CREATED_AT("created_at", Long.class),
    MODIFIED_AT("modified_at",Long.class),
    PW_VERSION("pw_version", Integer.class),
    PW_LAST_CHANGED_AT("pw_last_changed_at",Long.class),
	ALL_COLS("*", null), ;

    private final String columnName;
    private  Class<?> dataType =null;

    UserDetails(String columnName, Class<?> dataType ) {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public String value() {
        return columnName;
    }
    
    public Class<?> getDataType(){
    	return dataType;
    }
}
