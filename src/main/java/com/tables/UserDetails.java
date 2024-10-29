package com.tables;

public enum UserDetails implements Columns {
    USER_ID("user_id", Integer.class),
    USER_NAME("user_name", String.class),
    PASSWORD("password", String.class),
    FIRST_NAME("first_name", String.class),
    LAST_NAME("last_name", String.class),
    CONTACT_TYPE("contact_type", String.class),
	ALL_COLS("*", null);

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
